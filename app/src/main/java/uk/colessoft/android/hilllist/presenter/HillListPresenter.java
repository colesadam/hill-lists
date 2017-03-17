package uk.colessoft.android.hilllist.presenter;


import android.os.Handler;
import android.os.Message;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import uk.colessoft.android.hilllist.database.DbHelper;
import uk.colessoft.android.hilllist.model.TinyHill;
import uk.colessoft.android.hilllist.views.HillListView;

public class HillListPresenter extends MvpBasePresenter<HillListView> {

    private DbHelper dbHelper;

    @Inject
    public HillListPresenter(DbHelper dbHelper) {
        super();
        this.dbHelper = dbHelper;
    }

    public void loadHills(String groupId, String countryClause
            , String moreWhere, String orderBy, int filter) {
        dbHelper.getHills(groupId, countryClause, moreWhere, orderBy, filter)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(hills -> {
            if (isViewAttached()) {
                getView().setData(hills);
            }
        });
    }

    public void markHillClimbed(int hillNumber, LocalDate dateClimbed) {
        dbHelper.markHillClimbed(hillNumber, dateClimbed, "")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (isViewAttached()) {
                        getView().hillChangeBagging(result > 0 ? true : false, hillNumber);
                    }
                });
    }

    public void markHillNotClimbed(int hillNumber) {
        dbHelper.markHillNotClimbed(hillNumber)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    if (isViewAttached()) {
                        getView().hillChangeBagging(result > 0 ? true : false, hillNumber);
                    }
                });
    }

    public void setAllClimbedStatus(String groupId, String countryClause
            , String moreWhere, String orderBy, int filter, boolean setClimbed, Handler handler) {

        dbHelper.getHills(groupId, countryClause, moreWhere, orderBy, filter).flatMap(
                hills -> {
                    List<Observable<Long>> observableList = new ArrayList<>();
                    Message m1 = handler.obtainMessage();
                    m1.arg1 = hills.size();
                    m1.arg2 = -1;
                    handler.sendMessage(m1);
                    int i = 0;
                    for (TinyHill hill : hills) {
                        i++;
                        Message msg = handler.obtainMessage();
                        msg.arg1 = i;
                        handler.sendMessage(msg);
                        if (setClimbed) {
                            String notes = "";
                            LocalDate date = new LocalDate();
                            if (hill.isClimbed()) {
                                notes = hill.getNotes();
                                date = hill.getDateClimbed();
                            }
                            observableList.add(dbHelper.markHillClimbed(hill._id, date, notes));
                        } else {
                            observableList.add(dbHelper.markHillNotClimbed(hill._id).map(result -> (long) result));
                        }
                    }
                    return Observable.zip(observableList, result -> result);
                }).observeOn(AndroidSchedulers.mainThread()).subscribe(
                result -> {
                    Message msg = handler.obtainMessage();
                    msg.arg2 = -2;
                    handler.sendMessage(msg);
                    loadHills(groupId, countryClause, moreWhere, orderBy, filter);
                }
        );

    }


    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
    }


}
