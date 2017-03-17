package uk.colessoft.android.hilllist.presenter;


import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

import uk.colessoft.android.hilllist.database.DbHelper;

public class NearbyHillsPresenterFactory {

    private DbHelper dbHelper;


    @Inject
    public NearbyHillsPresenterFactory(DbHelper dbHelper) {
        super();
        this.dbHelper = dbHelper;
    }

    public NearbyHillsPresenter create(GoogleApiClient googleApiClient){
        return new NearbyHillsPresenter(googleApiClient,dbHelper);
    }
}
