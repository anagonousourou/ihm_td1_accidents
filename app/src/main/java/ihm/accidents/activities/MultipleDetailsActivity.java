package ihm.accidents.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ihm.accidents.R;
import ihm.accidents.fragments.DetailsAccidentFragment;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.utils.Utils;

public class MultipleDetailsActivity extends FragmentActivity {

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 viewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accidents_slide);

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        List<AccidentModel> accidentModels= getIntent().getParcelableArrayListExtra(Utils.accidentKey);
        if(accidentModels==null){
            accidentModels=new ArrayList<>();
        }
        pagerAdapter = new ScreenSlidePagerAdapter(this, accidentModels );

        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        private List<AccidentModel> accidents;
        public ScreenSlidePagerAdapter(FragmentActivity fa , List<AccidentModel> accidentModels) {
            super(fa);
            this.accidents=accidentModels;
        }

        /**
         *
         * @param position un indice allant de 0 à getItemCount()-1
         * @return un fragment correspondant à un accident
         */
        @Override
        public Fragment createFragment(int position) {
            return new DetailsAccidentFragment(accidents.get(position));
        }

        /**
         *
         * @param holder no idea
         * @param position an index  from 0 to getItemCount()-1
         * @param payloads the dataset that is to be displayed using the adapter
         */
        @Override
        public void onBindViewHolder(@NonNull FragmentViewHolder holder, int position, @NonNull List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);
        }

        /**
         *
         * @return the size of the dataset
         */
        @Override
        public int getItemCount() {
            return accidents.size();
        }
    }

}
