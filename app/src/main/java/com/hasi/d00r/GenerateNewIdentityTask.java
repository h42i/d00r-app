package com.hasi.d00r;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bouncycastle.util.io.Streams;

import com.hasi.hasid00r.R;
import com.hasi.sshtools.SshTools;
import com.jcraft.jsch.JSchException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.widget.Toast;

class GenerateNewIdentityTask extends AsyncTask<Object, Void, String> {
    public Exception exception = null;
    public ProgressDialog progressDialog = null;
    public AlertDialog.Builder alertDialogBuilder = null;
    public Activity associatedActivity = null;
    public Context context = null;
    public NewIdentity newIdentityActivity = null;

    @Override
    protected String doInBackground(Object... params) {
        try {
        	SshTools.generateNewIdentity((File) params[0], (String) params[1], (Integer) params[2]);
        } catch (Exception e) {
            this.exception = e;
        }
        
        return null;
    }
     
    @Override
    protected void onPreExecute() {
    	if (progressDialog != null) {
    		progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            
            progressDialog.show();
    	}
    }
    
    @Override
    protected void onPostExecute(String str) {
    	if (exception != null) {
    		exception.printStackTrace();
    	}
    	
    	progressDialog.dismiss();
    	
    	if (alertDialogBuilder != null) {
    		if (exception != null) {
    			if (newIdentityActivity != null) {
	    			newIdentityActivity.dialogType = NewIdentity.FAILED_GENERATING_IDENTITY_DIALOG;
	    		}
    			
	    		alertDialogBuilder.setPositiveButton(android.R.string.ok, newIdentityActivity);
	    		alertDialogBuilder.setMessage(R.string.failed_to_generate_new_identity);
	    		
	    		alertDialogBuilder.show();
    		} else {
    			if (newIdentityActivity != null) {
	    			newIdentityActivity.dialogType = NewIdentity.REGISTER_IDENTITY_DIALOG;
	    		}
    			
    			alertDialogBuilder.setPositiveButton(android.R.string.yes, newIdentityActivity);
    			alertDialogBuilder.setNegativeButton(android.R.string.no, newIdentityActivity);
	    		alertDialogBuilder.setMessage(R.string.successfully_generated_new_identity);
	    		
	    		alertDialogBuilder.show();
    		}
    	}
    }
    
    public Exception getException() {
    	return exception;
    }
}