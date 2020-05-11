package ihm.accidents.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ihm.accidents.R;
import ihm.accidents.adapters.ListIncidentAdapter;
import ihm.accidents.fragments.FilterOptionsFragment;
import ihm.accidents.fragments.SortOptionsFragment;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.models.SortOption;
import ihm.accidents.services.AccidentDownloader;
import ihm.accidents.services.TypeDownloader;

public class ListIncidentActivity extends IhmAbstractActivity implements FilterOptionsFragment.FilterOptionsListener, UpdateTypes, SortOptionsFragment.SortOptionListener {
    private static final String TAG = "ListIncidentActivity";
    private AccidentDownloader accidentDownloader;
    private List<AccidentModel> listAccidents= new ArrayList<>();
    private ListIncidentAdapter adapter;
    private List<String> accidentTypes=new ArrayList<>();
    private TypeDownloader typeDownloader =new TypeDownloader();
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_list_accident,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.filter_item){
            FilterOptionsFragment dialogFragment=new FilterOptionsFragment(this.accidentTypes);
            dialogFragment.setListener(this);
            dialogFragment.show(getSupportFragmentManager(),TAG);
            return true;
        }
        else if(item.getItemId()==R.id.sort_item){
            SortOptionsFragment sortOptionsFragment=new SortOptionsFragment();
            sortOptionsFragment.setListener(this);
            sortOptionsFragment.show(getSupportFragmentManager(),TAG);
            return true;
        }
        else if(item.getItemId()==R.id.homeItem){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accidentDownloader=new AccidentDownloader(this);
        setContentView(R.layout.list_incident);

        RecyclerView recyclerViewIncidents = findViewById(R.id.list_incidents_id);
        this.adapter = new ListIncidentAdapter(this, this);
        recyclerViewIncidents.setAdapter(adapter);

        this.typeDownloader.retrieveAccidentTypes(this);

        accidentDownloader.getAccidentsFromServer(listAccidents, this, adapter);
        recyclerViewIncidents.setLayoutManager(new LinearLayoutManager(this));
        adapter.setAccidentsList(listAccidents);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);



        setTitle("Liste d'incidents");

    }

    @Override
    public void onFilterOptionSelected(String filter) {
        Log.d(TAG, "onFilterOptionSelected: "+filter);
        if(filter.equals("Tout")){
            adapter.setAccidentsList(listAccidents);

        }
        else if(filter.equals("Signalés par vous")){
            List<AccidentModel> listFiltered =listAccidents.stream().filter(accidentModel -> accidentModel.getDeviceId()==this.getDeviceId() ).collect(Collectors.toList());
            Log.d(TAG, "onFilterOptionSelected: "+listFiltered);
            adapter.setAccidentsList(listFiltered);

        }
        else{
            List<AccidentModel> listFiltered =listAccidents.stream().filter(accidentModel -> accidentModel.getType().equals(filter) ).collect(Collectors.toList());
            Log.d(TAG, "onFilterOptionSelected: "+listFiltered);
            adapter.setAccidentsList(listFiltered);
        }
        adapter.notifyDataSetChanged();


    }

    @Override
    public void updateTypesList(String types) {
        try {
            this.accidentTypes.addAll(this.extractTypes(types));
        } catch (JSONException e) {
            Log.e(TAG, "updateTypesList: ", e);
        }
    }

    @Override
    public void onSortOptionSelected(SortOption sortOption) {
        Log.d(TAG, "onSortOptionSelected: "+sortOption);
        if(sortOption==SortOption.CLOSEST){

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if(location!=null){
                    adapter.setAccidentsList(adapter.getAccidentsList().stream().sorted(
                            (a,b)-> Double.compare(a.distanceTo(location),b.distanceTo(location))
                    ).collect(Collectors.toList()));
                    adapter.notifyDataSetChanged();
                }
                else{
                    Snackbar.make(findViewById(R.id.wrapper_layout),"Cannot find your location",Snackbar.LENGTH_SHORT).show();
                }


            });

        }

        else if(sortOption==SortOption.FARTHEST){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if(location!=null){
                    adapter.setAccidentsList(adapter.getAccidentsList().stream().sorted(
                            (a,b)-> - Double.compare(a.distanceTo(location),b.distanceTo(location))
                    ).collect(Collectors.toList()));
                    adapter.notifyDataSetChanged();
                }
                else{
                    Snackbar.make(findViewById(R.id.wrapper_layout),"Cannot find your location",Snackbar.LENGTH_SHORT).show();
                }



            });
        }
        else if(sortOption==SortOption.RECENT){
            adapter.setAccidentsList(adapter.getAccidentsList().stream().sorted(
                    (a,b)-> -Long.compare(a.getDate(),b.getDate()) ).collect(Collectors.toList())
            );
            adapter.notifyDataSetChanged();
        }
        else if(sortOption==SortOption.OLDER){
            adapter.setAccidentsList(adapter.getAccidentsList().stream().sorted(
                    (a,b)-> Long.compare(a.getDate(),b.getDate()) ).collect(Collectors.toList())
            );
            adapter.notifyDataSetChanged();
        }
    }
}
