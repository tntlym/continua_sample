package org.continuaalliance.mcesl.controller;

/*
 * BroadcastMessanger.java: Broadcast messanger class is ideally for Android devices.
 * 							This class uses the android Broadcast concept to broadcast
 * 							messages from library to the application
 * 
 * @author: Vigent
 */
import java.io.Serializable;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BroadcastMessanger extends AMessanger {

	public BroadcastMessanger(Context appContext) {
		super(appContext);
	}


	/**
	 * Handles event sent from ManagerController or device Manager
	 * 
	 * @param event Object 
	 */
	
	@Override
	public void handleEvent(Object event) {

		Intent i_login = new Intent();
		if(event instanceof String) {
			i_login.setAction((String)event);
			m_appContext.sendBroadcast(i_login);
		}		
	}

	/**
	 * Handles event sent from ManagerController or device Manager
	 * 
	 * @param event Object
	 * @param data Serializable 
	 */
	
	@Override
	public void handleEvent(Object event, Serializable data) {
		Log.i("","Handling event");
		Intent i_login = new Intent();
		i_login.setAction((String)event);
		i_login.putExtra((String)event, data);
		m_appContext.sendBroadcast(i_login);
		Log.i("","Broadcast sent");
		
	}

}