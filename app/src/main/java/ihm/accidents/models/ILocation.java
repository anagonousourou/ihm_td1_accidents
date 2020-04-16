package ihm.accidents.models;

import android.location.Location;

public interface ILocation {
    int R = 6371; // Radius of the earth
    double latitude();
    double longitude();
    default double distanceTo(ILocation otherLocation){


        double latDistance = Math.toRadians(otherLocation.latitude() - this.latitude());
        double lonDistance = Math.toRadians(otherLocation.longitude() - this.longitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitude())) * Math.cos(Math.toRadians(otherLocation.latitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters



        distance = Math.pow(distance, 2);//+ some  f(height ) but here we ignore height

        return Math.sqrt(distance);

    }

    default double distanceTo(Location otherLocation){


        double latDistance = Math.toRadians(otherLocation.getLatitude() - this.latitude());
        double lonDistance = Math.toRadians(otherLocation.getLongitude() - this.longitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitude())) * Math.cos(Math.toRadians(otherLocation.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters



        distance = Math.pow(distance, 2);//+ some  f(height ) but here we ignore height

        return Math.sqrt(distance);

    }
}
