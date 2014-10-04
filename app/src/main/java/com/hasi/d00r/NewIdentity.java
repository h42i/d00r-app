package com.hasi.d00r;

import com.hasi.hasid00r.R;
import com.hasi.sshtools.SshTools;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

public class NewIdentity extends Activity implements DialogInterface.OnClickListener {
	public static final int ARE_YOU_SURE_DIALOG = 1;
	public static final int FAILED_GENERATING_IDENTITY_DIALOG = 2;
	public static final int REGISTER_IDENTITY_DIALOG = 3;
	public static final int REGISTER_IDENTITY_RESULT_DIALOG = 4;
	
	public int dialogType = 0;
	
	private GenerateNewIdentityTask generateNewIdentityTask = null;
	private RegisterIdentityTask registerIdentityTask = null;
	
	private AlertDialog.Builder alertDialogBuilder = null;
	
	private SharedPreferences prefs = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_identity);
		// Show the Up button in the action bar.
		setupActionBar();
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
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
		getMenuInflater().inflate(R.menu.new_identity, menu);
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
	
	public void onClick(DialogInterface dialog, int which) {
		switch (dialogType) {
			case ARE_YOU_SURE_DIALOG:
				switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						reallyGenerateNow();
						
						break;
					default:
						break;
				}
				
				break;
			case REGISTER_IDENTITY_DIALOG:
				switch (which) {
					case DialogInterface.BUTTON_POSITIVE:
						registerIdentity();
						
						break;
					default:
						break;
				}
				
				break;
		}
	}
	
	private void reallyGenerateNow() {
		EditText name = (EditText) findViewById(R.id.edittext_your_name);
		Spinner strength = (Spinner) findViewById(R.id.spinner_key_strength_level);
		
		String comment = "<" + name.getText().toString() + "@d00r>";
		
		int keyStrengthLevel = strength.getSelectedItemPosition();
		int keyStrength = 1024;
		
		switch (keyStrengthLevel) {
			case 0:
				keyStrength = 1024;
				break;
			case 1:
				keyStrength = 2048;
				break;
			case 2:
				keyStrength = 4096;
				break;
			default:
				break;
		}
		
		generateNewIdentityTask = new GenerateNewIdentityTask();
		
		String progressDialogText = getString(R.string.generating_new_identity);
		
		ProgressDialog progressDialog = ProgressDialog.show(this, "", progressDialogText, true);
		
		alertDialogBuilder = new AlertDialog.Builder(this);
		
		generateNewIdentityTask.progressDialog = progressDialog;
		generateNewIdentityTask.alertDialogBuilder = alertDialogBuilder;
		generateNewIdentityTask.associatedActivity = this;
		generateNewIdentityTask.context = getApplicationContext();
		generateNewIdentityTask.newIdentityActivity = this;
		generateNewIdentityTask.execute(getFilesDir(), comment, keyStrength);
	}
	
	public void generateNewIdentity(View view) {
		if (SshTools.keyPairExists(getFilesDir())) {
			dialogType = ARE_YOU_SURE_DIALOG;
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setMessage(R.string.are_you_sure);
			builder.setPositiveButton(android.R.string.yes, this);
			builder.setNegativeButton(android.R.string.no, this);
			
			builder.show();
		} else {
			reallyGenerateNow();
		}
	}
	
	public void registerIdentity() {
		String serverAddress = prefs.getString(getString(R.string.pref_key_upstairs_server_address),
                                               getString(R.string.pref_default_upstairs_server_address));
		
		registerIdentityTask = new RegisterIdentityTask();
		
		alertDialogBuilder = new AlertDialog.Builder(this);
		
		registerIdentityTask.alertDialogBuilder = alertDialogBuilder;
		registerIdentityTask.context = getApplicationContext();
		registerIdentityTask.associatedActivity = this;
		registerIdentityTask.endActivity = true;
		registerIdentityTask.execute(getFilesDir(), serverAddress, 9337);
	}
}
