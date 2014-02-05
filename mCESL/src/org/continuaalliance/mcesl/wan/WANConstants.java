/*Copyright © 2011, Continua Health Alliance.
 *All rights reserved.
 *Terms and conditions of use are stated in the Reference Code License Agreement.
 */

package org.continuaalliance.mcesl.wan;

/*******************************
Developed by Vignet for Continua.
*******************************/
public class WANConstants {
	public static final String SERVER_URL = "";
	public static final String SERVER_PORT = "";
	public static final String SERVICE_NAME = "";
	public static final String NAMESPACE = "";
	
	public static final String TIMESTAMP = "TIMESTAMP";
	public static final String UNIT_CODE = "UNIT_CODE";
	
	public static final int UNITCODE = 0x0001;
	public static final int VALUE = 0x0002;
	
	// For Blood Glucose
	public static final String GLU_CAPILLARY_WHOLEBLOOD = "GLU_CAPILLARY_WHOLEBLOOD";
	public static final String GLU_UNIT = "GLU_UNIT";
	
	// For Blood Pressure
	public static final String SYSTOLIC = "SYSTOLIC";
	public static final String DIASTOLIC = "DIASTOLIC";	
	public static final String PULSE_RATE = "PULSE_RATE";
	public static final String MEAN = "MEAN";

	public static final String SYSTOLIC_UNIT = "GLU_UNIT";
	public static final String DIASTOLIC_UNIT = "DIASTOLIC_UNIT";
	public static final String PULSE_RATE_UNIT = "PULSE_RATE_UNIT";
	public static final String MEAN_UNIT = "MEAN_UNIT";
	
	// For PulseOx
	public static final String SPO2 = "SPO2";
	public static final String PULSERATE = "PULSERATE";

	public static final String SPO2_UNIT = "SPO2_UNIT";
	public static final String PULSERATE_UNIT = "PULSERATE_UNIT";
	
	// For Thermometer
	public static final String BODY_TEMPERATURE = "BODY_TEMPERATURE";
	public static final String TEMPERATURE_UNIT = "TEMPERATURE_UNIT";
	
	// For Weight
	public static final String BODY_WEIGHT = "BODY_WEIGHT";
	public static final String WEIGHT_UNIT = "WEIGHT_UNIT";
	
	// For Peak Flow
	public static final String TIME_STAMP = "TIME_STAMP";
	public static final String PERSONAL_BEST = "PERSONAL_BEST";
	public static final String MEASUREMENT_STATUS = "MEASUREMENT_STATUS";
	
	// For Helper Strength
	public static final String REPETITIONS = "REPETITIONS";
	public static final String RESISTENCE = "RESISTENCE";
	public static final String SET_TIME = "SET_TIME";
	
	// For Cardio
	public static final String SESSION_TIME = "SESSION_TIME";
	public static final String SESSION_HEART_RATE = "SESSION_HEART_RATE";
	public static final String SESSION_DISTANCE = "SESSION_DISTANCE";
	public static final String SUBSESSION_TIME = "SUBSESSION_TIME";
	public static final String SUBSESSION_HEART_RATE = "SUBSESSION_HEART_RATE";
	public static final String SUBSESSION_DISTANCE = "SUBSESSION_DISTANCE";

	// For Independent Living
	public static final String BIT_STR = "BIT_STR";
	public static final String TEMPERATURE_HIGH = "0x80000000";;
	public static final String TEMPERATURE_LOW  = "0x40000000";;
	public static final String TEMPERATURE_FASTRATE = "0x20000000";
	
	// For Insulin Pump
	public static final String BAASAL = "BAASAL";
	public static final String BOLUS_FAST = "BOLUS_FAST";
	public static final String BOLUS_SLOW = "BOLUS_SLOW";
	
	// For Medication
	public static final String VARIABLE_DOSE = "VARIABLE_DOSE";

}
