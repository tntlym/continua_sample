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

public class ObservationHelperPeakFlowMeter extends ObservationHelper {

	/*
	 * public static final String TIME_STAMP = "TIME_STAMP"; public static final
	 * String PERSONAL_BEST = "PERSONAL_BEST"; public static final String
	 * MEASUREMENT_STATUS = "MEASUREMENT_STATUS";
	 */

	ObservationHelperPeakFlowMeter(Properties observationporperties) {
		observationdetails = observationporperties;
	}

	@Override
	protected IWanMessage GetHl7Message() {

		IWanMessage wanMessage = createWanMessage();
		IWanMdsContext mdscntxt = createMdsContext("4117",
				"VignetCorp: Peak Expiratory Flow Monitor,Model 10");
		wanMessage.encodeMdsObservation(mdscntxt);
		SimpleAdapterNumeric myAdapterNumeric = createSimpleNumericAdapter(
				observationdetails.getProperty(WANConstants.PERSONAL_BEST),
				observationdetails.getProperty(WANConstants.TIME_STAMP),
				observationdetails.getProperty(WANConstants.TIME_STAMP), "3072", "1", null,
				"SCADA", "SCADA;21513", null);
		wanMessage.encodeObservation(myAdapterNumeric, mdscntxt);
		IAdapterEnumeration enumAdapter = createSimpleEnumerationAdapterSimpleBitStr(
				observationdetails.getProperty(WANConstants.MEASUREMENT_STATUS),
				observationdetails.getProperty(WANConstants.TIME_STAMP),
				observationdetails.getProperty(WANConstants.TIME_STAMP), null, "5",
				"PHD_DM", "PHD_DM;30720", null);

		wanMessage.encodeObservation(enumAdapter, mdscntxt);
		return wanMessage;
	}
}
