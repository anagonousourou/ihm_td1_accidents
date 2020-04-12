package ihm.accidents.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import ihm.accidents.activities.DetailsAccidentActivity;
import ihm.accidents.application.IncidentApplication;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.utils.Utils;
import ihm.accidents.R;

public class NotifierService extends Worker {
    private int notificationID=1;
    private static final String TAG = "NotifierService";
    public NotifierService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try{

            Intent resultIntent = new Intent(getApplicationContext(), DetailsAccidentActivity.class);
            resultIntent.putExtra(Utils.accidentKey,Utils.accidentModelFake);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addNextIntentWithParentStack(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notifBuilder;
            RemoteViews custoNotif= new RemoteViews(getApplicationContext().getPackageName(),R.layout.notification);
            notifBuilder = new NotificationCompat.Builder(getApplicationContext(), IncidentApplication.channelId)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setCustomContentView(custoNotif);
            notifBuilder.setContentIntent(resultPendingIntent);
            NotificationManagerCompat.from(getApplicationContext()).notify(notificationID, notifBuilder.build());
            return Result.success();
        }
        catch (Exception e){
            Log.e(TAG, "doWork: an exception occurred ", e);
            return Result.retry();
        }

    }
}
