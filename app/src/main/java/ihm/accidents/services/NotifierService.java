package ihm.accidents.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import ihm.accidents.activities.DetailsAccidentActivity;
import ihm.accidents.activities.MultipleDetailsActivity;
import ihm.accidents.application.IncidentApplication;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.utils.KeysTags;
import ihm.accidents.utils.Utils;
import ihm.accidents.R;

public class NotifierService extends Worker {
    private final int notificationID=1;
    private static final String TAG = "NotifierService";
    private final AccidentDownloader accidentDownloader=new AccidentDownloader();
    public NotifierService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    private void writeLastUpdateDateToFile(){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                KeysTags.preferencesFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(KeysTags.dateLastNotifUpdateKey,System.currentTimeMillis());
        editor.apply();
    }
    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: worker called");


            FusedLocationProviderClient fusedLocationProvider= LocationServices.getFusedLocationProviderClient(getApplicationContext());
            Task<Location> locationTask= fusedLocationProvider.getLastLocation();
            while (!locationTask.isComplete()){
                Log.d(TAG, "doWork: waiting for location to be ready" );
            }

        try {
            prepareNotification(locationTask.getResult());
        } catch (IOException e) {

            Log.e(TAG, "doWork: retrying ",e );
            return Result.retry();
        } catch (JSONException e) {
            Log.e(TAG, "doWork: ",e );
        }

        Log.d(TAG, "doWork: work finished");
        writeLastUpdateDateToFile();
        return Result.success();

    }


    private void prepareNotification(Location location) throws IOException, JSONException {
        Log.d(TAG, "prepareNotification: Location found :"+location);
        try{
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                    KeysTags.preferencesFile, Context.MODE_PRIVATE);

            long defDate=System.currentTimeMillis()-20*60*1000;
            Log.d(TAG, "prepareNotification: defaultLastUpdateValue: "+defDate);

            long lastUpdate=sharedPref.getLong(KeysTags.dateLastNotifUpdateKey,defDate);
            Log.d(TAG, "prepareNotification: lastUpdate: "+lastUpdate);
            long deviceId=sharedPref.getLong(KeysTags.deviceIdKey,new Random().nextLong());
            Log.d(TAG, "prepareNotification: deviceId:"+deviceId);
            int radius=sharedPref.getInt(KeysTags.notifRadiusKey,30) *1000;
            //we download the accidents Synchronously since we are already in another thread
            List<AccidentModel> accidents=accidentDownloader.accidentsFromServerSync();
            Log.d(TAG, "prepareNotification: without filtering "+accidents);
            accidents=accidents.stream().filter(accidentModel -> accidentModel.getDate() > lastUpdate && accidentModel.getDeviceId() != deviceId ).collect(Collectors.toList());
            Log.d(TAG, "prepareNotification: with initial filtering "+accidents);
            //if we have the location we add a filtering according to it
            if(location!=null) {
                accidents=accidents.stream().filter(accident -> accident.distanceTo(location) <= radius ).collect(Collectors.toList());
            }
            Log.d(TAG, "prepareNotification: with location filtering "+accidents);
            if(!accidents.isEmpty()) {
                if(accidents.size()==1){
                    Intent resultIntent = new Intent(getApplicationContext(), DetailsAccidentActivity.class);
                    //we get the first and only accident
                    AccidentModel accident= accidents.get(0);

                    resultIntent.putExtra(Utils.accidentKey,accident);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    stackBuilder.addNextIntentWithParentStack(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                    RemoteViews custoNotif= new RemoteViews(getApplicationContext().getPackageName(),R.layout.notification);
                    custoNotif.setTextViewText(R.id.title_accident_notif,"Type d'incident:" +accident.getType());
                    custoNotif.setTextViewText(R.id.detail_accident_notif,accident.getDetails());

                    Glide.with(getApplicationContext()).asBitmap().load(accident.getImageUrl()).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Log.d(TAG, "onResourceReady: the Bitmap is ready");
                            custoNotif.setImageViewBitmap(R.id.preview_image_notif,resource);
                            NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(getApplicationContext(), IncidentApplication.channelId)
                                    .setAutoCancel(true)
                                    .setDefaults(Notification.DEFAULT_ALL)
                                    .setWhen(System.currentTimeMillis())
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setCustomContentView(custoNotif);
                            notifBuilder.setContentIntent(resultPendingIntent);
                            NotificationManagerCompat.from(getApplicationContext()).notify(notificationID, notifBuilder.build());
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });

                }
                else{
                    Intent resultIntent = new Intent(getApplicationContext(), MultipleDetailsActivity.class);
                    resultIntent.putParcelableArrayListExtra(Utils.accidentKey, new ArrayList<>(accidents));
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    stackBuilder.addNextIntentWithParentStack(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                    RemoteViews custoNotif= new RemoteViews(getApplicationContext().getPackageName(),R.layout.multiple_notification);
                    custoNotif.setTextViewText(R.id.recap_nb_notifs,getApplicationContext().getString(R.string.nouveaux_incidents_str,accidents.size()));

                    custoNotif.setTextViewText(R.id.list_types_incidents,accidents.stream().map(accident-> accident.getType()).reduce((a,b)-> a+" , "+b).get());
                    NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(getApplicationContext(), IncidentApplication.channelId)
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setCustomContentView(custoNotif);
                    notifBuilder.setContentIntent(resultPendingIntent);
                    NotificationManagerCompat.from(getApplicationContext()).notify(notificationID, notifBuilder.build());
                }


            }




        }
        catch (Exception e){
            Log.e(TAG, "doWork: an exception occurred ", e);
            throw  e;
        }
        }


}
