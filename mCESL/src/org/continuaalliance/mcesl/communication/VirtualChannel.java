package org.continuaalliance.mcesl.communication;
import java.util.Queue;

import java.util.concurrent.Semaphore;

import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ApduType;

import android.util.Log;

/*
 * VirtualChannel.java:  Virtual channel abstract base class
 * 						 This class will be extended by different transports.
 * 							
 * @author: Vignet
 */


abstract public class VirtualChannel {
	
	protected Queue<ApduType> inQueue;
	protected Queue<ApduType> outQueue;
	protected Semaphore semaphoreOutQueue;
	protected Semaphore semaphoreInQueue;
	private String TAG = "VirtualChannel";
	/**
	 *	Abstract method to initialize channel. 
	 *	Implemented by derived classes 
	 */
	public abstract void Initialize();
	
	/**
	 *	Abstract method to add packet to write queue 
	 *	Implemented by derived classes 
	 *	@param ApduType packet to be written 
	 */
	public abstract void AddToWriteQueue(ApduType dataToSend);
	
	/**
	 *	Abstract method to read from read queue. 
	 *	Implemented by derived classes 
	 *	@return ApduType packet read
	 */
	public ApduType ReadFromInQueue()
	{
		Log.i(TAG,"ReadFromInQueue start");
		ApduType retValue = null;
		if(inQueue.size()>0)
		{
			try{
				semaphoreInQueue.acquire();
				retValue = inQueue.remove();
			}
			catch(InterruptedException IntExcp)
			{
				retValue = null;
			}
			semaphoreInQueue.release();
		}
		Log.i(TAG,"ReadFromInQueue End");
		return retValue;
	}
	public abstract void Destroy();
	
}
