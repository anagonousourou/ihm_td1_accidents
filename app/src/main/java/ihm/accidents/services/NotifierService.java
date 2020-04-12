package ihm.accidents.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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

import java.util.List;
import java.util.stream.Collectors;

import ihm.accidents.activities.DetailsAccidentActivity;
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
        try{
            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                    KeysTags.preferencesFile, Context.MODE_PRIVATE);
            long lastUpdate=sharedPref.getLong(KeysTags.dateLastNotifUpdateKey,System.currentTimeMillis()-16*60*1000);
                //we download the accidents Synchronously since we are already in another thread
                List<AccidentModel> accidents=accidentDownloader.accidentsFromServerSync().stream().filter(accidentModel -> accidentModel.getDate() > lastUpdate).collect(Collectors.toList());
                if(accidents.isEmpty()){
                    //if there is no accident we return success
                    writeLastUpdateDateToFile();
                    return Result.success();
                }
                else{
                    Intent resultIntent = new Intent(getApplicationContext(), DetailsAccidentActivity.class);
                    //we get the first accident
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

                    writeLastUpdateDateToFile();
                    return Result.success();
                }




        }
        catch (Exception e){
            Log.e(TAG, "doWork: an exception occurred ", e);
            return Result.retry();
        }


    }
}
