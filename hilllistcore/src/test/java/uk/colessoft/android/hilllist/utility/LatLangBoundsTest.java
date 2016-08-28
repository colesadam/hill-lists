package uk.colessoft.android.hilllist.utility;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class LatLangBoundsTest {

    @Test
    public void returnsCorrectMinsAndMaxes(){

        LatLangBounds llb = new LatLangBounds();

        llb.addLatLong(1.0,2.0);
        llb.addLatLong(55.0,2.0);
        llb.addLatLong(1.0,21.0);
        llb.addLatLong(0.5,2.0);
        llb.addLatLong(1.0,-5.0);

        assertEquals(llb.getLargestLat(),55.0,0.01);
        assertEquals(llb.getLargestLong(),21.0,0.01);
        assertEquals(llb.getSmallestLat(),0.5,0.01);
        assertEquals(llb.getSmallestLong(),-5.0,0.01);

    }
}
