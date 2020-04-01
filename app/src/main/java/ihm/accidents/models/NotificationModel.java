package ihm.accidents.models;

public class NotificationModel {

    private final String title;
    private final String imageUrl;
    private final String details;

    public NotificationModel(String titre, String imageUrl, String details) {
        this.title = titre;
        this.imageUrl = imageUrl;
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDetails() {
        return details;
    }
}
