package ihm.accidents.utils;

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
}
