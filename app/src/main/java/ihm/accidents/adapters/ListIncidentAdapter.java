package ihm.accidents.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;

import ihm.accidents.activities.DetailsAccidentActivity;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.utils.Placeholders;
import ihm.accidents.utils.Utils;
import my.ihm.exercice6.R;

public class ListIncidentAdapter extends RecyclerView.Adapter<ListIncidentAdapter.ViewHolder> {
    private static final String TAG = "ListIncidentAdapter";
    private final Context context;
    private final Activity activity;

    private List<AccidentModel> accidentsList=new ArrayList<>();

    public ListIncidentAdapter(Context ctx, Activity activity){
        this.context=ctx;
        this.activity=activity;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.accidentTitle.setText(accidentsList.get(position).getTitle());
        if(accidentsList.get(position).getImageBitmap()!=null) {
            holder.accidentImage.setImageBitmap(accidentsList.get(position).getImageBitmap());
        }else{
            holder.accidentImage.setImageBitmap(Utils.fromBase64(Placeholders.img_placeholder1));
        }
        holder.accidentDistance.setText(accidentsList.get(position).getAddress());
        holder.wrapperItem.setOnClickListener((view)->{
            Intent resultIntent = new Intent(context, DetailsAccidentActivity.class);
            resultIntent.putExtra(Utils.accidentKey,accidentsList.get(position));
            activity.startActivity(resultIntent);

        });
        /*if (position%2==0){
            holder.wrapperItem.setBackgroundColor(context.getColor(R.color.silver));
        }
        else{
            holder.wrapperItem.setBackgroundColor(context.getColor(R.color.lightgrey));
        }*/

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_incident,null);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return accidentsList.size();
    }

    public void setAccidentsList(List<AccidentModel> accidentsList) {
        this.accidentsList = accidentsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView accidentImage;
        private TextView accidentDistance;
        private TextView accidentTitle;
        private RelativeLayout wrapperItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            accidentImage= itemView.findViewById(R.id.photoIncident
            );
            accidentDistance=itemView.findViewById(R.id.distanceIncident);
            accidentTitle=itemView.findViewById(R.id.shortTitle);
            wrapperItem=itemView.findViewById(R.id.wrapperItemAccident);
        }
    }
}
