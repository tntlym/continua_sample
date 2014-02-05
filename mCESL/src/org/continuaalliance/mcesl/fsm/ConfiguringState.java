package org.continuaalliance.mcesl.fsm;


import org.continuaalliance.mcesl.Event.Event;
import org.continuaalliance.mcesl.appData.AppMDSObject;
import org.continuaalliance.mcesl.utils.Nomenclature;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_AnyDefinedBy;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_Handle;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_INTU16;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ApduType;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ConfigReportRsp;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.DataApdu;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.DataApduMessageChoice;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.EventReportArgumentSimple;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.EventReportResultSimple;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.GetResultSimple;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.PrstApdu;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.RelativeTime;
import org.openhealthtools.stepstone.phd.core.codes.EncodedDataTypeDefinitions;
import org.openhealthtools.stepstone.phd.encoding.mder.DecoderMDER;


import android.util.Log;

/*
 * ConfiguringState.java: A class which represents Configuring state of the manager
 * 							
 * @author: Vignet
 */
public class ConfiguringState extends State {

	/**
	 * Function which processes the input APDU data packet   
	 * @param ApduType from agent to be processed
	 */
	@Override
	public void ProcessData(ApduType DataRcvd) {

		if(DataRcvd.isPRST_CHOSEN())
		{
			Log.i("ConfiguringState", " prst apdu recvd");
			PrstApdu prstApdu = (PrstApdu) DataRcvd.getChoiceObject();
			DataApdu dataApdu = new DataApdu(new DecoderMDER(prstApdu.getOctetString()));

			DataApduMessageChoice choice = dataApdu.getMessage();
			switch(choice.getChoiceTag().getValue())
			{
			case EncodedDataTypeDefinitions.ROIV_CMIP_CONFIRMED_EVENT_REPORT_CHOSEN:
				Log.i("","ROIV_CMIP_CONFIRMED_EVENT_REPORT_CHOSEN");


				EventReportArgumentSimple eras = (EventReportArgumentSimple)choice.getChoiceObject();
				switch((int)eras.getEventType().getValue())
				{
				case Nomenclature.MDC_NOTI_CONFIG:
					ConfigReportRsp response = manager_state_controller.getAgentMDS().MDS_CONFIG_NOTIFICATION_Event(eras);
					if(response != null)
					{
						ApduType apdu = createConfigResposeApdu(dataApdu,response);
						Log.i("", "SENDING CONFIG RESPONSE");
						manager_state_controller.sendMessageToManager(Event.ADD_TO_OUT_QUEUE,apdu);
						Log.i("", "Changing to OPERATING STATE");
						// Add to ManagerControllers MDSObject dictionary
						manager_state_controller.setInDeviceDictionary(
								manager_state_controller.getAgentMDS().getConfigID(), 
								manager_state_controller.getAgentMDS());
						//call GET for all attributes and then switch to operating state
						Log.i("", "CALLING GET ");
						sendGetAllAttributes();
						Log.i("", "GET CALLED");
						//call to GET only works with real devices.
						
						//create AppMDSObject and send to application
						Log.i("", "Creating APP MDS object");
						AppMDSObject obj = manager_state_controller.getAgentMDS().createAppMDS();
						obj.setDeviceID(manager_state_controller.getID());
						Log.i("", "Sending it to app");
						manager_state_controller.sendMessageToManager(Event.MDS_OBJECT_RECVD, obj);
						
						//remove below comment only when get() not be used
						//manager_state_controller.ChangeState(StateConstants.OPERATING);
						
					}
					System.out.println("MDC_NOTI_CONFIG");

					break;
				case Nomenclature.MDC_NOTI_SCAN_REPORT_VAR:
					System.out.println("MDC_NOTI_SCAN_REPORT_VAR");
					break;
				case Nomenclature.MDC_NOTI_SCAN_REPORT_FIXED:
					System.out.println("MDC_NOTI_SCAN_REPORT_FIXED");
					break;
				case Nomenclature.MDC_NOTI_SCAN_REPORT_MP_VAR: 
					System.out.println("MDC_NOTI_SCAN_REPORT_MP_VAR");
					break;
				case Nomenclature.MDC_NOTI_SCAN_REPORT_MP_FIXED:
					System.out.println("MDC_NOTI_SCAN_REPORT_MP_FIXED");
					break; 
				}
				break;
			case EncodedDataTypeDefinitions.ROIV_CMIP_GET_CHOSEN:
				Log.i("","ROIV_CMIP_GET_CHOSEN");
				break;
				
			case EncodedDataTypeDefinitions.RORS_CMIP_GET_CHOSEN:
				Log.i("","RORS_CMIP_GET_CHOSEN");
				GetResultSimple grSimple = (GetResultSimple)choice.getChoiceObject();
				GetAttributeValuesFromResponse(grSimple);
				Log.i("","Switching to Operating state from configuring state");
				manager_state_controller.ChangeState(StateConstants.OPERATING);
				break;
			}
		}

		// check the data and change the state accordingly and send message to handler 
		// to call processData on new state.
		// This is kept empty as ASN1 data types and encoding is not yet available.
	}
	private ApduType createConfigResposeApdu(DataApdu dataApdu,ConfigReportRsp response)
	{

		ASN_Handle mdsHandle = (ASN_Handle)manager_state_controller.getAgentMDS().
		getAttribute(Nomenclature.MDC_ATTR_ID_HANDLE).getMainAttribute();
		RelativeTime rt = new RelativeTime(0);
		EventReportArgumentSimple eras = (EventReportArgumentSimple)dataApdu.getMessage().getChoiceObject();
		ASN_AnyDefinedBy any = new ASN_AnyDefinedBy(response);
		EventReportResultSimple errs = new EventReportResultSimple(mdsHandle, rt, 
				eras.getEventType(), any);
		ASN_INTU16 choice =
			EncodedDataTypeDefinitions.RORS_CMIP_CONFIRMED_EVENT_REPORT_CHOSEN_INTU16;

		DataApduMessageChoice dataMsg = new DataApduMessageChoice(choice, errs);
		DataApdu dApdu = new DataApdu(dataApdu.getInvokeId(), dataMsg);
		PrstApdu pApdu = new PrstApdu(dApdu.getBytes());
		ApduType retVal = new ApduType(EncodedDataTypeDefinitions.PRST_CHOSEN_INTU16, pApdu);
		return retVal;
	}
	/**
	 * Single parameter Constructor   
	 * @param DeviceManager object which controls the state machine
	 */
	public ConfiguringState(final DeviceManager state_controller)
	{
		manager_state_controller  = state_controller;
		stateName = "Configuring";
		state = StateConstants.CONFIGURING;
	}

	@Override
	public void ProcessEvent(int event) {
		// nothing as of now

	}
	
}
