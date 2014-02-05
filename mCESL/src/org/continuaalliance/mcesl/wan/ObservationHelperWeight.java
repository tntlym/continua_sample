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

public class ObservationHelperWeight extends ObservationHelper {
	/*
	 * static final String TIMESTAMP = "TIMESTAMP"; static final String
	 * BODY_WEIGHT = "BODY_WEIGHT";
	 */

	public ObservationHelperWeight(Properties observationdetails) {
		super();
		this.observationdetails = observationdetails;
	}

	@Override
	protected IWanMessage GetHl7Message() {

		IWanMessage wanMessage = createWanMessage();
		IWanMdsContext mdsCntxt = createMdsContext("4111",
				"VignetCorp;Weight Scale 1.0.0.1");

		// Unit Code = 1731
		SimpleAdapterNumeric myAdapterNumeric = createSimpleNumericAdapter(
				observationdetails.getProperty(WANConstants.BODY_WEIGHT),
				observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.TIMESTAMP), 
				observationdetails.getProperty(WANConstants.WEIGHT_UNIT), "1",
				"57664", "2", "2;57664", null);
		wanMessage.encodeMdsObservation(mdsCntxt);
		wanMessage.encodeObservation(myAdapterNumeric, mdsCntxt);

		return wanMessage;
	}

}
