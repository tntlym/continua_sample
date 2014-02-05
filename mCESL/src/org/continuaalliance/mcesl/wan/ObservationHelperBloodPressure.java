/*Copyright © 2011, Continua Health Alliance.
 *All rights reserved.
 *Terms and conditions of use are stated in the Reference Code License Agreement.
 */

package org.continuaalliance.mcesl.wan;

/*******************************
 Developed by Vignet for Continua.
 *******************************/
import java.util.Properties;

import org.ca.cesl.wan.encoding.api.IWanMdsContext;
import org.ca.cesl.wan.encoding.api.IWanMessage;
import org.ca.cesl.wan.encoding.simpleadapters.SimpleAdapterNumeric;

import android.util.Log;

public class ObservationHelperBloodPressure extends ObservationHelper {

	/*
	 * static final String TIMESTAMP = "TIMESTAMP"; static final String SYSTOLIC
	 * = "SYSTOLIC"; static final String DIASTOLIC = "DIASTOLIC"; static final
	 * String PULSE_RATE = "PULSE_RATE"; static final String MEAN = "MEAN";
	 */

	public ObservationHelperBloodPressure(Properties observationdetails) {
		super();
		this.observationdetails = observationdetails;
	}

	@Override
	protected IWanMessage GetHl7Message() {
		// Auto-generated method stub
		IWanMessage wanMessage = createWanMessage();
		IWanMdsContext mdsCntxt = createMdsContext("4103",
				"VignetCorp;Blood Pressure Monitor 1.0.0.1");
		wanMessage.encodeMdsObservation(mdsCntxt);
		
		String sBPCompund = getCompoundMessage(
				observationdetails.getProperty(WANConstants.SYSTOLIC),
				observationdetails.getProperty(WANConstants.MEAN),
				observationdetails.getProperty(WANConstants.DIASTOLIC));

		Log.i("", "Systolic : "+observationdetails.getProperty(WANConstants.SYSTOLIC));
		Log.i("", "Diastolic : "+observationdetails.getProperty(WANConstants.DIASTOLIC));
		Log.i("", "Mean : "+observationdetails.getProperty(WANConstants.MEAN));
		// Unit Code - 3872
		SimpleAdapterNumeric bloodpressureAdapter = createSimpleNumericAdapter(
				sBPCompund, observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.TIMESTAMP), 
				"3872", 
				"1","18948", "2", "2;18948", "18949;18950;18951");
		wanMessage.encodeObservation(bloodpressureAdapter, mdsCntxt);

		/*SimpleAdapterNumeric pulserateAdapter = createSimpleNumericAdapterBasicValue(
				observationdetails.getProperty(WANConstants.PULSE_RATE),
				observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.TIMESTAMP), "2720", "1",
				"18474", "2", "2;18474", null);
		wanMessage.encodeObservation(pulserateAdapter, mdsCntxt);*/

		return wanMessage;
	}

	private String getCompoundMessage(String systolic, String mean,
			String dystolic) {
		//Auto-generated method stub
		return systolic + ";" + mean + ";" + dystolic;
	}

}
