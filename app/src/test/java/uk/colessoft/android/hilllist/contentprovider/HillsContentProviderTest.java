package uk.colessoft.android.hilllist.contentprovider;

import android.database.Cursor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowContentResolver;
import org.robolectric.shadows.ShadowLog;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import uk.colessoft.android.hilllist.BHApp;
import uk.colessoft.android.hilllist.BuildConfig;
import uk.colessoft.android.hilllist.database.ColumnKeys;
import uk.colessoft.android.hilllist.database.TableNames;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class HillsContentProviderTest {

    private HillsContentProvider provider;
    private ShadowContentResolver scr;

    @Before
    public void setup() {
        BHApp.BHAppComponent appComponent = DaggerHillsContentProviderTest_TestAppComponent.create();
        ((BHApp) RuntimeEnvironment.application).setTestComponent(appComponent);
        ShadowLog.stream = System.out;
        scr = Shadows.shadowOf(RuntimeEnvironment.application.getContentResolver());
        provider = new HillsContentProvider();
        provider.onCreate();
        ShadowContentResolver.registerProvider("uk.colessoft.android.hilllist.contentprovider", provider);
    }

    @Component(modules = DatabaseModule.class)
    interface TestAppComponent extends BHApp.BHAppComponent {
    }

    @Module
    static class DatabaseModule {

        @Provides
        String provideCsvName() {
            return "test_data.csv";
        }
    }

    @Test
    public void benChonzieExists() {
        String[] projection = {ColumnKeys.KEY_HILLNAME};
        Cursor cursor = scr.query(HillsContentProvider.HILLS_CONTENT_URI, projection, TableNames.HILLS_TABLE + "._id=?", new String[]{"1"}, null);
        cursor.moveToFirst();
        assertEquals(cursor.getString(0), "Ben Chonzie");
        cursor.close();
    }
}
