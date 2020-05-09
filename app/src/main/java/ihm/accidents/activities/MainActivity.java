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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import ihm.accidents.R;
import ihm.accidents.fragments.MapFragment;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.services.NotifierService;
import ihm.accidents.utils.KeysTags;
import ihm.accidents.utils.Utils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends IhmAbstractActivity {


    private static final String TAG = "MainActivity";
    private Random rand=new Random();


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
        if(item.getItemId()==R.id.parameterItem){
            Intent intent=new Intent(this, ParameterActivity.class );

            this.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

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
        
        setUpDeviceId();
        setUpImagePreferenceDefault();


    }


    private void setUpImagePreferenceDefault(){
        if(!this.preferenceService.hideActualPicturesExists()){
            this.preferenceService.setHideActualPictures(false);
        }

    }

    private void setUpDeviceId(){
        
        SharedPreferences sharedPref = this.getSharedPreferences(
                KeysTags.preferencesFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if(!sharedPref.contains(KeysTags.deviceIdKey)){
            editor.putLong(KeysTags.deviceIdKey,this.rand.nextLong());
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





}
