package com.hasi.d00r;

import java.io.File;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.Button;

import com.hasi.hasid00r.R;
import com.hasi.sshtools.SshTools;
import com.jcraft.jsch.JSchException;

class ExecuteOnSshTask extends AsyncTask<Object, Void, String> {
    private Exception exception = null;
    public AlertDialog.Builder alertDialogBuilder = null;
    public VibrateTask vibrateTask = null;
    
    @Override
    protected String doInBackground(Object... params) {
        try {
        	if (SshTools.keyPairExists((File) params[0])) {
        		// test connection to see if there is a connection for error handling
        		Socket socket = new Socket((String) params[2], 22);
        		socket.close();
        		
        		SshTools.connectAndExecute((File) params[0], (String) params[1], (String) params[2], 22, (String) params[3]);
        	} else {
        		throw new NoSshKeysException();
        	}
        } catch (Exception e) {
            this.exception = e;
        }
        
        return null;
    }
    
    @Override
    protected void onPostExecute(String str) {
    	if (exception != null) {
    		exception.printStackTrace();
    	}
    	
    	if (alertDialogBuilder != null) {
    		if (exception != null) {
    			if (exception != null) {
        			if (exception instanceof JSchException) {
    	    			alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
    	    			alertDialogBuilder.setMessage(R.string.failed_to_open_door_identity_not_registered);
    	    			alertDialogBuilder.show();
        			} else if (exception instanceof NoSshKeysException) {
        				alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
    	    			alertDialogBuilder.setMessage(R.string.failed_to_open_door_no_identity);
    	    			alertDialogBuilder.show();
        			} else if (exception instanceof UnknownHostException) {
        				alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
    	    			alertDialogBuilder.setMessage(R.string.failed_to_open_door_connection);
    	    			alertDialogBuilder.show();
        			} else if (exception instanceof ConnectException) {
        				alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
    	    			alertDialogBuilder.setMessage(R.string.failed_to_open_door_connection);
    	    			alertDialogBuilder.show();
        			} else {
        				alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
    	    			alertDialogBuilder.setMessage(R.string.unknown_error);
    	    			alertDialogBuilder.show();
        			}
        		}
    		}
    	}
    	
    	if (vibrateTask != null) {
    		if (exception == null) {
    			vibrateTask.execute();
    		}
    	}
    }
    
    public Exception getException() {
    	return exception;
    }
}