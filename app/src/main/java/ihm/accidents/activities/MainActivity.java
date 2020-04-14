package ihm.accidents.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import ihm.accidents.R;
import ihm.accidents.fragments.MapFragment;
import ihm.accidents.services.NotifierService;
import ihm.accidents.utils.KeysTags;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    protected static final int PERMISSION_ACCESS_FINE_LOCATION = 2;
    protected static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.choixTrajetItem){
            Intent intent=new Intent(this, ChoicePathActivity.class );

            this.startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //On commence par vérifier si on a les permissions de Localisation et on les demande si besoin
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "retrieveLocationAndPlug: We don't have permissions to ACCESS COARSE LOCATION");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "retrieveLocationAndPlug: We don't have permissions to ACCESS FINE LOCATION");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
        }
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        org.osmdroid.config.IConfigurationProvider osmConf = org.osmdroid.config.Configuration.getInstance();
        File basePath = new File(this.getCacheDir().getAbsolutePath(), "osmdroid");
        osmConf.setOsmdroidBasePath(basePath);
        File tileCache = new File(osmConf.getOsmdroidBasePath().getAbsolutePath(), "tile");
        osmConf.setOsmdroidTileCache(tileCache);
            Fragment fragmentMap = new MapFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.placeHolderMapFragment, fragmentMap).commit();


        setUpNotifierService();
        setUpPreferencesFile();
        setUpDeviceId();

    }

    private void setUpNotifierService(){
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)//besoin de connexion internet
                .setRequiresBatteryNotLow(true)//quand la batterie n'est pas faible
                .build();

        PeriodicWorkRequest saveRequest =
                new PeriodicWorkRequest.Builder(NotifierService.class, 16, TimeUnit.MINUTES)
                        .setConstraints(constraints)


                        .build();

        WorkManager.getInstance(this)
                .enqueueUniquePeriodicWork ("Notifier", ExistingPeriodicWorkPolicy.KEEP, saveRequest);
    }

    private void setUpPreferencesFile(){
        SharedPreferences sharedPref = this.getSharedPreferences(
                KeysTags.preferencesFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(!sharedPref.contains(KeysTags.dateLastNotifUpdateKey)){
            editor.putLong(KeysTags.dateLastNotifUpdateKey, System.currentTimeMillis());
            editor.apply();
        }


    }

    private void setUpDeviceId(){
        Random rand=new Random();
        SharedPreferences sharedPref = this.getSharedPreferences(
                KeysTags.preferencesFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(!sharedPref.contains(KeysTags.deviceIdKey)){
            editor.putLong(KeysTags.deviceIdKey,rand.nextLong());
            editor.apply();
        }
    }


    public void onClickListAccidents(View view) {
        Intent intent = new Intent(this, ListIncidentActivity.class);
        this.startActivity(intent);
    }

    public void goToCreation(View v){
        Intent intent=new Intent(getApplicationContext(), CreationAccidentActivity.class);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: ALL GOOD");
                } else {
                    Toast.makeText(this, "Need your coarse location!", Toast.LENGTH_SHORT).show();
                }

                break;
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: ALL GOOD WE HAVE FINE LOCATION ");
                } else {
                    Toast.makeText(this, "Need your fine location!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }



}
