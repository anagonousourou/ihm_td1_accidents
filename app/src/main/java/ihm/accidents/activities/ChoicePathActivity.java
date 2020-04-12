package ihm.accidents.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;

import ihm.accidents.R;
import ihm.accidents.fragments.EditeurTrajetFragment;
import ihm.accidents.fragments.MapFragment;
import ihm.accidents.utils.Utils;

public class ChoicePathActivity extends AppCompatActivity {
    private static final String TAG = "ChoicePathActivity";
    protected static final int PERMISSION_ACCESS_FINE_LOCATION = 2;
    protected static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.second_menu,menu);
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        setContentView(R.layout.choice_path_activity);

        //on obtient un object fusedLocationProviderClient
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //le fragment de la carte
        Fragment fragmentMap=new MapFragment();
        //le fragment pour éditer
        Fragment fragmentEditeur=new EditeurTrajetFragment();
        Log.d(TAG, "onCreate: "+ fusedLocationClient);

        //le getLastLocation peut lever une exception si on a pas les permissions ce qui
        //peut etre le cas si c'est la premiere fois qu'on ouvre l'activité
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            Bundle locationBundle=new Bundle();
            //la location peut être nulle mais on laisse le fragmentEditeur se débrouiller avec ça
            Log.d(TAG, "onCreate: OnSucessGetLastlocation "+location);
            locationBundle.putParcelable(Utils.locationKey,location);

            fragmentEditeur.setArguments(locationBundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.placeHolderMapFragment ,fragmentMap).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.placeHolderEditeurFragment ,fragmentEditeur).commit();
        }).addOnFailureListener(e->{
            Toast.makeText(this,"Cannot get your location",Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onCreate Exception : ",e );
            getSupportFragmentManager().beginTransaction().replace(R.id.placeHolderMapFragment ,fragmentMap).commit();
            getSupportFragmentManager().beginTransaction().replace(R.id.placeHolderEditeurFragment ,fragmentEditeur).commit();
        });







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
