package org.continuaalliance.mcesl.measurement;


/**
 * DeviceMeasurement.java: represents a measurement object received from device
 * 
 * @author Vignet
 */
import java.util.HashMap;

@SuppressWarnings("serial")
public class DeviceMeasurement extends Measurement {

	private HashMap<Integer, Reading> values;
	
	public DeviceMeasurement(int deviceSpecilization,HashMap<Integer, Reading> data)
	{
		setSpecialization(deviceSpecilization);
		setValues(data);
	}	
	
	/**
	 * Sets the values.
	 * @param values HashMap<Integer, Reading>
	 */
	public void setValues(HashMap<Integer, Reading> values) 
	{
		this.values = values;
	}
	
	/**
	 * Returns measurement values.
	 * @return HashMap<Integer, Reading>
	 */
	public HashMap<Integer, Reading> getValues() 
	{
		return values;
	}
	
}
