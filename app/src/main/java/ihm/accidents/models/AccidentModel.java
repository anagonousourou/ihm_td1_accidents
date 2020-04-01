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
    private String imageb64;
    private double date;

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

    public String getImageb64() {
        return imageb64;
    }

    public double getDate() {
        return date;
    }



    public String userFormatDate(){
         long difftime= (long)((System.currentTimeMillis()/1000)-date);

         if(difftime == 0){
             return "A l'instant";
         }
         if(difftime > 0){
            return "Il y a "+Utils.convertToHighestScalePossible((long) difftime)+" "+Utils.scaleReached((long) difftime);
         }

         if(difftime < 0){
             return "Dans "+ Utils.convertToHighestScalePossible((long) -difftime)+" "+Utils.scaleReached((long) -difftime);
         }
         return difftime+" seconds";
    }

    protected AccidentModel(Parcel in) {
        title=in.readString();
        address = in.readString();
        type = in.readString();
        details = in.readString();
        imageb64 = in.readString();
        date = in.readDouble();
    }

    public AccidentModel(String titre,String address, String type, String details, String b64, double dt) {
        this.title=titre;
        this.address = address;
        this.type = type;
        this.details = details;
        this.imageb64 = b64;
        this.date = dt;
    }

    public Bitmap getImageBitmap(){
        return Utils.fromBase64(this.imageb64);
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
        dest.writeString(imageb64);
        dest.writeDouble(date);
    }

    @Override
    public String toJSONString() {
        try {
            return new JSONObject().put("title",title)
                    .put("address",address)
                    .put("type",type)
                    .put("details",details)
                    .put("imageb64",imageb64)
                    .put("date",date).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject().toString();
        }
    }
}
