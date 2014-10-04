package com.hasi.d00r;

import com.hasi.hasid00r.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
 
public class Settings extends PreferenceActivity {
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        // Show the Up button in the action bar.
     	setupActionBar();
        
        //final EditTextPreference upstairsOpenTime = (EditTextPreference) getPreferenceScreen().findPreference(getString(R.string.pref_key_upstairs_open_time));
        //final EditTextPreference downstairsOpenTime = (EditTextPreference) getPreferenceScreen().findPreference(getString(R.string.pref_key_downstairs_open_time));
        final CheckBoxPreference vibration = (CheckBoxPreference) getPreferenceScreen().findPreference(getString(R.string.pref_key_vibration));
        final EditTextPreference downstairsServerAddress = (EditTextPreference) getPreferenceScreen().findPreference(getString(R.string.pref_key_downstairs_server_address));
        final EditTextPreference upstairsServerAddress = (EditTextPreference) getPreferenceScreen().findPreference(getString(R.string.pref_key_upstairs_server_address));
        
        final Activity thisActivity = this;
        
        /*upstairsOpenTime.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference bla, Object newValue) {
				String newString = (String) newValue;
				
				if (newString.equals("") || Integer.parseInt(newString) > 20) {
            		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(thisActivity);
            		
            		alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
            		alertDialogBuilder.setMessage(R.string.pref_invalid_value_upstairs_open_time);
                	alertDialogBuilder.show();
                	
                	return false;
            	}
				
				return true;
			}
        });
        
        downstairsOpenTime.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference bla, Object newValue) {
				String newString = (String) newValue;
				
				if (newString.equals("") || Integer.parseInt(newString) > 20) {
            		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(thisActivity);
            		
            		alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
            		alertDialogBuilder.setMessage(R.string.pref_invalid_value_downstairs_open_time);
                	alertDialogBuilder.show();
                	
                	return false;
            	}
				
				return true;
			}
        });*/

        downstairsServerAddress.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference bla, Object newValue) {
				String newString = (String) newValue;

				if (newString.equals("")) {
            		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(thisActivity);

            		alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
            		alertDialogBuilder.setMessage(R.string.pref_resetted_to_default_downstairs_server_address);
                	alertDialogBuilder.show();

                	return false;
            	}

				return true;
			}
        });

        upstairsServerAddress.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference bla, Object newValue) {
                String newString = (String) newValue;

                if (newString.equals("")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(thisActivity);

                    alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
                    alertDialogBuilder.setMessage(R.string.pref_resetted_to_default_upstairs_server_address);
                    alertDialogBuilder.show();

                    return false;
                }

                return true;
            }
        });
    }
    
    /**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setDisplayShowHomeEnabled(false);
			getActionBar().setDisplayUseLogoEnabled(false);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// This ID represents the Home or Up button. In the case of this
				// activity, the Up button is shown. Use NavUtils to allow users
				// to navigate up one level in the application structure. For
				// more details, see the Navigation pattern on Android Design:
				//
				// http://developer.android.com/design/patterns/navigation.html#up-vs-back
				//
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}