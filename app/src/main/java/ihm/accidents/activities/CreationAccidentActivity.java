package ihm.accidents.activities;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.utils.Utils;
import my.ihm.exercice6.R;


public class CreationAccidentActivity extends AppCompatActivity {
    private int notificationID=1;
    private Bitmap image;
    private String pathToPhoto = null;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        imageView=findViewById(R.id.photoView);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creation_accident);
    }

    private String getTableValue(String idd){
        EditText editText=findViewById(R.id.adresse);
        InputMethodManager imm=(InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        return "";
    }


    public void onButtonCreationCliked(View button) {
        EditText editTextAdresse = findViewById(R.id.adresse);
        EditText editTextType = findViewById(R.id.type);
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
            type = editTextType.getText() + "";
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
