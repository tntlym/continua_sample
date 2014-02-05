package org.continuaalliance.mcesl.appData;

/**
 * StateEvent.java: This class used in sending the constants in events like device state change.
 * 
 * @author Vignet
 */
import java.io.Serializable;

@SuppressWarnings("serial")
public class StateEvent implements Serializable{

	public final static int CONNECTED = 100;
	public final static int UNASSOCIATED = 101;
	public final static int ASSOCIATING = 102;
	public final static int ASSOCIATED = 103;
	public final static int OPERATING = 104;
	public final static int DISCONNECTED = 105;
	public final static int DISASSOCIATING = 106;
	public final static int CONFIGURING = 107;
	
	public final int State;
	public StateEvent(int state) {
		State = state;
	}
}
