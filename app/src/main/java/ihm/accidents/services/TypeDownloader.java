package ihm.accidents.services;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ihm.accidents.activities.UpdateTypes;
import ihm.accidents.utils.Utils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class TypeDownloader implements Callback {
    private final OkHttpClient client = new OkHttpClient();
    private UpdateTypes updater;


    public void retrieveAccidentTypes(UpdateTypes updateTypes){
        updater=updateTypes;
        Request request=new Request.Builder().url(Utils.webserviceUrl+"/api/types").build();
        client.newCall(request).enqueue(this);
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {

    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if(response.isSuccessful()){
            String reps=response.body().string();
            updater.updateTypesList(reps);


        }
    }
}
