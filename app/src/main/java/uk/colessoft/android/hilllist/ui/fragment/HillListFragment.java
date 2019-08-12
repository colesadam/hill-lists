package uk.colessoft.android.hilllist.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.dao.IsHillClimbed;
import uk.colessoft.android.hilllist.ui.activity.ListHillsMapFragmentActivity;
import uk.colessoft.android.hilllist.ui.activity.Main;
import uk.colessoft.android.hilllist.ui.adapter.HillDetailListAdapter;
import uk.colessoft.android.hilllist.dao.HillsOrder;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.domain.entity.Bagging;
import uk.colessoft.android.hilllist.domain.HillDetail;
import uk.colessoft.android.hilllist.ui.viewmodel.HillListViewModel;

import static uk.colessoft.android.hilllist.database.HillsTables.KEY_HILLNAME;

public class HillListFragment extends DaggerFragment{

    private int climbedCount = 0;

    private List<HillDetail> hills;

    @Inject
    BritishHillsDatasource dbAdapter;

    private String where = null;
    private String orderBy;

    private RecyclerView hillsView;
    private boolean useMetricHeights;
    private String hilltype;
    private String hilllistType;
    private String countryClause;
    private final DecimalFormat df3 = new DecimalFormat();
    private int filterHills;
    private OnHillSelectedListener hillSelectedListener;
    private CheckBoxListener checkBoxListener;
    private HillListViewModel viewModel;

    private int currentRowId;
    private ProgressDialog dialog;
    private RecyclerView.Adapter<HillDetailListAdapter.HillDetailViewHolder> hillsAdapter;

    private ProgressDialog pdialog;


    public interface OnHillSelectedListener {
        void onHillSelected(long rowid);
    }

    public interface CheckBoxListener {
        void onCheckBoxClicked(int hill_id, boolean checked);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hillsView.setAdapter(hillsAdapter);

        HillDetailFragment fragment = (HillDetailFragment) getActivity()
                .getSupportFragmentManager().findFragmentById(
                        R.id.hill_detail_fragment);

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
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = ViewModelProviders.of(getActivity()).get(HillListViewModel.class);
        checkBoxListener = new CheckBoxListener() {
            @Override
            public void onCheckBoxClicked(int hill_id, boolean checked) {
                if (!checked) viewModel.markHillNotClimbed(hill_id);
                else viewModel.markHillClimbed(new Bagging(hill_id, new Date(), ""));
            }
        };

        final Observer<List<HillDetail>> nameObserver = new Observer<List<HillDetail>>() {
            @Override
            public void onChanged(@Nullable final List<HillDetail> newHills) {
                hillsAdapter = new HillDetailListAdapter(newHills, hillSelectedListener, checkBoxListener);
                hillsView.setAdapter(hillsAdapter);
                hills = newHills;
                hillsAdapter.notifyDataSetChanged();
                Log.d("ChangedData", "Hills returned:" + newHills.size());
                String updateTitle = hilllistType + " - " + String.valueOf(newHills.size())
                        + " hills found";
                getActivity().setTitle(updateTitle);
            }
        };

        viewModel.getHills().observe(getActivity(), nameObserver);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.hill_lists_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewer = inflater.inflate(R.layout.list_hills, container, false);
        hillsView = viewer
                .findViewById(R.id.myListView);
        hillsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        df3.isParseIntegerOnly();
        super.onCreate(savedInstanceState);

        registerForContextMenu(hillsView);

        hilllistType = getActivity().getIntent().getExtras()
                .getString("hilllistType");
        if (!"".equals(hilllistType)) {
            getActivity().setTitle(hilllistType);
        } else
            getActivity().setTitle("Results");

        return viewer;
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
                viewModel.orderHills(HillsOrder.NAME_ASC);
                return true;
            }

            case (R.id.menu_list_height): {
                viewModel.orderHills(HillsOrder.HEIGHT_DESC);
                return true;
            }

            case (R.id.menu_show_climbed): {

                viewModel.filterClimbed(IsHillClimbed.YES);
                return true;
            }

            case (R.id.menu_show_not_climbed): {
                viewModel.filterClimbed(IsHillClimbed.NO);
                return true;
            }

            case (R.id.menu_show_all): {
                viewModel.filterClimbed(null);
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

                            viewModel.searchHills(where);

                        });

                alert.setNegativeButton("Cancel",
                        (dialog1, whichButton) -> {

                        });

                AlertDialog search = alert.create();
                search.show();
                return true;
            }
            case (R.id.menu_all_climbed): {
                viewModel.markAllHillsClimbed();
                return true;
            }

            case (R.id.menu_none_climbed): {
                viewModel.markAllHillsNotClimbed();
                return true;
            }
        }
        return false;

    }

}
