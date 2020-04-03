package ihm.accidents.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import ihm.accidents.models.AccidentModel;
import ihm.accidents.utils.Utils;
import my.ihm.exercice6.R;


/**
 * To present the details of an Accident wherever the user clicks on a
 * notification or whatever else in the flow of the appplication
 */
public class DetailsAccidentActivity extends Activity {
    private static final String TAG = "DetailsAccidentActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_accident_activity);

        Intent intent = getIntent();
        AccidentModel accidentModel = intent.getParcelableExtra(Utils.accidentKey);
        TextView titretv =  this.findViewById(R.id.titre_details_accident_txtview);
        TextView lieutv =  this.findViewById(R.id.lieu_accident_txtview);
        TextView momenttv = this.findViewById(R.id.temps_accident_txtview);
        TextView detailstv= this.findViewById(R.id.desc_accident_txtview);
        ImageView imageView=this.findViewById(R.id.image_details_accident);

        imageView.setImageBitmap(accidentModel.getImageBitmap());
        detailstv.setText(accidentModel.getDetails());


        momenttv.setText(getString(R.string.temps_accident,accidentModel.userFormatDate()));
        lieutv.setText(getString(R.string.lieu_accident, accidentModel.getAddress()));
        titretv.setText(getString(R.string.titre_accident, accidentModel.getTitle()));

    }
}
