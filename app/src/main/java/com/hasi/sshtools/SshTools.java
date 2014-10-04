package com.hasi.sshtools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.KeyPair;
import com.jcraft.jsch.Session;

public class SshTools {
	public static final boolean keyPairExists(File keyDir) {
		File privateKeyFile = new File(keyDir, "id_rsa");
		File publicKeyFile = new File(keyDir, "id_rsa.pub");
		
		if (privateKeyFile.exists() && publicKeyFile.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static final void generateNewIdentity(File keyDir, String comment, int strength) throws Exception {
		JSch jsch = new JSch();
		
		KeyPair newKeyPair = KeyPair.genKeyPair(jsch, KeyPair.RSA, strength);
		
		File privateKeyFile = new File(keyDir, "id_rsa");
		File publicKeyFile = new File(keyDir, "id_rsa.pub");
		
		if (privateKeyFile.exists()) {
			privateKeyFile.delete();
		}
		
		if (publicKeyFile.exists()) {
			publicKeyFile.delete();
		}
		
		newKeyPair.writePrivateKey(new FileOutputStream(privateKeyFile));
		newKeyPair.writePublicKey(new FileOutputStream(publicKeyFile), comment);
	    
		newKeyPair.dispose();
	}
	
	public static final void connectAndExecute(File keyDir, String user, String host, int port, String command) throws Exception {
        JSch jsch = new JSch();
        
        File privateKeyFile = new File(keyDir, "id_rsa");
		File publicKeyFile = new File(keyDir, "id_rsa.pub");
		
		FileInputStream privateKeyIn = new FileInputStream(privateKeyFile);
		FileInputStream publicKeyIn = new FileInputStream(publicKeyFile);
		
		byte privateKey[] = new byte[(int) privateKeyFile.length()];
		byte publicKey[] = new byte[(int) publicKeyFile.length()];
		
		privateKeyIn.read(privateKey);
		publicKeyIn.read(publicKey);
		
		privateKeyIn.close();
		publicKeyIn.close();
		
		byte passphrase[] = new byte[0];
		
        jsch.addIdentity("authkey", privateKey, publicKey, passphrase);
        System.out.println("identity added ");

        Session session = jsch.getSession(user, host, port);
        System.out.println("session created.");

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.connect();
        System.out.println("session connected.....");

        Channel channel = session.openChannel("shell");
        
        InputStream is = new ByteArrayInputStream(command.getBytes());
        channel.setInputStream(is);
        
        channel.setOutputStream(System.out);
        
        channel.connect(15 * 1000);
        
        Thread.sleep(2 * 1000);
        
        System.out.println("shell channel connected....");
        System.out.println("done");
        
        channel.disconnect();
        session.disconnect();
	}
}
