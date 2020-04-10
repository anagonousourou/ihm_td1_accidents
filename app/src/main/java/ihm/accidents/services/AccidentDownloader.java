package ihm.accidents.services;

import android.app.Activity;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import ihm.accidents.adapters.ListIncidentAdapter;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.utils.Utils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AccidentDownloader implements Callback {
    private List<AccidentModel> accidents;
    private static final String TAG = "AccidentDownloader";
    private final OkHttpClient client = new OkHttpClient();

    private static final String url= Utils.webserviceUrl+"/api/accidents/";
    private ListIncidentAdapter adapter;
    private Activity activity;

    public void getAccidentsFromServer(List<AccidentModel> accidentModels, Activity activity, ListIncidentAdapter adapter){
        this.activity=activity;
        this.adapter=adapter;
        this.accidents=accidentModels;
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(this);

    }
    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {

    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if(!response.isSuccessful()){
            Log.d(TAG, "onResponse: could not get the accidents from the server");
        }
        else {
            try {
                JSONObject jsonObject = new JSONObject(response.body().string());
                JSONArray accidentsJson=jsonObject.getJSONArray("accidents");
                for (int i = 0; i < accidentsJson.length(); i++) {
                    JSONObject accidentJson=accidentsJson.getJSONObject(i);
                    if(accidentJson.has("imageUrl")){
                        this.accidents.add(new AccidentModel("",accidentJson.getString("adresse"),
                                accidentJson.getString("type"),
                                accidentJson.getString("commentaire"),
                                Utils.webserviceUrl+"/"+ accidentJson.getString("imageUrl")
                        ));
                    }

                }
                activity.runOnUiThread(()->{
                    adapter.notifyDataSetChanged();
                });

            } catch (JSONException e) {
                Log.e(TAG, "onResponse: json transformation of whole response failed:",e);
            }
        }
    }
}
