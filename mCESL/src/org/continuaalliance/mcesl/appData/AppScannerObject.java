package org.continuaalliance.mcesl.appData;


/*
 * AppScannerObject.java: This class represents the Scanner object at application  
 * @author: Vignet
 */


import java.io.Serializable;

@SuppressWarnings("serial")
public class AppScannerObject implements Serializable{

	public short handle;
	public int type;
	public short opState;
}
