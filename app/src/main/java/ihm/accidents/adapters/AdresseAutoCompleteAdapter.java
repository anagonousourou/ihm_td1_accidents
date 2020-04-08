package ihm.accidents.adapters;


import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

import ihm.accidents.utils.AdresseFetcher;

public class AdresseAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> adresses;

    public AdresseAutoCompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.adresses = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return adresses.size();
    }

    @Override
    public String getItem(int index) {
        return mData.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null) {
                    // A class that queries a web API, parses the data and returns an ArrayList<Style>
                    AdresseFetcher fetcher = new AdresseFetcher();
                    try {
                        mData = fetcher.retrieveResults(constraint.toString());
                    }
                    catch(Exception e) {
                        Log.e("myException", e.getMessage());
                    }
                    // Now assign the values and count to the FilterResults object
                    filterResults.values = mData;
                    filterResults.count = mData.size();
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