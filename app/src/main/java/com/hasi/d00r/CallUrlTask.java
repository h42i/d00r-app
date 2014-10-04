package com.hasi.d00r;

import java.net.ConnectException;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.hasi.hasid00r.R;
import com.jcraft.jsch.JSchException;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.Button;

class CallUrlTask extends AsyncTask<String, Void, String> {
    private Exception exception = null;
    public AlertDialog.Builder alertDialogBuilder = null;
    public VibrateTask vibrateTask = null;

    @Override
    protected String doInBackground(String... urls) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            
            HttpGet httpGet = new HttpGet(urls[0]);
            httpGet.setHeader("User-Agent", "d00r");
            
            HttpResponse httpResponse = httpClient.execute(httpGet);
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
    			if (exception instanceof ConnectException) {
    				alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
	    			alertDialogBuilder.setMessage(R.string.failed_to_open_door_connection);
	    			alertDialogBuilder.show();
    			} else if (exception instanceof UnknownHostException) {
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