package org.namelessrom.providers.sample;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import org.namelessrom.providers.sample.fragments.WeatherFragment;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends Activity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager            mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), getFragments(),
                getTitles());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    private ArrayList<Integer> getTitles() {
        final ArrayList<Integer> titles = new ArrayList<Integer>(1);
        titles.add(R.string.weather);
        return titles;
    }

    private ArrayList<Fragment> getFragments() {
        final ArrayList<Fragment> titles = new ArrayList<Fragment>(1);
        titles.add(new WeatherFragment());
        return titles;
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final ArrayList<Fragment> fragments;
        private final ArrayList<Integer>  titles;

        public SectionsPagerAdapter(final FragmentManager fm, final ArrayList<Fragment> fragments,
                final ArrayList<Integer> titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(final int position) { return fragments.get(position); }

        @Override
        public int getCount() { return fragments.size(); }

        @Override
        public CharSequence getPageTitle(int position) {
            final Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(titles.get(position)).toUpperCase(l);
            }
            return null;
        }
    }

}
