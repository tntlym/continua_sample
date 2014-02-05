package org.continuaalliance.mcesl.appData;


/*
 * AppMDSObject.java: This class represents the MDS object at application  
 * @author: Vignet
 */


import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class AppMDSObject implements Serializable {

	private int deviceID;
	private ArrayList<AppNumeric> numerics = null;
	private ArrayList<AppPMStore> pmStores = null;
	private ArrayList<AppScannerObject> scanners = null;
	
	/**
	 * Adds application specific numeric object to the list
	 * @param obj AppNumeric
	 */
	public void addNumeric(AppNumeric obj)
	{
		if(numerics == null)
		{
			numerics = new ArrayList<AppNumeric>();
		}
		numerics.add(obj);
	}
	
	/**
	 * Adds application specific PMStore object to the list
	 * @param obj AppPMStore
	 */
	public void addPMStore(AppPMStore obj)
	{
		if(pmStores == null)
		{
			pmStores = new ArrayList<AppPMStore>();
		}
		pmStores.add(obj);
	}
	
	/**
	 * Adds application specific scanner object to the list
	 * @return AppScannerObject
	 */
	public void addScannerObject(AppScannerObject obj)
	{
		if(scanners == null)
		{
			scanners = new ArrayList<AppScannerObject>();
		}
		scanners.add(obj);
	}
	
	/**
	 * Gets all the AppScannerObject 
	 * @return ArrayList<AppScannerObject>
	 */
	public ArrayList<AppScannerObject> getScanners()
	{
		return scanners;
	}
	
	/**
	 * Gets all the AppNumeric 
	 * @return ArrayList<AppNumeric>
	 */
	public ArrayList<AppNumeric> getNumerics()
	{
		return numerics;
	}
	
	/**
	 * Gets all the AppNumeric 
	 * @return ArrayList<AppPMStore> 
	 */
	public ArrayList<AppPMStore> getPMStores()
	{
		return pmStores;
	}
	
	/**
	 * Sets device id for the MDS object
	 * @param deviceID int
	 */
	public void setDeviceID(int deviceID)
	{
		this.deviceID = deviceID;
	}
	
	/**
	 * Get device ID of the MDS object
	 * @return device id int
	 */
	public int getDeviceID()
	{
		return deviceID;
	}
}

