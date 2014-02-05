package org.continuaalliance.mcesl.communication;

import org.continuaalliance.mcesl.communication.EConnectionType;
import org.continuaalliance.mcesl.communication.hdp.HDPConnection;

import org.continuaalliance.mcesl.controller.ManagerController;

/*
 * ConnectionFactory.java: A factory class for Connection object  
 * 							
 * @author: Vignet
 */
public class ConnectionFactory {

	static AConnection connObj = null;
	
	/**
	 * Creates the connection class object according to the parameter sent.   
	 * @param connType object which controls the state machine
	 */
	public static AConnection CreateConnection(int connType, ManagerController listener)
	{
		switch(connType)
		{
			case EConnectionType.BT_SPP:
				break;	
			case EConnectionType.BT_HDP:
				connObj = new HDPConnection(listener);				
				break;
			
		}
		return connObj;
	}
}
