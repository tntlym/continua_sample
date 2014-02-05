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

public class ObservationHelperMedication extends ObservationHelper {

	/*
	 * public static final String VARIABLE_DOSE = "VARIABLE_DOSE"; public static
	 * final String TIMESTAMP = "TIMESTAMP";
	 */

	public ObservationHelperMedication(Properties observationproperties) {
		//Auto-generated constructor stub
		observationdetails = observationproperties;
	}

	@Override
	protected IWanMessage GetHl7Message() {
		//Auto-generated method stub
		IWanMessage wanMessage = createWanMessage();
		IWanMdsContext mdscntxt = createMdsContext("4168",
				"VignetCorp: Variable-dosage medication monitor  1.0.0.1");
		wanMessage.encodeMdsObservation(mdscntxt);

		SimpleAdapterNumeric myAdapterNumeric = createSimpleNumericAdapter(
				observationdetails.getProperty(WANConstants.VARIABLE_DOSE),
				observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.TIMESTAMP), "1618", "4", null,
				"130", "130;13313", null);
		wanMessage.encodeObservation(myAdapterNumeric, mdscntxt);
		return wanMessage;
	}

}
