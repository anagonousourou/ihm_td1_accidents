package ihm.accidents.services;

import android.content.Context;
import android.content.SharedPreferences;

import ihm.accidents.utils.KeysTags;

public final class PreferenceService {

    private SharedPreferences preferences;
    public PreferenceService(Context context){
        preferences= context.getSharedPreferences(KeysTags.preferencesFile, Context.MODE_PRIVATE);
    }


    public void setHideActualPictures(boolean accessActualPictures){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KeysTags.hideActualImagesKey,accessActualPictures );
        editor.apply();


    }
    public boolean hideActualPicturesExists(){
        return preferences.contains(KeysTags.hideActualImagesKey);
    }

    public boolean hideActualPictures(){
        return preferences.getBoolean(KeysTags.hideActualImagesKey,true);
    }

    public long retrieveDeviceId(){
        return preferences.getLong(KeysTags.deviceIdKey,0);
    }







}
