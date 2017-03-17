package uk.colessoft.android.hilllist.presenter;


import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.joda.time.LocalDate;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import uk.colessoft.android.hilllist.database.DbHelper;
import uk.colessoft.android.hilllist.views.HillDetailView;

public class HillDetailPresenter extends MvpBasePresenter<HillDetailView> {

    private DbHelper dbHelper;

    @Inject
    public HillDetailPresenter(DbHelper dbHelper) {
        super();
        this.dbHelper = dbHelper;
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
        dbHelper.getHill(rowid).observeOn(AndroidSchedulers.mainThread()).subscribe(hill -> {
            if (isViewAttached()) {
                getView().updateHill(hill);
            }
        });

    }

    public void markHillClimbed(int hillNumber, LocalDate dateClimbed, String notes, String message) {
        dbHelper.markHillClimbed(hillNumber, dateClimbed, notes)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (isViewAttached()) {
                        getView().hillMarkedClimbed(result > 0 ? true : false, message);
                    }
                });
    }

    public void markHillNotClimbed(int hillNumber) {
        dbHelper.markHillNotClimbed(hillNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (isViewAttached()) {
                        getView().hillMarkedUnclimbed(result > 0 ? true : false);
                    }
                });
    }
}
