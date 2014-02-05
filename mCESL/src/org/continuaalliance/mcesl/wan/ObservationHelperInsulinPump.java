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

public class ObservationHelperInsulinPump extends ObservationHelper {

	/*
	 * public static final String TIMESTAMP = "TIMESTAMP"; public static final
	 * String BAASAL = "BAASAL"; public static final String BOLUS_FAST =
	 * "BOLUS_FAST"; public static final String BOLUS_SLOW = "BOLUS_SLOW";
	 */

	ObservationHelperInsulinPump(Properties observstionproperties) {
		observationdetails = observstionproperties;
	}

	@Override
	protected IWanMessage GetHl7Message() {
		//Auto-generated method stub
		IWanMessage wanMessage = createWanMessage();
		IWanMdsContext mdscntxt = createMdsContext("4115",
				"VignetCorp: Insulin Pump,Model 10");
		wanMessage.encodeMdsObservation(mdscntxt);
		SimpleAdapterNumeric myAdapterNumeric = createSimpleNumericAdapter(
				observationdetails.getProperty(WANConstants.BAASAL),
				observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.TIMESTAMP), "5696", "1", null,
				"SCADA", "SCADA,58368", null);
		wanMessage.encodeObservation(myAdapterNumeric, mdscntxt);
		SimpleAdapterNumeric myAdapterNumeric1 = createSimpleNumericAdapter(
				observationdetails.getProperty(WANConstants.BOLUS_FAST),
				observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.TIMESTAMP), "5696", "3", null,
				"PHD_DM", "SCADA,58380", null);
		wanMessage.encodeObservation(myAdapterNumeric1, mdscntxt);

		SimpleAdapterNumeric myAdapterNumeric2 = createSimpleNumericAdapter(
				observationdetails.getProperty(WANConstants.BOLUS_SLOW),
				observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.TIMESTAMP), "5696", "1", null,
				"SCADA", "SCADA,58384", null);
		wanMessage.encodeObservation(myAdapterNumeric2, mdscntxt);

		return wanMessage;
	}

}
