package ihm.accidents.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ihm.accidents.models.SortOption;

public class SortOptionsFragment extends DialogFragment {

    private static final String TAG = "SortOptionsFragment";
    private SortOptionListener listener;


    public void setListener(SortOptionListener listener){
        this.listener=listener;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder  builder= new AlertDialog.Builder(getActivity());

        builder.setTitle("Trier les accidents par");
        String[ ] labels= new String[SortOption.values().length];
        for (int i = 0; i < SortOption.values().length; i++) {
            labels[i]=SortOption.values()[i].getLabel();
        }
        builder.setItems(labels,((dialog, which) -> listener.onSortOptionSelected(SortOption.values()[which] ) ));
        return builder.create();
    }

    public interface SortOptionListener{
        public void onSortOptionSelected(SortOption sortOption);
    }
}
