package ihm.accidents.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Utils {
    public static final String accidentKey = "accidentData";
    public static final long secondsInDay = 24 * 60 * 60;
    public static final long secondsInMinute = 60;
    public static final long secondsInHour = 60 * 60;

    private Utils() {

    }

    public static String scaleReached(long seconds) {
        if (seconds >= secondsInDay) {
            return "day";
        }
        if (seconds >= secondsInHour) {
            return "heure";
        }
        if (seconds >= secondsInMinute) {
            return "minute";
        }
        return "second";
    }

    public static int convertToHighestScalePossible(long seconds) {
        if (seconds >= secondsInDay) {
            return (int) (seconds / secondsInDay);
        }
        if (seconds >= secondsInHour) {
            return (int) (seconds / secondsInHour);
        }
        if (seconds >= secondsInMinute) {
            return (int) (seconds / secondsInMinute);
        }
        return (int) seconds;
    }

    public static Bitmap fromBase64(String base64){
        String pureBase64Encoded = base64.substring(base64.indexOf(",")  + 1);
        byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
