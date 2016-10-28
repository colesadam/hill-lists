package uk.colessoft.android.hilllist.views;


import com.hannesdorfmann.mosby.mvp.MvpView;

import uk.colessoft.android.hilllist.model.Hill;

public interface HillDetailView extends MvpView {

    void updateHill(Hill hill);

    void hillMarkedClimbed(boolean succeeded, String message);

    void hillMarkedUnclimbed(boolean succeeded);
}
