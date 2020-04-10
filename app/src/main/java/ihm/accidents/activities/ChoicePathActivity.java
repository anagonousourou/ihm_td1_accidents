package ihm.accidents.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import ihm.accidents.R;
import ihm.accidents.fragments.EditeurTrajetFragment;
import ihm.accidents.fragments.SomeFragment;

public class ChoicePathActivity extends AppCompatActivity {
    private static final String TAG = "ChoicePathActivity";
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.second_menu,menu);
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_path_activity);
        Fragment fragmentMap=new SomeFragment();
        Fragment fragmentEditeur=new EditeurTrajetFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.placeHolderMapFragment ,fragmentMap).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.placeHolderEditeurFragment ,fragmentEditeur).commit();



    }


}
