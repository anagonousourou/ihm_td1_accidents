package ihm.accidents.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.WorkManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ihm.accidents.R;
import ihm.accidents.utils.KeysTags;

public class ParameterActivity extends IhmAbstractActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "ParameterActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parameter_activity);
        setTitle("Paramètres");
        Spinner spinner=findViewById(R.id.distance_notif_control);
        spinner.setOnItemSelectedListener(this);
        loadSwitchStateFromPreferences();
        loadSpinnerValueFromPreferences(spinner);
        loadSwitchActualImagesStateFromPreferences();

    }

    private void loadSwitchStateFromPreferences(){
        Switch switchControl =findViewById(R.id.switch_notif);
        SharedPreferences sharedPreferences=getSharedPreferences(KeysTags.preferencesFile, Context.MODE_PRIVATE);
        boolean value=sharedPreferences.getBoolean(KeysTags.notifEnabledKey,true);
        switchControl.setChecked(value);
        findViewById(R.id.distance_notif_control).setEnabled(value);

    }


    private void loadSwitchActualImagesStateFromPreferences(){
        Switch switchControl=findViewById(R.id.switch_actual_images);
        Log.d(TAG, "loadSwitchActualImagesStateFromPreferences: "+this.preferenceService.hideActualPictures());
        switchControl.setChecked(this.preferenceService.hideActualPictures());

    }

    private void loadSpinnerValueFromPreferences(Spinner spinner){
        SharedPreferences sharedPreferences=getSharedPreferences(KeysTags.preferencesFile, Context.MODE_PRIVATE);
        int lastValue=sharedPreferences.getInt(KeysTags.notifRadiusKey,5);//
        SpinnerAdapter adapter =spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if(adapter.getItem(i).toString().equals(String.valueOf(lastValue))){
                spinner.setSelection(i);
                return;
            }
        }
    }

    public void toggleNotificationEnable(View view) {
        Switch switchControl=(Switch)view;
        SharedPreferences sharedPreferences=getSharedPreferences(KeysTags.preferencesFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Log.d(TAG, "toggleNotificationEnable: "+switchControl.isChecked());
        if(switchControl.isChecked()){
            editor.putBoolean(KeysTags.notifEnabledKey,true);
            findViewById(R.id.distance_notif_control).setEnabled(true);
            editor.apply();
            //relancer les notifications
            setUpNotifierService();
        }
        else{
            editor.putBoolean(KeysTags.notifEnabledKey,false);

            findViewById(R.id.distance_notif_control).setEnabled(false);
            editor.apply();
            //arreter les notifications
            WorkManager.getInstance(this).cancelAllWorkByTag(KeysTags.notifierWorkTag);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int rayon= Integer.parseInt(parent.getItemAtPosition(position).toString());
            Log.d(TAG, "onItemSelected: "+rayon);
        SharedPreferences sharedPreferences=getSharedPreferences(KeysTags.preferencesFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(KeysTags.notifRadiusKey,rayon);
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void toggleHideActualImages(View view) {
        Switch switchcontrol=(Switch)view;
        this.preferenceService.setHideActualPictures( switchcontrol.isChecked() );
    }
}
