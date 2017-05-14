package uk.colessoft.android.hilllist.presenter;


import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import uk.colessoft.android.hilllist.database.DbHelper;
import uk.colessoft.android.hilllist.model.TinyHill;
import uk.colessoft.android.hilllist.views.NearbyHillsView;

import static android.content.ContentValues.TAG;

public class NearbyHillsPresenter extends MvpBasePresenter<NearbyHillsView>
        implements LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private DbHelper dbHelper;
    private double radius = 16;
    private Comparator comparator;
    private Boolean climbed = false;
    private List<TinyHill> hills;

    public NearbyHillsPresenter(GoogleApiClient googleApiClient,DbHelper dbHelper) {
        mGoogleApiClient = googleApiClient;
        this.dbHelper = dbHelper;
        createLocationRequest();
    }

    public void startListening() {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
            Log.e(TAG, "Permission not granted");
        }
        if (mLastLocation != null) {
            updateListOfHills(radius);
        }

    }

    public void setmGoogleApiClient(GoogleApiClient apiClient){
        this.mGoogleApiClient=apiClient;
    }

    public void updateListOfHills(double radius) {
        this.radius = radius;
        dbHelper.getNearbyHills(mLastLocation.getLatitude(),
                mLastLocation.getLongitude(), radius).observeOn(AndroidSchedulers.mainThread()).subscribe(
                result -> {
                    if (isViewAttached()) {
                        if (comparator != null) {
                            Collections.sort(result, comparator);
                        }
                        hills = result;
                        filterClimbed(climbed);
                        getView().listChanged(result);
                    }
                }
        );
    }

    private void sort() {
        if(isViewAttached() && hills != null){
            Collections.sort(hills, comparator);
            getView().listChanged(hills);
        }

    }

    public void sortByHeight(){
        comparator = new HillHeightComparator();
        sort();
    }

    public void sortByName(){
        comparator = new HillNameComparator();
        sort();
    }

    public void sortByDistance(){
        comparator = new HillDistanceComparator();
        sort();
    }

    public void filter(Boolean climbed) {
        this.climbed = climbed;

        if(climbed != null){
            filterClimbed(climbed);
        }

        Collections.sort(hills, comparator);
        if(isViewAttached()){
            getView().listChanged(hills);
        }

    }

    public List<TinyHill> getCurrentHills(){
        return hills;
    }

    public void search(String query){
        List<TinyHill> filteredHills = new ArrayList<>();
        for(TinyHill hill:hills) {
            if(hill.getHillname().toLowerCase().contains(query.toLowerCase())){
                filteredHills.add(hill);
            }
        }
        hills = filteredHills;
        if(isViewAttached()){
            getView().listChanged(filteredHills);
        }
    }

    private void filterClimbed(Boolean climbed) {
        List<TinyHill> filteredHills = new ArrayList<>();
        for(TinyHill hill:hills){
            if(climbed.equals(true) && hill.getDateClimbed() != null) {
                filteredHills.add(hill);
            }
            else if(climbed.equals(false) && hill.getDateClimbed() == null) {
                filteredHills.add(hill);
            }
        }
        hills = filteredHills;
    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        updateListOfHills(radius);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(5);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(10); // 10 meters
    }


    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    private static class HillHeightComparator implements Comparator {

        public int compare(Object hill1, Object hill2) {

            Float height1 = ((TinyHill) hill1).getHeightM();
            Float height2 = ((TinyHill) hill2).getHeightM();
            return (height2.compareTo(height1));
        }

    }

    private static class HillDistanceComparator implements Comparator {

        public int compare(Object hill1, Object hill2) {

            Double distance1 = ((TinyHill) hill1).getDistance();
            Double distance2 = ((TinyHill) hill2).getDistance();
            return (distance2.compareTo(distance1));
        }

    }

    private static class HillNameComparator implements Comparator {

        public int compare(Object hill1, Object hill2) {

            String name1 = ((TinyHill) hill1).getHillname();
            String name2 = ((TinyHill) hill2).getHillname();
            return (name1.compareTo(name2));
        }

    }
}
