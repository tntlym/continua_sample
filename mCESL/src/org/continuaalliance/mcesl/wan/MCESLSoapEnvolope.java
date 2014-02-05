/*Copyright © 2011, Continua Health Alliance.
 *All rights reserved.
 *Terms and conditions of use are stated in the Reference Code License Agreement.
 */

package org.continuaalliance.mcesl.wan;

/*******************************
Developed by Vignet for Continua.
*******************************/
import java.io.IOException;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlSerializer;

public class MCESLSoapEnvolope extends SoapSerializationEnvelope {

	private String theBodyLiteral;
	
	public String getTheBodyLiteral() {
		return theBodyLiteral;
	}
	public void setTheBodyLiteral(String theBodyLiteral) {
		this.theBodyLiteral = theBodyLiteral;
	}
	public MCESLSoapEnvolope(int version) {
		super(version);
		
	}
	@Override
	public void writeHeader(XmlSerializer writer) throws IOException {
		
	}
	@Override
	public void write(XmlSerializer writer) throws IOException {
		
		    writer.setPrefix("i", xsi);
	        writer.setPrefix("d", xsd);
	        writer.setPrefix("c", enc);
	        writer.setPrefix("v", env);
	        writer.startTag(env, "Envelope");
	      
	        writer.startTag(env, "Body");
	        writeBody(writer);
	        writer.endTag(env, "Body");
	        writer.endTag(env, "Envelope");
	}
@Override
public void writeObjectBody(XmlSerializer writer, SoapObject obj)
		throws IOException {
	
	super.writeObjectBody(writer, obj);
	writer.text(theBodyLiteral);
}
@Override
public Object getResponse() throws SoapFault {
	
	return bodyIn;
}
}
