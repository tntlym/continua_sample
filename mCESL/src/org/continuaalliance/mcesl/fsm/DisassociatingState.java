package org.continuaalliance.mcesl.fsm;

import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ApduType;

/*
 * DissassociatingState.java: A class which represents Disassociating state of the manager
 * 							
 * @author: Vignet
 */

public class DisassociatingState extends State {

		
	/**
	 * Single parameter Constructor   
	 * @param DeviceManager object which controls the state machine
	 */
	public DisassociatingState(final DeviceManager state_controller)
	{
		manager_state_controller  = state_controller;
		stateName = "Disassociating";
		state = StateConstants.DISASSOCIATING;
	}

	@Override
	public void ProcessEvent(int event) {
		// nothing as of now
		
	}
	
	/**
	 * Function which processes the input APDU data packet   
	 * @param ApduType from agent to be processed
	 */
	
	@Override
	public void ProcessData(ApduType DataRcvd) {
		
	}
}
