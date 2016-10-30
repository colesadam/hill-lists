package uk.colessoft.android.hilllist.views;


import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;

import uk.colessoft.android.hilllist.model.TinyHill;

public interface HillListView extends MvpLceView<List<TinyHill>> {
    void hillChangeBagging(boolean succeeded, int id);

}
