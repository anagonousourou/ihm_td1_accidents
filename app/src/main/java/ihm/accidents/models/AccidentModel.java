package ihm.accidents.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import ihm.accidents.utils.Utils;


public class AccidentModel implements Parcelable, JSONString {
    private String title;
    private String address;
    private String type;
    private String details;
    private String imageUrl= "https://www.nswcompensationlawyers.com.au/media/Video-Placeholder-Motor-Accident-1024x576.png";
    private long date;

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



    public AccidentModel(String title, String address, String type, String details, String imageUrl, long date){
        this.title=title;
        this.address=address;
        this.type=type;
        this.details=details;
        this.imageUrl = imageUrl;
        this.date= date;

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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String userFormatDate(){
         long difftime= System.currentTimeMillis()-date;

         if(difftime == 0){
             return "A l'instant";
         }
         if(difftime > 0){
            return "Il y a "+Utils.convertToHighestScalePossible(difftime)+" "+Utils.scaleReached(difftime);
         }

         if(difftime < 0){
             return "Dans "+ Utils.convertToHighestScalePossible(-difftime)+" "+Utils.scaleReached( -difftime);
         }
         return difftime+" seconds";
    }

    public static AccidentModel fromJson(String jsonString){
        //TODO
        return null;
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
        dest.writeDouble(date);
    }

    @Override
    public String toJSONString() {
        try {
            return new JSONObject().put("title",title)
                    .put("address",address)
                    .put("type",type)
                    .put("details",details)
                    .put("imageUrl", imageUrl)
                    .put("date",date).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject().toString();
        }
    }
}
