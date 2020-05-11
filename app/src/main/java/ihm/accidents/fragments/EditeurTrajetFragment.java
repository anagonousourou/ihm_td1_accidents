package ihm.accidents.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import ihm.accidents.R;
import ihm.accidents.activities.ChoicePathActivity;
import ihm.accidents.adapters.AdresseAutoCompleteAdapter;
import ihm.accidents.services.ReverseGeocoder;

public class EditeurTrajetFragment extends Fragment {
    private static final String TAG = "EditeurTrajetFragment";
    private AutoCompleteTextView departTv;
    private final ReverseGeocoder reverseGeocoder=new ReverseGeocoder();
    private Button sendBtn;
    private MaterialBetterSpinner spinner;
    private String transport="bicycle";
    private boolean expanded=true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_editeur_trajet, container, false);
        sendBtn = root.findViewById(R.id.find_route);
        this.departTv=root.findViewById(R.id.edit_depart);
        spinner = root.findViewById(R.id.transport);
        spinner.setUnderlineColor(Color.BLACK);
        spinner.setHintTextColor(Color.BLACK);
        ImageButton myLocationBtn=root.findViewById(R.id.my_location_btn);
        setMeanOfTransport();

        myLocationBtn.setOnClickListener((view)->{
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if(location!=null){
                    try {
                        reverseGeocoder.findAddressFromLocation(location,getActivity(),departTv);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "onCreateView: Impossible to get location",e );
                    }
                }
                else{
                    Toast.makeText(getContext(),"Could not get your location",Toast.LENGTH_SHORT).show();
                }

            });
        });

        ImageButton toggleBtn=root.findViewById(R.id.toggle_btn);
        toggleBtn.setOnClickListener(this::toggleEditor);

        AutoCompleteTextView destTv=root.findViewById(R.id.edit_destination);
        Log.d(TAG, "setUpAutoCompletion: "+departTv);
        Log.d(TAG, "setUpAutoCompletion: "+destTv);
        departTv.setAdapter(new AdresseAutoCompleteAdapter(getContext(), android.R.layout.simple_dropdown_item_1line));
        destTv.setAdapter(new AdresseAutoCompleteAdapter(getContext(), android.R.layout.simple_dropdown_item_1line));
        sendBtn.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
            String src = ((EditText) root.findViewById(R.id.edit_depart)).getText().toString();
            String dst = ((EditText) root.findViewById(R.id.edit_destination)).getText().toString();
            ChoicePathActivity myActivity = (ChoicePathActivity) getActivity();
            if (!src.isEmpty() && !dst.isEmpty()){
                myActivity.setRoute(src, dst, transport);
            }
        });
        return root;
    }

    private void toggleEditor(View view) {
        ImageButton toggle=(ImageButton)view;
        if(this.expanded){
            toggle.setImageResource(R.drawable.baseline_expand_more_black_36);
            this.getView().findViewById(R.id.editor_content).setVisibility(View.GONE);

        }else{
            toggle.setImageResource(R.drawable.baseline_expand_less_black_24);
            this.getView().findViewById(R.id.editor_content).setVisibility(View.VISIBLE);
        }
        this.expanded=!this.expanded;



    }




    private void setMeanOfTransport() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("En voiture");
        arrayList.add("À vélo");
        arrayList.add("À pied");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_dropdown_item_1line, arrayList);
        arrayAdapter.setDropDownViewResource(R.layout.my_spinner);
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
