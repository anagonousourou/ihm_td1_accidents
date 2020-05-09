package ihm.accidents.services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ihm.accidents.activities.UpdateTypes;
import ihm.accidents.adapters.ListIncidentAdapter;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.utils.KeysTags;
import ihm.accidents.utils.Utils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AccidentDownloader implements Callback, UpdateTypes {
    private List<AccidentModel> accidents;
    private static final String TAG = "AccidentDownloader";
    private final OkHttpClient client = new OkHttpClient();

    private static final String url= Utils.webserviceUrl+"/api/accidents/";
    private ListIncidentAdapter adapter;
    private Activity activity;
    private Context context;
    private PreferenceService preferenceService;
    private Map<String, String> typesIcons;
    private TypeDownloader typeDownloader;




    public AccidentDownloader(Context context){
        this.context=context;
        this.preferenceService= new PreferenceService(context);
        typesIcons=new HashMap<>();
        typeDownloader=new TypeDownloader();
        typeDownloader.retrieveAccidentTypes(this);

    }

    public void getAccidentsFromServer(List<AccidentModel> accidentModels, Activity activity, ListIncidentAdapter adapter){
        this.activity=activity;
        this.adapter=adapter;
        this.accidents=accidentModels;
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(this);

    }

    public List<AccidentModel> accidentsFromServerSync() throws IOException, JSONException {

        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response= client.newCall(request).execute();
        if(response.isSuccessful()){
            String serverResponse=response.body().string();

            JSONArray accidentsJson= new JSONObject(serverResponse).getJSONArray("accidents");
            return AccidentModel.listFromJson(accidentsJson.toString()).stream().map(accident-> accident.updateWithImagePreference(preferenceService.hideActualPictures(),typesIcons )).collect(Collectors.toList());
        }
        else{
            throw new IOException("Problem while downloading accidents from server:"+response);
        }

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
                    AccidentModel accident= AccidentModel.fromJson(accidentJson.toString()) ;

                    if(accident!=null){
                        //Log.d("AccidentDownloader",accident.getAddress()+"--"+accident.getid());
                        this.accidents.add(accident.updateWithImagePreference(this.preferenceService.hideActualPictures(),typesIcons));
                    }
                }
                activity.runOnUiThread(()->
                    adapter.notifyDataSetChanged()
                );

            } catch (JSONException e) {
                Log.e(TAG, "onResponse: json transformation of whole response failed:",e);
            }
        }
    }

    @Override
    public void updateTypesList(String types) {
        try {
            JSONArray array=new JSONArray(types);

            for (int i = 0; i < array.length(); i++) {
                JSONObject json=array.getJSONObject(i);
                typesIcons.put( json.getString("label"),json.getString("iconUrl") );
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


