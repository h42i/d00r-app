package com.hasi.d00r;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.bouncycastle.util.io.Streams;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.widget.Button;

public class VibrateTask extends AsyncTask<Object, Void, String> {
	private Exception exception = null;
    public Context context = null;
    public int vibrationTime = 0;

    @Override
    protected String doInBackground(Object... params) {
    	if (context != null) {
	    	Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(vibrationTime);
    	}
        
        return null;
    }
    
    @Override
    protected void onPostExecute(String str) {
    	if (exception != null) {
    		exception.printStackTrace();
    	}
    }
    
    public Exception getException() {
    	return exception;
    }
}
