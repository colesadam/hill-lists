package uk.colessoft.android.hilllist.mvp;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;


public interface HillsPresenter extends MvpPresenter<HillsView> {

    public void loadHills(final boolean pullToRefresh);
}
