package org.continuaalliance.mcesl.measurement;

/**
 * Measurement.java: Represents measurement object base class
 * 
 * @author Vignet
 */

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Measurement implements Serializable{

	private int personId;
	protected int specialization;
	
	/**
	 * Sets the specialization for the measurement
	 * @param specialization int
	 */
	public void setSpecialization(int specialization) 
	{
		this.specialization = specialization;
	}
	
	/**
	 * Returns the specialization of the measurement.
	 * @return specialization int
	 */
	public int getSpecialization() 
	{
		return specialization;
	}
	
	/**
	 * Sets the personnel ID for the measurement.
	 * @param personId int
	 */
	public void setPersonId(int personId) {
		this.personId = personId;
	}

	/**
	 * returns the person id of the measurement.
	 * @return person id
	 */
	public int getPersonId() {
		return personId;
	}
	
}
