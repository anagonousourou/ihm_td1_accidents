package ihm.accidents.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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
    private Spinner spinner;
    private String transport;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_editeur_trajet, container, false);
        sendBtn = root.findViewById(R.id.find_route);
        this.departTv=root.findViewById(R.id.edit_depart);
        spinner = root.findViewById(R.id.transport);
        ImageButton myLocationBtn=root.findViewById(R.id.my_location_btn);
        setMeanOfTransport();
        myLocationBtn.setOnClickListener((view)->{
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
                    myActivity.setRoute(src, dst, transport);
                }
            }
        });
        return root;
    }


    private void setMeanOfTransport() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("En voiture");
        arrayList.add("À vélo");
        arrayList.add("À pied");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.my_spinner, arrayList);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    transport = "pedestrian";
                } else if(position == 0){
                    transport = "fastest";
                } else {
                    transport = "bicycle";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
