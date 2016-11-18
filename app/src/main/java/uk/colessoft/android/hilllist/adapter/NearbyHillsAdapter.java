package uk.colessoft.android.hilllist.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.PreferencesActivity;
import uk.colessoft.android.hilllist.model.TinyHill;

public class NearbyHillsAdapter extends SupportAnnotatedAdapter implements NearbyHillsAdapterBinder {


    public interface RowClickListener {
        void onRowClicked(int index);
    }

    private RowClickListener rowClickListener;

    private boolean useMetricHeights;
    private boolean useMetricDistances;

    public NearbyHillsAdapter(Context context,
                              RowClickListener rowClickListener) {
        super(context);
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        useMetricHeights = prefs.getBoolean(
                PreferencesActivity.PREF_METRIC_HEIGHTS, false);
        useMetricDistances = prefs.getBoolean(
                PreferencesActivity.PREF_METRIC_DISTANCES, false);
        this.rowClickListener = rowClickListener;

    }

    @ViewType(
            layout = R.layout.nearby_hill_item,
            views = {
                    @ViewField(
                            id = R.id.hillname_entry,
                            name = "hillName",
                            type = TextView.class)
                    , @ViewField(
                    id = R.id.height_entry,
                    name = "hillHeight",
                    type = TextView.class)
                    , @ViewField(
                    id = R.id.rowid,
                    name = "rowid",
                    type = TextView.class)
                    , @ViewField(
                    id = R.id.distance_entry,
                    name = "distance",
                    type = TextView.class)
            }
    )
    public final int VIEWTYPE_HILL = 0;

    private ArrayList<Map<String, ?>> hills;


    public void setHills(ArrayList<Map<String, ?>> hills) {
        this.hills = hills;
    }

    public ArrayList<Map<String, ?>> getHills() {
        return hills;
    }

    @Override
    public int getItemCount() {
        return hills == null ? 0 : hills.size();
    }

    @Override
    public void bindViewHolder(NearbyHillsAdapterHolders.VIEWTYPE_HILLViewHolder vh, int position) {

        final DecimalFormat df3 = new DecimalFormat();
        final DecimalFormat df2 = new DecimalFormat("####.00");
        df3.isParseIntegerOnly();
        vh.hillName.setText((String) hills.get(position).get("hillname"));
        int hillId = (Integer) hills.get(position).get("rowid");
        vh.rowid.setText(String.valueOf(hillId));

        String height = df3.format((Float) hills.get(position).get("height"));
        if (useMetricHeights) height = height + "m";
        else height = height + "ft";

        vh.hillHeight.setText(height);

        String distance = df2.format(hills.get(position).get("distance"));
        if (useMetricDistances) distance = distance + "km";
        else distance = distance + "mi";

        vh.distance.setText(distance);


        vh.itemView.setOnClickListener(view -> {
            rowClickListener.onRowClicked(hillId);
        });

    }
}
