package uk.colessoft.android.hilllist.adapter;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.text.DecimalFormat;
import java.util.List;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.PreferencesActivity;
import uk.colessoft.android.hilllist.model.TinyHill;

public class HillsAdapter extends SupportAnnotatedAdapter implements HillsAdapterBinder {

    public interface BaggingHillListener{
        void onChecked(View v, int id);
        void onUnchecked(View v, int id);
    }

    public interface RowClickListener{
        void onRowClicked(int hillId);
    }

    private BaggingHillListener baggingHillListener;
    private RowClickListener rowClickListener;

    private boolean useMetricHeights;
    private int lightGreenColour;

    public HillsAdapter(Context context, BaggingHillListener baggingHillListener,
                        RowClickListener rowClickListener ) {
        super(context);
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        useMetricHeights = prefs.getBoolean(
                PreferencesActivity.PREF_METRIC_HEIGHTS, false);
        lightGreenColour = context.getResources().getColor(R.color.paler_light_green);
        this.baggingHillListener = baggingHillListener;
        this.rowClickListener = rowClickListener;

    }

    @ViewType(
            layout = R.layout.simple_hill_item,
            views = {
                    @ViewField(
                            id = R.id.name_entry,
                            name = "hillName",
                            type = TextView.class)
                    , @ViewField(
                    id = R.id.number_entry,
                    name = "hillHeight",
                    type = TextView.class)
                    , @ViewField(
                    id = R.id.rowid,
                    name = "rowid",
                    type = TextView.class)
                    , @ViewField(
                    id = R.id.check_hill_climbed,
                    name = "climbedCheckBox",
                    type = CheckBox.class)
            }
    )
    public final int VIEWTYPE_HILL = 0;

    private List<TinyHill> hills;


    public void setHills(List<TinyHill> hills) {
        this.hills = hills;
    }

    public List<TinyHill> getHills() {
        return hills;
    }

    @Override public int getItemCount() {
        return hills == null ? 0 : hills.size();
    }

    @Override
    public void bindViewHolder(HillsAdapterHolders.VIEWTYPE_HILLViewHolder vh, int position) {

        final DecimalFormat df3 = new DecimalFormat();
        df3.isParseIntegerOnly();
        vh.hillName.setText(hills.get(position).getHillname());
        int hillId = hills.get(position)._id;
        vh.rowid.setText(String.valueOf(hillId));
        if(hills.get(position).isClimbed()){
            vh.climbedCheckBox.setChecked(true);
            ((ConstraintLayout) vh.hillName.getParent()).setBackgroundColor(lightGreenColour);

        }else vh.climbedCheckBox.setChecked(false);

        if(useMetricHeights)
            vh.hillHeight.setText(df3.format(hills.get(position).getHeightM())+"m");
                else vh.hillHeight.setText(df3.format(hills.get(position).getHeightF())+"ft") ;

        vh.climbedCheckBox.setOnCheckedChangeListener((compoundButton, checked) -> {
            if(checked) baggingHillListener.onChecked(vh.climbedCheckBox, hillId);
            else baggingHillListener.onUnchecked(vh.climbedCheckBox, hillId);
        });

        vh.itemView.setOnClickListener(view -> {
            rowClickListener.onRowClicked(hillId);
        });

    }
}
