package org.continuaalliance.mcesl.fsm;

import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ApduType;

import android.util.Log;

/*
 * AssociatedState.java: A class which represents Associated state of the manager
 * 							
 * @author: Vignet
 */
public class AssociatedState extends State {

	/**
	 * Function which processes the input APDU data packet   
	 * @param ApduType from agent to be processed
	 */
	@Override
	public void ProcessData(ApduType DataRcvd) {
		
		Log.e("", "Process data in state: "+stateName);
		// check the data and change the state accordingly and send message to handler 
		// to call processData on new state.
		// This is kept empty as ASN1 data types and encoding is not yet available.
	}
	
	/**
	 * Single parameter Constructor   
	 * @param DeviceManager object which controls the state machine
	 */
	public AssociatedState (final DeviceManager state_controller)
	{
		manager_state_controller  = state_controller;
		stateName = "Associated";
		state = StateConstants.ASSOCIATED;
	}

	@Override
	public void ProcessEvent(int event) {
		//nothing as of now
		
	}
}
