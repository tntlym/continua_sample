package org.continuaalliance.mcesl.utils;

/*
 * MessageFactory.java: Contains factory methods which are used to create APDU packets
 * 
 * @author: Vignet
 */

import org.openhealthtools.stepstone.phd.core.MdsObject;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_AnyDefinedBy;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_BITS16;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_BITS32;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_FloatType;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_Handle;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_INTU16;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_INTU32;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_INTU8;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_OIDType;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_Object;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_Octet_String_Var;
import org.openhealthtools.stepstone.phd.core.asn1.base.AbsoluteTime;
import org.openhealthtools.stepstone.phd.core.asn1.base.NullObject;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.AVA_Type;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.AareApdu;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.AbrtApdu;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.AbsoluteTimeAdjust;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ActionArgumentSimple;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ApduType;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.AttrValMap;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.AttributeIdList;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.AttributeList;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.AttributeModEntry;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.BasicNuObsValue;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.BasicNuObsValueComp;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.BatMeasure;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ConfigId;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ConfigReportRsp;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ConfigResult;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ConfirmMode;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.DataApdu;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.DataApduMessageChoice;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.DataProto;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.DataReqModeCapab;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.DataReqModeFlags;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.EnumObservedValue;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.EnumPrintableString;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.EventReportResultSimple;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.GetArgumentSimple;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.HandleAttrValMap;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.HandleList;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.HighResRelativeTime;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.InstNumber;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.MdsTimeInfo;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.MeasurementStatus;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.MetricIdList;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.MetricSpecSmall;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.MetricStructureSmall;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ModificationList;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ModifyOperator;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.NomPartition;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.NuObservedValue;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.NuObservedValueComp;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.OperationalState;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.PersonId;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.PhdAssociationInformation;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.PmSegmentEntryMap;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.PmStoreCapab;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.PowerStatus;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ProductionSpec;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.PrstApdu;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.RelativeTime;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ReleaseRequestReason;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.RlreApdu;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.RlrqApdu;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SaSpec;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ScaleRangeSpec16;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ScaleRangeSpec32;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ScaleRangeSpec8;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SegmentStatistics;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SetArgumentSimple;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SimpleNuObsValue;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SimpleNuObsValueComp;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.StoSampleAlg;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SupplementalTypeList;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SystemId;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SystemModel;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SystemType;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.Type;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.TypeVerList;
import org.openhealthtools.stepstone.phd.core.codes.EncodedDataTypeDefinitions;
import org.openhealthtools.stepstone.phd.encoding.IDecoderService;
import org.openhealthtools.stepstone.phd.encoding.mder.DecoderMDER;

import android.util.Log;


public class MessageFactory {

	public static ApduType createSetScannerCommand(int objHandle, short modifyOperator, int attrId, short value,int invokeId)
	{
		ApduType ret = null;
		ModifyOperator mOperator = new ModifyOperator(modifyOperator);
		OperationalState oState = new OperationalState(value);
		ASN_AnyDefinedBy aDb = new ASN_AnyDefinedBy(oState);
		AVA_Type attribute = new AVA_Type(new ASN_OIDType(attrId), aDb);
		AttributeModEntry ame = new AttributeModEntry(mOperator, attribute);
		ModificationList mList = new ModificationList();
		mList.addMember(ame);
		SetArgumentSimple sAs = new SetArgumentSimple(new ASN_Handle(objHandle), mList);
		DataApduMessageChoice dataMsg =
				new DataApduMessageChoice(	EncodedDataTypeDefinitions.ROIV_CMIP_CONFIRMED_SET_CHOSEN_INTU16,
						sAs);
		DataApdu dApdu = new DataApdu(new ASN_INTU16(invokeId), dataMsg);
		PrstApdu pApdu = new PrstApdu(dApdu.getBytes());
		ret = new ApduType(EncodedDataTypeDefinitions.PRST_CHOSEN_INTU16, pApdu);
		return ret;
	}
	public static ApduType getRejectApdu(int reason)
	{
		DataProto dp = new DataProto(new ASN_INTU16(ASN1Constants.DATA_PROTO_ID_EMPTY), null);
		AareApdu reject = new AareApdu(new ASN_INTU16(reason), dp);
		ApduType ret = new ApduType(EncodedDataTypeDefinitions.AARE_CHOSEN_INTU16, reject);
		return ret;
	}
	public static ApduType getAareApdu(long result)
	{
		ApduType retApdu = null;
		//ASN_BITS32 protocolVersion = new ASN_BITS32(-2147483648);
		ASN_BITS32 protocolVersion = new ASN_BITS32(0x80000000);		
		ASN_BITS16 encodingRule = new ASN_BITS16(32768);
		ASN_BITS32 NomenclatureV = new ASN_BITS32(-2147483648);
		ASN_BITS32 functionalUnit = new ASN_BITS32(0);
		SystemType sType = new SystemType(-2147483648);
		//SystemId sysID = new SystemId(ManagerInfo.Manager_Id);
		byte[] tempID = {(byte)0xFE,(byte)0xED,(byte)0xAB, (byte)0xEE, (byte)0xBE, (byte)0xAD, (byte)0xDE, (byte)0xEF};
		SystemId sysID = new SystemId(tempID);
		
		ConfigId configId = new ConfigId((short)ASN1Constants.CONF_ID_MANAGER_CONFIG_RESPONSE);
		DataReqModeCapab drmc = new DataReqModeCapab(new DataReqModeFlags((short)0),new ASN_INTU8(0),new ASN_INTU8(0));
		AttributeList aList = new AttributeList();
		PhdAssociationInformation phdAssoc = new PhdAssociationInformation(protocolVersion,
				encodingRule,NomenclatureV,functionalUnit,sType,
				sysID,configId,drmc,aList);


		DataProto toBSent = new DataProto(new ASN_INTU16(20601),new ASN_AnyDefinedBy(phdAssoc));
		AareApdu response = new AareApdu(new ASN_INTU16((int)result),toBSent);
		retApdu = new ApduType(EncodedDataTypeDefinitions.AARE_CHOSEN_INTU16, response);
		return retApdu;

	}
	public static ApduType getAbortMessage(ASN_INTU16 reason)
	{
		AbrtApdu abortMsg = new AbrtApdu(reason);
		ApduType apdu = new ApduType(EncodedDataTypeDefinitions.ABRT_CHOSEN_INTU16, abortMsg);
		return apdu;
	}
	public static ApduType getRlrqMessage_NORMAL()
	{
		RlrqApdu releaseReq = new RlrqApdu(ReleaseRequestReason.NORMAL);
		ApduType apdu = new ApduType(EncodedDataTypeDefinitions.RLRQ_CHOSEN_INTU16, releaseReq);
		return apdu;
	}
	public static ApduType createRlre(ASN_INTU16 reason)
	{
		RlreApdu rlre = new RlreApdu(reason);
		ApduType apdu = new ApduType(EncodedDataTypeDefinitions.RLRE_CHOSEN_INTU16, rlre);
		return apdu;
	}
	public static ApduType createResponseConfirmedEReport(DataApdu data,int event)
	{
		ASN_AnyDefinedBy any = new ASN_AnyDefinedBy(new NullObject());
		EventReportResultSimple result =
				new EventReportResultSimple(MdsObject.MDSOBJECT_HANDLE, new RelativeTime(0), new ASN_OIDType(event), any);
		ASN_INTU16 choice =
				EncodedDataTypeDefinitions.RORS_CMIP_CONFIRMED_EVENT_REPORT_CHOSEN_INTU16;
		DataApduMessageChoice dataMsg = new DataApduMessageChoice(choice, result);
		DataApdu dApdu = new DataApdu(data.getInvokeId(), dataMsg);
		PrstApdu pApdu = new PrstApdu(dApdu.getBytes());
		return new ApduType(EncodedDataTypeDefinitions.PRST_CHOSEN_INTU16, pApdu);

	}
	public static ApduType GetAllAttributes(ASN_INTU16 invokeId)
	{
		ApduType retVal = null;
		AttributeIdList attrIds = new AttributeIdList();
		GetArgumentSimple getArg = new GetArgumentSimple(MdsObject.MDSOBJECT_HANDLE, attrIds);
		DataApduMessageChoice dataMsg =
				new DataApduMessageChoice(	EncodedDataTypeDefinitions.ROIV_CMIP_GET_CHOSEN_INTU16,
						getArg);
		DataApdu dApdu = new DataApdu(invokeId, dataMsg);
		PrstApdu pApdu = new PrstApdu(dApdu.getBytes());
		retVal = new ApduType(EncodedDataTypeDefinitions.PRST_CHOSEN_INTU16, pApdu);
		return retVal;
	}
	public static Object getAttributeValueDecoded(int id, byte[] value)
	{
		Log.i("", "in getAttributeValueDecoded with id --> "+id);
		ASN_Object retValue = null;
		if(value!=null)
		{
			IDecoderService decoder = new DecoderMDER(value);
			switch (id){
			case Nomenclature.MDC_ATTR_CONFIRM_MODE :

				ConfirmMode cmode = new ConfirmMode(decoder) ;
				retValue = cmode;
				break;

			case Nomenclature.MDC_ATTR_CONFIRM_TIMEOUT : 

				RelativeTime rTime  = new RelativeTime(decoder);
				retValue = rTime;
				break;

			case Nomenclature.MDC_ATTR_SOURCE_HANDLE_REF :  
			case Nomenclature.MDC_ATTR_ID_HANDLE :
			{
				ASN_Handle val = new ASN_Handle(decoder);
				retValue = val;
			}
			break;

			case Nomenclature.MDC_ATTR_ID_INSTNO :
			{
				InstNumber val = new InstNumber(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_UNIT_LABEL_STRING :  
			case Nomenclature.MDC_ATTR_SYS_ID :  
			case Nomenclature.MDC_ATTR_SIMP_SA_OBS_VAL :  
			case Nomenclature.MDC_ATTR_ID_LABEL_STRING :
			case Nomenclature.MDC_ATTR_PM_STORE_LABEL_STRING :
			case Nomenclature.MDC_ATTR_PM_SEG_LABEL_STRING :  
			{
				ASN_Octet_String_Var val = new ASN_Octet_String_Var(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_ID_MODEL : 
			{
				SystemModel val = new SystemModel(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_OID :  
			case Nomenclature.MDC_ATTR_UNIT_CODE:  
			case Nomenclature.MDC_ATTR_ID_PHYSIO : 
			{
				ASN_OIDType val = new ASN_OIDType(decoder);

				retValue = val;
			}

			break;
			case Nomenclature.MDC_ATTR_ID_PROD_SPECN : 
			{
				ProductionSpec val = new ProductionSpec(decoder);

				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_SYS_TYPE :  
			case Nomenclature.MDC_ATTR_ID_TYPE : 
			{
				Type val = new Type(decoder);

				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_METRIC_STORE_CAPAC_CNT :
			case Nomenclature.MDC_ATTR_METRIC_STORE_USAGE_CNT :
			case Nomenclature.MDC_ATTR_SEG_USAGE_CNT :  
			{

				ASN_INTU32 val = new ASN_INTU32(decoder);

				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_METRIC_STORE_SAMPLE_ALG : 
			{
				StoSampleAlg val = new StoSampleAlg(decoder);

				retValue = val;
			}
			break;

			case Nomenclature.MDC_ATTR_MSMT_STAT : 
			{
				MeasurementStatus val = new MeasurementStatus(decoder);

				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_TIME_PD_MSMT_ACTIVE : 
			case Nomenclature.MDC_ATTR_NU_ACCUR_MSMT :  
			{
				ASN_FloatType val = new ASN_FloatType(decoder);

				retValue = val;
			}		
			break;
			case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS :  
			{
				NuObservedValueComp val = new NuObservedValueComp(decoder);

				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_NU_VAL_OBS :  
			{
				NuObservedValue val = new NuObservedValue(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_VAL_BATT_CHARGE : 
			case Nomenclature.MDC_ATTR_TX_WIND :
			case Nomenclature.MDC_ATTR_NUM_SEG :  
			{

				ASN_INTU16 val = new ASN_INTU16(decoder);

				retValue = val;
			}
			break;

			case Nomenclature.MDC_ATTR_OP_STAT :  
			{
				OperationalState val =  new OperationalState(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_POWER_STAT :  
			{
				PowerStatus val = new PowerStatus(decoder);

				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_SA_SPECN : 
			{
				SaSpec val = new SaSpec(decoder);

				retValue = val;
			}

			break;
			case Nomenclature.MDC_ATTR_SCALE_SPECN_I16 : 
			{
				ScaleRangeSpec16 val = new ScaleRangeSpec16(decoder);

				retValue = val;
			}		
			break;
			case Nomenclature.MDC_ATTR_SCALE_SPECN_I32 : 
			{
				ScaleRangeSpec32 val = new ScaleRangeSpec32(decoder);

				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_SCALE_SPECN_I8 :
			{
				ScaleRangeSpec8 val = new ScaleRangeSpec8(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_SCAN_REP_PD :  
			case Nomenclature.MDC_ATTR_TIME_PD_SAMP :
			case Nomenclature.MDC_ATTR_TIME_REL :
			case Nomenclature.MDC_ATTR_TIME_STAMP_REL :
			case Nomenclature.MDC_ATTR_SCAN_REP_PD_MIN :
			case Nomenclature.MDC_ATTR_CLEAR_TIMEOUT :  
			case Nomenclature.MDC_ATTR_TRANSFER_TIMEOUT : 
			{
				RelativeTime val = new RelativeTime(decoder);
				retValue = val;
			}
			break;

			case Nomenclature.MDC_ATTR_TIME_BATT_REMAIN :  
			{
				BatMeasure val = new BatMeasure(decoder);

				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_TIME_STAMP_ABS : 
			case Nomenclature.MDC_ATTR_TIME_START_SEG :
			case Nomenclature.MDC_ATTR_TIME_ABS :  
			case Nomenclature.MDC_ATTR_TIME_END_SEG :
			{
				AbsoluteTime val = new AbsoluteTime(decoder); 

				retValue = val;
			}
			break;

			case Nomenclature.MDC_ATTR_VAL_ENUM_OBS :  
			{
				EnumObservedValue val = new EnumObservedValue(decoder);

				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_TIME_STAMP_REL_HI_RES :  
			case Nomenclature.MDC_ATTR_TIME_REL_HI_RES :  
			{
				HighResRelativeTime val = new HighResRelativeTime(decoder); 

				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_DEV_CONFIG_ID:  
			{
				ConfigId val = new ConfigId(decoder);

				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_MDS_TIME_INFO :  
			{
				MdsTimeInfo val = new MdsTimeInfo(decoder);

				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_METRIC_SPEC_SMALL:  
			{
				MetricSpecSmall val = new MetricSpecSmall(decoder);

				retValue = val;
			}
			break;

			case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_STR :  
			{
				EnumPrintableString val = new EnumPrintableString(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_REG_CERT_DATA_LIST :  
			{
				/*RegCertDataList val = new RegCertDataList(decoder);
				retValue = val;*/
			}
			break;
			case Nomenclature.MDC_ATTR_NU_VAL_OBS_BASIC :  
			{
				BasicNuObsValue val = new BasicNuObsValue(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_PM_STORE_CAPAB :  
			{
				PmStoreCapab val = new PmStoreCapab(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_PM_SEG_MAP :  
			{
				PmSegmentEntryMap val = new PmSegmentEntryMap(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_PM_SEG_PERSON_ID :  
			{
				PersonId val = new PersonId(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_SEG_STATS :  
			{
				SegmentStatistics val = new SegmentStatistics(decoder);

				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_SCAN_HANDLE_ATTR_VAL_MAP :  ;
			{
				HandleAttrValMap val = new HandleAttrValMap(decoder);	
				retValue = val;
			}
			break;

			case Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP:  
			{
				AttrValMap val = new AttrValMap(decoder);

				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_NU_VAL_OBS_SIMP :  
			{

				SimpleNuObsValue val = new SimpleNuObsValue(decoder); 
				retValue = val;
			}
			break;

			case Nomenclature.MDC_ATTR_SYS_TYPE_SPEC_LIST:  
			{
				TypeVerList val = new TypeVerList(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_METRIC_ID_PART : 
			case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_PART :  
			{
				NomPartition val = new NomPartition(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_SUPPLEMENTAL_TYPES :  
			{
				SupplementalTypeList val = new SupplementalTypeList(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_TIME_ABS_ADJUST :  
			{
				AbsoluteTimeAdjust val = new AbsoluteTimeAdjust(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR :  
			{

				ASN_BITS32 val = new ASN_BITS32(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_BASIC_BIT_STR :  
			{
				ASN_BITS16 val = new ASN_BITS16(decoder);

				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_METRIC_STRUCT_SMALL :  
			{
				MetricStructureSmall val = new MetricStructureSmall(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_SIMP :  
			{
				SimpleNuObsValueComp val = new SimpleNuObsValueComp(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_BASIC :  
			{
				BasicNuObsValueComp val = new BasicNuObsValueComp(decoder);	
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_ID_PHYSIO_LIST :  
			{
				//ASN_OIDType val = new ASN_OIDType(decoder);
				MetricIdList val = new MetricIdList(decoder);
				retValue = val;
			}
			break;
			case Nomenclature.MDC_ATTR_SCAN_HANDLE_LIST :  
			{
				HandleList val = new HandleList(decoder);

				retValue = val;
			}
			break;
			}
			
			
		}
		Log.i("", "in getAttributeValueDecoded end");
		return retValue;

	}
	static public ConfigReportRsp createConfigResponse(ConfigId id, short result)
	{
		return new ConfigReportRsp(id, new ConfigResult(result));
	}
	public static ApduType GetActionArgumentApdu(	ASN_INTU16 invokeId,
			ASN_Handle handle,
			ASN_OIDType actionType,
			ASN_AnyDefinedBy actionInfoArgs) 
	{
		ActionArgumentSimple actionArg =
				new ActionArgumentSimple(handle, actionType, actionInfoArgs);
		DataApduMessageChoice dataMsg =
				new DataApduMessageChoice(	EncodedDataTypeDefinitions.ROIV_CMIP_CONFIRMED_ACTION_CHOSEN_INTU16,
						actionArg);
		DataApdu dApdu = new DataApdu(invokeId, dataMsg);
		PrstApdu pApdu = new PrstApdu(dApdu.getBytes());
		return new ApduType(EncodedDataTypeDefinitions.PRST_CHOSEN_INTU16, pApdu);
	}
	
}
