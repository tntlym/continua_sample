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

public class ObservationHelperThermometer extends ObservationHelper {

	/*
	 * static final String TIMESTAMP = "TIMESTAMP"; static final String
	 * BODY_TEMPERATURE = "BODY_TEMPERATURE";
	 */

	public ObservationHelperThermometer(Properties observationdetails) {
		super();
		this.observationdetails = observationdetails;
	}

	@Override
	protected IWanMessage GetHl7Message() {
		// Auto-generated method stub

		IWanMessage wanMessage = createWanMessage();
		IWanMdsContext mdsCntxt = createMdsContext("4104",
				"VignetCorp;Themometer 1.0.0.1");
		
		// Unit Code - "4416"
		SimpleAdapterNumeric myAdapterNumeric = createSimpleNumericAdapter(
				observationdetails.getProperty(WANConstants.BODY_TEMPERATURE),
				observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.TIMESTAMP), 
				observationdetails.getProperty(WANConstants.TEMPERATURE_UNIT),
				"1","19292", "2", "2;19292", null);
		
		wanMessage.encodeMdsObservation(mdsCntxt);
		wanMessage.encodeObservation(myAdapterNumeric, mdsCntxt);
		return wanMessage;
	}

}
