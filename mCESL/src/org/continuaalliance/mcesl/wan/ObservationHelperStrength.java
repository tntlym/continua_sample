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
import org.ca.cesl.wan.encoding.api.IWanMdsContext;
import org.ca.cesl.wan.encoding.api.IWanMessage;
import org.ca.cesl.wan.encoding.simpleadapters.SimpleAdapterNumeric;

public class ObservationHelperStrength extends ObservationHelper {

	/*
	 * public static final String TIMESTAMP = "TIMESTAMP"; public static final
	 * String REPETITIONS = "REPETITIONS"; public static final String RESISTENCE
	 * = "RESISTENCE"; public static final String SET_TIME = "SET_TIME";
	 */

	public ObservationHelperStrength(Properties observationproperties) {
		// Auto-generated constructor stub
		super();
		observationdetails = observationproperties;
	}

	@Override
	protected IWanMessage GetHl7Message() {
		//  Auto-generated method stub
		IWanMessage wanMessage = createWanMessage();
		IWanMdsContext mdscntxt = createMdsContext("4138",
				"VignetCorp: Strength and Fitness Monitor  1.0.0.1");
		wanMessage.encodeMdsObservation(mdscntxt);
		IAdapterEnumeration enumAdapter1 = createSimpleEnumerationAdapter(
				"532", observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.SET_TIME), "4", "7", "129;200");

		wanMessage.encodeObservation(enumAdapter1, mdscntxt);
		IAdapterEnumeration enumAdapter = createSimpleEnumerationAdapter(
				"1205", observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.TIMESTAMP), null, "4", "129",
				"129;204");

		wanMessage.encodeObservation(enumAdapter, mdscntxt);

		SimpleAdapterNumeric myAdapterNumeric = createSimpleNumericAdapter(
				observationdetails.getProperty(WANConstants.RESISTENCE),
				observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.TIMESTAMP), "1760", "4", null,
				"129", "129;203", null);
		wanMessage.encodeObservation(myAdapterNumeric, mdscntxt);

		SimpleAdapterNumeric myAdapterNumeric1 = createSimpleNumericAdapter(
				observationdetails.getProperty(WANConstants.REPETITIONS),
				observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.TIMESTAMP), "", "4", null,
				"129", "129;202", null);
		wanMessage.encodeObservation(myAdapterNumeric1, mdscntxt);
		return wanMessage;
	}

}
