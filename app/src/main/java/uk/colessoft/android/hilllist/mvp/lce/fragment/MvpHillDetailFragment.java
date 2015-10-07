package uk.colessoft.android.hilllist.mvp.lce.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.PreferencesActivity;
import uk.colessoft.android.hilllist.mvp.HillPresenter;
import uk.colessoft.android.hilllist.mvp.HillView;
import uk.colessoft.android.hilllist.mvp.lce.SimpleHillPresenter;
import uk.colessoft.android.hilllist.objects.Hill;

public class MvpHillDetailFragment extends MvpFragment<HillView, HillPresenter>
        implements HillView {

    private boolean useMetricHeights;
    private final DecimalFormat df3 = new DecimalFormat();

    @Bind(R.id.detail_hill_name)
    TextView hillname;

    @Bind(R.id.detail_hill_height)
    TextView hillheight;

    @Bind(R.id.detail_hill_latitude)
    TextView hillLatitude;

    @Bind(R.id.detail_hill_longitude)
    TextView hillLongitude;

    @Bind(R.id.detail_hill_osgrid)
    TextView osgridref;

    @Bind(R.id.detail_hill_section)
    TextView hillsection;

    @Bind(R.id.detail_os50k)
    TextView os50k;

    @Bind(R.id.detail_os25k)
    TextView os25k;

    @Bind(R.id.region_detail)
    TextView region;

    @Bind(R.id.detail_hill_summit_feature)
    TextView summitFeature;

    @Bind(R.id.detail_colheight)
    TextView colHeight;

    @Bind(R.id.detail_colgridref)
    TextView colGridRef;

    @Bind(R.id.detail_drop)
    TextView drop;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long rowid = getActivity().getIntent().getExtras().getLong("rowid");
        presenter.loadHill(rowid);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getContext());

        useMetricHeights = prefs.getBoolean(
                PreferencesActivity.PREF_METRIC_HEIGHTS, false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.hill_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public HillPresenter createPresenter() {
        return new SimpleHillPresenter(this.getContext());
    }

    @Override
    public void updateHill(Hill hill) {
        hillname.setText(hill.getHillname());
        if (useMetricHeights)
            hillheight.setText(convText(hill.getHeightm()) + "m");
        else
            hillheight.setText(convText(hill.getHeightf()) + "ft");
        colGridRef.setText(hill.getColgridref());
        colHeight.setText(hill.getColheight() + "m");
        drop.setText(hill.getDrop() + "m");
        hillLatitude.setText(String.valueOf(hill.getLatitude()) + "N");
        String ll = "E";
        double absL = hill.getLongitude();
        if (hill.getLongitude() < 0) {
            ll = "W";
            absL = 0 - absL;
        }
        hillLongitude.setText(String.valueOf(absL) + ll);
        summitFeature.setText(hill.getFeature());

        osgridref.setText(hill.getGridref());
        hillsection.setText(hill.getSection());
        os50k.setText(hill.getMap());
        os25k.setText(hill.getMap25());
        region.setText(hill.getRegion());
    }

    private String convText(float dblAmt) {

        return df3.format(dblAmt);

    }
}
