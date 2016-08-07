package uk.colessoft.android.hilllist.mvp;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;


public interface RxHillsPresenter extends MvpLceRxPresenter<HillsView> {

    public void loadHills(final boolean pullToRefresh, String orderBy);
}
