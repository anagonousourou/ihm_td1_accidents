package ihm.accidents.activities;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import ihm.accidents.services.NotifierService;
import ihm.accidents.utils.KeysTags;


public abstract class IhmAbstractActivity extends AppCompatActivity {
    private static final String TAG = "IhmAbstractActivity";


    protected long getDeviceId(){
        SharedPreferences sharedPref = this.getSharedPreferences(
                KeysTags.preferencesFile, Context.MODE_PRIVATE);
        return sharedPref.getLong(KeysTags.deviceIdKey, new Random().nextLong());
    }

    protected void setUpNotifierService(){

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)//besoin de connexion internet
                .setRequiresBatteryNotLow(true)//quand la batterie n'est pas faible
                .build();

        PeriodicWorkRequest saveRequest =
                new PeriodicWorkRequest.Builder(NotifierService.class, 15, TimeUnit.MINUTES)
                        .setConstraints(constraints)

                        .addTag(KeysTags.notifierWorkTag)


                        .build();

        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork ("Notifier", ExistingPeriodicWorkPolicy.KEEP, saveRequest);
    }



}
