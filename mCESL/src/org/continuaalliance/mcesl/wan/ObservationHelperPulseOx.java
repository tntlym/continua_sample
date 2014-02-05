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

public class ObservationHelperPulseOx extends ObservationHelper {

	/*
	 * public static final String TIMESTAMP = "TIMESTAMP"; public static final
	 * String SPO2 = "SPO2"; static final String PULSERATE = "PULSERATE";
	 */

	public ObservationHelperPulseOx(Properties observationdetails) {
		super();
		this.observationdetails = observationdetails;
	}

	@Override
	protected IWanMessage GetHl7Message() {
		//  Auto-generated method stub
		IWanMessage wanMessage = createWanMessage();
		IWanMdsContext mdsCntxt = createMdsContext("4100",
				"VignetCorp;Pulse Oximeter 1.0.0.1");
		wanMessage.encodeMdsObservation(mdsCntxt);
		// Pulseoxymetery Pulse Rate

		SimpleAdapterNumeric myAdapterNumeric;
		SimpleAdapterNumeric myAdapterNumeric1;
		//for (int i = 0; i < 5; i++) {
			// Unit Code =- "2720"
		if(observationdetails.containsKey(WANConstants.PULSERATE)){
			myAdapterNumeric = createSimpleNumericAdapter(
					observationdetails.getProperty(WANConstants.PULSERATE),
					observationdetails.getProperty(WANConstants.TIMESTAMP),
					observationdetails.getProperty(WANConstants.TIMESTAMP),
					observationdetails.getProperty(WANConstants.PULSERATE_UNIT),
					"1", "18458", "2", "2;18458", null);

			wanMessage.encodeObservation(myAdapterNumeric, mdsCntxt);
		}
		if(observationdetails.containsKey(WANConstants.SPO2)){
			// Unit Code - "544"
			myAdapterNumeric1 = createSimpleNumericAdapter(
					observationdetails.getProperty(WANConstants.SPO2),
					observationdetails.getProperty(WANConstants.TIMESTAMP),
					observationdetails.getProperty(WANConstants.TIMESTAMP),
					observationdetails.getProperty(WANConstants.SPO2_UNIT),
					"1", "19384", "2", "2;19384", null);
			wanMessage.encodeObservation(myAdapterNumeric1, mdsCntxt);
		}
		//}
		return wanMessage;
	}

}
