package ihm.accidents.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Arrays;
import java.util.List;



public class FilterOptionsFragment extends DialogFragment {
    private static final String TAG = "FilterOptionsFragment";
    private FilterOptionsListener listener;
    private List<String> accidentTypes= Arrays.asList(
            "Personne seule :)",
        "Vélo",
    "Voiture(s)",
    "Camion (s)",
    "Trains ou TGV ou métro"
    );

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        builder.setTitle("Filter les accidents par:");
        builder.setItems(
                (String[]) accidentTypes.toArray(),((dialog, which) -> listener.onFilterOptionSelected(accidentTypes.get(which)) )
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
