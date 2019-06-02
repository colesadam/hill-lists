package uk.colessoft.android.hilllist.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.CheckBox;

import uk.colessoft.android.hilllist.R;

public class PreferencesActivity extends PreferenceActivity{
	private CheckBox metricHeights;CheckBox metricDistances;
	public static final String PREF_METRIC_HEIGHTS = "PREF_METRIC_HEIGHTS";
	public static final String PREF_METRIC_DISTANCES = "PREF_METRIC_DISTANCES";
	
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		SharedPreferences _sharedPreferences  = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	
		
        // Get the custom preference
        //CheckBoxPreference metricHeightsPref = (CheckBoxPreference) findPreference("metricHeightsPref");

	}

	private void updateUIFromPreferences() {
		boolean metricHeightChecked = prefs.getBoolean(PREF_METRIC_HEIGHTS, false);
		metricHeights.setChecked(metricHeightChecked);
		
	}

}
