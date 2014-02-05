/*Copyright © 2011, Continua Health Alliance.
 *All rights reserved.
 *Terms and conditions of use are stated in the Reference Code License Agreement.
 */

package org.continuaalliance.mcesl.wan;

/*******************************
Developed by Vignet for Continua.
*******************************/
import java.io.IOException;

import org.ca.cesl.wan.encoding.api.IWanLogger;
import org.ca.cesl.wan.encoding.api.IWanMessage;
import org.ca.cesl.wan.transport.WanServiceResult;
import org.ca.cesl.wan.transport.api.IWanAuditorDescriptor;
import org.ca.cesl.wan.transport.api.IWanInvocationContext;
import org.ca.cesl.wan.transport.api.IWanServiceDescriptor;
import org.ca.cesl.wan.transport.api.IWanServiceResult;
import org.ca.cesl.wan.transport.api.IWanTransportSystemDescriptor;
import org.ca.cesl.wan.transport.api.IWanTransportUserDescriptor;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class MCESLWanService extends org.ca.cesl.wan.transport.AbstractWanServiceClient
{
		private static final String METHOD_NAME = "CommunicatePCDData";
		 private static final String NAMESPACE = "urn:ihe:pcd:dec:2010";
	protected static IWanLogger logger = null;

	public static IWanLogger getLogger() {
		return logger;
	}
	protected static void setLogger(IWanLogger logger) {
		MCESLWanService.logger = logger;
	}
	protected MCESLWanService(IWanServiceDescriptor wanServiceDesc,
			IWanInvocationContext wanInvocationContext,
			IWanTransportSystemDescriptor wanSystemDesc,
			IWanTransportUserDescriptor wanUserDesc,
			IWanAuditorDescriptor wanAuditorDesc) {
		super(wanServiceDesc, wanInvocationContext, wanSystemDesc, wanUserDesc,
				wanAuditorDesc);
		
		if(getLogger()==null)
			setLogger(new Logger());
	}
	public boolean wasTransferSuccessful(IWanServiceResult theResult)
	{
			boolean bResult = wasMessageAccepted(theResult.getResult());
			getLogger().logInfo("Reply from server");
			getLogger().logInfo(theResult.getResult());
			getLogger().logInfo("Status : " + bResult);
			return bResult;
	}

	
	public IWanServiceResult sendMessage(IWanMessage arg0) {
		
		Log.i("MCESLWanService", "In sendMessage - MCESLWanService...");
		 SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12); //put all required data into a soap envelope
		 
		 
		 // SOAP 1.2 requires the soap namespace as http://www.w3.org/2003/05/soap-envelope
		 envelope.env = "http://www.w3.org/2003/05/soap-envelope";
	     //prepare request
	    
		 /*getLogger().logInfo("Sending Observation :");
		 getLogger().logInfo( arg0.toHl7String());*/
		 SoapPrimitive request = new SoapPrimitive(NAMESPACE,METHOD_NAME, arg0.toHl7String());
			envelope.setOutputSoapObject(request); 
		String url=wanServiceDescriptor.getServiceUri();
		//getLogger().logInfo("To Address : " + url + " .Port : " + wanServiceDescriptor.getServicePort() + " .Service Name : " + wanServiceDescriptor.getServiceName() + " .Action : " + wanServiceDescriptor.getWsaAction());
		
	    MCESLTransport httpTransport = new MCESLTransport(url);
	    httpTransport.debug = true;  //this is optional, use it if you don't want to use a packet sniffer to check what the sent message was (httpTransport.requestDump)
	    try {
			httpTransport.call(wanServiceDescriptor.getWsaAction(), envelope);
		} catch (IOException e) {
	
			e.printStackTrace();
		
			getLogger().logErrorException(e);
		} catch (XmlPullParserException e) {
			
			getLogger().logErrorException(e);
		} //send request
	    
	    IWanServiceResult retServiceResult = new WanServiceResult(envelope.bodyIn);
	    return retServiceResult;
	    
	 
		
	}




}
