package uk.colessoft.android.hilllist.utility;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LatLangBounds {

    private Set<Double[]> latLongs = new HashSet<Double[]>();

    public void addLatLang(Double latitude, Double longitude) {
        latLongs.add(new Double[]{latitude, longitude});
    }

    public Double getSmallestLat() {

        Set<Double> lats = new HashSet<Double>();
        for (Double[] latLng : latLongs) {
            lats.add(latLng[0]);
        }
        return Collections.min(lats);

    }

    public Double getLargestLat() {
        Set<Double> lats = new HashSet<Double>();
        for (Double[] latLng : latLongs) {
            lats.add(latLng[0]);
        }
        return Collections.max(lats);
    }

    public Double getSmallestLong() {
        Set<Double> longs = new HashSet<Double>();
        for (Double[] latLng : latLongs) {
            longs.add(latLng[1]);
        }
        return Collections.min(longs);
    }

    public Double getLargestLong() {
        Set<Double> longs = new HashSet<Double>();
        for (Double[] latLng : latLongs) {
            longs.add(latLng[1]);
        }
        return Collections.max(longs);
    }
}
