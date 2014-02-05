package org.continuaalliance.mcesl.appData;

/**
 * This class is used to send device related information to the application
 */
import java.io.Serializable;

@SuppressWarnings("serial")
public class DeviceObject implements Serializable{

	private byte[] SystemID = null;
	private byte[] Manufacturer = null;
	private byte[] SysModel = null;
	
	public DeviceObject(byte[] sysID, byte[] manufacturer,byte[] sysModel)
	{
		setSystemID(sysID);
		setSysModel(sysModel);
		setManufacturer(manufacturer);
	}

	/**
	 * Sets the system id
	 * @param systemID system id of the device in bytes
	 */
	public void setSystemID(byte[] systemID) {
		SystemID = systemID;
	}

	/**
	 * Returns the system id of the device
	 * @return System ID in byte array
	 */
	public byte[] getSystemID() {
		return SystemID;
	}

	/**
	 * Sets the manufacturer
	 * @param manufacturer name in bytes
	 */
	public void setManufacturer(byte[] manufacturer) {
		Manufacturer = manufacturer;
	}

	/**
	 * Returns the manufacturer
	 * @return manufacturer in bytes
	 */
	public byte[] getManufacturer() {
		return Manufacturer;
	}

	/**
	 * Sets the System model of the device
	 * @param sysModel 
	 */
	public void setSysModel(byte[] sysModel) {
		SysModel = sysModel;
	}

	/**
	 * Returns System model of the device.
	 * @return system model
	 */
	public byte[] getSysModel() {
		return SysModel;
	}
	
}
