package ihm.accidents.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ihm.accidents.adapters.AdresseAutoCompleteAdapter;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.services.AccidentUploader;
import ihm.accidents.utils.Utils;
import ihm.accidents.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CreationAccidentActivity extends AppCompatActivity implements Callback {
    private static final String TAG = "CreationAccidentActivit";
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 2;
    private LocationManager locationManager;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private int notificationID = 1;
    private Bitmap image;
    private String pathToPhoto = null;
    private File photoFile=null;
    private AutoCompleteTextView adresseTextView;
    private ImageView imageView;
    private final OkHttpClient client = new OkHttpClient();
    private final AccidentUploader accidentUploader=new AccidentUploader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.creation_accident);
        imageView = findViewById(R.id.photoView);
        this.adresseTextView = findViewById(R.id.adresse);
        this.adresseTextView.setAdapter(new AdresseAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));

    }

    private String getTableValue(String idd) {
        EditText editText = findViewById(R.id.adresse);
        getApplicationContext();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        return "";
    }


    public void onButtonCreationCliked(View button) {
        EditText editTextAdresse = findViewById(R.id.adresse);
        Spinner editTextType = findViewById(R.id.type);
        EditText editTextCommentaire = findViewById(R.id.commentaire);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextAdresse.getWindowToken(), 0);
        String adresse;
        try {
            adresse = editTextAdresse.getText() + "";
        } catch (StringIndexOutOfBoundsException e) {
            adresse = "";
        }
        imm.hideSoftInputFromWindow(editTextType.getWindowToken(), 0);
        String type = editTextType.getSelectedItem() + "";

        imm.hideSoftInputFromWindow(editTextCommentaire.getWindowToken(), 0);
        String commentaire = editTextCommentaire.getText() + "";
        if (!adresse.equals("") && !type.equals("")) {

            AccidentModel accidentModel = new AccidentModel("", adresse, type, commentaire);
            Log.d(TAG, "onButtonCreationCliked: "+photoFile);
            accidentUploader.postAccidentToServer(photoFile,accidentModel);
            Toast.makeText(this, "accident créé", Toast.LENGTH_LONG).show();
            Utils.list.add(accidentModel);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);


        }
    }

    public void goBack(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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


    public void takePic(View v) {
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePic.resolveActivity(getPackageManager()) != null) {
            File photoFile;
            photoFile = createPhotoFile();
            if (photoFile != null) {
                pathToPhoto = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(CreationAccidentActivity.this, "ihm.accidents", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePic, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageView = findViewById(R.id.photoView);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                photoFile = new File(pathToPhoto);
                image = BitmapFactory.decodeFile(pathToPhoto);
                imageView.setImageBitmap(image);
            }
        }
    }

    private File createPhotoFile() {
        String name = new SimpleDateFormat("yyyyMMdd_MMmmaa").format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(name, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private Location getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_ACCESS_COARSE_LOCATION);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);

        }
        for (String provider : providers) {

            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }



    public void retrieveLocationAndPlug(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "retrieveLocationAndPlug: We don't have permissions to ACCESS COARSE LOCATION");
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    PERMISSION_ACCESS_FINE_LOCATION);
        }
        else{
            Log.d(TAG, "retrieveLocationAndPlug: We have the permissions for location");
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if(locationManager!=null){
                Location location=this.getLastKnownLocation();
                if(location!=null){
                    try {
                        String url=Utils.getRevereseGeocodingUrl(location.getLatitude(),location.getLongitude());
                        Log.d(TAG, "retrieveLocationAndPlug: "+url);
                        Request request = new Request.Builder().url(url).build();

                        client.newCall(request).enqueue(this);
                    }
                    catch (UnsupportedEncodingException exception){
                        Log.e(TAG, "retrieveLocationAndPlug: ",exception );
                    }
                }
                else{
                    Log.d(TAG, "retrieveLocationAndPlug: location was null");
                    Toast.makeText(this,"Could not get your location",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Log.d(TAG, "retrieveLocationAndPlug: locationManager was null");
                Toast.makeText(this,"Could not get your location",Toast.LENGTH_SHORT).show();
            }






        }
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {

    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (!response.isSuccessful()) {
            runOnUiThread(()->
                Toast.makeText(this,"Could not get your adress, sorry",Toast.LENGTH_SHORT).show()
            );
        }

        final String body = response.body().string();


        try {

            final String adresse = new JSONObject(body).getJSONArray("results").getJSONObject(0).getString("formatted");
            runOnUiThread(() -> {
                adresseTextView.setText(adresse);

            });
        } catch (JSONException e) {
            runOnUiThread(()->
                    Toast.makeText(this,"Could not get your adress, sorry",Toast.LENGTH_SHORT).show()
            );
            Log.e(TAG, "onResponse: ",e );
        }



    }
}
