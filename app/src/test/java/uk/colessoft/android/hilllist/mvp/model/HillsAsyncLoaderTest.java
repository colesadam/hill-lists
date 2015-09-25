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

import java.util.List;
import java.util.concurrent.TimeUnit;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import uk.colessoft.android.hilllist.BHApp;
import uk.colessoft.android.hilllist.BuildConfig;
import uk.colessoft.android.hilllist.activities.Main;
import uk.colessoft.android.hilllist.contentprovider.HillsContentProvider;
import uk.colessoft.android.hilllist.objects.Hill;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class HillsAsyncLoaderTest {

    private HillsContentProvider provider;
    private ShadowContentResolver scr;

    @Before
    public void setup() {
        BHApp.BHAppComponent appComponent = DaggerHillsAsyncLoaderTest_TestAppComponent.create();
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
    public void loaderReturnsBenChonzie() throws Exception{
        HillsAsyncLoader task = new HillsAsyncLoader(false,new HillsAsyncLoader.HillsLoaderListener(){
            public void onSuccess(List<Hill> countries){}
            public void onError(Exception e){
                e.printStackTrace();
            };
        },RuntimeEnvironment.application);
        Bundle bundle = new Bundle();
        bundle.putString("hilltype","Munro");
        bundle.putInt("country", Main.SCOTLAND);
        task.execute(bundle);
        ShadowApplication.runBackgroundTasks();
        List<Hill> hills = task.get(100, TimeUnit.MILLISECONDS);
        assert(hills.size()==2);
        assert(hills.get(1).getHillname().equals("Ben Chonzie"));
    }
}
