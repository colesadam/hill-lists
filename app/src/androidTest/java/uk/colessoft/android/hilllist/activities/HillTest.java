package uk.colessoft.android.hilllist.activities;

import android.os.Build;
import android.util.Log;

import static androidx.test.InstrumentationRegistry.getInstrumentation;

public class HillTest {


    static void grantExternalStoragePermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getInstrumentation().getUiAutomation().executeShellCommand(
                    " pm grant " + "uk.colessoft.android.hilllist.test"
                            + " android.permission.WRITE_EXTERNAL_STORAGE");
            Log.d(HillTest.class.getName(),"########################################set permissions");
        }
    }
}
