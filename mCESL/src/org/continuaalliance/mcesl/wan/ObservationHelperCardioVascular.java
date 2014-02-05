/*Copyright © 2011, Continua Health Alliance.
 *All rights reserved.
 *Terms and conditions of use are stated in the Reference Code License Agreement.
 */

package org.continuaalliance.mcesl.wan;

/*******************************
 Developed by Vignet for Continua.
 *******************************/
import java.util.Properties;

import org.ca.cesl.wan.encoding.api.IAdapterEnumeration;
import org.ca.cesl.wan.encoding.api.IAdapterNumeric;
import org.ca.cesl.wan.encoding.api.IWanMdsContext;
import org.ca.cesl.wan.encoding.api.IWanMessage;

public class ObservationHelperCardioVascular extends ObservationHelper {

	/*
	  static final String SESSION_TIME = "SESSION_TIME"; 
	  static final String SESSION_HEART_RATE = "SESSION_HEART_RATE"; 
	  static final String TIMESTAMP = "TIMESTAMP"; 
	  static final String SESSION_DISTANCE = "SESSION_DISTANCE";
	  static final String SUBSESSION_TIME = "SUBSESSION_TIME";
	  static final String SUBSESSION_HEART_RATE = "SUBSESSION_HEART_RATE"; 
	  static final String SUBSESSION_DISTANCE = "SUBSESSION_DISTANCE";
	 */

	public ObservationHelperCardioVascular(Properties observationproperties) {
		// Auto-generated constructor stub
		super();
		observationdetails = observationproperties;
	}

	@Override
	protected IWanMessage GetHl7Message() {
		// Auto-generated method stub
		IWanMessage wanMessage = createWanMessage();
		IWanMdsContext mdscntxt = createMdsContext("4137",
				"VignetCorp: Cardio Monitor  1.0.0.1");
		wanMessage.encodeMdsObservation(mdscntxt);

		IAdapterEnumeration enumAdapter = createSimpleEnumerationAdapter(
				"1011", observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.SESSION_TIME), "1",
				"129", "129;123");

		wanMessage.encodeObservation(enumAdapter, mdscntxt);

		IAdapterNumeric numericAdapterHeartRate = createSimpleNumericAdapter(
				observationdetails.getProperty(WANConstants.SESSION_HEART_RATE),
				observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.TIMESTAMP), "", "5", null,
				"129", "129;114", null);
		wanMessage.encodeObservation(numericAdapterHeartRate, mdscntxt);

		IAdapterNumeric numericAdapterDistance = createSimpleNumericAdapter(
				observationdetails.getProperty(WANConstants.SESSION_DISTANCE),
				observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.TIMESTAMP), "1280", "6", null,
				"129", "129;103", null);
		wanMessage.encodeObservation(numericAdapterDistance, mdscntxt);
		if (observationdetails.containsKey(WANConstants.SUBSESSION_DISTANCE)) {
			IAdapterEnumeration enumAdapterSubSession = createSimpleEnumerationAdapter(
					"1011", observationdetails.getProperty(WANConstants.TIMESTAMP),
					observationdetails.getProperty(WANConstants.TIMESTAMP),
					observationdetails.getProperty(WANConstants.SUBSESSION_TIME), "1",
					"129", "129;123");

			wanMessage.encodeObservation(enumAdapterSubSession, mdscntxt);

			IAdapterNumeric numericAdapterHeartRateSubSession = createSimpleNumericAdapter(
					observationdetails.getProperty(WANConstants.SUBSESSION_HEART_RATE),
					observationdetails.getProperty(WANConstants.TIMESTAMP),
					observationdetails.getProperty(WANConstants.TIMESTAMP), "", "5", null,
					"129", "129;114", null);
			wanMessage.encodeObservation(numericAdapterHeartRateSubSession,
					mdscntxt);
			IAdapterNumeric numericAdapterDistanceSubSsssion = createSimpleNumericAdapter(
					observationdetails.getProperty(WANConstants.SUBSESSION_DISTANCE),
					observationdetails.getProperty(WANConstants.TIMESTAMP),
					observationdetails.getProperty(WANConstants.TIMESTAMP), "1280", "6",
					null, "129", "129;103", null);
			wanMessage.encodeObservation(numericAdapterDistanceSubSsssion,
					mdscntxt);
		}
		return wanMessage;
	}

}
