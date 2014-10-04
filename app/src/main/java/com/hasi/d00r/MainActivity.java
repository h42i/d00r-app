package com.hasi.d00r;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.hasi.hasid00r.R;
import com.hasi.sshtools.SshTools;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;

public class MainActivity extends Activity implements DialogInterface.OnClickListener {
	public static final String OPEN_UPSTAIRS_COMMAND_PREFIX = "sh /home/door/open_door_upstairs.sh ";
	
	public static final String OPEN_DOWNSTAIRS_URL_PREFIX = "http://";
	public static final String OPEN_DOWNSTAIRS_URL_SUFFIX = ":4337/";
	
	public static final int NO_IDENTITY_DIALOG = 1;
	
	private int dialogType = 0;
	
	SharedPreferences prefs = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		if (!SshTools.keyPairExists(getFilesDir()) && !InstanceConfig.alreadyAskedNoIdentity) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setMessage(R.string.no_identity_create_one);
			builder.setPositiveButton(android.R.string.yes, this);
			builder.setNegativeButton(android.R.string.no, this);
			
			dialogType = NO_IDENTITY_DIALOG;
			
			InstanceConfig.alreadyAskedNoIdentity = true;
			
			builder.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    	case R.id.action_register_identity:
	    		registerIdentity();
	    		return true;
	        case R.id.action_generate_new_identity:
	        	generateNewIdentity();
	            return true;
	        case R.id.action_settings:
	        	showSettings();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void generateNewIdentity() {
		Intent intent = new Intent(this, NewIdentity.class);
		
		startActivity(intent);
	}
	
	public void registerIdentity() {
        String serverAddress = prefs.getString(getString(R.string.pref_key_upstairs_server_address),
                                               getString(R.string.pref_default_upstairs_server_address));
		
		RegisterIdentityTask registerIdentityTask = new RegisterIdentityTask();
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		
		registerIdentityTask.alertDialogBuilder = alertDialogBuilder;
		registerIdentityTask.context = getApplicationContext();
		registerIdentityTask.execute(getFilesDir(), serverAddress, 9337);
	}
	
	public void showSettings() {						
		Intent intent = new Intent(this, Settings.class);
		
		startActivity(intent);
	}
	
	public void openUpstairs(View view) {
		String serverAddress = prefs.getString(getString(R.string.pref_key_upstairs_server_address),
				                               getString(R.string.pref_default_upstairs_server_address));
        boolean vibration = prefs.getBoolean(getString(R.string.pref_key_vibration), true);
		
		ExecuteOnSshTask executeOnSshTask = new ExecuteOnSshTask();
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		
		VibrateTask vibrateTask = new VibrateTask();
		
		vibrateTask.context = getApplicationContext();
		vibrateTask.vibrationTime = 4000;

		executeOnSshTask.alertDialogBuilder = alertDialogBuilder;

        if (vibration) {
            executeOnSshTask.vibrateTask = vibrateTask;
        }

		executeOnSshTask.execute(getFilesDir(), "door", serverAddress, OPEN_UPSTAIRS_COMMAND_PREFIX + "\r\n");
	}
	
	public void openDownstairs(View view) {
		String serverAddress = prefs.getString(getString(R.string.pref_key_downstairs_server_address),
                                               getString(R.string.pref_default_downstairs_server_address));
        boolean vibration = prefs.getBoolean(getString(R.string.pref_key_vibration), true);
		
		CallUrlTask callUrlTask = new CallUrlTask();
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		
		VibrateTask vibrateTask = new VibrateTask();
		
		vibrateTask.context = getApplicationContext();
		vibrateTask.vibrationTime = 4000;
		
		callUrlTask.alertDialogBuilder = alertDialogBuilder;

        if (vibration) {
            callUrlTask.vibrateTask = vibrateTask;
        }

		callUrlTask.execute(OPEN_DOWNSTAIRS_URL_PREFIX + serverAddress + OPEN_DOWNSTAIRS_URL_SUFFIX + "dummystring");
	}
	
	public void onClick(DialogInterface dialog, int which) {
		switch (dialogType) {
			case NO_IDENTITY_DIALOG: 
				switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						generateNewIdentity();
					default:
						break;
				}
				
				break;
			default:
				break;
		}
	}
}
