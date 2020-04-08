package ihm.accidents.activities;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ihm.accidents.adapters.AdresseAutoCompleteAdapter;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.utils.Utils;
import my.ihm.exercice6.R;


public class CreationAccidentActivity extends AppCompatActivity {
    private static final String TAG = "CreationAccidentActivit";
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private int notificationID=1;
    private Bitmap image;
    private String pathToPhoto = null;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: We don't have permissions to ACCESS COARSE LOCATION");
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_accident);
        imageView=findViewById(R.id.photoView);
        AutoCompleteTextView adressView= findViewById(R.id.adresse);
        adressView.setAdapter(new AdresseAutoCompleteAdapter(this,android.R.layout.simple_dropdown_item_1line));

    }

    private String getTableValue(String idd){
        EditText editText=findViewById(R.id.adresse);
        InputMethodManager imm=(InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        return "";
    }


    public void onButtonCreationCliked(View button) {
        EditText editTextAdresse = findViewById(R.id.adresse);
        Spinner editTextType = findViewById(R.id.type);
        EditText editTextCommentaire = findViewById(R.id.commentaire);
        InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextAdresse.getWindowToken(), 0);
        String adresse;
        try {
            adresse = editTextAdresse.getText() + "";
        } catch (StringIndexOutOfBoundsException e) {
            adresse = "";
        }
        imm.hideSoftInputFromWindow(editTextType.getWindowToken(), 0);
        String type;
        try {
            type = editTextType.getSelectedItem() + "";
        } catch (StringIndexOutOfBoundsException e) {
            type = "";
        }
        imm.hideSoftInputFromWindow(editTextCommentaire.getWindowToken(), 0);
        String commentaire;
        commentaire = editTextCommentaire.getText() + "";
        String image64="";
        if(image!=null) {
            image64=Utils.convertTo64(image);
        }
        if (!adresse.equals("") && !type.equals("")) {
            AccidentModel accidentModel = new AccidentModel("", adresse, type, commentaire, image64);
            Toast.makeText(this,"accident créé", Toast.LENGTH_LONG ).show();
            Utils.list.add(accidentModel);
            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);


        }
    }

    public void goBack(View v){
        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: ALL GOOD");
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
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
                Uri photoURI = FileProvider.getUriForFile(CreationAccidentActivity.this, "com.eat_with_us", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePic, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageView=findViewById(R.id.photoView);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
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


}
