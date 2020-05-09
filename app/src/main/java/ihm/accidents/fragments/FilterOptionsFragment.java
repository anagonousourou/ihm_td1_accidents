package ihm.accidents.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.List;


public class FilterOptionsFragment extends DialogFragment  {
    private static final String TAG = "FilterOptionsFragment";
    private FilterOptionsListener listener;

    private List<String> accidentTypes;

    public FilterOptionsFragment(List<String> accidentTypes){
        super();
        this.accidentTypes=accidentTypes;
        accidentTypes.set(0,"Signalés par vous");
        accidentTypes.set(0,"Tout");


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setTitle("Filter les accidents par:");
        Log.d(TAG, "onCreateDialog: "+accidentTypes);
        builder.setItems(
                accidentTypes.toArray(new String[0]),((dialog, which) -> listener.onFilterOptionSelected(accidentTypes.get(which)) )
        );
        return builder.create();
    }

    public void setListener(FilterOptionsListener listener){
        this.listener=listener;
    }



    public interface FilterOptionsListener {
        public void onFilterOptionSelected(String filter);
    }

}
