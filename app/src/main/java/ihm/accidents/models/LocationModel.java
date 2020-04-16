package ihm.accidents.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationModel implements Parcelable, ILocation{

    private final double lat;
    private final double lng;

    protected LocationModel(Parcel in) {
        lat = in.readDouble();
        lng = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LocationModel> CREATOR = new Creator<LocationModel>() {
        @Override
        public LocationModel createFromParcel(Parcel in) {
            return new LocationModel(in);
        }

        @Override
        public LocationModel[] newArray(int size) {
            return new LocationModel[size];
        }
    };

    @Override
    public double latitude() {
        return lat;
    }

    @Override
    public double longitude() {
        return this.lng;
    }
}
