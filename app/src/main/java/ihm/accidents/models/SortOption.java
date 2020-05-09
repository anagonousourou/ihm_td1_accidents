package ihm.accidents.models;

public enum SortOption {

    RECENT("Les plus récents"),
    OLDER("Les plus anciens"),
    CLOSEST("Les plus proches"),
    FARTHEST("Les plus éloignés");

    private String label="";


    SortOption(String label){
        this.label=label;
    }

    public String getLabel() {
        return label;
    }
}
