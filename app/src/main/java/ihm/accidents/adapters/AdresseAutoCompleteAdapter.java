package ihm.accidents.adapters;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ihm.accidents.utils.AdresseFetcher;
import my.ihm.exercice6.R;

public class AdresseAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private static final String TAG = "AdrAutoCompleteAdapter";
    private List<String> adresses;

    public AdresseAutoCompleteAdapter(Context context, int resource) {
        
        super(context, resource);
        this.adresses = new ArrayList<>();
        this.adresses.add("Salut");

        this.addAll(this.adresses);

        Log.d(TAG, "AdresseAutoCompleteAdapter: dans le constructeur");

    }

    @Override
    public int getCount() {
        return adresses.size();
    }

    @Override
    public String getItem(int index) {
        return adresses.get(index);
    }



    @Override
    public Filter getFilter() {
        Log.d(TAG, "getFilter: In getFilter");
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.d(TAG, "performFiltering: In perform filtering");
                FilterResults filterResults = new FilterResults();
                if(constraint != null) {
                    // A class that queries a web API, parses the data and returns an ArrayList<Style>
                    AdresseFetcher fetcher = new AdresseFetcher();
                    try {
                        adresses = fetcher.retrieveAdressesMatching(constraint.toString());
                    }
                    catch(Exception e) {
                        Log.e(TAG, e.getMessage());
                    }
                    // Now assign the values and count to the FilterResults object
                    filterResults.values = adresses;
                    filterResults.count = adresses.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if(results != null && results.count > 0) {

                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }
}