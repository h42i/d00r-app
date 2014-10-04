package com.hasi.d00r;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bouncycastle.util.io.Streams;

import com.hasi.hasid00r.R;
import com.hasi.sshtools.SshTools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.widget.Toast;

class RegisterIdentityTask extends AsyncTask<Object, Void, String> {
    public Exception exception = null;
    public AlertDialog.Builder alertDialogBuilder = null;
    public Context context;
    public NewIdentity associatedActivity = null;
    public boolean endActivity = false;

    @Override
    protected String doInBackground(Object... params) {
        try {
        	if (SshTools.keyPairExists((File) params[0])) {
	        	InetAddress address = InetAddress.getByName((String) params[1]);
	        	int port = (Integer) params[2];
	        	
	            Socket client = new Socket(address, port);
	            
	            OutputStream outToServer = client.getOutputStream();
	            DataOutputStream out = new DataOutputStream(outToServer);
	            
	            FileInputStream in = new FileInputStream(new File((File) params[0], "id_rsa.pub"));
	            
	            Streams.pipeAll(in, out);
	            
	            out.flush();
	            out.close();
	            
	            client.close();
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
    			if (exception instanceof NoSshKeysException) {
    				alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
	    			alertDialogBuilder.setMessage(R.string.failed_to_request_identity_registration_no_identity);
	    			alertDialogBuilder.show();
    			} else if (exception instanceof UnknownHostException) {
    				alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
	    			alertDialogBuilder.setMessage(R.string.failed_to_request_identity_registration_connection);
	    			alertDialogBuilder.show();
	    			System.out.println("test1");
    			} else if (exception instanceof ConnectException) {
    				alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
	    			alertDialogBuilder.setMessage(R.string.failed_to_request_identity_registration_connection);
	    			alertDialogBuilder.show();
    			} else {
    				alertDialogBuilder.setPositiveButton(android.R.string.ok, null);
	    			alertDialogBuilder.setMessage(R.string.unknown_error);
	    			alertDialogBuilder.show();
    			}
    		} else {
    			Toast message = Toast.makeText(context, R.string.requested_identity_registration, Toast.LENGTH_LONG);
    			
    			message.show();
    			
    			if (associatedActivity != null && endActivity) {
    				NavUtils.navigateUpFromSameTask(associatedActivity);
    			}
        	}
    	}
    }
    
    public Exception getException() {
    	return exception;
    }
}