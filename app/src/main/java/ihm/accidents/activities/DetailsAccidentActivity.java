package ihm.accidents.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ihm.accidents.R;
import ihm.accidents.databinding.DetailsAccidentActivityBinding;
import ihm.accidents.fragments.DetailsAccidentFragment;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.services.AccidentDeleter;
import ihm.accidents.utils.Utils;
import okhttp3.OkHttpClient;


/**
 * To present the details of an Accident wherever the user clicks on a
 * notification or whatever else in the flow of the appplication
 */
public class DetailsAccidentActivity extends AppCompatActivity {
    private static final String TAG = "DetailsAccidentActivity";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_accident_activity_wrapper);
        Intent intent = getIntent();
        AccidentModel accidentModel = intent.getParcelableExtra(Utils.accidentKey);
        DetailsAccidentFragment fragment=new DetailsAccidentFragment(accidentModel);

        getSupportFragmentManager().beginTransaction().replace(R.id.placeHolderDetailsFragment,fragment).commit();
        Log.d(TAG, "onCreate: "+accidentModel.toString());



    }


}
