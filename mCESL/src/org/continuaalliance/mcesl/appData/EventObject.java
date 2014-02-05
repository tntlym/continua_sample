package org.continuaalliance.mcesl.appData;

/**
 * EventObject.java: This class is used to send Measurement list to device manager
 * 
 * @author Vignet 
 */
import java.io.Serializable;
import java.util.ArrayList;

import org.continuaalliance.mcesl.measurement.Measurement;

@SuppressWarnings("serial")
public class EventObject implements Serializable{
	private ArrayList<Measurement> mList;
	private int configurationType;
	
	public EventObject(ArrayList<Measurement> list, int spec)
	{
		setSpecializationType(spec);
		setMeasurements(list);
	}
	
	/**
	 * Sets the list of Measurements.
	 * @param list ArrayList<Measurement>
	 */
	private void setMeasurements(ArrayList<Measurement> list) {
		this.mList = list;
	}
	
	/**
	 * Returns ArrayList of Measurements
	 * @return ArrayList<Measurement>
	 */
	public ArrayList<Measurement> getMeasurements() {
		return mList;
	}
	
	/**
	 * Sets the specialization type.
	 * @param configurationType int
	 */
	private void setSpecializationType(int configurationType) {
		this.configurationType = configurationType;
	}
	/**
	 * Returns the configuration type. 
	 * @return
	 */
	public int getConfigurationType() {
		return configurationType;
	}
	

}
