package ihm.accidents.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.UnsupportedEncodingException;

import ihm.accidents.R;
import ihm.accidents.adapters.AdresseAutoCompleteAdapter;
import ihm.accidents.services.ReverseGeocoder;
import ihm.accidents.utils.Utils;

public class EditeurTrajetFragment extends Fragment {
    private static final String TAG = "EditeurTrajetFragment";
    private AutoCompleteTextView departTv;
    private final ReverseGeocoder reverseGeocoder=new ReverseGeocoder();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_editeur_trajet, container, false);
        this.departTv=root.findViewById(R.id.edit_depart);
        ImageButton mylocationBtn=root.findViewById(R.id.my_location_btn);
        mylocationBtn.setOnClickListener((view)->{
            Bundle bundle=getArguments();

            try {
                if(bundle!=null){
                    reverseGeocoder.findAddressFromLocation(bundle.getParcelable(Utils.locationKey),getActivity(),departTv);
                }

            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, "onCreateView: Impossible to get location",e );

            }
        });
        AutoCompleteTextView destTv=root.findViewById(R.id.edit_destination);
        Log.d(TAG, "setUpAutoCompletion: "+departTv);
        Log.d(TAG, "setUpAutoCompletion: "+destTv);
        departTv.setAdapter(new AdresseAutoCompleteAdapter(getContext(), android.R.layout.simple_dropdown_item_1line));
        destTv.setAdapter(new AdresseAutoCompleteAdapter(getContext(), android.R.layout.simple_dropdown_item_1line));
        return root;
    }


}
