package uk.colessoft.android.hilllist.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;

import org.joda.time.LocalDate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.colessoft.android.hilllist.BHApplication;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.ListHillsMapFragmentActivity;
import uk.colessoft.android.hilllist.activities.Main;
import uk.colessoft.android.hilllist.adapter.HillsAdapter;
import uk.colessoft.android.hilllist.database.HillsTables;
import uk.colessoft.android.hilllist.model.TinyHill;
import uk.colessoft.android.hilllist.presenter.HillListPresenter;
import uk.colessoft.android.hilllist.views.HillListView;

import static uk.colessoft.android.hilllist.database.HillsTables.KEY_HILLNAME;

public class HillListFragment extends MvpFragment<HillListView,HillListPresenter>  implements HillListView, HillsAdapter.BaggingHillListener
        , HillsAdapter.RowClickListener {

    private int climbedCount = 0;
    HillsAdapter adapter;

    @BindView(R.id.myListView)
    RecyclerView recyclerView;

    private String where = null;
    private String orderBy;
    private String hilltype;
    private String hilllistType;
    private String countryClause;
    private int filterHills;
    private OnHillSelectedListener hillSelectedListener;

    private int currentRowId;

    private ProgressDialog dialog;

    private final Handler updateHandler = new Handler() {
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
    public void setData(List<TinyHill> data) {
        String updateTitle=hilllistType + " - " + String.valueOf(data.size())
                + " hills found";
        getActivity().setTitle(updateTitle);
        adapter.setHills(data);
        adapter.notifyDataSetChanged();
    }

    public void loadData() {
        presenter.loadHills(hilltype, countryClause, where, orderBy, filterHills);
    }

    @Override
    public void onChecked(View xcv, int id) {

        ((ConstraintLayout) xcv.getParent()).setBackgroundColor(getResources().getColor(R.color.paler_light_green));
        presenter.markHillClimbed(id, new LocalDate());
    }

    @Override
    public void onUnchecked(View xcv, int id) {
        ((ConstraintLayout) xcv.getParent()).setBackgroundColor(getResources().getColor(R.color.white));
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
    public void hillChangeBagging(boolean succeeded, int id) {
        updateDetailIfPresent(id);
    }

    public interface OnHillSelectedListener {
        void onHillSelected(int rowid);
    }

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

        registerForContextMenu(recyclerView);

        adapter = new HillsAdapter(getActivity(), this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        loadData();
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
                presenter.setAllClimbedStatus(hilltype, countryClause, where, orderBy, filterHills, true, updateHandler);
                return true;
            }

            case (R.id.menu_none_climbed): {
                showUpdatingDialog("Marking Hills Not Climbed");
                presenter.setAllClimbedStatus(hilltype, countryClause, where, orderBy, filterHills, false, updateHandler);
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
}
