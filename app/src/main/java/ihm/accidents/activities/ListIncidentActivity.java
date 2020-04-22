package ihm.accidents.activities;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ihm.accidents.R;
import ihm.accidents.adapters.ListIncidentAdapter;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.services.AccidentDownloader;

public class ListIncidentActivity extends IhmAbstractActivity {
    private AccidentDownloader accidentDownloader;
    private final List<AccidentModel> listAccidents= new ArrayList<>();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_list_accident,menu);
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accidentDownloader=new AccidentDownloader(this);
        setContentView(R.layout.list_incident);

        RecyclerView recyclerViewIncidents = findViewById(R.id.list_incidents_id);
        ListIncidentAdapter adapter = new ListIncidentAdapter(this, this);
        recyclerViewIncidents.setAdapter(adapter);


        accidentDownloader.getAccidentsFromServer(listAccidents, this, adapter);
        recyclerViewIncidents.setLayoutManager(new LinearLayoutManager(this));
        adapter.setAccidentsList(listAccidents);

        setTitle("Liste d'incidents");

    }
}
