package ihm.accidents.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import ihm.accidents.adapters.ListIncidentAdapter;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.models.ListAccidentModel;
import ihm.accidents.utils.Placeholders;
import my.ihm.exercice6.R;

public class ListIncidentActivity extends Activity {
    private RecyclerView recyclerViewIncidents;
    private ListIncidentAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_incident);

        recyclerViewIncidents=findViewById(R.id.list_incidents_id);
        adapter=new ListIncidentAdapter(this,this);
        recyclerViewIncidents.setAdapter(adapter);
        List<AccidentModel> listAccidents=new ArrayList<>();
        AccidentModel accidentModelFake=new AccidentModel("Placeholder image selected","Wonderland","solo","Alice vient de trébucher sur le chat de Cheschire. La Reine s'en vient. à suivre :)",
                Placeholders.img_placeholder1);

        AccidentModel accidentModelFake2=new AccidentModel("Breaking News","Polytech Nice Sophia","solo","OMG, Un étudiant est tombé de son quad. :) mdr",
                Placeholders.img_placeholder2);

        /*try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        listAccidents.add(accidentModelFake);
        listAccidents.add(accidentModelFake2);
        recyclerViewIncidents.setLayoutManager(new LinearLayoutManager(this));
        System.out.println("json accident before"+listAccidents);
        adapter.setAccidentsList(listAccidents);
        new JsonTask().execute();

    }

    private class JsonTask extends AsyncTask<String, String, ListAccidentModel> {

        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected ListAccidentModel doInBackground(String... params) {


            ListAccidentModel accidentsServeur= new ListAccidentModel();
            try {
                return accidentsServeur.getListFromWeb();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ListAccidentModel result) {
            if(result!=null) {
                adapter.setAccidentsList(result.getAccidents());
            }
           adapter.notifyDataSetChanged();
        }
    }
}
