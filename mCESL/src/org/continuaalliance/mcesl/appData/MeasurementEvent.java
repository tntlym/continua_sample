package org.continuaalliance.mcesl.appData;

/**
 * MeasurementEvent.java: This class is used to send Event 
 * 						object to device manager
 * @author Vignet 
 */
import java.io.Serializable;

@SuppressWarnings("serial")
public class MeasurementEvent implements Serializable{

	public static final int PULSE_DEVICE = 0x1004;
	private  int DeviceID;
	private  EventObject eObject;
	
	
	public MeasurementEvent()
	{
		DeviceID = -1;
		
	}
	public MeasurementEvent(int device, EventObject eObj) {
		
		setDeviceID(device);
		setEventObject(eObj);
	}	

	/**
	 * Sets the device ID
	 * @param deviceID
	 */
	private void setDeviceID(int deviceID) {
		DeviceID = deviceID;
	}

	/**
	 * Returns the device ID
	 * @return Device ID
	 */
	public int getDeviceID() {
		return DeviceID;
	}
	
	/**
	 * Sets the event object
	 * @param eObject
	 */
	private void setEventObject(EventObject eObject) {
		this.eObject = eObject;
	}
	
	/**
	 * Returns the event object
	 * @return Event object
	 */
	public EventObject getEventObject() {
		return eObject;
	}
	
}
