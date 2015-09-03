package uk.colessoft.android.hilllist.utility;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class DistanceCalculatorTest {

    @Test
    public void distanceCalcWorks(){
        assertEquals(DistanceCalculator.CalculationByDistance(55.0, 60, 0, -1.5),563.11,0.1);
    }

    @Test
    public void purposefullyFailingTest() throws Exception{
        assertNotSame("foo", "foo");
    }
}
