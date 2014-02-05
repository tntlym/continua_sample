package org.continuaalliance.mcesl.fsm;

import java.util.List;

import org.continuaalliance.mcesl.Event.Event;
import org.continuaalliance.mcesl.deviceSpecialization.DeviceSpecializationFactory;
import org.continuaalliance.mcesl.deviceSpecialization.ExtendedConfDeviceSpecialization;
import org.continuaalliance.mcesl.deviceSpecialization.PulseOxDeviceSpecialization;
import org.continuaalliance.mcesl.dim.MDSObject;
import org.continuaalliance.mcesl.utils.ASN1Constants;
import org.continuaalliance.mcesl.utils.ManagerInfo;
import org.continuaalliance.mcesl.utils.MessageFactory;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_BITS16;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_BITS32;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.AarqApdu;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ApduType;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.DataProto;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.DataProtoList;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.PhdAssociationInformation;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SystemType;

import android.util.Log;

/*
 * ConnectedState.java: A class which represents Connected state of the manager
 * 							
 * @author: Vignet
 */

public class ConnectedState extends State {

	/**
	 * Function which processes the input APDU data packet
	 * 
	 * @param ApduType
	 *            from agent to be processed
	 */
	@Override
	public void ProcessData(ApduType DataRcvd) {

		Log.i(TAG, "ProcessData in ConnectedState start");
		if (DataRcvd != null) {
			if (DataRcvd.isAARQ_CHOSEN()) {
				manager_state_controller
						.ChangeState(StateConstants.ASSOCIATING);
				Associating(DataRcvd);
			} else if (DataRcvd.isAARE_CHOSEN() || DataRcvd.isRLRQ_CHOSEN()
					|| DataRcvd.isPRST_CHOSEN()) {
				sendUnknownAbort();
				manager_state_controller
						.ChangeState(StateConstants.UNASSOCIATED);
			}
		}
		Log.i(TAG, "ProcessData in ConnectedState End");
	}

	/**
	 * Single parameter Constructor
	 * 
	 * @param DeviceManager
	 *            object which controls the state machine
	 */
	public ConnectedState(final DeviceManager state_controller) {
		manager_state_controller = state_controller;
		stateName = "Connected";
		state = StateConstants.CONNECTED;
		TAG = "CONNECTED STATE";
	}

	private Boolean checkIfStandardConfig(long configId) {
		Boolean retVal = false;
		/*if ((configId >= ASN1Constants.CONF_ID_STANDARD_CONFIG_START)
				&& (configId <= ASN1Constants.CONF_ID_STANDARD_CONFIG_END)) {
			retVal = true;
		}*/
		//remove above check after nonin pulse ox is checked.

		return retVal;
	}

	/**
	 * Creates MDS object according to data received from device.
	 * @param phdAssocInfo PhdAssociationInformaion received from device
	 */
	private void createMDSAccordingToConfig(
			PhdAssociationInformation phdAssocInfo) {
		short id = phdAssocInfo.getDevConfigId().getValue();
		if (id >= 400 && id <= 499) {
			// create Pulse ox MDS
			Log.i("createMDSAccordingToConfig", "Create pulse ox dev spec");
			PulseOxDeviceSpecialization pOxDevSpec = DeviceSpecializationFactory
					.createPulseOximeterSpecialization(phdAssocInfo
							.getSystemId().getBytes(), phdAssocInfo
							.getDevConfigId());
			manager_state_controller.setAgentMDS(pOxDevSpec);
			Log.i("createMDSAccordingToConfig", "pulse ox dev spec created");
		} /*else if ((1500 <= id) && (id <= 1599)) {
			// Weight device specialisation implementation for standard
			// configuration.
		}*/ else  //((ASN1Constants.CONF_ID_EXTENDED_CONFIG_START <= id)) {
		{
			// Extended configuration
			Log.i("createMDSAccordingToConfig", "Create Extended cnf");
			ExtendedConfDeviceSpecialization exDevSpec = DeviceSpecializationFactory
					.createExtendedSpecialization(phdAssocInfo.getSystemId()
							.getBytes(), phdAssocInfo.getDevConfigId());
			manager_state_controller.setAgentMDS(exDevSpec);
			manager_state_controller.getAgentMDS().setConfigId(id);
			Log.i("createMDSAccordingToConfig", "Extended cnf object");
		}
	}

	/**
	 * Describes the associating state of the Manager state machine.
	 * @param AssociationReq Association request from device.
	 */
	@SuppressWarnings("unchecked")
	private void Associating(ApduType AssociationReq) {
		Log.i(TAG, "Associating start");
		if (AssociationReq != null) {
			AarqApdu aarq = (AarqApdu) AssociationReq.getChoiceObject();
			DataProtoList dpList = aarq.getDataProtoList();
			List<DataProto> dpLMembers = dpList.getMembers();
			DataProto dp = null;
			Boolean selected = false;
			for (int i = 0; i < dpLMembers.size(); i++) {
				dp = (DataProto) dpLMembers.get(i);
				if (dp.isDATA_PROTO_ID_20601()) {
					selected = true;
					Log.e("", "20601 data proto");
					PhdAssociationInformation phdAssocInfo = (PhdAssociationInformation) dp
							.getDataProtoInfo().getAnyDefinedByObject();
					if (checkIfPhdIsValid(phdAssocInfo)) {
						Log.e(TAG, "Congig ID: --> "
								+ phdAssocInfo.getDevConfigId().getValue());
						if (checkIfStandardConfig(phdAssocInfo.getDevConfigId()
								.getValue())) {
							Log.e("", "Standard configuration");
							createMDSAccordingToConfig(phdAssocInfo);
							processAssociationResponse(MessageFactory
									.getAareApdu(ASN1Constants.AR_ACCEPTED),
									StateConstants.OPERATING);
							break;
						} else {
							MDSObject savedMDS = null;
							Log.e(TAG, "Congig ID: --> "
									+ phdAssocInfo.getDevConfigId().getValue());
							Log.e(TAG, "EXTERNAL Configuration");
							savedMDS = manager_state_controller
									.checkInSpecDictionery(phdAssocInfo
											.getDevConfigId().getValue());
							if (savedMDS != null) {
								manager_state_controller.setAgentMDS(savedMDS);
								processAssociationResponse(
										MessageFactory
												.getAareApdu(ASN1Constants.AR_ACCEPTED),
										StateConstants.OPERATING);
							} else {
								createMDSAccordingToConfig(phdAssocInfo);
								processAssociationResponse(
										MessageFactory
												.getAareApdu(ASN1Constants.AR_ACCEPTED_UNKNOWN_CONFIG),
										StateConstants.CONFIGURING);
							}
							break;
						}
					}
				}
			}
			if (!selected) {
				// no common protocol found.
				ApduType DataToSend = MessageFactory
						.getRejectApdu(ASN1Constants.AR_REJECTED_NO_COMMON_PROTOCOL);
				manager_state_controller.sendMessageToManager(
						Event.ADD_TO_OUT_QUEUE, DataToSend);
			}

		}
		Log.i(TAG, "Associating end");
	}

	/**
	 * It sends Association response to device and changes the state of the manager.
	 * @param DataToSend ApduType that is to be sent to device
	 * @param stateChange Next State of the Manager.
	 */
	private void processAssociationResponse(ApduType DataToSend, int stateChange) {
		Log.i(TAG, "processAssociationResponse start");
		manager_state_controller.sendMessageToManager(Event.ADD_TO_OUT_QUEUE,
				DataToSend);
		manager_state_controller.ChangeState(stateChange);
		Log.i(TAG, "processAssociationResponse End");
	}

	/**
	 * Checks the protocol version
	 * @param pVersion Protocol version
	 * @return true if satisfied else false
	 */
	private Boolean checkProtocolVersion(ASN_BITS32 pVersion) {

		Boolean retValue = false;
		Log.i(TAG, "checkProtocolVersion start pVersion--->" + pVersion);
		byte protocolFromDev[] = pVersion.getBytes();
		if (protocolFromDev.length == 4) {
			int val = 0;
			for (int k = 0; k < protocolFromDev.length; k++) {
				val = (val << 8) | (0xFF & (protocolFromDev[k]));
			}
			if ((val & ManagerInfo.protocol_version) == ManagerInfo.protocol_version) {
				Log.i(TAG, "Protocol version matches");
				retValue = true;
			}
		}
		Log.i(TAG, "checkProtocolVersion end");
		return retValue;
	}

	/**
	 * Checks if the device supports MDER encoding
	 * @param encoding 
	 * @return true if MDER supported else false
	 */
	private Boolean isMDERSupported(ASN_BITS16 encoding) {
		Log.i(TAG, "checkEncodingRule start");
		Boolean retValue = false;

		byte encodingFromDev[] = encoding.getBytes();

		int val = 0;
		for (int k = 0; k < encodingFromDev.length; k++) {
			val = (val << 8) | (0xFF & (encodingFromDev[k]));
		}

		if ((val & ManagerInfo.enc_mder) != 0) {
			Log.i(TAG, "Encoding rule matches");
			retValue = true;
		}

		Log.i(TAG, "checkEncodingRule end");
		return retValue;
	}

	/**
	 * Checks if the Nomenclature is supported by manager
	 * @param nVersion Nomenclature from device.
	 * @return true if supported else false
	 */
	
	private Boolean checkNomenclature(ASN_BITS32 nVersion) {
		Log.i(TAG, "checkNomenclature start");
		Boolean retValue = false;

		byte nomFromDev[] = nVersion.getBytes();

		int val = 0;
		for (int k = 0; k < nomFromDev.length; k++) {
			val = (val << 8) | (0xFF & (nomFromDev[k]));
		}
		if ((val & ManagerInfo.nomenclature_version)!=0) {
			Log.i(TAG, "Nmenclature matches");
			retValue = true;
		}

		Log.i(TAG, "checkNomenclature end");
		return retValue;
	}

	/**
	 * Checks if the system type is Agent.
	 * @param SystemType type of the device system.
	 * @return true if agent type else false
	 */
	
	private Boolean checkSystemType(SystemType sType) {
		Log.i(TAG, "checkSystemType start");
		Boolean retValue = false;
		int val = 0;
		byte[] value = sType.getBytes();
		for (int k = 0; k < value.length; k++) {
			val = (val << 8) | (0xFF & (value[k]));
		}
		if (val == ASN1Constants.SYS_TYPE_AGENT) {
			Log.i(TAG, "system type matches");
			retValue = true;
		}
		Log.i(TAG, "checkSystemType end");
		return retValue;
	}

	/**
	 * Checks if the Phd data received is valid or not
	 * @param phdAssocInfo PhdAssociationInformation from device
	 * @return true if Phd information is valid else false
	 */
	private Boolean checkIfPhdIsValid(PhdAssociationInformation phdAssocInfo) {
		Log.i(TAG, "checkIfPhdIsValid start");
		Boolean retValue = false;
		if (phdAssocInfo != null) {
			if (checkProtocolVersion(phdAssocInfo.getProtocolVersion())) {
				if (isMDERSupported(phdAssocInfo.getEncodingRules())) {
					if (checkNomenclature(phdAssocInfo.getNomenclatureVersion())) {
						// left functional units right now
						if (checkSystemType(phdAssocInfo.getSystemType())) {
							retValue = true;
						}
					}
				}
			}

		}
		Log.i(TAG, "checkIfPhdIsValid end with retValue-> " + retValue);
		return retValue;
	}

	@Override
	public void ProcessEvent(int event) {

		switch (event) {
		case Event.TRANSPORT_DISCONNECTED:
			manager_state_controller = null;
			break;
		case Event.ABORT_FROM_APP:
			sendUnknownAbort();
			break;
		case Event.UNASSOCIATE_FROM_APP:
			break;
		}

	}
}
