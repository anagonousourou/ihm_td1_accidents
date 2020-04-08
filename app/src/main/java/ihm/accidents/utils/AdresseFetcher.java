package ihm.accidents.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdresseFetcher {

    private static final String TAG ="AdresseFetcher" ;
    public List<String> retrieveAdressesMatching(String toString) {

        Log.d(TAG, "retrieveAdressesMatching: Doing work");
        return parseJsonToList(retrieveJsonFromUrl("https://api-adresse.data.gouv.fr/search/?q="+ URLEncoder.encode(toString) ));
    }

    protected String retrieveJsonFromUrl(String stringUrl) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(stringUrl).build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            return null;
        }
    }

    protected List<String> parseJsonToList(String s) {
        List<String> result=new ArrayList<>();
        Log.d(TAG, "onPostExecute: "+s);


        try {
            JSONObject rawResult=new JSONObject(s);

            JSONArray adressesObject= rawResult.getJSONArray("features");
            for (int i = 0; i < adressesObject.length(); i++) {
                result.add(adressesObject.getJSONObject(i).getJSONObject("properties").getString("label"));
            }
        } catch (JSONException e) {
            Log.e(TAG, "onPostExecute: ",e );;
        }
    return  result;
    }

}
