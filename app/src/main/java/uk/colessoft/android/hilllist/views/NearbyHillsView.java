package uk.colessoft.android.hilllist.views;


import android.location.Location;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.ArrayList;
import java.util.List;

import uk.colessoft.android.hilllist.model.TinyHill;

public interface NearbyHillsView extends MvpView{

    void listChanged(List<TinyHill> hills);

}
