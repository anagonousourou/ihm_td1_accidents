package ihm.accidents.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ihm.accidents.R;
import ihm.accidents.adapters.AdresseAutoCompleteAdapter;

public class EditeurTrajetFragment extends Fragment {
    private static final String TAG = "EditeurTrajetFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_editeur_trajet, container, false);
        AutoCompleteTextView departTv=root.findViewById(R.id.edit_depart);
        AutoCompleteTextView destTv=root.findViewById(R.id.edit_destination);
        Log.d(TAG, "setUpAutoCompletion: "+departTv);
        Log.d(TAG, "setUpAutoCompletion: "+destTv);
        departTv.setAdapter(new AdresseAutoCompleteAdapter(getContext(), android.R.layout.simple_dropdown_item_1line));
        destTv.setAdapter(new AdresseAutoCompleteAdapter(getContext(), android.R.layout.simple_dropdown_item_1line));
        return root;
    }
}
