package ihm.accidents.fragments;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.UnsupportedEncodingException;

import ihm.accidents.R;
import ihm.accidents.activities.ChoicePathActivity;
import ihm.accidents.adapters.AdresseAutoCompleteAdapter;
import ihm.accidents.services.ReverseGeocoder;
import ihm.accidents.utils.Utils;

public class EditeurTrajetFragment extends Fragment {
    private static final String TAG = "EditeurTrajetFragment";
    private AutoCompleteTextView departTv;
    private final ReverseGeocoder reverseGeocoder=new ReverseGeocoder();
    private Button sendBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_editeur_trajet, container, false);
        sendBtn = root.findViewById(R.id.find_route);
        this.departTv=root.findViewById(R.id.edit_depart);
        ImageButton myLocationBtn=root.findViewById(R.id.my_location_btn);
        myLocationBtn.setOnClickListener((view)->{
            ChoicePathActivity myActivity = (ChoicePathActivity) getActivity();
            myActivity.enableGeoLocalization(root);
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
        sendBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
                String src = ((EditText) root.findViewById(R.id.edit_depart)).getText().toString();
                String dst = ((EditText) root.findViewById(R.id.edit_destination)).getText().toString();
                ChoicePathActivity myActivity = (ChoicePathActivity) getActivity();
                if (!src.isEmpty() && !dst.isEmpty()){
                    myActivity.setRoute(src, dst);
                }
            }
        });
        return root;
    }
}
