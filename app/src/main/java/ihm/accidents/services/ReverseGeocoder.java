package ihm.accidents.services;

import android.app.Activity;
import android.location.Location;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import ihm.accidents.utils.Utils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ReverseGeocoder implements Callback {
    private Activity activity;
    private  OkHttpClient client =new OkHttpClient();
    private AutoCompleteTextView autoCompleteTextView;
    private static final String TAG = "ReverseGeocoder";

    public void findAddressFromLocation(Location location, Activity activity, AutoCompleteTextView autoCompleteTextView) throws UnsupportedEncodingException {
        this.activity=activity;
        this.autoCompleteTextView=autoCompleteTextView;
        if(location!=null){
            String url= Utils.getRevereseGeocodingUrl(location.getLatitude(),location.getLongitude());
            Log.d(TAG, "retrieveLocationAndPlug: "+url);
            Request request = new Request.Builder().url(url).build();

            client.newCall(request).enqueue(this);
        }


    }
    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {

    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (!response.isSuccessful()) {
            activity.runOnUiThread(()->
                    Toast.makeText(activity,"Could not get your adress, sorry",Toast.LENGTH_SHORT).show()
            );
        }

        final String body = response.body().string();


        try {

            final String adresse = new JSONObject(body).getJSONArray("results").getJSONObject(0).getString("formatted");
            activity.runOnUiThread(() -> {
                autoCompleteTextView.setText(adresse);

            });
        } catch (JSONException e) {
            activity.runOnUiThread(()->
                    Toast.makeText(activity,"Could not get your adress, sorry",Toast.LENGTH_SHORT).show()
            );
            Log.e(TAG, "onResponse: ",e );
        }
    }
}
