package ihm.accidents.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

import my.ihm.exercice6.R;

public class SomeFragment extends Fragment {
    private MapView map;
    private ArrayList<OverlayItem> items = new ArrayList<>();

    public SomeFragment(){
        this.items.add(new OverlayItem("C'est chez moi :D","test",new GeoPoint(43.684129, 7.202671)));
    }

    public SomeFragment(String address,String title){
        this.items.add(new OverlayItem(title,"test",this.getLocationFromAddress(getContext(),address)));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.some_layout, container, false);
        Configuration.getInstance().load(getContext(),
                PreferenceManager.getDefaultSharedPreferences(getContext())
        );
        map = root.findViewById(R.id.mapwidget);
        Log.d("mapView Value", map.toString());
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setTileSource(TileSourceFactory.MAPNIK);
        IMapController mapController = map.getController();
        mapController.setZoom(18.0);
        GeoPoint startPoint = new GeoPoint(43.684129, 7.202671);
        mapController.setCenter(startPoint);
        ItemizedOverlayWithFocus<OverlayItem> nOverlay = new ItemizedOverlayWithFocus<OverlayItem>(getContext(), items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
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

        return  root;
    }

    public GeoPoint getLocationFromAddress(Context context,String myAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        GeoPoint p = null;

        try {
            address = coder.getFromLocationName(myAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p = new GeoPoint((double) (location.getLatitude() * 1E6),
                    (double) (location.getLongitude() * 1E6));

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
}
