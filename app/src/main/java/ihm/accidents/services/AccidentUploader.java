package ihm.accidents.services;


import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import ihm.accidents.models.AccidentModel;
import ihm.accidents.utils.Utils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AccidentUploader implements Callback {
    private static final String TAG = "AccidentUploader";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    private final OkHttpClient client = new OkHttpClient();
    private  AccidentModel accident;

    
    public void postAccidentToServer(File photoFile, AccidentModel accidentModel){
        Log.d(TAG, "postAccidentToServer: ");
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("adresse", accidentModel.getAddress()).addFormDataPart("type", accidentModel.getType())
                .addFormDataPart("commentaire", accidentModel.getDetails()).addFormDataPart("photoB64", accidentModel.getImageUrl())
                .addFormDataPart("dateCreation", String.valueOf(accidentModel.getDate()))
                .addFormDataPart("accidentImage", "square.png",
                        RequestBody.create(
                                photoFile,
                                MEDIA_TYPE_PNG))
                .build();

        Request request = new Request.Builder()

                .url("https://webserviceinfoincident.herokuapp.com/api/accidents/").post(requestBody).build();
        this.accident=accidentModel;
        //permet de faire le travail en background sur un autre thread
        client.newCall(request).enqueue(this);
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (!response.isSuccessful()){
            Log.d(TAG, "onResponse: Unexpected code  "+ response);
        }
        else{
            Log.d(TAG, "onResponse: successfully send the accident");
        }

    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Log.e(TAG, "onFailure: ",e);
    }
}
