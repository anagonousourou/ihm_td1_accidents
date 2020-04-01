package my.myself.exercice6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import my.myself.accidents.models.AccidentModel;

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

        lieutv.setText(getString(R.string.lieu_accident, accidentModel.getAddress()));
        titretv.setText(getString(R.string.titre_accident, accidentModel.getTitle()));
    }
}
