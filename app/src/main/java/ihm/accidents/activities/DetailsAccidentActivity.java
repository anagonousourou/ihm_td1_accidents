package ihm.accidents.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import ihm.accidents.models.AccidentModel;
import my.ihm.exercice6.R;
import my.ihm.exercice6.Utils;

/**
 * To present the details of an Accident wherever the user clicks on a
 * notification or whatever else in the flow of the appplication
 */
public class DetailsAccidentActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_accident_activity);

        Intent intent = getIntent();
        AccidentModel accidentModel = intent.getParcelableExtra(Utils.accidentKey);
        TextView titretv = (TextView) this.findViewById(R.id.titre_details_accident_txtview);
        TextView lieutv = (TextView) this.findViewById(R.id.lieu_accident_txtview);
        TextView momenttv =(TextView) this.findViewById(R.id.temps_accident_txtview);

        momenttv.setText(getString(R.string.temps_accident,accidentModel.userFormatDate()));
        lieutv.setText(getString(R.string.lieu_accident, accidentModel.getAddress()));
        titretv.setText(getString(R.string.titre_accident, accidentModel.getTitle()));

    }
}
