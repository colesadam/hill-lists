package uk.colessoft.android.hilllist.mvp;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;


public interface HillPresenter extends MvpPresenter<HillView> {

    public void loadHill(final long hillId);
}
