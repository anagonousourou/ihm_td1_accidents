package ihm.accidents.activities;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import ihm.accidents.utils.KeysTags;


public abstract class IhmAbstractActivity extends AppCompatActivity {
    private static final String TAG = "IhmAbstractActivity";


    protected long getDeviceId(){
        SharedPreferences sharedPref = this.getSharedPreferences(
                KeysTags.preferencesFile, Context.MODE_PRIVATE);
        return sharedPref.getLong(KeysTags.deviceIdKey, new Random().nextLong());
    }



}
