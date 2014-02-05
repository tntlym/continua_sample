package org.continuaalliance.mcesl.controller;

import java.util.HashMap;

import android.content.Context;

/*
 * LibConfig.java: Stores the library configuration. 
 *  
 * @author: Vignet
 */

public class LibConfig {
	private HashMap<Integer, Integer> transports = null;
	private HashMap<Integer, Integer> devSpecialization = null;
	private Context appContext = null;
	private AMessanger messanger = null;

	/**
	 * Returns the transports.   
	 * 
	 * @return Hashmap containing transport constants.
	 * 
	 */	
	public HashMap<Integer, Integer> GetTransports()
	{
		return transports;
	}
	
	/**
	 * Returns the device specializations.   
	 * 
	 * @return Hashmap containing device specializations.
	 * 
	 */
	public HashMap<Integer, Integer> GetDevSpecialization()
	{
		return devSpecialization;
	}
	
	/**
	 * Adds the transport to the contained hashmap   
	 * 
	 * @param transport constant that defines the transport
	 */
	public void addTransport(int transportConstant)
	{
		// add to transport list
		if(transports == null)
		{
			transports = new HashMap<Integer,Integer>();
		}
		if(transports.containsValue(transportConstant) == false)
		{
			transports.put(transports.size(), transportConstant);
		}
	}
	
	/**
	 * Adds the device specialization to the contained hashmap   
	 * 
	 * @param device specialization constant
	 */
	public void addDeviceSpecialization(int devSpec)
	{
		if(devSpecialization == null)
		{
			devSpecialization = new HashMap<Integer,Integer>();
		}
		if(devSpecialization.containsValue(devSpec) == false)
		{
			devSpecialization.put(devSpecialization.size(), devSpec);
		}
	}
	
	/**
	 * Gets the application context   
	 * 
	 * @return returns application context
	 */
	public Context getConfigContext()
	{
		return appContext;
	}
	
	/**
	 * Setter for the application context   
	 * 
	 * @param Context which needs to be saved.
	 */
	public void setConfigContext(final Context con)
	{
		appContext = con;
	}
	
	/**
	 * Gets the Messanger object   
	 * 
	 * @return returns AMessanger reference
	 */
	public AMessanger getMessanger()
	{
		return messanger;
	}

	/**
	 * Setter for the application context   
	 * 
	 * @param Context which needs to be saved.
	 */
	public void setMessanger(final AMessanger mess)
	{
		messanger = mess;
	}

}
