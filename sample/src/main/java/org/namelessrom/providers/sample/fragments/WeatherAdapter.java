package org.namelessrom.providers.sample.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.namelessrom.providers.sample.R;

import java.util.ArrayList;

public class WeatherAdapter extends BaseAdapter {

    private static final String WTF = "res is null? WTF!";
    private final ArrayList<String> names;
    private final Context           context;
    private final Typeface          superTypeface;

    private Resources res;

    public WeatherAdapter(final Context context, final ArrayList<String> names) {
        this.context = context;
        this.names = names;
        try {
            this.res = this.context.getPackageManager()
                    .getResourcesForApplication("org.namelessrom.providers");
        } catch (Exception e) { this.res = null; }

        this.superTypeface = Typeface.createFromAsset(this.context.getAssets(), "weatherfont.ttf");
    }

    private static final class ViewHolder {
        final TextView title;
        final TextView summary;

        public ViewHolder(final View rootView) {
            title = (TextView) rootView.findViewById(R.id.title);
            summary = (TextView) rootView.findViewById(R.id.summary);
        }
    }

    @Override public int getCount() { return names.size(); }

    @Override public Object getItem(final int i) { return names.get(i); }

    @Override public long getItemId(final int i) { return 0; }

    @Override public View getView(final int position, View v, final ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (v == null) {
            v = ((Activity) context).getLayoutInflater()
                    .inflate(R.layout.item_weather, viewGroup, false);
            assert (v != null);
            viewHolder = new ViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.title.setText(names.get(position));

        final String weatherString = getWeatherString(names.get(position));
        if (!weatherString.equals(WTF)) viewHolder.summary.setTypeface(superTypeface);
        viewHolder.summary.setText(weatherString);

        return v;
    }

    private String getWeatherString(final String id) {
        if (res == null) return WTF;
        return res.getString(res.getIdentifier(id, "string", "org.namelessrom.providers"));
    }

}
