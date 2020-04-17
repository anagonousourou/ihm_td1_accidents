package ihm.accidents.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ihm.accidents.R;
import ihm.accidents.databinding.DetailsAccidentActivityBinding;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.services.AccidentDeleter;
import ihm.accidents.utils.Utils;
import okhttp3.OkHttpClient;


/**
 * To present the details of an Accident wherever the user clicks on a
 * notification or whatever else in the flow of the appplication
 */
public class DetailsAccidentActivity extends Activity {
    private static final String TAG = "DetailsAccidentActivity";
    DetailsAccidentActivityBinding dataBinding;
    private AccidentDeleter accidentDeleter=new AccidentDeleter();
    private AccidentModel accidentModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding= DataBindingUtil.setContentView(this,R.layout.details_accident_activity);
        Intent intent = getIntent();
        this.accidentModel = intent.getParcelableExtra(Utils.accidentKey);
        dataBinding.setAccident(this.accidentModel);
        Log.d(TAG, "onCreate: "+accidentModel.toString());



    }
    public void goToSendMessage(View view) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String message = "Attention\n"+accidentModel.getTitle()+"\n A l'adresse: "+accidentModel.getAddress();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Nouvel Incident");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(sharingIntent, "Partager avec"));
    }

    public void deleteMessage(View view){
        Log.d(TAG, "deleteMessage: "+accidentModel.getid());
            accidentDeleter.deleteAccident(accidentModel.getid(),this);


    }
}
