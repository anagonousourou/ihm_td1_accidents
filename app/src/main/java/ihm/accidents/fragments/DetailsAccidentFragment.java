package ihm.accidents.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import ihm.accidents.R;
import ihm.accidents.databinding.DetailsAccidentActivityBinding;
import ihm.accidents.models.AccidentModel;


public class DetailsAccidentFragment extends Fragment {

    private final AccidentModel accident;

    public DetailsAccidentFragment(AccidentModel accidentModel){
        this.accident=accidentModel;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DetailsAccidentActivityBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.details_accident_activity, container, false);
        View view = binding.getRoot();
        view.findViewById(R.id.send).setOnClickListener((v)->
            goToSendMessage(v)
        );
        binding.setAccident(this.accident);
        return view;
    }

    public void goToSendMessage(View view) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String message = "Attention\n"+accident.getTitle()+"\n A l'adresse: "+accident.getAddress();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Nouvel Incident");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(sharingIntent, "Partager avec"));
    }
}
