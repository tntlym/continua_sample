package org.continuaalliance.mcesl.fsm;

import java.util.List;

import org.continuaalliance.mcesl.Event.Event;
import org.continuaalliance.mcesl.appData.DeviceObject;
import org.continuaalliance.mcesl.utils.ASN1Constants;
import org.continuaalliance.mcesl.utils.MessageFactory;
import org.continuaalliance.mcesl.utils.Nomenclature;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_INTU16;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_OIDType;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_Octet_String_Var;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ApduType;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.GetResultSimple;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SystemModel;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.TypeVer;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.TypeVerList;

import android.util.Log;

/*
 * State.java: Defines the abstract base class for state machine 
 *  
 * @author: Vignet
 */

abstract public class State {
	protected String stateName;
	protected DeviceManager manager_state_controller;
	protected int state;
	protected String TAG;
	protected int invokeid = 1000;

	/**
	 * Gets the name of the state currently in.
	 * 
	 * @return String name of the state currently in.
	 */
	public String getStateValue() {
		return stateName;
	}

	/**
	 * Gets the state identification constant of the state currently in.
	 * 
	 * @return int state identification constant of the state currently in.
	 */
	public int getState() {
		return state;
	}

	/**
	 * Abstract function which would be implemented by derived classes.
	 * 
	 * @param ApduType
	 *            from agent to be processed
	 */
	public abstract void ProcessData(ApduType DataRcvd);

	
	/**
	 * Processes the incoming event
	 * @param event int
	 */
	public abstract void ProcessEvent(int event);

	/**
	 * Processes incoming event according to the object sent
	 * @param event int
	 * @param obj Object
	 */
	public void ProcessEventWithObject(int event, Object obj) {
		Log.i("", "Base class ProcessEventWithObject called");
	}

	/**
	 * Creates Packet to GET all the attributes of the device MDS object and
	 * adds to write queue.
	 * 
	 */
	protected void sendGetAllAttributes() // called only from Configuring and
	// operating state.
	{
		ApduType DataToSend = MessageFactory.GetAllAttributes(new ASN_INTU16(
				++invokeid));
		manager_state_controller.sendMessageToManager(Event.ADD_TO_OUT_QUEUE,
				DataToSend);
	}

	/**
	 * Creates the packet to abort the connection with device and adds to write
	 * queue.
	 */
	protected void sendUnknownAbort() {
		ApduType DataToSend = MessageFactory.getAbortMessage(new ASN_INTU16(
				ASN1Constants.ABRT_RE_UNDEFINED));
		manager_state_controller.addToWriteQueue(DataToSend);

	}

	/**
	 * Sends packet to unassociate normally with the device.
	 * 
	 */
	protected void sendUnassociate_Normal() {
		ApduType DataToSend = MessageFactory.getRlrqMessage_NORMAL();
		manager_state_controller.sendMessageToManager(Event.ADD_TO_OUT_QUEUE,
				DataToSend);
	}

	/**
	 * Gets all the attribute information received from the device.
	 * 
	 * @param grs
	 *            GetResultSimple
	 */
	@SuppressWarnings("unchecked")
	protected void GetAttributeValuesFromResponse(GetResultSimple grs) {
		DeviceObject dObject = null;
		Boolean retValue = manager_state_controller.getAgentMDS()
				.getAttributesResponse(grs);
		if (retValue == true) {
			// now get the necessary attributes in Device Info object and send
			// to app.
			// Get the model, manufacturer.
			byte[] bManufacturer = null;
			byte[] bModelNo = null;
			byte[] bSystemID = null;
			try {
				SystemModel val = (SystemModel) manager_state_controller
						.getAgentMDS()
						.getAttribute(Nomenclature.MDC_ATTR_ID_MODEL)
						.getMainAttribute();
				if (val != null) {
					ASN_Octet_String_Var manufacturer = val.getManufacturer();
					if (manufacturer != null) {
						bManufacturer = manufacturer.getOctetString();
					}
					ASN_Octet_String_Var modelNo = val.getModelNumber();
					if (modelNo != null) {
						bModelNo = modelNo.getOctetString();
					}
				}
				ASN_Octet_String_Var sysID = (ASN_Octet_String_Var) manager_state_controller
						.getAgentMDS()
						.getAttribute(Nomenclature.MDC_ATTR_SYS_ID)
						.getMainAttribute();
				if (sysID != null) {
					bSystemID = sysID.getOctetString();
				}
			} catch (ClassCastException e) {
				// exception may cause because only PULSE oximeter sends System
				// ID as byte[] and not as ASN_Octet_String_Var
				bSystemID = (byte[]) manager_state_controller.getAgentMDS()
						.getAttribute(Nomenclature.MDC_ATTR_SYS_ID)
						.getMainAttribute();
			}
			dObject = new DeviceObject(bSystemID, bManufacturer, bModelNo);
			TypeVerList tvList = (TypeVerList) manager_state_controller
					.getAgentMDS()
					.getAttribute(Nomenclature.MDC_ATTR_SYS_TYPE_SPEC_LIST)
					.getMainAttribute();
			if (tvList != null) {
				List<TypeVer> tv = tvList.getMembers();
				for (int i = 0; i < tv.size(); i++) {
					TypeVer tVer = (TypeVer) tv.get(i);
					ASN_OIDType type = tVer.getType();
					manager_state_controller.getAgentMDS().setSpecilization(
							type.getValue());
				}
			}
		}
		// send deviceObject to app.
		manager_state_controller.sendMessageToManager(Event.DEVICE_INFO,
				dObject);
	}

}
