package uk.colessoft.android.hilllist.activities;

import uk.colessoft.android.hilllist.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PreferencesActivity extends PreferenceActivity{
	CheckBox metricHeights;CheckBox metricDistances;
	public static final String PREF_METRIC_HEIGHTS = "PREF_METRIC_HEIGHTS";
	public static final String PREF_METRIC_DISTANCES = "PREF_METRIC_DISTANCES";
	
	SharedPreferences prefs;

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
