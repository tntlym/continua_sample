package org.continuaalliance.mcesl.deviceSpecialization;

/**
 * ExtendedConfDeviceSpecialization.java: This class represents Extended device specilization
 * 
 * @author Vignet
 */

import java.util.Hashtable;
import org.continuaalliance.mcesl.dim.Attribute;
import org.continuaalliance.mcesl.dim.MDSObject;
import org.continuaalliance.mcesl.utils.Constants;



public class ExtendedConfDeviceSpecialization extends MDSObject {

	public ExtendedConfDeviceSpecialization(Hashtable<Long, Attribute> map) {
		super(map);
		configurationType = Constants.EXTENDED_CONFIGURATION;
	}

	@Override
	public int getNomenclatureCode() {
		
		return 0;
	}

}
