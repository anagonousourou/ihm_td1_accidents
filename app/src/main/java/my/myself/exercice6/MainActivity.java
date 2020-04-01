package my.myself.exercice6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;

import org.osmdroid.views.MapView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RemoteViews;

import my.myself.accidents.models.AccidentModel;
import my.myself.accidents.models.NotificationModel;

public class MainActivity extends AppCompatActivity {
    private  MapView mapView;
    private int notificationID=1;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.choixTrajetItem){
            Intent intent=new Intent(this,ChoicePathActivity.class );

            this.startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Fragment fragmentMap=new SomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.placeHolderMapFragment ,fragmentMap).commit();




    }

    public void displayNotificationTest(View view){
        //---PendingIntent to launch activity if the user selects
        // this notification---
        AccidentModel accidentModelFake=new AccidentModel("Breaking News","Polytech Nice Sophia","solo","Un étudiant est tombé de son vélo. :) mdr","",
                1585743847.0);
        Intent resultIntent = new Intent(this, DetailsAccidentActivity.class);
        resultIntent.putExtra(Utils.accidentKey,accidentModelFake);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationCompat.Builder notifBuilder;

        RemoteViews custoNotif= new RemoteViews(getPackageName(),R.layout.notification);

        notifBuilder = new NotificationCompat.Builder(getApplicationContext(),IncidentApplication.channelId)
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
