package uk.colessoft.android.hilllist.mvp.lce.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.activities.ListHillsMapFragmentActivity;
import uk.colessoft.android.hilllist.activities.Main;
import uk.colessoft.android.hilllist.database.ColumnKeys;
import uk.colessoft.android.hilllist.database.HillDbAdapter;
import uk.colessoft.android.hilllist.mvp.HillsAdapter;
import uk.colessoft.android.hilllist.mvp.HillsPresenter;
import uk.colessoft.android.hilllist.mvp.HillsView;
import uk.colessoft.android.hilllist.mvp.lce.SimpleHillsPresenter;
import uk.colessoft.android.hilllist.objects.Hill;

public class MvpHillListFragment extends MvpLceFragment<SwipeRefreshLayout, List<Hill>, HillsView, HillsPresenter>
        implements HillsView, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    HillsAdapter adapter;
    private Boolean aToZ = false;
    private int filterHills;
    private final int ALL_HILLS = 0;
    private final int CLIMBED_HILLS = 1;
    private final int UNCLIMBED_HILLS = 2;
    private String orderBy;
    private Menu mMenu;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        ButterKnife.bind(this, view);
        orderBy = "cast(" + ColumnKeys.KEY_HEIGHTF + " as float)" + " desc";
        contentView.setOnRefreshListener(this);

        // Setup recycler view
        adapter = new HillsAdapter(getActivity(), (HillsAdapter.RecyclerItemViewClick) getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        loadData(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mvp_list_hills, container, false);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getLocalizedMessage();
    }

    @Override
    public HillsPresenter createPresenter() {

        return new SimpleHillsPresenter(this.getContext(), getActivity().getIntent().getExtras());
    }

    @Override
    public void setData(List<Hill> data) {
        adapter.setHills(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadHills(pullToRefresh,orderBy);

    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    @Override
    public void showContent() {
        super.showContent();
        contentView.setRefreshing(false);
    }

    @Override
    public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        contentView.setRefreshing(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.hill_lists_menu, menu);
        mMenu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        //int index = myListView.getSelectedItemPosition();

        switch (item.getItemId()) {
            case android.R.id.home: {
                // app icon in Action Bar clicked; go home
                Intent intent = new Intent(getActivity(), Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;

            }
            case (R.id.menu_list_alpha): {

                orderBy = ColumnKeys.KEY_HILLNAME;
                aToZ = true;
                onPrepareOptionsMenu(mMenu);
                orderBy = ColumnKeys.KEY_HILLNAME;
                loadData(false);
                recyclerView.scrollToPosition(0);
                return true;

            }
            case (R.id.menu_list_height): {

                orderBy = "cast(" + HillDbAdapter.KEY_HEIGHTM + " as float)"
                        + " desc";
                aToZ = false;
                loadData(false);
               recyclerView.scrollToPosition(0);
                onPrepareOptionsMenu(mMenu);
                return true;

            }

            case (R.id.menu_show_climbed): {

                filterHills = CLIMBED_HILLS;
                //updateList();
                return true;
            }

            case (R.id.menu_show_not_climbed): {
                filterHills = UNCLIMBED_HILLS;
                // updateList();
                return true;
            }

            case (R.id.menu_show_all): {
                filterHills = ALL_HILLS;
                // updateList();
                return true;
            }
            case (R.id.menu_show_map): {
                Intent intent = new Intent(getActivity(),
                        ListHillsMapFragmentActivity.class);
//                intent.putExtra("groupId", hilltype);
//                intent.putExtra("orderBy", orderBy);
//                intent.putExtra("moreWhere", where);
//                intent.putExtra("countryClause", countryClause);
//                intent.putExtra("title", "Map of " + hilllistType);
//                intent.putExtra("selectedHill", currentRowId);
//                startActivity(intent);
                return true;

            }

            case (R.id.menu_search): {
//                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//
//                alert.setTitle("Search");
//
//                // Set an EditText view to get user input
//                final EditText searchText = new EditText(getActivity());
//
//                alert.setView(searchText);
//
//                alert.setPositiveButton("Ok",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int whichButton) {
//                                String value = searchText.getText().toString();
//
//                                if (!"".equals(value))
//                                    where = "hillname like "
//                                            + DatabaseUtils.sqlEscapeString("%"
//                                            + value + "%");
//                                else
//                                    where = null;
//
//                                updateList();
//
//                            }
//                        });
//
//                alert.setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int whichButton) {
//
//                            }
//                        });
//
//                AlertDialog search = alert.create();
//                search.show();
                return true;
            }
            case (R.id.menu_all_climbed): {
                // markAllHills(1);
                return true;
            }

            case (R.id.menu_none_climbed): {
                //  markAllHills(0);
                return true;
            }
        }
        return false;

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.menu_list_alpha).setVisible(!aToZ);
        menu.findItem(R.id.menu_list_height).setVisible(aToZ);

    }
}
