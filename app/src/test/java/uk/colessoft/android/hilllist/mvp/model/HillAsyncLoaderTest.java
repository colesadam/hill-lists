package uk.colessoft.android.hilllist.mvp.model;

import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowContentResolver;
import org.robolectric.shadows.ShadowLog;

import java.util.concurrent.TimeUnit;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import uk.colessoft.android.hilllist.BHApp;
import uk.colessoft.android.hilllist.BuildConfig;
import uk.colessoft.android.hilllist.contentprovider.HillsContentProvider;
import uk.colessoft.android.hilllist.objects.Hill;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class HillAsyncLoaderTest {

    private HillsContentProvider provider;
    private ShadowContentResolver scr;

    @Before
    public void setup() {
        BHApp.BHAppComponent appComponent = DaggerHillAsyncLoaderTest_TestAppComponent.create();
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
    public void benChonzieHasFiveClassifications() throws Exception{
        HillAsyncLoader task = new HillAsyncLoader(new HillAsyncLoader.HillLoaderListener(){
            public void onSuccess(Hill hill){}
            public void onError(Exception e){
                e.printStackTrace();
            };
        },RuntimeEnvironment.application);
        Bundle bundle = new Bundle();

        task.execute(1L);
        ShadowApplication.runBackgroundTasks();
        Hill hill = task.get(100, TimeUnit.MILLISECONDS);
        assert(hill.getClassifications().size()==5);
        //assert(hill.getClassifications()..get(1).getHillname().equals("Ben Chonzie"));
    }
}
