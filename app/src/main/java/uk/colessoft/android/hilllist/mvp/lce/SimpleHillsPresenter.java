package uk.colessoft.android.hilllist.mvp.lce;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import uk.colessoft.android.hilllist.mvp.HillsView;
import uk.colessoft.android.hilllist.mvp.model.HillsAsyncLoader;
import uk.colessoft.android.hilllist.objects.Hill;

public class SimpleHillsPresenter extends MvpBasePresenter<HillsView> implements uk.colessoft.android.hilllist.mvp.HillsPresenter {

    private static final String TAG = "SimpleHillsPresenter";

    private Context mContext;
    private Bundle activityArguments;

    private int failingCounter = 0;
    private HillsAsyncLoader hillsLoader;

    public SimpleHillsPresenter(Context context, Bundle args) {
        super();
        mContext = context;
        activityArguments = args;
    }

    @Override
    public void loadHills(final boolean pullToRefresh) {
        getView().showLoading(pullToRefresh);

        if (hillsLoader != null && !hillsLoader.isCancelled()) {
            hillsLoader.cancel(true);
        }

        hillsLoader = new HillsAsyncLoader(false,
                new HillsAsyncLoader.HillsLoaderListener() {
                    @Override
                    public void onSuccess(List<Hill> hills) {
                        if (isViewAttached()) {
                            getView().setData(hills);
                            getView().showContent();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        if (isViewAttached()) {

                            Log.d(TAG, "showError(" + e.getLocalizedMessage() + " , " + pullToRefresh + ")");
                            getView().showError(e, pullToRefresh);
                        }
                    }
                }, mContext);
        hillsLoader.execute(activityArguments);
    }

    @Override
    public void attachView(HillsView view) {
        super.attachView(view);

    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);

        if (!retainInstance) {
            if (hillsLoader != null) {
                hillsLoader.cancel(true);
            }
        }
    }
}
