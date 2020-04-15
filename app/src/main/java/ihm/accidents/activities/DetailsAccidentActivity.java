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
import ihm.accidents.utils.Utils;


/**
 * To present the details of an Accident wherever the user clicks on a
 * notification or whatever else in the flow of the appplication
 */
public class DetailsAccidentActivity extends Activity {
    private static final String TAG = "DetailsAccidentActivity";
    DetailsAccidentActivityBinding dataBinding;
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
        Intent sendMessage = new Intent(this ,SendMessageActivity.class);
        sendMessage.putExtra(Utils.accidentKey,this.accidentModel);
        startActivity(sendMessage);
    }

    public void deleteMessage(View view){
        Thread t= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(Utils.webserviceUrl+"/api/accidents/"+accidentModel.getid()+"/");
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("DELETE");
                    http.connect();
                   // finish(); //retourne à l'activité précedente
                    Intent i= new Intent(DetailsAccidentActivity.this,MainActivity.class);
                    startActivity(i);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }
}
