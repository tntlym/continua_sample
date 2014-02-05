/*Copyright © 2011, Continua Health Alliance.
 *All rights reserved.
 *Terms and conditions of use are stated in the Reference Code License Agreement.
 */

package org.continuaalliance.mcesl.wan;

/*******************************
Developed by Vignet for Continua.
*******************************/
import java.io.IOException;
import java.util.Properties;

import org.ca.cesl.wan.encoding.api.IAdapterMds;
import org.ca.cesl.wan.encoding.api.IWanEncodingSystemDescriptor;
import org.ca.cesl.wan.encoding.api.IWanEncodingUserDescriptor;
import org.ca.cesl.wan.encoding.api.IWanMdsContext;
import org.ca.cesl.wan.encoding.api.IWanMessage;
import org.ca.cesl.wan.encoding.context.WanMdsContext;
import org.ca.cesl.wan.encoding.descriptor.WanEncodingSystemDescriptor;
import org.ca.cesl.wan.encoding.descriptor.WanEncodingUserDescriptor;
import org.ca.cesl.wan.encoding.message.WanMessage;
import org.ca.cesl.wan.encoding.simpleadapters.SimpleAdapterEnumeration;
import org.ca.cesl.wan.encoding.simpleadapters.SimpleAdapterMds;
import org.ca.cesl.wan.encoding.simpleadapters.SimpleAdapterMetric;
import org.ca.cesl.wan.encoding.simpleadapters.SimpleAdapterNumeric;
import org.ca.cesl.wan.transport.WanServiceDescriptor;
import org.ca.cesl.wan.transport.api.IWanServiceDescriptor;
import org.ca.cesl.wan.transport.api.IWanServiceResult;
import org.continuaalliance.mcesl.settings.Settings;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public abstract class ObservationHelper {

	protected Properties observationdetails;

	protected abstract IWanMessage GetHl7Message();
	static final String SOAP_ACTION = "urn:ihe:pcd:2010:CommunicatePCDData";
	public ObservationHelper() {
		super();
	}
	
	protected IWanMdsContext createMdsContext(String systemSpecList,String systemModel) {
		Properties properties = new Properties();
		//properties.setProper`ty(SimpleAdapterMds.MDSPROPKEY_SYSTEMTYPE, "2;576");
		properties.setProperty(SimpleAdapterMds.MDSPROPKEY_SYSTEMID , "VIGNETAGENT");
		properties.setProperty(SimpleAdapterMds.MDSPROPKEY_SYSTEMMODEL, systemModel);
		properties.setProperty(SimpleAdapterMds.MDSPROPKEY_PRODUCTIONSPEC,"");
		properties.setProperty(SimpleAdapterMds.MDSPROPKEY_SYSTEMTYPESPECLIST, "8;"+systemSpecList);
		IAdapterMds mdsAdapter  = new SimpleAdapterMds(properties );

		
		IWanMdsContext mdsCntxt = new WanMdsContext(mdsAdapter );
		return mdsCntxt;
	}
	protected IWanMessage createWanMessage()
	{
		IWanEncodingUserDescriptor userDescriptor = new WanEncodingUserDescriptor("034CD8500AB0","VigNetCorp","Thompson","Laurie","LT");
		byte[] mgrId = { 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, (byte) 0x88 };
		
		IWanEncodingSystemDescriptor systemDescriptor = new WanEncodingSystemDescriptor("VignetAhd", mgrId);
		IWanMessage wanMessage = new WanMessage("MSGID123456789", userDescriptor, systemDescriptor);
		wanMessage.encodeObservationGroup(null);
		return wanMessage;
	}
	protected SimpleAdapterNumeric createSimpleNumericAdapter(String observation, String utcTimeStamp, String absoluteTimeStamp, String unitCode, String handle, String merticID, String partition,String type,String metricIDList) 
	{
		Properties p1 = new Properties();
		
		if(metricIDList != null)
			p1.setProperty(SimpleAdapterNumeric.NUMERICPROPKEY_COMPOUNDBASICNUOBSVALUE,observation );
		else
			p1.setProperty(SimpleAdapterNumeric.NUMERICPROPKEY_SIMPLENUOBSVALUE,observation );
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_UTCTIMESTAMP, utcTimeStamp);		
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_ABSOLUTETIMESTAMP, absoluteTimeStamp);		
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_UNITCODE, unitCode);
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_HANDLE, handle);
		if(merticID != null)
			p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_METRICID, merticID);
		if(metricIDList !=null )
		p1.setProperty(SimpleAdapterNumeric.METRICPROPKEY_METRICIDLIST, metricIDList);
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_METRICIDPARTITION, partition);
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_TYPE, type);
		SimpleAdapterNumeric myAdapterNumeric = new SimpleAdapterNumeric(p1 );
		return myAdapterNumeric;
	}
	protected SimpleAdapterNumeric createSimpleNumericAdapterBasicValue(String observation, String utcTimeStamp, String absoluteTimeStamp, String unitCode, String handle, String merticID, String partition,String type,String metricIDList) 
	{
		Properties p1 = new Properties();
		
	
			p1.setProperty(SimpleAdapterNumeric.NUMERICPROPKEY_BASICNUOBSVALUE,observation );
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_UTCTIMESTAMP, utcTimeStamp);		
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_ABSOLUTETIMESTAMP, absoluteTimeStamp);		
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_UNITCODE, unitCode);
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_HANDLE, handle);
		if(merticID != null)
			p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_METRICID, merticID);
		if(metricIDList !=null )
		p1.setProperty(SimpleAdapterNumeric.METRICPROPKEY_METRICIDLIST, metricIDList);
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_METRICIDPARTITION, partition);
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_TYPE, type);
		SimpleAdapterNumeric myAdapterNumeric = new SimpleAdapterNumeric(p1 );
		return myAdapterNumeric;
	}
	protected SimpleAdapterEnumeration createSimpleEnumerationAdapter(String valOid, String utcTimeStamp, String absoluteTimeStamp, String activeTime, String handle,  String partition ,String merticID) {
		Properties p1 = new Properties();

		p1.setProperty(SimpleAdapterEnumeration.ENUMERATIONPROPKEY_ENUMOBSVALUESIMPLEOID,valOid);
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_UTCTIMESTAMP, utcTimeStamp);		
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_ABSOLUTETIMESTAMP, absoluteTimeStamp);	
		if(activeTime!=null)
		
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_MEASUREACTIVEPERIOD, activeTime);
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_TYPE , merticID);
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_HANDLE, handle);
		//p1.setProperty(SimpleAdapterEnumeration., handle);
		p1.setProperty(SimpleAdapterEnumeration.ENUMERATIONPROPKEY_ENUMOBSVALUEPARTITION, partition);
		
		SimpleAdapterEnumeration myAdapterNumeric = new SimpleAdapterEnumeration(p1 );
		return myAdapterNumeric;
	}
	protected SimpleAdapterEnumeration createSimpleEnumerationAdapterSimpleBitStr(String bitStr, String utcTimeStamp, String absoluteTimeStamp, String activeTime, String handle,  String partition ,String merticID,String supplimentalTypes) {
		Properties p1 = new Properties();
		
		p1.setProperty(SimpleAdapterEnumeration.ENUMERATIONPROPKEY_ENUMOBSVALUESIMPLEBITSTR, bitStr);
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_UTCTIMESTAMP, utcTimeStamp);		
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_ABSOLUTETIMESTAMP, absoluteTimeStamp);	
		if(activeTime!=null)
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_MEASUREACTIVEPERIOD, activeTime);
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_TYPE , merticID);
		p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_HANDLE, handle);
		if(supplimentalTypes != null)
			p1.setProperty(SimpleAdapterMetric.METRICPROPKEY_SUPPLEMENTALTYPES, supplimentalTypes);//
		p1.setProperty(SimpleAdapterEnumeration.ENUMERATIONPROPKEY_ENUMOBSVALUEPARTITION, partition);
		
		SimpleAdapterEnumeration myAdapterNumeric = new SimpleAdapterEnumeration(p1 );
		return myAdapterNumeric;
	}

	public boolean send() throws IOException, XmlPullParserException {
		
		Log.i("ObservationHelper", "In send - ObservationHelper...");
		
		IWanServiceDescriptor wanServiceDesc = new WanServiceDescriptor();
		//wanServiceDesc.setServicePort(8080);
		wanServiceDesc.setServicePort(Integer.parseInt(((String) Settings.getInstance().get(SettingItemNames.portNumber))));
		//wanServiceDesc.setServiceIp("continua.bxihealth.com");
		//wanServiceDesc.setServiceName("/axis2/services/DeviceObservationConsumer_ServiceVanilla");
		wanServiceDesc.setServiceIp((String) Settings.getInstance().get(SettingItemNames.server_url));
		wanServiceDesc.setServiceName((String) Settings.getInstance().get(SettingItemNames.serviceName));
		wanServiceDesc.setWsaAction((String) Settings.getInstance().get(SettingItemNames.serviceAction));
		//wanServiceDesc.setWsaAction(SOAP_ACTION);
		MCESLWanService aService = new MCESLWanService(wanServiceDesc , null, null, null, null);
		aService.setLastMessageFlag(true);
		IWanServiceResult aRes = aService.sendMessage(GetHl7Message());
		//tv.setText(aRes.getResult());
		return aService.wasTransferSuccessful(aRes);
	
	
	
	}
  
}