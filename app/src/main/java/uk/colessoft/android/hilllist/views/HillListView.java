package uk.colessoft.android.hilllist.views;


import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;

import java.util.List;

import uk.colessoft.android.hilllist.model.TinyHill;

public interface HillListView extends MvpView {
    void hillChangeBagging(boolean succeeded, int id);
    void setData(List<TinyHill> hills);
}
