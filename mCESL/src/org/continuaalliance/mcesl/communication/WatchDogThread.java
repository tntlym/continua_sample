package org.continuaalliance.mcesl.communication;

import android.util.Log;

import java.lang.InterruptedException;

/**
 * WatchDogThread.java: Its a watch dog thread for blocking calls such as 
 * 						connect/read/write. 
 * @author Vignet
 */

public class WatchDogThread extends Thread{

	private int timeOutInterval; 
	private Thread ReadWriteThread;
	private static final String LOG_TAG = "WatchDogThread";
	public WatchDogThread(final Thread rWThread)
	{
		timeOutInterval = 10000;// in milliseconds by default
		ReadWriteThread = rWThread;
	}
	/**
	 * Sets the time out interval in seconds.
	 * @param ms time interval in milliseconds
	 * 
	 */
	public void setTimeOut(final int ms)
	{
		if(ms>0){
			timeOutInterval = ms;
		}
	}
	public void run()
	{
		try
		{
			Log.e(LOG_TAG, "WatchDog Sleeping");
			sleep(timeOutInterval);
			Log.e(LOG_TAG, "WatchDog Sleep Complete going to intterupt connection Obj");
			ReadWriteThread.interrupt();
		}
		catch(InterruptedException ie){
			Log.e(LOG_TAG, "interruptedException");
			return;
		}	
		catch(Exception e){
			Log.e(LOG_TAG, e.getMessage());
			return;
		}
	}
}
