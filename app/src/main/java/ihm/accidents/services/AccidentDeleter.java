package ihm.accidents.services;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import ihm.accidents.activities.ListIncidentActivity;
import ihm.accidents.utils.Utils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AccidentDeleter implements Callback {
    private static final String TAG = "AccidentDeleter";
    private Activity activity;
    private OkHttpClient client=new OkHttpClient();

    public void deleteAccident(long id, Activity activity){
        this.activity=activity;
        String url = Utils.webserviceUrl+"/api/accidents/"+id+"/";
        Request request = new Request.Builder().url(url ).delete().build();

       client.newCall(request).enqueue(this);

    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Log.e(TAG, "onFailure: ", e);
        activity.runOnUiThread(()->
                Toast.makeText(activity,"L'accident n'a pas pu être supprimé.",Toast.LENGTH_SHORT).show()
        );

    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if(!response.isSuccessful()){
            Log.d(TAG, "onResponse: "+response.body().string());
            activity.runOnUiThread(()-> {
                Toast.makeText(activity, "L'accident n'a pas pu être supprimé.", Toast.LENGTH_SHORT).show();

            });
        }
        else{
            activity.runOnUiThread(()-> {
                Toast.makeText(activity, "Accident supprimé avec succès.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, ListIncidentActivity.class);
                activity.startActivity(intent);
            });
            Log.d(TAG, "onResponse: accident supprimé");
        }

    }
}
