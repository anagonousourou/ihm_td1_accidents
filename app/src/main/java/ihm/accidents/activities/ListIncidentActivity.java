package ihm.accidents.activities;


import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ihm.accidents.R;
import ihm.accidents.adapters.ListIncidentAdapter;
import ihm.accidents.fragments.FilterOptionsFragment;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.services.AccidentDownloader;

public class ListIncidentActivity extends IhmAbstractActivity implements FilterOptionsFragment.FilterOptionsListener {
    private static final String TAG = "ListIncidentActivity";
    private AccidentDownloader accidentDownloader;
    private List<AccidentModel> listAccidents= new ArrayList<>();
    private ListIncidentAdapter adapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_list_accident,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.filter_item){
            FilterOptionsFragment dialogFragment=new FilterOptionsFragment();
            dialogFragment.setListener(this);
            dialogFragment.show(getSupportFragmentManager(),TAG);
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


        accidentDownloader.getAccidentsFromServer(listAccidents, this, adapter);
        recyclerViewIncidents.setLayoutManager(new LinearLayoutManager(this));
        adapter.setAccidentsList(listAccidents);

        setTitle("Liste d'incidents");

    }

    @Override
    public void onFilterOptionSelected(String filter) {
        Log.d(TAG, "onFilterOptionSelected: "+filter);
        List<AccidentModel> listFiltered =listAccidents.stream().filter(accidentModel -> accidentModel.getType().equals(filter) ).collect(Collectors.toList());
        Log.d(TAG, "onFilterOptionSelected: "+listFiltered);
        adapter.setAccidentsList(listFiltered);
        adapter.notifyDataSetChanged();

    }
}
