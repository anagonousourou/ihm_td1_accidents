package ihm.accidents.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import ihm.accidents.R;
import ihm.accidents.fragments.EditeurTrajetFragment;
import ihm.accidents.fragments.MapFragment;
import ihm.accidents.utils.Utils;

public class ChoicePathActivity extends IhmAbstractActivity{
    private static final String TAG = "ChoicePathActivity";
    protected static final int PERMISSION_ACCESS_FINE_LOCATION = 2;
    protected static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private GeoPoint src, dst;
    private MapFragment fragmentMap;
    private EditeurTrajetFragment fragmentEditeur;
    private String mWhichRouteProvider = "bicycle"; //CECI EST UN EXEMPLE, un TextView sera utilisé, plus tard


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.second_menu,menu);
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //On commence par vérifier si on a les permissions de Localisation et on les demande si besoin
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "retrieveLocationAndPlug: We don't have permissions to ACCESS COARSE LOCATION");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "retrieveLocationAndPlug: We don't have permissions to ACCESS FINE LOCATION");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_path_activity);

        //on obtient un object fusedLocationProviderClient
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //le fragment de la carte
        fragmentMap = new MapFragment();

        //le fragment pour éditer
        fragmentEditeur = new EditeurTrajetFragment();
        Log.d(TAG, "onCreate: "+ fusedLocationClient);

        //le getLastLocation peut lever une exception si on a pas les permissions ce qui
        //peut etre le cas si c'est la premiere fois qu'on ouvre l'activité
        if (savedInstanceState == null) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                Bundle locationBundle = new Bundle();
                //la location peut être nulle mais on laisse le fragmentEditeur se débrouiller avec ça
                Log.d(TAG, "onCreate: OnSucessGetLastlocation " + location);
                locationBundle.putParcelable(Utils.locationKey, location);
                    if(location == null){
                        asksToEnableGeo();
                    } else {
                        Toast.makeText(this, "Géolocalisation activée !", Toast.LENGTH_LONG).show();
                    }
                fragmentEditeur.setArguments(locationBundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.placeHolderMapFragment, fragmentMap).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.placeHolderEditeurFragment, fragmentEditeur).commit();

            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Cannot get your location", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onCreate Exception : ", e);
                getSupportFragmentManager().beginTransaction().replace(R.id.placeHolderMapFragment, fragmentMap).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.placeHolderEditeurFragment, fragmentEditeur).commit();
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: ALL GOOD");
                } else {
                    Toast.makeText(this, "Need your coarse location!", Toast.LENGTH_SHORT).show();
                }

                break;
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: ALL GOOD WE HAVE FINE LOCATION ");
                } else {
                    Toast.makeText(this, "Need your fine location!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void setRoute(String srcAdr, String dstAdr, String transport) {
        mWhichRouteProvider = transport;
        src = fragmentMap.getLocationFromAddress(getApplicationContext(), srcAdr);
        dst = fragmentMap.getLocationFromAddress(getApplicationContext(), dstAdr);
        ArrayList<GeoPoint> route = new ArrayList<>();
        route.add(src);
        route.add(dst);

        //On génère l'AsyncTask pour la création du trajet
        new UpdateRoadTask(this).execute(route);
    }

    public void generateRoads(Road[] roads){
        for(Road road : roads) {
            if (road.mStatus != Road.STATUS_OK)
                Toast.makeText(this, "Error when loading the road - status=" + road.mStatus, Toast.LENGTH_SHORT).show();

            List<Overlay> tmp = fragmentMap.getMap().getOverlays().stream().filter(o -> !(o instanceof Polyline)).collect(Collectors.toList());
            fragmentMap.getMap().getOverlays().clear();
            fragmentMap.getMap().getOverlays().addAll(tmp);
            fragmentMap.getMap().getController().setCenter(src);

            //Création de la route step by step
            int i = 0;
            for (; i < road.mRouteHigh.size() - 1; i++) {
                ArrayList<GeoPoint> currentPath = new ArrayList<>();
                currentPath.add(road.mRouteHigh.get(i));
                currentPath.add(road.mRouteHigh.get(i + 1));

                createOneRoad(currentPath);
            }

            Drawable marker = getResources().getDrawable(R.drawable.marker);
            Bitmap bitmap2 = ((BitmapDrawable) marker).getBitmap();
            marker = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap2, 100,100, true));

            ArrayList<OverlayItem> items = new ArrayList<>();

            OverlayItem start = new OverlayItem("Départ", "", road.mRouteHigh.get(0));
            start.setMarker(marker);
            items.add(start);

            OverlayItem end = new OverlayItem("Arrivée", "", road.mRouteHigh.get(i));
            end.setMarker(marker);
            items.add(end);

            ItemizedOverlayWithFocus<OverlayItem> nOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this,
                    items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                @Override
                public boolean onItemSingleTapUp(int index, OverlayItem item) {
                    return true;
                }

                @Override
                public boolean onItemLongPress(int index, OverlayItem item) {
                    return false;
                }
            });
            nOverlay.setFocusItemsOnTap(true);
            fragmentMap.getMap().getOverlays().add(nOverlay);
        }

        fragmentMap.getMap().invalidate();
    }

    public void createOneRoad(ArrayList<GeoPoint> route){
        Polyline line = new Polyline();
        line.setWidth(5);
        line.setColor(Color.RED);
        line.setPoints(route);
        line.setGeodesic(true);
        line.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, fragmentMap.getMap()));

        fragmentMap.getMap().getOverlays().add(line);
    }


    public void asksToEnableGeo() {
        //Message + redirection vers paramètres si géolocalisation désactivée
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Géolocalisation");
            alertDialog.setMessage("Votre géolocalisation est désactivée. Veuillez l'activer dans vos paramètres, puis redémarrer l'appli.");
            alertDialog.setPositiveButton("Paramètres de géolocalisation", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();

    }

    /**
     * Async task to get the road in a separate thread.
     */
    private class UpdateRoadTask extends AsyncTask<ArrayList<GeoPoint>, Void, Road[]> {

        private final Context mContext;

        public UpdateRoadTask(Context context) {
            this.mContext = context;
        }

        protected Road[] doInBackground(ArrayList<GeoPoint>... params) {
            ArrayList<GeoPoint> waypoints = params[0];
            RoadManager roadManager;
            ArrayList<GeoPoint> tmp = new ArrayList<>();
            tmp.add(new GeoPoint(43.736524, 7.413023));
            Locale locale = Locale.getDefault();
            switch (mWhichRouteProvider){
                case "bicycle":
                    roadManager = new MapQuestRoadManager("fIEGwqlAyXgEuVHVigNkRB5dkCMYvbwX");
                    roadManager.addRequestOption("routeType=bicycle");
                    break;
                case "pedestrian":
                    roadManager = new MapQuestRoadManager("fIEGwqlAyXgEuVHVigNkRB5dkCMYvbwX");
                    roadManager.addRequestOption("routeType=pedestrian");
                    break;
                case "fastest":
                    roadManager = new MapQuestRoadManager("fIEGwqlAyXgEuVHVigNkRB5dkCMYvbwX");
                    roadManager.addRequestOption("routeType=fastest");
                    roadManager.addRequestOption("prefers=highway");
                    roadManager.addRequestOption("drivingStyle=normal");
                    break;
                case "shortest":
                    roadManager = new MapQuestRoadManager("fIEGwqlAyXgEuVHVigNkRB5dkCMYvbwX");
                    roadManager.addRequestOption("routeType=shortest");
                    break;
                case "multimodal":
                    roadManager = new MapQuestRoadManager("fIEGwqlAyXgEuVHVigNkRB5dkCMYvbwX");
                    roadManager.addRequestOption("routeType=multimodal");
                    break;
                default:
                    roadManager = new MapQuestRoadManager("fIEGwqlAyXgEuVHVigNkRB5dkCMYvbwX");
                    roadManager.addRequestOption("locale="+locale.getLanguage());
                    break;
            }
            return roadManager.getRoads(waypoints);
        }

        protected void onPostExecute(Road[] result) {
            generateRoads(result);
        }
    }
}
