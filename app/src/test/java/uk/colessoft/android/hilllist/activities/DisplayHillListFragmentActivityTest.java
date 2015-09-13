package uk.colessoft.android.hilllist.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowListView;
import org.robolectric.shadows.ShadowLog;

import uk.colessoft.android.hilllist.BuildConfig;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.database.HillDbAdapter;
import uk.colessoft.android.hilllist.fragments.DisplayHillListFragment;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class DisplayHillListFragmentActivityTest {

    private DisplayHillListFragmentActivity activity;

    @Before
    public void setupActivityBasedOnScottishHills() {
        ShadowLog.stream = System.out;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("hilllistType", HillDbAdapter.KEY_marilyn);
        intent.putExtra("hilltype", "Scottish Marilyns");
        intent.putExtra("country", Main.SCOTLAND);
        activity = Robolectric.buildActivity(DisplayHillListFragmentActivity.class).withIntent(intent).create()
                .get();
        assertNotNull("activity not instantiated", activity);
    }

    @Test
    public void firstHillInListShouldBeBenNevis() {

 /*       DisplayHillListFragment displayListFragment = (DisplayHillListFragment) activity.getSupportFragmentManager().getFragments().get(0);

        ListView listView=(ListView)displayListFragment.getView().findViewById(R.id.myListView);

        ShadowListView shadowListView = Shadows.shadowOf(listView);
        shadowListView.populateItems();
        Log.d("First:+++++++++",listView.getChildAt(0).toString());
        assertTrue("Ben Nevis is first", "Ben Nevis".equals((listView.getChildAt(0))));
        assertTrue(3 == listView.getChildCount());*/

    }
}
