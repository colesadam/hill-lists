package uk.colessoft.android.hilllist.utility;


import android.content.Context;
import android.content.res.AssetManager;
import android.util.TypedValue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Util {
    public static String getClassification(String key,Context context) throws IOException {
        Properties properties = new Properties();;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open("classifications.properties");
        properties.load(inputStream);
        return properties.getProperty(key);

    }

    public static int getThemeAccentColor (final Context context, int colour) {
        final TypedValue value = new TypedValue ();
        context.getTheme ().resolveAttribute (colour, value, true);
        return value.data;
    }
}
