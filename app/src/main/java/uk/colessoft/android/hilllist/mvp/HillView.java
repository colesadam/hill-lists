package uk.colessoft.android.hilllist.mvp;

import com.hannesdorfmann.mosby.mvp.MvpView;

import uk.colessoft.android.hilllist.objects.Hill;


public interface HillView extends MvpView {

    void updateHill(Hill hill);
}
