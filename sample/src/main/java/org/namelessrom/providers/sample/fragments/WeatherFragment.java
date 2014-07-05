package org.namelessrom.providers.sample.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.namelessrom.providers.sample.R;

import java.util.ArrayList;

public class WeatherFragment extends Fragment {

    private ListView mList;

    @Override public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_weather, container, false);

        mList = (ListView) v.findViewById(android.R.id.list);

        return v;
    }

    @Override public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ArrayList<String> weatherList = new ArrayList<String>(49);
        for (int i = 0; i < 48; i++) {
            weatherList.add(String.format("weather_%s", i));
        }
        weatherList.add(String.format("weather_%s", 3200));

        mList.setAdapter(new WeatherAdapter(getActivity(), weatherList));
    }

}
