/*Copyright © 2011, Continua Health Alliance.
 *All rights reserved.
 *Terms and conditions of use are stated in the Reference Code License Agreement.
 */

package org.continuaalliance.mcesl.wan;

/*******************************
Developed by Vignet for Continua.
*******************************/
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.transport.AndroidServiceConnection;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;





/**
 * A J2SE based HttpTransport layer.
 */
public class MCESLTransport extends HttpTransportSE {

    /**
     * Creates instance of HttpTransportSE with set url
     * 
     * @param url
     *            the destination to POST SOAP data
     */
    public MCESLTransport(String url) {
        super(url);
    }

    public void call(String soapAction, SoapEnvelope envelope) throws IOException, XmlPullParserException {
    	
    	Log.i("MCESLTransport", "In call - MCESLTransport...");
    	
        if (soapAction == null)
            soapAction = "\"\"";
        byte[] requestData = createRequestData(envelope);
        requestDump = debug ? new String(requestData) : null;
        responseDump = null;
        ServiceConnection connection = getServiceConnection();
		/* SOAP2 HTTP HEADER
		Content-Type: appli\cation/soap+xml; charset=utf-8; action="urn:ihe:pcd:2009:CommunicatePCDData"
		Host: 127.0.0.1:9080
		Content-Length: 1109
		Expect: 100-continue
		Accept-Encoding: gzip, deflate
		Connection: Keep-Alive
		 */
        connection.setRequestProperty("User-Agent", "kSOAP/2.0");
        connection.setRequestProperty("SOAPAction", soapAction);
        connection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8; action=\"urn:ihe:pcd:2009:CommunicatePCDData\"");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Content-Length", "" + requestData.length);
        connection.setRequestMethod("POST");
        connection.connect();
        OutputStream os = connection.openOutputStream();
        os.write(requestData, 0, requestData.length);
        os.flush();
        os.close();
        requestData = null;
        InputStream is;
        try {
        	Log.i("MCESLTransport", "In call - trying to connect.");
            connection.connect();
            is = connection.openInputStream();
            Log.i("MCESLTransport", "In call - connection received.");
        } catch (IOException e) {
            is = connection.getErrorStream();
            if (is == null) {
                connection.disconnect();
                throw (e);
            }
        }
        if (debug) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[256];
            while (true) {
                int rd = is.read(buf, 0, 256);
                if (rd == -1)
                    break;
                bos.write(buf, 0, rd);
            }
            bos.flush();
            buf = bos.toByteArray();
            responseDump = new String(buf);
            is.close();
            is = new ByteArrayInputStream(buf);
        }
        parseResponse(envelope, is);
    }

    protected ServiceConnection getServiceConnection() throws IOException {
        return new AndroidServiceConnection(url);
    }
}