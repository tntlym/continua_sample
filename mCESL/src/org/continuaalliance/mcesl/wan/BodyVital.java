package org.continuaalliance.mcesl.wan;

import java.util.HashMap;

public class BodyVital {

	private int personId;
	private int specialization;
	public HashMap<Integer, HashMap<Integer, Object>> readings;
	
	public BodyVital() {
		specialization = -1;
		personId = -1;
		readings = new HashMap<Integer, HashMap<Integer, Object>>();
	}
	
	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public int getSpecialization() {
		return specialization;
	}

	public void setSpecialization(int specialization) {
		this.specialization = specialization;
	}
	
}
