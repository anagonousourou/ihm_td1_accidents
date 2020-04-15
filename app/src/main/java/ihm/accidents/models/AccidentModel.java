package ihm.accidents.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import java.util.ArrayList;
import java.util.List;

import ihm.accidents.utils.KeysTags;
import ihm.accidents.utils.Utils;


public class AccidentModel implements Parcelable, JSONString {
    private static final String TAG = "AccidentModel";
    private int id;
    private String title;
    private String address;
    private String type;
    private String details;
    private String imageUrl= "https://www.nswcompensationlawyers.com.au/media/Video-Placeholder-Motor-Accident-1024x576.png";
    private long date;
    private long deviceId;

    @NotNull
    @Override
    public String toString() {
        return "AccidentModel{" +
                "title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", type='" + type + '\'' +
                ", date=" + date +
                '}';
    }

    protected AccidentModel(Parcel in) {
        title=in.readString();
        address = in.readString();
        type = in.readString();
        details = in.readString();
        imageUrl = in.readString();
        date = in.readLong();
        deviceId=in.readLong();
    }

    public AccidentModel(String title, String address, String type, String details, String url,long deviceId){
        this.title=title;
        this.address=address;
        this.type=type;
        this.details=details;
        this.imageUrl=url;
        this.date= System.currentTimeMillis();
        this.deviceId=deviceId;

    }

    public AccidentModel(String title, String address, String type, String details, String url){
        this.title=title;
        this.address=address;
        this.type=type;
        this.details=details;
        this.imageUrl=url;
        this.date= System.currentTimeMillis();
    }

    public AccidentModel(String title, String address, String type, String details){
        this.title=title;
        this.address=address;
        this.type=type;
        this.details=details;
        this.date= System.currentTimeMillis();

    }



    public AccidentModel(String title, String address, String type, String details, String imageUrl, long date,long deviceId,int accidentId){
        this.title=title;
        this.address=address;
        this.type=type;
        this.details=details;
        this.imageUrl = imageUrl;
        this.date= date;
        this.deviceId=deviceId;
        this.id=accidentId;
    }

    public static AccidentModel fromJson(String jsonString) {
        try{
            JSONObject accidentJson = new JSONObject(jsonString);

            if(accidentJson.has(KeysTags.addressKey) && accidentJson.has(KeysTags.commentKey) && accidentJson.has(KeysTags.deviceIdKey) &&
                    accidentJson.has(KeysTags.dateKey)&& accidentJson.has(KeysTags.imageUrlKey) && accidentJson.has(KeysTags.typeKey)){
                return new AccidentModel(
                        "",
                        accidentJson.getString(KeysTags.addressKey),
                        accidentJson.getString(KeysTags.typeKey),
                        accidentJson.getString(KeysTags.commentKey),
                        accidentJson.getString("imageUrl").startsWith("http")?accidentJson.getString("imageUrl"):Utils.webserviceUrl+"/"+ accidentJson.getString("imageUrl"),
                        accidentJson.getLong(KeysTags.dateKey),
                        accidentJson.getLong(KeysTags.deviceIdKey),
                        accidentJson.getInt(KeysTags.idKey)
                );
            }
        }
        catch (Exception e){
            Log.e(TAG, "fromJson: ",e );
            return null;
        }
        return null;
    }

    public static List<AccidentModel> listFromJson(String jsonString){
        List<AccidentModel> accidents=new ArrayList<>();

        try {
            JSONArray accidentsJson = new JSONArray(jsonString);
            for (int i = 0; i < accidentsJson.length(); i++) {
                JSONObject accidentJson=accidentsJson.getJSONObject(i);
                AccidentModel accident= AccidentModel.fromJson(accidentJson.toString());
                if(accident!=null){
                    accidents.add(accident);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "listFromJson: input not correct", e);
            return accidents;
        }

        return accidents;
    }


    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getType() {
        return type;
    }

    public String getDetails() {
        return details;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getDate() {
        return date;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public String userFormatDate(){
         long difftime= System.currentTimeMillis() - this.date;

         if(difftime == 0){
             return "A l'instant";
         }
         if(difftime > 0){
            return "Il y a "+Utils.convertToHighestScalePossible(difftime)+" "+Utils.scaleReached(difftime);
         }

        return "Dans "+ Utils.convertToHighestScalePossible(-difftime)+" "+Utils.scaleReached( -difftime);
    }



    public static final Creator<AccidentModel> CREATOR = new Creator<AccidentModel>() {
        @Override
        public AccidentModel createFromParcel(Parcel in) {
            return new AccidentModel(in);
        }

        @Override
        public AccidentModel[] newArray(int size) {
            return new AccidentModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(address);
        dest.writeString(type);
        dest.writeString(details);
        dest.writeString(imageUrl);
        dest.writeLong(date);
        dest.writeLong(deviceId);
    }

    @Override
    public String toJSONString() {
        try {
            return new JSONObject().put("title",title)
                    .put(KeysTags.addressKey,address)
                    .put(KeysTags.typeKey,type)
                    .put(KeysTags.commentKey,details)
                    .put(KeysTags.imageUrlKey, imageUrl)
                    .put(KeysTags.dateKey,date)
                    .put(KeysTags.deviceIdKey,deviceId)
                    .toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject().toString();
        }
    }

    public int getid() {
        return id;
    }
}
