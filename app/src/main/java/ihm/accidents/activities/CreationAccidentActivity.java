package ihm.accidents.activities;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RemoteViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ihm.accidents.application.IncidentApplication;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.utils.Utils;
import my.ihm.exercice6.R;

public class CreationAccidentActivity extends AppCompatActivity {
    private int notificationID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
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
        EditText editTextAdresse=findViewById(R.id.adresse);
        EditText editTextType=findViewById(R.id.type);
        EditText editTextCommentaire=findViewById(R.id.commentaire);
        InputMethodManager imm=(InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextAdresse.getWindowToken(), 0);
        String adresse;
        try{
            adresse=editTextAdresse.getText()+"";
        }catch (StringIndexOutOfBoundsException e){
            adresse="";
        }
        imm.hideSoftInputFromWindow(editTextType.getWindowToken(), 0);
        String type;
        try{
            type=editTextType.getText()+"";
        }catch (StringIndexOutOfBoundsException e){
            type="";
        }
        imm.hideSoftInputFromWindow(editTextCommentaire.getWindowToken(), 0);
        String commentaire;
        commentaire=editTextCommentaire.getText()+"";
        if(!adresse.equals("")&&!type.equals("")){
            AccidentModel accidentModel=new AccidentModel("",adresse,type,commentaire,"");
            Intent resultIntent = new Intent(this, DetailsAccidentActivity.class);
            resultIntent.putExtra(Utils.accidentKey,accidentModel);
            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);



            NotificationCompat.Builder notifBuilder;

            RemoteViews custoNotif= new RemoteViews(getPackageName(),R.layout.notification);

            notifBuilder = new NotificationCompat.Builder(getApplicationContext(), IncidentApplication.channelId)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setCustomContentView(custoNotif);

            //pour préciser quel activité est lancée lorsqu'on click sur la notification
            notifBuilder.setContentIntent(resultPendingIntent);

            NotificationManagerCompat.from(this).notify(notificationID, notifBuilder.build());

        }



    }

}
