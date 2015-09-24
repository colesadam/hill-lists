package uk.colessoft.android.hilllist.mvp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.Field;
import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.text.DecimalFormat;
import java.util.List;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.PreferencesActivity;
import uk.colessoft.android.hilllist.objects.Hill;

public class HillsAdapter extends SupportAnnotatedAdapter implements HillsAdapterBinder {

    private Context mContext;
    private boolean useMetricHeights;
    private final DecimalFormat df3 = new DecimalFormat();

    public HillsAdapter(Context context, RecyclerItemViewClick inListener) {
        super(context);
        mContext = context;
        hillSelectedListener = inListener;
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);

        useMetricHeights = prefs.getBoolean(
                PreferencesActivity.PREF_METRIC_HEIGHTS, false);
    }

    static class HillClickListener implements View.OnClickListener {

        public int id;

        public void onClick(View v) {


            hillSelectedListener.hillClicked(id);
        }
    }

    static RecyclerItemViewClick hillSelectedListener;

    @ViewType(
            layout = R.layout.simple_hill_item,
            views = {
                    @ViewField(
                            id = R.id.name_entry,
                            name = "name",
                            type = TextView.class),
                    @ViewField(
                            id = R.id.number_entry,
                            name = "height",
                            type = TextView.class),
                    @ViewField(
                            id = R.id.rowid,
                            name = "id",
                            type = TextView.class)
            },
            fields = {
                    @Field(
                            type = HillClickListener.class,
                            name = "clickListener"
                    )
            })
    public final int VIEWTYPE_HILL = 0;

    private List<Hill> hills;


    public void setHills(List<Hill> hills) {
        this.hills = hills;
    }

    public List<Hill> getHills() {
        return hills;
    }

    @Override
    public int getItemCount() {
        return hills == null ? 0 : hills.size();
    }


    @Override
    public void bindViewHolder(HillsAdapterHolders.VIEWTYPE_HILLViewHolder vh, int position) {


        vh.name.setText(hills.get(position).getHillname());
        if(useMetricHeights)
            vh.height.setText(convText(hills.get(position).getHeightm())+"m");
        else
            vh.height.setText(convText(hills.get(position).getHeightf())+"ft");
        vh.id.setText(String.valueOf(hills.get(position).get_id()));

        vh.clickListener = new HillClickListener();
        vh.itemView.setOnClickListener(vh.clickListener);
        vh.clickListener.id = hills.get(position).get_id();
    }

    public interface RecyclerItemViewClick {
        void hillClicked(int id);
    }

    private String convText(float dblAmt) {

        return df3.format(dblAmt);

    }


}
