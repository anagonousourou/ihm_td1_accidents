package ihm.accidents.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import ihm.accidents.utils.Utils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccidentModel implements Parcelable, JSONString {
    @JsonProperty("title")
    private String title;
    @JsonProperty("adresse")
    private String address;
    @JsonProperty("type")
    private String type;
    @JsonProperty("commentaire")
    private String commentaire;
    @JsonProperty("imageb64")
    private String imageb64;
    @JsonProperty("date")
    private double date;

    @Override
    public String toString() {
        return "AccidentModel{" +
                "title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", type='" + type + '\'' +
                ", commentaire= '"+commentaire+ '\''+
                ", date=" + date +
                '}';
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

    public String getCommentaire() {
        return commentaire;
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
        commentaire = in.readString();
        imageb64 = in.readString();
        date = in.readDouble();
    }
    @JsonCreator
    public AccidentModel(@JsonProperty("title")String titre,
                         @JsonProperty("adresse")String address,
                         @JsonProperty("type")String type,
                         @JsonProperty("commentaire") String Comment,
                         @JsonProperty("imageb64") String b64,
                         @JsonProperty("date") double dt) {
        this.title=titre;
        this.address = address;
        this.type = type;
        this.commentaire = Comment;
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
        dest.writeString(commentaire);
        dest.writeString(imageb64);
        dest.writeDouble(date);
    }

    @Override
    public String toJSONString() {
        try {
            return new JSONObject().put("title",title)
                    .put("adresse",address)
                    .put("type",type)
                    .put("commentaire", commentaire)
                    .put("imageb64",imageb64)
                    .put("date",date).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject().toString();
        }
    }
}
