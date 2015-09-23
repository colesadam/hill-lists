package uk.colessoft.android.hilllist.mvp.lce.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.mvp.HillsAdapter;
import uk.colessoft.android.hilllist.mvp.HillsPresenter;
import uk.colessoft.android.hilllist.mvp.HillsView;
import uk.colessoft.android.hilllist.mvp.lce.SimpleHillsPresenter;
import uk.colessoft.android.hilllist.objects.Hill;

public class MvpHillListFragment extends MvpLceFragment<SwipeRefreshLayout, List<Hill>, HillsView, HillsPresenter>
        implements HillsView ,SwipeRefreshLayout.OnRefreshListener, HillsAdapter.RecyclerItemViewClick{

    @Bind(R.id.recyclerView)RecyclerView recyclerView;

    HillsAdapter adapter;
    private OnHillSelectedListener hillSelectedListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            hillSelectedListener = (OnHillSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHillSelectedListener");
        }
    }

    public interface OnHillSelectedListener {
        public void onHillSelected(int rowid);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        ButterKnife.bind(this, view);
        // Setup contentView == SwipeRefreshView
        contentView.setOnRefreshListener(this);

        // Setup recycler view
        adapter = new HillsAdapter(getActivity(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        loadData(false);
    }

    @Nullable @Override
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

        return new SimpleHillsPresenter(this.getContext(),getActivity().getIntent().getExtras());
    }

    @Override
    public void setData(List<Hill> data) {
        adapter.setHills(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.loadHills(pullToRefresh);

    }

    @Override public void onRefresh() {
        loadData(true);
    }

    @Override public void showContent() {
        super.showContent();
        contentView.setRefreshing(false);
    }

    @Override public void showError(Throwable e, boolean pullToRefresh) {
        super.showError(e, pullToRefresh);
        contentView.setRefreshing(false);
    }

    @Override
    public void hillClicked(int id) {
        hillSelectedListener.onHillSelected(id);
    }
}
