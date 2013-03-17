package com.cedarstreettimes.ideasofmarch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

public class AlertManager{
  private NotificationManager mNManager;
	private static final int NOTIFY_ID = 1100;
	private Context ctx;
	private File storageDir;
	
	public AlertManager(Context ctx){
		this.ctx = ctx;
		String ns = Context.NOTIFICATION_SERVICE;
		mNManager = (NotificationManager) ctx.getSystemService(ns);
		storageDir = new File(Environment.getDataDirectory() + "/data/com.cedarstreettimes.ideasofmarch");
	}
	
	public void createNotification(CharSequence contentTitle, CharSequence contentText, Intent msgIntent){
		final Notification msg = new Notification(R.drawable.arrow_down,
		"New event of importance", System.currentTimeMillis());
		Context context = ctx.getApplicationContext();
		PendingIntent intent = PendingIntent.getActivity(ctx, 0, msgIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		msg.defaults |= Notification.DEFAULT_SOUND;
		msg.flags |= Notification.FLAG_AUTO_CANCEL;
		msg.setLatestEventInfo(context, contentTitle, contentText,
				intent);
		mNManager.notify(NOTIFY_ID, msg);
	}
	
	public boolean checkNewPosts(Article article,String storageFile) throws FileNotFoundException{
		Date oldDate = null, newDate = null;
		try{
			newDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.ENGLISH).parse(article.getDate());
		}catch (Exception e){
			Log.d("APP","Unable to parse date");
		}
		
        File file = new File(storageDir, storageFile);
        
        if (storageDir.canWrite()){
        	//Get old File
        	FileInputStream fis = new FileInputStream(file);
        	BufferedReader bfr = new BufferedReader(new InputStreamReader(fis));
        	try{
        		oldDate = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.ENGLISH).parse(bfr.readLine());
        	}catch(Exception e){
        		Log.d("APP","Error parsing date: " + e);
        		return false;
        	}
        	
        	Log.d("APP","Checking for new posts - New: " + newDate.toGMTString() + " vs Old: " + oldDate.toGMTString()); 
        	if(newDate.after(oldDate)){
        		try{
					Intent intent = new Intent("android.intent.action.MAIN");
					intent.setComponent(ComponentName.unflattenFromString("com.cedarstreettimes.ideasofmarch/com.cedarstreettimes.ideasofmarch.MainActivity"));
					intent.addCategory("android.intent.category.LAUNCHER");
					createNotification("New Articles Available",article.getTitle().substring(0, 30) + "...",intent);
                    FileWriter filewriter = new FileWriter(file);
                    BufferedWriter out = new BufferedWriter(filewriter);
                    out.write(newDate.toGMTString());
                    out.close();
                    Log.d("APP","Wrote to " + file.toString() + " : "+newDate.toGMTString());
                    return true;
        		}catch(Exception e){
        			Log.d("APP","IO error: Unable to write to "+file.toString());
        			return false;
        		}
        	}
        	else{
        		Log.d("APP","Posts are up to date.");
        		return false;
        	}
        }else
        	Log.d("APP","Permission Denied - write to file: "+file.toString()+" : "+newDate.toGMTString());
        return false;
	}
}
