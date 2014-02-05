package org.continuaalliance.mcesl.appData;



/*
 * Device.java: This class represents the Device object at application
 * 				Its object can be used to call abort connection, get PM data from the device. 
 * @author: Vignet
 */

import org.continuaalliance.mcesl.Event.Event;
import org.continuaalliance.mcesl.controller.ManagerController;
import org.continuaalliance.mcesl.fsm.DeviceManager;

import android.util.Log;


public class Device {

	private int mDataType;
	private ManagerController mController;
	private int mDeviceManagerId;
	public Device(final ManagerController controller, int devMgrId,int dataType)
	{
		mController = controller;
		mDeviceManagerId = devMgrId;
		mDataType = dataType;
	}
	
	
	public void setDataType(int mDataType) {
		this.mDataType = mDataType;
	}
	public int getDataType() {
		return mDataType;
	}
	
	/**
	 * Abort the connection with the device.
	 * 
	 */
	public void abort()
	{
		Log.e("ABORT","abort with devicemanagerID->"+mDeviceManagerId);
		DeviceManager toAbortDM = mController.getDeviceManager(mDeviceManagerId);
		if(toAbortDM != null)
		{
			Log.e("ABORT","abort message is being sent");
			toAbortDM.sendMessageToManager(Event.ABORT_FROM_APP);
		}
		
	}
	/**
	 * Unassociate with the device.
	 */
	public void unAssociate()
	{
		//send association release request.
		DeviceManager toAbortDM = mController.getDeviceManager(mDeviceManagerId);
		if(toAbortDM != null)
		{
			toAbortDM.sendMessageToManager(Event.UNASSOCIATE_FROM_APP);
		}
	}
	
	/**
	 * Get device MDS information
	 */
	public void Get()
	{
		Log.e("ABORT","GET with devicemanagerID->"+mDeviceManagerId);
		DeviceManager toAbortDM = mController.getDeviceManager(mDeviceManagerId);
		if(toAbortDM != null)
		{
			toAbortDM.sendMessageToManager(Event.GET_REQUEST_FROM_APP);
		}
	}
	/**
	 * Gets all PMSegment related information.
	 * @param pmHandle handle of PMStore object
	 */
	public void getPMStoreSegmentInfo(short pmHandle)
	{
		Log.e("","getPMStoreSegmentInfo with devicemanagerID->"+mDeviceManagerId);
		DeviceManager toAbortDM = mController.getDeviceManager(mDeviceManagerId);
		if(toAbortDM != null)
		{
			toAbortDM.sendMessageToManager(Event.GET_PM_STORE_SEGMENTS_INFO,pmHandle);
		}
	}
	/**
	 * Get data from the segment
	 * @param segment from which data is to be retrieved
	 */
	public void getSegmentData(AppPMSegment segment)
	{
		Log.e("","getPMStoreSegmentInfo with devicemanagerID->"+mDeviceManagerId);
		DeviceManager toAbortDM = mController.getDeviceManager(mDeviceManagerId);
		if(toAbortDM != null)
		{
			toAbortDM.sendMessageToManager(Event.GET_PM_SEGMENT_DATA,segment);
		}
	}
	
	/**
	 * Sets device manager id
	 * @param mDeviceManagerId manager id of the device manager
	 */
	public void setDeviceManagerId(int mDeviceManagerId) {
		this.mDeviceManagerId = mDeviceManagerId;
	}
	
	/**
	 * Returns device manager ID
	 * @return device manager id int
	 */
	public int getDeviceManagerId() {
		return mDeviceManagerId;
	}
	
	
	public void setManagerController(ManagerController tController)
	{
		mController = tController;
	}
	
	/**
	 * Enables the operational state of given scanner object.
	 * @param obj AppScannerObject
	 */
	public void enableOperationalState(AppScannerObject obj)
	{
		Log.e("","enableOperationalState with devicemanagerID->"+mDeviceManagerId);
		DeviceManager toAbortDM = mController.getDeviceManager(mDeviceManagerId);
		if(toAbortDM != null)
		{
			if(obj!=null)
			{
				toAbortDM.sendMessageToManager(Event.ENABLE_SCANNER,obj);
			}
		}
	}
}
