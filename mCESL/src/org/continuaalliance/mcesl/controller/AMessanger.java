package org.continuaalliance.mcesl.controller;

/*
 * AMessanger.java: Base class for all the messangers
 * @author: Vignet
 */
import java.io.Serializable;

import android.content.Context;

public abstract class AMessanger {

	protected Context m_appContext = null;
	
	public AMessanger(final Context appContext)
	{
		m_appContext = appContext;
	}
	/**
	 * Handles event sent from ManagerController or device Manager
	 * 
	 * @param event Object 
	 */
	public abstract void handleEvent(Object event);
	
	/**
	 * Handles event sent from ManagerController or device Manager
	 * 
	 * @param event Object
	 * @param data Serializable 
	 */
	public abstract void handleEvent(Object event, Serializable data);
}
