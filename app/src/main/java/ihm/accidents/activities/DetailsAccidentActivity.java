package ihm.accidents.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


import ihm.accidents.databinding.DetailsAccidentActivityBinding;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.utils.Utils;
import ihm.accidents.R;


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
}
