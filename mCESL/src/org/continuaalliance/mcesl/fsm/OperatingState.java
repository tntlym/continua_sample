package org.continuaalliance.mcesl.fsm;

import java.util.ArrayList;
import org.continuaalliance.mcesl.Event.Event;
import org.continuaalliance.mcesl.appData.AppPMSegment;
import org.continuaalliance.mcesl.appData.AppScannerObject;
import org.continuaalliance.mcesl.appData.EventObject;
import org.continuaalliance.mcesl.appData.MeasurementEvent;
import org.continuaalliance.mcesl.measurement.Measurement;
import org.continuaalliance.mcesl.utils.ASN1Constants;
import org.continuaalliance.mcesl.utils.MessageFactory;
import org.continuaalliance.mcesl.utils.Nomenclature;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_AnyDefinedBy;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_Handle;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_INTU16;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ActionResultSimple;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ApduType;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.DataApdu;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.DataApduMessageChoice;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.EventReportArgumentSimple;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.GetResultSimple;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.InstNumber;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ObservationScanFixedSequence;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.PrstApdu;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ScanReportInfoFixed;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ScanReportInfoMPFixed;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ScanReportInfoVar;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ScanReportPerFixed;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ScanReportPerFixedSequence;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SegmSelection;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SegmentDataEvent;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.TrigSegmDataXferReq;
import org.openhealthtools.stepstone.phd.core.codes.EncodedDataTypeDefinitions;
import org.openhealthtools.stepstone.phd.core.codes.NomenclatureCodes;
import org.openhealthtools.stepstone.phd.encoding.mder.DecoderMDER;

import android.util.Log;

/*
 * OperatingState.java: A class which represents Operating state of the manager
 * 							
 * @author: Vignet
 */
public class OperatingState extends State {

	/**
	 * Function which processes the input APDU data packet   
	 * @param ApduType from agent to be processed
	 */
	
	@Override
	public void ProcessData(ApduType DataRcvd) {
		Log.i("Operating","Operating state -->"+DataRcvd.isPRST_CHOSEN());
		Log.i("Operating","Operating state -->"+DataRcvd.isAARQ_CHOSEN());
		Log.i("Operating","Operating state -->"+DataRcvd.isAARE_CHOSEN());
		Log.i("Operating","Operating state -->"+DataRcvd.isABRT_CHOSEN());
		Log.i("Operating","Operating state -->"+DataRcvd.isRLRE_CHOSEN());
		Log.i("Operating","Operating state -->"+DataRcvd.isRLRQ_CHOSEN());
		// Ideally should only receive PrstApdu
		if(DataRcvd.isPRST_CHOSEN())
		{
			PrstApdu prstApdu = (PrstApdu) DataRcvd.getChoiceObject();
			DataApdu dataApdu = new DataApdu(new DecoderMDER(prstApdu.getOctetString()));

			
			DataApduMessageChoice choice = dataApdu.getMessage();
			switch(choice.getChoiceTag().getValue())
			{
			case EncodedDataTypeDefinitions.ROIV_CMIP_CONFIRMED_EVENT_REPORT_CHOSEN:
			{
				Log.i("","ROIV_CMIP_CONFIRMED_EVENT_REPORT_CHOSEN");
				EventReportArgumentSimple eras = (EventReportArgumentSimple)choice.getChoiceObject();
				switch((int)eras.getEventType().getValue())
				{
				case Nomenclature.MDC_NOTI_CONFIG:
					System.out.println("MDC_NOTI_CONFIG");
					break;
				case Nomenclature.MDC_NOTI_SCAN_REPORT_VAR:
					System.out.println("MDC_NOTI_SCAN_REPORT_VAR");
					perform_MDC_NOTI_SCAN_REPORT_VAR(eras);
					ApduType responseER = MessageFactory.
					createResponseConfirmedEReport(
							dataApdu,NomenclatureCodes.MDC_NOTI_SCAN_REPORT_FIXED);
					manager_state_controller.addToWriteQueue(responseER);
					break;
				case Nomenclature.MDC_NOTI_SCAN_REPORT_FIXED:
					System.out.println("MDC_NOTI_SCAN_REPORT_FIXED");
					perform_MDC_NOTI_SCAN_REPORT_FIXED(eras);
					ApduType responseFixed = MessageFactory.
					createResponseConfirmedEReport(
							dataApdu,NomenclatureCodes.MDC_NOTI_SCAN_REPORT_FIXED);
					manager_state_controller.addToWriteQueue(responseFixed);
					break;
				case Nomenclature.MDC_NOTI_SCAN_REPORT_MP_VAR: 
					System.out.println("MDC_NOTI_SCAN_REPORT_MP_VAR");
					break;
				case Nomenclature.MDC_NOTI_SCAN_REPORT_MP_FIXED:
					perform_MDC_NOTI_SCAN_REPORT_MP_FIXED(eras);
					ApduType response = MessageFactory.
					createResponseConfirmedEReport(
							dataApdu,NomenclatureCodes.MDC_NOTI_SCAN_REPORT_MP_FIXED);
					System.out.println("MDC_NOTI_SCAN_REPORT_MP_FIXED");
					manager_state_controller.addToWriteQueue(response);
					Log.i("","MDC_NOTI_SCAN_REPORT_MP_FIXED respnse sent");					
					break;
					
				case Nomenclature.MDC_NOTI_SEGMENT_DATA: //this is for segment data transfer.
					System.out.println("MDC_NOTI_SEGMENT_DATA");
					perform_MDC_NOTI_SEGMENT_DATA(eras);
				}	
			}
			break;
			case EncodedDataTypeDefinitions.ROIV_CMIP_EVENT_REPORT_CHOSEN:
				Log.i("","ROIV_CMIP_EVENT_REPORT_CHOSEN");

				EventReportArgumentSimple eras = (EventReportArgumentSimple)choice.getChoiceObject();

				switch((int)eras.getEventType().getValue())
				{
				case Nomenclature.MDC_NOTI_CONFIG:

					System.out.println("MDC_NOTI_CONFIG");
					break;
				case Nomenclature.MDC_NOTI_SCAN_REPORT_VAR:
					System.out.println("MDC_NOTI_SCAN_REPORT_VAR");
					perform_MDC_NOTI_SCAN_REPORT_VAR(eras);
					break;
				case Nomenclature.MDC_NOTI_SCAN_REPORT_FIXED:
					System.out.println("MDC_NOTI_SCAN_REPORT_FIXED");
					perform_MDC_NOTI_SCAN_REPORT_FIXED(eras);
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
			case EncodedDataTypeDefinitions.ROIV_CMIP_SET_CHOSEN:
				Log.i("","ROIV_CMIP_SET_CHOSEN");
				break;
			case EncodedDataTypeDefinitions.ROIV_CMIP_CONFIRMED_SET_CHOSEN:
				Log.i("","ROIV_CMIP_CONFIRMED_SET_CHOSEN");
				break;
			case EncodedDataTypeDefinitions.ROIV_CMIP_ACTION_CHOSEN:
				Log.i("","ROIV_CMIP_ACTION_CHOSEN");
				break;
			case EncodedDataTypeDefinitions.ROIV_CMIP_CONFIRMED_ACTION_CHOSEN:
				Log.i("","ROIV_CMIP_CONFIRMED_ACTION_CHOSEN");
				//Control should come here for PM Store  
				//ActionArgumentSimple aaSimple = (ActionArgumentSimple) choice.getChoiceObject();
				//perform_ROIV_CMIP_CONFIRMED_ACTION_CHOSEN(aaSimple);
				break;
			case EncodedDataTypeDefinitions.RORS_CMIP_CONFIRMED_EVENT_REPORT_CHOSEN:
				Log.i("","RORS_CMIP_CONFIRMED_EVENT_REPORT_CHOSEN");
				break;
			case EncodedDataTypeDefinitions.RORS_CMIP_GET_CHOSEN:
				Log.i("","RORS_CMIP_GET_CHOSEN in operating state - jack shephard");
				GetResultSimple grSimple = (GetResultSimple)choice.getChoiceObject();
				GetAttributeValuesFromResponse(grSimple);
				break;
			case EncodedDataTypeDefinitions.RORS_CMIP_CONFIRMED_SET_CHOSEN:
				Log.i("","RORS_CMIP_CONFIRMED_SET_CHOSEN");
				break;
			case EncodedDataTypeDefinitions.RORS_CMIP_CONFIRMED_ACTION_CHOSEN:
				Log.i("","RORS_CMIP_CONFIRMED_ACTION_CHOSEN");
				ActionResultSimple aRSimple = (ActionResultSimple) choice.getChoiceObject();
				perform_RORS_CMIP_CONFIRMED_ACTION_CHOSEN(aRSimple);
				break;
			case EncodedDataTypeDefinitions.ROER_CHOSEN:
				Log.i("","ROER_CHOSEN");
				break;
			case EncodedDataTypeDefinitions.RORJ_CHOSEN:
			default:
				Log.i("","RORJ_CHOSEN");
			}

		}
		else if(DataRcvd.isRLRQ_CHOSEN())
		{
			manager_state_controller.sendMessageToManager(Event.ADD_TO_OUT_QUEUE,MessageFactory.createRlre(EncodedDataTypeDefinitions.RELEASE_RESPONSE_REASON_NORMAL_INTU16));
			manager_state_controller.ChangeState(StateConstants.UNASSOCIATED);
		}
		else if(DataRcvd.isRLRE_CHOSEN() || DataRcvd.isAARQ_CHOSEN() ||
				DataRcvd.isAARE_CHOSEN() )
		{
			manager_state_controller.sendMessageToManager(Event.ADD_TO_OUT_QUEUE,MessageFactory.getAbortMessage(new ASN_INTU16(ASN1Constants.ABRT_RE_UNDEFINED)));
			manager_state_controller.ChangeState(StateConstants.UNASSOCIATED);
			System.out.println("RLRE, AARQ, AARE recvd");
		}
		else if(DataRcvd.isABRT_CHOSEN())
		{
			manager_state_controller.ChangeState(StateConstants.UNASSOCIATED);
		}
	}
	
	/**
	 * Handles MDC_NOTI_SEGMENT_DATA event type of PMStore object
	 * @param eras EventReportArgumentSimple 
	 */
	private void perform_MDC_NOTI_SEGMENT_DATA(EventReportArgumentSimple eras)
	{
		Log.i("","perform_MDC_NOTI_SEGMENT_DATA start");
		SegmentDataEvent sDEvent = (SegmentDataEvent) eras.getEventInfo().getAnyDefinedByObject();
		byte[] segData = sDEvent.getSegmDataEventEntries().getOctetString();
		String sSegData = "";
		for(int i = 0; i <segData.length; i++)
		{
			sSegData = String.format("%s %2x", sSegData, segData[i]);
		}
		Log.i("","Segment data received - \n"+sSegData);
		Log.i("","perform_MDC_NOTI_SEGMENT_DATA end");
	}
	
	/**
	 * Handles MDC_NOTI_SCAN_REPORT_MP_FIXED event type of MDS object
	 * @param confirmedReport EventReportArgumentSimple
	 */
	@SuppressWarnings("unchecked")
	private void perform_MDC_NOTI_SCAN_REPORT_MP_FIXED(EventReportArgumentSimple confirmedReport)
	{
		ScanReportInfoMPFixed sRIMPF = (ScanReportInfoMPFixed)confirmedReport.getEventInfo().
										getAnyDefinedByObject();
		ArrayList<Measurement> retVal = null;
		ScanReportPerFixedSequence seq = sRIMPF.getScanPerFixed();
		Measurement measurement = null;
		for(int i = 0; i< seq.getCount(); i++)
		{
			ScanReportPerFixed srpf = seq.getMember(i);
			ObservationScanFixedSequence oSeq = srpf.getObsScanFixed();
			
			measurement = manager_state_controller.getAgentMDS().MDS_Dynamic_Data_Update_MP_Fixed(oSeq.getMembers());
			measurement.setPersonId(srpf.getPersonId().getValue());
			measurement.setSpecialization(manager_state_controller.getDeviceDataType());
			if(retVal == null)
			{
				retVal = new ArrayList<Measurement>();
			}
			retVal.add(measurement);
		}
		
		Log.i("","RCVD arraylist with count - "+retVal.size());
		EventObject eObject = new EventObject(retVal, manager_state_controller.getAgentMDS().getConfigurationType());
		MeasurementEvent mEvent = new MeasurementEvent(manager_state_controller.getDeviceDataType(),eObject);
		manager_state_controller.sendMessageToManager(Event.MEASUREMENT_RECVD,(Object)mEvent);
	}
	
	/**
	 * Handles GET Segment info result 
	 * @param aRSimple ActionResultSimple
	 */
	private void perform_RORS_CMIP_CONFIRMED_ACTION_CHOSEN(ActionResultSimple aRSimple)
	{
		
		short actionType = aRSimple.getActionType().getValue();	
		if(actionType == NomenclatureCodes.MDC_ACT_SEG_GET_INFO)
		{
		// store each segment info in respective PM store object.
			
			ArrayList<AppPMSegment> retObject = manager_state_controller.getAgentMDS().Get_PM_Store_Info(aRSimple);
			Log.i("", "Sending Segment info to app");
			manager_state_controller.sendMessageToManager(Event.SEGMENT_INFO_RECEIVED, retObject);
			
			
		}
		else if(actionType == NomenclatureCodes.MDC_ACT_SEG_TRIG_XFER)
		{
			Log.i("","MDC_ACT_SEG_TRIG_XFER");
			manager_state_controller.getAgentMDS().ProcessTrigSegmDataXferRsp(aRSimple);
		}
	}
	
	/**
	 * Handles MDC_NOTI_SCAN_REPORT_FIXED event of MDS object.
	 * @param confirmedReport EventReportArgumentSimple
	 */
	@SuppressWarnings("unchecked")
	private void perform_MDC_NOTI_SCAN_REPORT_FIXED(EventReportArgumentSimple confirmedReport)
	{
		ArrayList<Measurement> retVal = null;
		ScanReportInfoFixed sRIF = (ScanReportInfoFixed)confirmedReport.getEventInfo().getAnyDefinedByObject();
		ObservationScanFixedSequence obsSeq = sRIF.getObsScanFixed();
		Log.i("", "Obs len-->"+obsSeq.getCount());
		Measurement measurementObj = null;
		measurementObj = manager_state_controller.getAgentMDS().MDS_Dynamic_Data_Update_Fixed(obsSeq.getMembers());//getMeasurements(obsSeq.getMembers());
		measurementObj.setPersonId(-1);
		measurementObj.setSpecialization(manager_state_controller.getDeviceDataType());
		if(retVal == null)
		{
			retVal = new ArrayList<Measurement>();
		}
		retVal.add(measurementObj);
		EventObject eObject = new EventObject(retVal, manager_state_controller.getAgentMDS().getConfigurationType());
		MeasurementEvent mEvent = new MeasurementEvent(manager_state_controller.getDeviceDataType(),eObject);
		manager_state_controller.sendMessageToManager(Event.MEASUREMENT_RECVD,(Object)mEvent);
	}
	
	/**
	 * Handles MDC_NOTI_SCAN_REPORT_VAR event of MDS object.
	 * @param confirmedReport EventReportArgumentSimple
	 */
	private void perform_MDC_NOTI_SCAN_REPORT_VAR(EventReportArgumentSimple confirmedReport)
	{
		ScanReportInfoVar sRIV = (ScanReportInfoVar) confirmedReport.getEventInfo().getAnyDefinedByObject();
		Measurement obj = manager_state_controller.getAgentMDS().MDS_Dynamic_Data_Update_Var(sRIV);
		obj.setSpecialization(manager_state_controller.getDeviceDataType());
		ArrayList<Measurement> mList = new ArrayList<Measurement>();
		mList.add(obj);
		EventObject eObject = new EventObject(mList, manager_state_controller.getAgentMDS().getConfigurationType());
		MeasurementEvent mEvent = new MeasurementEvent(manager_state_controller.getDeviceDataType(),eObject);
		manager_state_controller.sendMessageToManager(Event.MEASUREMENT_RECVD,(Object)mEvent);		
	}
	
	/**
	 * 
	 * This gets data from PM store of the agent device.
	 * The important thing here to check if there is any PM Store 
	 * object available in Agent Configuration. 
	 * This is kept as private so that only operating state can be used
	 * 
	 */
	
	private void getPMStoreSegmentInfo(ASN_Handle handleOfPmObject)
	{
		// First request all the PM Segment info.
		Log.i("","getPMStoreSegmentInfo start");
		SegmSelection segmSelection =
				new SegmSelection(	EncodedDataTypeDefinitions.ALL_SEGMENTS_CHOSEN_INTU16,
									new ASN_INTU16(0));
			ASN_AnyDefinedBy actionInfoArgs = new ASN_AnyDefinedBy(segmSelection);
			ApduType actionArgSimple = MessageFactory.GetActionArgumentApdu(new ASN_INTU16(invokeid++),
								handleOfPmObject,
								NomenclatureCodes.MDC_ACT_SEG_GET_INFO_OID_Type,
								actionInfoArgs);
			manager_state_controller.sendMessageToManager(Event.ADD_TO_OUT_QUEUE,actionArgSimple);
		Log.i("","getPMStoreSegmentInfo end");
		
	}
	
	private void getPMSegmentData(ASN_Handle handleOfPmObject,InstNumber instanceNo)
	{
		Log.i("","getPMSegmentData start");
		TrigSegmDataXferReq trigSegmDataXferReq = new TrigSegmDataXferReq(instanceNo);
		ASN_AnyDefinedBy actionInfoArgs = new ASN_AnyDefinedBy(trigSegmDataXferReq);
		ApduType actionArgSimple = MessageFactory.GetActionArgumentApdu(new ASN_INTU16(invokeid++),
				handleOfPmObject,
				NomenclatureCodes.MDC_ACT_SEG_TRIG_XFER_OID_Type,
				actionInfoArgs);
		Log.i("","Sending MDC_ACT_SEG_TRIG_XFER_OID_Type");
		manager_state_controller.sendMessageToManager(Event.ADD_TO_OUT_QUEUE,actionArgSimple);
		Log.i("","getPMSegmentData end");
	}
	
	/**
	 * Sets the operational state of the scanner.
	 * @param scanner AppScannerObject
	 */
	private void enableScannerObject(AppScannerObject scanner)
	{
		ApduType value = MessageFactory.createSetScannerCommand(scanner.handle,(short)0, Nomenclature.MDC_ATTR_OP_STAT,(short) 1,invokeid++);
		Log.i("","Sending Set Command");
		manager_state_controller.sendMessageToManager(Event.ADD_TO_OUT_QUEUE,value);
		Log.i("","Set Command sent");
	}
	/**
	 * Single parameter Constructor   
	 * @param DeviceManager object which controls the state machine
	 */
	public OperatingState (final DeviceManager state_controller)
	{
		manager_state_controller  = state_controller;
		stateName = "Operating";
		state = StateConstants.OPERATING;
		
	}
	@Override
	public void ProcessEvent(int event) {

		switch(event)
		{
		case Event.TRANSPORT_DISCONNECTED:
			break;
		case Event.ABORT_FROM_APP:
			sendUnknownAbort();
			break;
		case Event.UNASSOCIATE_FROM_APP:
			sendUnassociate_Normal();
			break;
		case Event.GET_REQUEST_FROM_APP:
			sendGetAllAttributes();
			break;
				
		}
	}
	public void ProcessEventWithObject(int event, Object obj)
	{
		Log.i("","Operating state ProcessEventWithObject called");
		switch(event)
		{
		case Event.GET_PM_STORE_SEGMENTS_INFO:
			ASN_Handle handle = new ASN_Handle((Short)obj);
			getPMStoreSegmentInfo(handle);
			break;
		case Event.GET_PM_SEGMENT_DATA:
			AppPMSegment segment = (AppPMSegment) obj;
			getPMSegmentData(new ASN_Handle(segment.pmStoreHandle), new InstNumber(segment.instanceNo));
			break;
		case Event.ENABLE_SCANNER:
			AppScannerObject sObject = (AppScannerObject)obj;
			enableScannerObject(sObject);
			break;
		}
	}

}
