package ihm.accidents.fragments;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ihm.accidents.R;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.utils.Utils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapFragment extends Fragment {
    private static final String TAG = "SomeFragment";
    private MapView map;
    private ArrayList<OverlayItem> items = new ArrayList<>();
    private GeoPoint posUser ;
    public MapFragment() {
        posUser =  new GeoPoint(43.684129, 7.202671);
        this.items.add(new OverlayItem("C'est chez moi :D", "test", new GeoPoint(43.684129, 7.202671)));
    }

    public MapFragment(String address, String title, Context context) {
        this.items.add(new OverlayItem(title, "test", this.getLocationFromAddress(context, address)));
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.some_layout, container, false);
        Log.d(TAG, "onCreateView: Outside Permissions Granted");

        {
            Log.d(TAG, "onCreateView: Inside Permissions Granted");
            // Permission is granted
            Configuration.getInstance().load(getContext(), PreferenceManager.getDefaultSharedPreferences(getContext()));
            map  = root.findViewById(R.id.mapwidget);
            Log.d("mapView Value", map.toString());
            map.setBuiltInZoomControls(true);
            map.setMultiTouchControls(true);
            map.setTileSource(TileSourceFactory.MAPNIK);
            IMapController mapController = map.getController();
            mapController.setZoom(18.0);
            GeoPoint startPoint = new GeoPoint(43.684129, 7.202671);
            mapController.setCenter(startPoint);
            ItemizedOverlayWithFocus<OverlayItem> nOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getContext(),
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
            map.getOverlays().add(nOverlay);
        }
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<AccidentModel> accident = accidentsFromServerByDistance(100);
                    Log.d("MapFragment",accident.toString());
                    /**
                     * ajouter liste accident à
                     //to implement ajout des point de la liste la liste de GeoPoint
                     */

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        return root;
    }

    public GeoPoint getLocationFromAddress(Context context, String myAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        GeoPoint p = null;

        try {
            address = coder.getFromLocationName(myAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p = new GeoPoint((location.getLatitude()), (location.getLongitude()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    public MapView getMap(){
        return this.map;
    }

    public List<AccidentModel> accidentsFromServerByDistance(int distance) throws IOException, JSONException {
        Request request = new Request.Builder()
                .url( Utils.webserviceUrl+"/api/accidents/"+posUser.getLatitude()+"/"+posUser.getLongitude()+"/"+distance)
                .build();
        Response response= new OkHttpClient().newCall(request).execute();
        if(response.isSuccessful()){
            String serverResponse=response.body().string();

            JSONArray accidentsJson= new JSONObject(serverResponse).getJSONArray("accidents");
            return AccidentModel.listFromJson(accidentsJson.toString());
        }
        else{
            throw new IOException("Problem while downloading accidents from server:"+response);
        }

    }


}
