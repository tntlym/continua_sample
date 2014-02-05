package org.continuaalliance.mcesl.communication;

import java.util.HashMap;

import org.continuaalliance.mcesl.controller.ManagerController;

/*
 * Abstract base class for all the connection classes.
 * 
 * @author Vignet
 */ 
abstract public class AConnection extends Thread{
	
	protected ManagerController m_controller = null;
	
	/**
	 * Sets the device specializations for the transport.
	 * 
	 * @param: Hashmap which stores the device specializations.
	 */
	abstract public void setSpecializations(final HashMap<Integer,Integer> specs);
	
}
