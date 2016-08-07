package uk.colessoft.android.hilllist.mvp.lce;

import android.content.Context;
import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import uk.colessoft.android.hilllist.mvp.HillPresenter;
import uk.colessoft.android.hilllist.mvp.HillView;
import uk.colessoft.android.hilllist.mvp.model.HillAsyncLoader;
import uk.colessoft.android.hilllist.model.Hill;

public class SimpleHillPresenter extends MvpBasePresenter<HillView> implements HillPresenter {

    private static final String TAG = "SimpleHillsPresenter";

    private Context mContext;
    private Long hillId;

    private int failingCounter = 0;
    private HillAsyncLoader hillLoader;

    public SimpleHillPresenter(Context context) {
        super();
        mContext = context;
    }

    @Override
    public void loadHill(long hillId) {


        if (hillLoader != null && !hillLoader.isCancelled()) {
            hillLoader.cancel(true);
        }

        hillLoader = new HillAsyncLoader(
                new HillAsyncLoader.HillLoaderListener() {
                    @Override
                    public void onSuccess(Hill hill) {
                        if (isViewAttached()) {
                            getView().updateHill(hill);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        if (isViewAttached()) {

                            Log.d(TAG, "showError(" + e.getLocalizedMessage());

                        }
                    }
                }, mContext);
        hillLoader.execute(hillId);
    }

    @Override
    public void attachView(HillView view) {
        super.attachView(view);

    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);

        if (!retainInstance) {
            if (hillLoader != null) {
                hillLoader.cancel(true);
            }
        }
    }
}
