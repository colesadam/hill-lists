package uk.colessoft.android.hilllist.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;

import org.joda.time.LocalDate;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import uk.colessoft.android.hilllist.BHApplication;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.ListHillsMapFragmentActivity;
import uk.colessoft.android.hilllist.activities.Main;
import uk.colessoft.android.hilllist.activities.PreferencesActivity;
import uk.colessoft.android.hilllist.adapter.HillsAdapter;
import uk.colessoft.android.hilllist.database.BaggingTable;
import uk.colessoft.android.hilllist.database.DbHelper;
import uk.colessoft.android.hilllist.database.HillsTables;
import uk.colessoft.android.hilllist.model.TinyHill;
import uk.colessoft.android.hilllist.presenter.HillListPresenter;
import uk.colessoft.android.hilllist.views.HillListView;

import static uk.colessoft.android.hilllist.database.HillsTables.KEY_HILLNAME;

public class HillListFragment extends
        MvpLceFragment<SwipeRefreshLayout, List<TinyHill>, HillListView, HillListPresenter> implements HillListView, SwipeRefreshLayout.OnRefreshListener, HillsAdapter.BaggingHillListener
        , HillsAdapter.RowClickListener {

    private int climbedCount = 0;
    HillsAdapter adapter;
    @BindView(R.id.myListView)
    RecyclerView recyclerView;

    @Override
    public void onRefresh() {

    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @Override
    public void setData(List<TinyHill> data) {
        adapter.setHills(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadHills(hilltype, countryClause, where, orderBy, filterHills);
    }

    @Override
    public void onChecked(View xcv, int id) {

        ((RelativeLayout) xcv.getParent()).setBackgroundColor(getResources().getColor(R.color.paler_light_green));
        presenter.markHillClimbed(id, new LocalDate());
    }

    @Override
    public void onUnchecked(View xcv, int id) {
        ((RelativeLayout) xcv.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
        presenter.markHillNotClimbed(id);
    }

    private void updateDetailIfPresent(int id) {
        HillDetailFragment fragment = (HillDetailFragment) getActivity()
                .getSupportFragmentManager().findFragmentById(
                        R.id.hill_detail_fragment);

        if (fragment != null && fragment.isInLayout()) {
            hillSelectedListener.onHillSelected(id);
        }
    }

    @Override
    public void onRowClicked(int hillId) {
        hillSelectedListener.onHillSelected(hillId);
    }

    @Override
    public void hillMarkedUnclimbed(boolean succeeded, int id) {
        updateDetailIfPresent(id);
    }

    @Override
    public void hillMarkedClimbed(boolean succeeded, int id) {
        updateDetailIfPresent(id);
    }


    public interface OnHillSelectedListener {
        void onHillSelected(int rowid);
    }


    @Inject
    DbHelper dbHelper;

    private String where = null;
    private String orderBy;


    private String hilltype;
    private String hilllistType;
    private String countryClause;
    private final DecimalFormat df3 = new DecimalFormat();
    private int filterHills;
    private OnHillSelectedListener hillSelectedListener;

    private int currentRowId;

    private ProgressDialog dialog;


    private final Handler handlerHc = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int total = msg.arg1;
            if (msg.arg2 == -1) {
                dialog.setMax(msg.arg1);
            } else if (msg.arg2 == -2) {
                dialog.cancel();
            } else {
                dialog.setProgress(total);
            }

        }
    };


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            hillSelectedListener = (OnHillSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHillSelectedListener");
        }
    }

    @Override
    public HillListPresenter createPresenter() {
        return ((BHApplication) getActivity().getApplication()).getHillListComponent().presenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BHApplication) getActivity().getApplication()).getDbComponent().inject(this);
        df3.isParseIntegerOnly();


        hilltype = getActivity().getIntent().getExtras().getString("hilltype");
        hilllistType = getActivity().getIntent().getExtras()
                .getString("hilllistType");
        if (!"".equals(hilllistType)) {
            getActivity().setTitle(hilllistType);
        } else
            getActivity().setTitle("Results");
        int country = getActivity().getIntent().getExtras().getInt("country");
        where = getActivity().getIntent().getExtras().getString("search");

        switch (country) {
            case Main.SCOTLAND: {
                countryClause = "cast(_Section as float) between 1 and 28.9";
                break;

            }
            case Main.WALES: {
                countryClause = "cast(_Section as float) between 30 and 32.9";
                break;

            }
            case Main.ENGLAND: {
                countryClause = "cast(_Section as float) between 33 and 42.9";
                break;

            }
            case Main.OTHER_GB: {
                countryClause = "_Section='29' OR (cast(_Section as float) between 43 and 45)";
                break;

            }

        }

        orderBy = "cast(" + HillsTables.KEY_HEIGHTM + " as float)" + " desc";


        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.hill_lists_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.list_hills, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        ButterKnife.bind(this, view);

        contentView.setOnRefreshListener(this);
        registerForContextMenu(recyclerView);

        adapter = new HillsAdapter(getActivity(), this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        loadData(false);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home: {
                // app icon in Action Bar clicked; go home
                Intent intent = new Intent(getActivity(), Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            }
            case (R.id.menu_list_alpha): {

                orderBy = KEY_HILLNAME;
                presenter.loadHills(hilltype, countryClause, where, orderBy, filterHills);
                return true;

            }
            case (R.id.menu_list_height): {

                orderBy = "cast(" + HillsTables.KEY_HEIGHTM + " as float)"
                        + " desc";
                presenter.loadHills(hilltype, countryClause, where, orderBy, filterHills);
                return true;

            }

            case (R.id.menu_show_climbed): {

                int CLIMBED_HILLS = 1;
                filterHills = CLIMBED_HILLS;
                presenter.loadHills(hilltype, countryClause, where, orderBy, filterHills);
                return true;
            }

            case (R.id.menu_show_not_climbed): {
                int UNCLIMBED_HILLS = 2;
                filterHills = UNCLIMBED_HILLS;
                presenter.loadHills(hilltype, countryClause, where, orderBy, filterHills);
                return true;
            }

            case (R.id.menu_show_all): {
                int ALL_HILLS = 0;
                filterHills = ALL_HILLS;
                presenter.loadHills(hilltype, countryClause, where, orderBy, filterHills);
                return true;
            }
            case (R.id.menu_show_map): {
                Intent intent = new Intent(getActivity(),
                        ListHillsMapFragmentActivity.class);
                intent.putExtra("groupId", hilltype);
                intent.putExtra("orderBy", orderBy);
                intent.putExtra("moreWhere", where);
                intent.putExtra("countryClause", countryClause);
                intent.putExtra("title", "Map of " + hilllistType);
                intent.putExtra("selectedHill", currentRowId);
                startActivity(intent);
                return true;

            }

            case (R.id.menu_search): {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

                alert.setTitle("Search");

                // Set an EditText view to get user input
                final EditText searchText = new EditText(getActivity());

                alert.setView(searchText);

                alert.setPositiveButton("Ok",
                        (dialog12, whichButton) -> {
                            String value = searchText.getText().toString();

                            if (!"".equals(value))
                                where = KEY_HILLNAME + " like "
                                        + DatabaseUtils.sqlEscapeString("%"
                                        + value + "%");
                            else
                                where = null;

                            presenter.loadHills(hilltype, countryClause, where, orderBy, filterHills);
                        });

                alert.setNegativeButton("Cancel",
                        (dialog1, whichButton) -> {

                        });

                AlertDialog search = alert.create();
                search.show();
                return true;
            }
            case (R.id.menu_all_climbed): {
                showUpdatingDialog("Marking Hills Climbed");
                presenter.setAllClimbedStatus(hilltype, countryClause, where, orderBy, filterHills, true, handlerHc);
                return true;
            }

            case (R.id.menu_none_climbed): {
                showUpdatingDialog("Marking Hills Not Climbed");
                presenter.setAllClimbedStatus(hilltype, countryClause, where, orderBy, filterHills, false, handlerHc);
                return true;
            }
        }
        return false;

    }

    private void showUpdatingDialog(String message) {
        dialog = new ProgressDialog(getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        this.dialog.setMessage(message);
        this.dialog.show();
    }

    private void refresh(final Cursor result) {

        //cursorAdapter.swapCursor(result);
        // populate detail view with first row

        if (result.getCount() != 0) {

            result.moveToPosition(0);
            int firstRow = result.getInt(result.getColumnIndexOrThrow("hill_id"));

            HillDetailFragment fragment = (HillDetailFragment) getActivity()
                    .getSupportFragmentManager().findFragmentById(
                            R.id.hill_detail_fragment);

            if (fragment != null && fragment.isInLayout()) {

                // populate detail view with first row
                currentRowId = firstRow;
                hillSelectedListener.onHillSelected(firstRow);
            }
            // hillSelectedListener.onHillSelected(firstRow);

        }
        String updateTitle = hilllistType + " - " + String.valueOf(result.getCount())
                + " hills found";
        getActivity().setTitle(updateTitle);
        //reset climbed count


    }


}
