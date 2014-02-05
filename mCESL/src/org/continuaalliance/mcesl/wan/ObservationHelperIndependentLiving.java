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

public class ObservationHelperIndependentLiving extends ObservationHelper {

	/*
	 * public static final String BIT_STR = "BIT_STR"; public static final
	 * String TIMESTAMP = "TIMESTAMP"; public static final String
	 * TEMPERATURE_HIGH = "0x80000000";; public static final String
	 * TEMPERATURE_LOW = "0x40000000";; public static final String
	 * TEMPERATURE_FASTRATE = "0x20000000";
	 */

	public ObservationHelperIndependentLiving(Properties observationdetails) {
		super();
		this.observationdetails = observationdetails;
	}

	@Override
	protected IWanMessage GetHl7Message() {
		// Auto-generated method stub
		IWanMessage wanMessage = createWanMessage();
		IWanMdsContext mdscntxt = createMdsContext("4167",
				"VignetCorp: Independent living activity hub  1.0.0.1");
		wanMessage.encodeMdsObservation(mdscntxt);

		IAdapterEnumeration enumAdapter = createSimpleEnumerationAdapterSimpleBitStr(
				observationdetails.getProperty(WANConstants.BIT_STR),
				observationdetails.getProperty(WANConstants.TIMESTAMP),
				observationdetails.getProperty(WANConstants.TIMESTAMP), null, "4", "130",
				"130;14", "130;3072");

		wanMessage.encodeObservation(enumAdapter, mdscntxt);

		return wanMessage;
	}

}
