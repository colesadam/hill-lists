package uk.colessoft.android.hilllist.mvp.lce.fragment;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.io.InputStream;
import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.PreferencesActivity;
import uk.colessoft.android.hilllist.model.hillsummits.Example;
import uk.colessoft.android.hilllist.model.hillsummits.Result;
import uk.colessoft.android.hilllist.mvp.HillPresenter;
import uk.colessoft.android.hilllist.mvp.HillView;
import uk.colessoft.android.hilllist.mvp.lce.SimpleHillPresenter;
import uk.colessoft.android.hilllist.model.Hill;
import uk.colessoft.android.hilllist.service.HillSummitsApi;

public class MvpHillDetailFragment extends MvpFragment<HillView, HillPresenter>
        implements HillView {

    private boolean useMetricHeights;
    private final DecimalFormat df3 = new DecimalFormat();

    @Bind(R.id.hill_photo)
    ImageView hillPhoto;

    @Bind(R.id.hill_classifications)
    LinearLayout classificationsLayout;

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

    private Menu mMenu;

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
        Toolbar toolbar = ((Toolbar) view.findViewById(R.id.toolbar));
        toolbar.inflateMenu(R.menu.hill_detail_menu);
        setHasOptionsMenu(true);
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

        TextView classificationTextView = null;
        for (String classification : hill.getClassifications()) {

            classificationTextView = new TextView(getActivity());
            // classificationTextView.setBackgroundResource(R.color.white);
            classificationTextView.setText(classification);
            classificationTextView.setPadding(5, 5, 5, 5);
            classificationTextView.setTextColor(getResources().getColor(
                    R.color.pale_blue));

            classificationsLayout.addView(classificationTextView);
            //classificationTextView.setOnClickListener(detailFilterOnClickListener);
        }
        if (classificationTextView != null) {

            classificationTextView.setPadding(5, 5, 5, 5);
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://hillsummits.piwigo.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        HillSummitsApi api = retrofit.create(HillSummitsApi.class);

        api.search(hill.get_id() + " " + hill.getHillname() + " portrait", "json", "pwg.images.search")
                .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Example>() {

            @Override
            public void call(Example example) {
                if (example.getResult().getImages().size()>0) {
                    DownloadImageTask imageTask = new DownloadImageTask(hillPhoto);
                    imageTask.execute(example.getResult().getImages().get(0).getDerivatives().getXlarge().getUrl());
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.hill_detail_menu,menu);
        // mMenu = menu;
    }

    private String convText(float dblAmt) {

        return df3.format(dblAmt);

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Drawable d1 = bmImage.getDrawable();
            Drawable d2 = new BitmapDrawable(bmImage.getResources(), result);
            TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                    d1, d2});

            bmImage.setImageDrawable(td);

            td.startTransition(500);

        }
    }
}
