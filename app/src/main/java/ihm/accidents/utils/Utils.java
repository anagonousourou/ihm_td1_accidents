package ihm.accidents.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ihm.accidents.models.AccidentModel;

public class Utils {
    public static final String accidentKey = "accidentData";
    public static final String locationKey="location";
    public static final long secondsInDay = 24 * 60 * 60;
    public static final long secondsInMinute = 60;
    public static final long secondsInHour = 60 * 60;
    public static final String webserviceUrl="https://webserviceinfoincident.herokuapp.com";
    public static final String apiKeyOpencagedata="d23bb7fdffb74ee086bd061c3fa99264";
    public static final String urlApiOpencageData="https://api.opencagedata.com/geocode/v1/json?key=";
    public static final AccidentModel accidentModelFake=new AccidentModel("Placeholder image selected","Wonderland","solo","Alice vient de trébucher sur le chat de Cheschire. La Reine s'en vient. à suivre :)",
            Placeholders.img_placeholder1);

    public static final AccidentModel accidentModelFake2=new AccidentModel("Breaking News","Polytech Nice Sophia","solo","OMG, Un étudiant est tombé de son quad. :) mdr",
            Placeholders.img_placeholder2);


    private Utils() {

    }

    public static String getRevereseGeocodingUrl(double latitude,double longitude) throws UnsupportedEncodingException {
        return urlApiOpencageData+apiKeyOpencagedata+"&q=" +URLEncoder.encode(+latitude+","+longitude, "UTF-8");
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

    public static Bitmap accessBitmap(String pathToPhoto){
        Bitmap image = BitmapFactory.decodeFile(pathToPhoto);
        return image;
    }


    public static String convertTo64(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 1, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }
}
