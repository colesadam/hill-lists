package uk.colessoft.android.hilllist.mvp.lce;

import android.content.Context;
import android.os.Bundle;

import uk.colessoft.android.hilllist.mvp.HillsPresenter;
import uk.colessoft.android.hilllist.mvp.HillsView;
import uk.colessoft.android.hilllist.mvp.model.HillsAsyncLoader;

public class SqlBriteHillsPresenter implements HillsPresenter {

    private static final String TAG = "SqlBriteHillsPresenter";

    private Context mContext;
    private Bundle activityArguments;

    private int failingCounter = 0;
    private HillsAsyncLoader hillsLoader;
    @Override
    public void loadHills(boolean pullToRefresh, String orderBy) {

    }

    @Override
    public void attachView(HillsView view) {

    }

    @Override
    public void detachView(boolean retainInstance) {

    }
}
