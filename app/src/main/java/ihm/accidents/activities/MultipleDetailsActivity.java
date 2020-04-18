package ihm.accidents.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;
import java.util.List;

import ihm.accidents.R;
import ihm.accidents.fragments.DetailsAccidentFragment;
import ihm.accidents.models.AccidentModel;
import ihm.accidents.utils.Utils;

public class MultipleDetailsActivity extends FragmentActivity {
    private Toast backToast;
    private long backPressedTime=0;
    private static final String TAG = "MultipleDetailsActivity";
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
        if(accidentModels == null){
            accidentModels = Arrays.asList(Utils.accidentModelFake2,Utils.accidentModelFake);
        }
        pagerAdapter = new ScreenSlidePagerAdapter(this, accidentModels );
        List<AccidentModel> finalAccidentModels = accidentModels;
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position==0){
                    Log.d(TAG, "onPageSelected: "+position);
                    findViewById(R.id.left_chevron).setVisibility(View.INVISIBLE);
                }
                if(position == finalAccidentModels.size()-1){
                    findViewById(R.id.right_chevron).setVisibility(View.INVISIBLE);
                }

                if(position != 0){
                    findViewById(R.id.left_chevron).setVisibility(View.VISIBLE);
                }
                if(position != finalAccidentModels.size()-1){
                    findViewById(R.id.right_chevron).setVisibility(View.VISIBLE);
                }

            }
        });
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            if(backToast!=null){
                backToast.cancel();
            }
            return;
        }
        else{
            backToast= Toast.makeText(this,"Appuyez encore pour quitter",Toast.LENGTH_SHORT);
            backToast.show();
            backPressedTime=System.currentTimeMillis();
        }

    }

    public void previousPage(View view) {
        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }

    }

    public void nextPage(View view) {
        if (viewPager.getCurrentItem() < viewPager.getAdapter().getItemCount()-1) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
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
         * @return the size of the dataset
         */
        @Override
        public int getItemCount() {
            return accidents.size();
        }
    }

}
