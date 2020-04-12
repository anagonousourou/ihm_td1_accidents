package ihm.accidents.activities;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ihm.accidents.R;
import ihm.accidents.adapters.ListIncidentAdapter;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.services.AccidentDownloader;

public class ListIncidentActivity extends Activity {
    private AccidentDownloader accidentDownloader=new AccidentDownloader();
    private final List<AccidentModel> listAccidents= new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_incident);

        RecyclerView recyclerViewIncidents = findViewById(R.id.list_incidents_id);
        ListIncidentAdapter adapter = new ListIncidentAdapter(this, this);
        recyclerViewIncidents.setAdapter(adapter);


        accidentDownloader.getAccidentsFromServer(listAccidents, this, adapter);
        recyclerViewIncidents.setLayoutManager(new LinearLayoutManager(this));
        adapter.setAccidentsList(listAccidents);

    }
}
