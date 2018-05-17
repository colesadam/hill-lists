package uk.colessoft.android.hilllist;


import android.content.Context;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import uk.colessoft.android.hilllist.components.DaggerTestDatabaseComponent;
import uk.colessoft.android.hilllist.components.TestDatabaseComponent;
import uk.colessoft.android.hilllist.database.BritishHillsDatasource;
import uk.colessoft.android.hilllist.modules.AppModule;
import uk.colessoft.android.hilllist.modules.TestDatabaseModule;

public class TestComponentRule implements TestRule {
    private final TestDatabaseComponent mTestComponent;
    private final Context mContext;

    public TestComponentRule(Context context) {
        mContext = context;
        BHApplication application = (BHApplication) context.getApplicationContext();
        mTestComponent = DaggerTestDatabaseComponent.builder()
                .appModule(new AppModule(application)).testDatabaseModule(new TestDatabaseModule())
                .build();
    }

    public BritishHillsDatasource getDbHelper(){

        return mTestComponent.dbHelper();

    }


    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                BHApplication application = (BHApplication) mContext.getApplicationContext();
                // Set the TestComponent before the test runs
                application.setComponent(mTestComponent);
                base.evaluate();
                // Clears the component once the tets finishes so it would use the default one.
                application.setComponent(null);
            }
        };
    }
}