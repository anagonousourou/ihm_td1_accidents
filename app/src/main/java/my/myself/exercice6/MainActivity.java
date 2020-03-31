package my.myself.exercice6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import org.osmdroid.views.MapView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RemoteViews;

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
        Intent i = new Intent(this, NotificationModel.class);
        i.putExtra("notificationID", notificationID);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);



        NotificationCompat.Builder notifBuilder;

        RemoteViews custoNotif= new RemoteViews(getPackageName(),R.layout.notification);

        notifBuilder = new NotificationCompat.Builder(getApplicationContext(),IncidentApplication.channelId)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setCustomContentView(custoNotif);
        NotificationManagerCompat.from(this).notify(notificationID, notifBuilder.build());

    }


}
