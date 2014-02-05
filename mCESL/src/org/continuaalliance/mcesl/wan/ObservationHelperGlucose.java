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

public class ObservationHelperGlucose extends ObservationHelper {
	/*
	 * static final String TIMESTAMP = "TIMESTAMP"; static final String
	 * GLU_CAPILLARY_WHOLEBLOOD = "GLU_CAPILLARY_WHOLEBLOOD";
	 */
	public ObservationHelperGlucose(Properties observationdetails) {
		super();
		this.observationdetails = observationdetails;
	}

	@Override
	protected IWanMessage GetHl7Message() {
		//  Auto-generated method stub
		IWanMessage wanMessage = createWanMessage();
		IWanMdsContext mdsCntxt = createMdsContext("4113",
				"VignetCorp;Gluocose Meter 1.0.0.1");

		// Unit Code - 1746
		SimpleAdapterNumeric myAdapterNumeric = createSimpleNumericAdapter(
				observationdetails
						.getProperty(WANConstants.GLU_CAPILLARY_WHOLEBLOOD),
				observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.TIMESTAMP), 
				observationdetails.getProperty(WANConstants.GLU_UNIT), 
				"1","29112", "2", "2;29112", null);
		
		// System.out.println("Done");
		wanMessage.encodeMdsObservation(mdsCntxt);
		wanMessage.encodeObservation(myAdapterNumeric, mdsCntxt);

		return wanMessage;
	}

}
