package uk.colessoft.android.hilllist.mvp;

import android.content.Context;
import android.widget.TextView;

import com.hannesdorfmann.annotatedadapter.annotation.ViewField;
import com.hannesdorfmann.annotatedadapter.annotation.ViewType;
import com.hannesdorfmann.annotatedadapter.support.recyclerview.SupportAnnotatedAdapter;

import java.util.List;

import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.objects.Hill;

public class HillsAdapter extends SupportAnnotatedAdapter implements HillsAdapterBinder{

public HillsAdapter(Context context){
    super(context);
}

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


            }) public final int VIEWTYPE_HILL = 0;

    private List<Hill> hills;


    public void setHills(List<Hill> hills) {
        this.hills = hills;
    }

    public List<Hill> getHills() {
        return hills;
    }

    @Override public int getItemCount() {
        return hills == null ? 0 : hills.size();
    }

    @Override
    public void bindViewHolder(HillsAdapterHolders.VIEWTYPE_HILLViewHolder vh, int position) {
        vh.name.setText(hills.get(position).getHillname());
        vh.height.setText(String.valueOf(hills.get(position).getHeightf()));
        vh.id.setText(String.valueOf(hills.get(position).get_id()));
    }


}
