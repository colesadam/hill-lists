package uk.colessoft.android.hilllist.presenter;


import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import uk.colessoft.android.hilllist.database.DbHelper;
import uk.colessoft.android.hilllist.model.Hill;
import uk.colessoft.android.hilllist.views.HillDetailView;

import static android.content.ContentValues.TAG;

public class HillDetailPresenter extends MvpBasePresenter<HillDetailView> {

    private DbHelper dbHelper;

    @Inject
    public HillDetailPresenter(DbHelper dbHelper){
        super();
        this.dbHelper=dbHelper;
    }

    @Override
    public void attachView(HillDetailView view) {
        super.attachView(view);
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
    }

    public void getHill(int rowid) {
        Log.d(TAG,"presenter hit");
        Hill hill = dbHelper.getHill(rowid);
        if(isViewAttached()){
            getView().updateHill(hill);
        }
    }
}
