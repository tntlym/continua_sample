package org.continuaalliance.mcesl.communication.hdp;

import android.os.ParcelFileDescriptor;

/*
 * HDPData.java: This class is used to send HDP connection data to device manager 
 *  
 * @author: Vignet
 */

public class HDPData {
	private final ParcelFileDescriptor fd;
	private final int channelId;
	private final int dataType;
	
	public HDPData(ParcelFileDescriptor tfD, int tchannelId,int dType)
	{
		fd = tfD;
		channelId = tchannelId;
		dataType = dType;
	}
	
	/**
	 * Gets the ParcelFileDescriptor associated with HDP connection with device.
	 * @return ParcelFileDescriptor
	 */
	public ParcelFileDescriptor getFd() {
		return fd;
	}
	
	/**
	 * Returns the channel ID associated to the connection
	 * @return channelId int
	 */
	public int getChannelId() {
		return channelId;
	}
	
	/**
	 * Returns the data type of the connected device.
	 * @return data type int
	 */
	public int getDataType() {
		return dataType;
	}
}
