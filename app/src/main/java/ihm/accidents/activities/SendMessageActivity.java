package ihm.accidents.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ihm.accidents.models.AccidentModel;
import ihm.accidents.utils.Utils;
import my.ihm.exercice6.R;

public class SendMessageActivity extends AppCompatActivity {

    private static final String TAG = "SendMessageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
    }


    public void pickContact(View view) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {

        super.onActivityResult(requestCode, resultCode, resultIntent);
        if (requestCode == 1) {

            if (resultCode == RESULT_OK) {
                Uri contactUri = resultIntent.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                assert contactUri != null;
                Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);
                EditText tel = findViewById(R.id.tel);
                tel.setText(number);
            }
        }
    }

    public void send(View view) {
        Intent intent = getIntent();
        AccidentModel accidentModel = intent.getParcelableExtra(Utils.accidentKey);
        EditText number = findViewById(R.id.tel);
        //String message = "Attention\n"+accidentModel.getTitle()+"\nA l'adresse: "+accidentModel.getAddress();
        String message = "say hello";
        Log.d(TAG, message);
        Log.d(TAG, accidentModel.toString());
        Log.d(TAG, accidentModel.toJSONString());

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number.getText().toString(),null,message,null,null);
        Toast.makeText(this, "Votre message a été envoyé" , Toast.LENGTH_LONG).show();
        Intent goback = new Intent(SendMessageActivity.this,MainActivity.class);
        startActivity(goback);
    }
}