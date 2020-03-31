package my.myself.exercice6;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import java.util.Objects;

public class IncidentApplication extends Application {
    public static String channelId="CHANNEL_ACCIDENT";
    private static NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationChannel channel=createNotificationChannel(channelId,"Name of Channel Accident", NotificationManager.IMPORTANCE_HIGH,"Le channel de notification pour accident");
        Log.d("NOTIFCATION_CHANNEL",channel.toString());
        notificationManager = getSystemService(NotificationManager.class);
        Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
    }

    private NotificationChannel createNotificationChannel(String channelId, CharSequence name, int importance, String channelDescription) {
        // Créer le NotificationChannel, seulement pour API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(channelDescription);
            return channel;
        }
        return null;
    }

}
