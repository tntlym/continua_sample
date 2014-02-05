package org.continuaalliance.mcesl.dim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.continuaalliance.mcesl.appData.AppMDSObject;
import org.continuaalliance.mcesl.appData.AppNumeric;
import org.continuaalliance.mcesl.appData.AppPMSegment;
import org.continuaalliance.mcesl.appData.AppPMStore;
import org.continuaalliance.mcesl.appData.AppScannerObject;
import org.continuaalliance.mcesl.measurement.DeviceMeasurement;
import org.continuaalliance.mcesl.measurement.Measurement;
import org.continuaalliance.mcesl.measurement.Reading;
import org.continuaalliance.mcesl.utils.Constants;
import org.continuaalliance.mcesl.utils.MessageFactory;
import org.continuaalliance.mcesl.utils.Nomenclature;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_AnyDefinedBy;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_BITS16;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_BITS32;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_Handle;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_OIDType;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.AVA_Type;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ActionResultSimple;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.AttrValMap;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.AttrValMapEntry;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.AttributeList;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.BasicNuObsValue;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.BasicNuObsValueComp;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ConfigObject;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ConfigObjectList;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ConfigReport;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ConfigReportRsp;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ConfigResult;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.EnumPrintableString;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.EventReportArgumentSimple;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.GetResultSimple;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.InstNumber;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.MetricIdList;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ObservationScan;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ObservationScanFixed;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ObservationScanSequence;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.OperationalState;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ScanReportInfoMPVar;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ScanReportInfoVar;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ScanReportPerVar;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ScanReportPerVarSequence;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SegmentInfo;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SegmentInfoList;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SimpleNuObsValue;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SimpleNuObsValueComp;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.TrigSegmDataXferRsp;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.TrigSegmXferRsp;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.Type;
import org.openhealthtools.stepstone.phd.encoding.mder.DecoderMDER;

import android.util.Log;

/*
 * This class specifies the MDS attributes and its events. 
 */

public abstract class MDSObject extends DIMObject{

	protected Hashtable<Long, Numeric> numericObject;
	protected Hashtable<Long, Enumeration> enumObject = null;
	protected Hashtable<Long, PMStore> pMObject = null;
	protected Hashtable<Long, EpisodicScanner> eScanner = null;
	protected Hashtable<Long, PeriodicScanner> pScanner = null;
	protected short specializationValue = -1;
	protected int configurationType = Constants.STANDARD_CONFIGURATION;
	protected short config_id;

	public void setSpecilization(short specialization)
	{
		specializationValue = specialization;
	}
	public short getSpecialization()
	{
		return specializationValue;
	}
	public void setConfigId(short configId)
	{
		config_id = configId;
	}
	public short getConfigID()
	{
		return config_id;
	}
	public int getConfigurationType()
	{
		return configurationType;
	}
	//Below function is for PMStore objects only.
	public ArrayList<AppPMSegment> Get_PM_Store_Info(ActionResultSimple aaSimple)
	{
		Log.i("", "Get_PM_Store_Info in");
		ArrayList<AppPMSegment> pmSegments = null;
		AppPMSegment segment = null;
		ASN_Handle pmObjectHandle = aaSimple.getObjHandle();
		PMStore dimObj =(PMStore)getDIMObject((long)pmObjectHandle.getValue());
		ASN_AnyDefinedBy aDefinedType = aaSimple.getActionInfoArgs();
		SegmentInfoList sInfoList = (SegmentInfoList)aDefinedType.getAnyDefinedByObject();
		SegmentInfo element = null;
		for(int i = 0; i< sInfoList.getCount(); i++)
		{				
			element = sInfoList.getMember(i);	
			dimObj.addSegmentInfo(element);

			if(pmSegments == null)
			{
				pmSegments = new ArrayList<AppPMSegment>();
			}
			segment = new AppPMSegment();
			segment.instanceNo = element.getSegInstNo().getValue();
			Log.i("", "instnace no-->"+segment.instanceNo);
			segment.pmStoreHandle = pmObjectHandle.getValue();
			Log.i("", "segment.pmStoreHandle-->"+segment.pmStoreHandle);
			pmSegments.add(segment);

			AttributeList list = element.getSegInfo();
			for(int k = 0;k<list.getCount();k++)
			{
				AVA_Type type = list.getMember(k);
				Log.i("","Attribute ID->"+type.getAttributeId());
				Log.i("","Attribute Value->"+type.getAttributeValue());
			}
		}
		Log.e("", "Get_PM_Store_Info out");
		return pmSegments;
	}
	public void ProcessTrigSegmDataXferRsp(ActionResultSimple aRSimple)
	{
		Log.i("", "ProcessTrigSegmDataXferRsp start");
		ASN_AnyDefinedBy aDefinedType = aRSimple.getActionInfoArgs();
		TrigSegmDataXferRsp rsp = (TrigSegmDataXferRsp) aDefinedType.getAnyDefinedByObject();
		InstNumber iNumber  = rsp.getSegInstNo();
		Log.i("", "Instance No->"+iNumber.getValue());
		TrigSegmXferRsp tsXfR = rsp.getTrigSegmXferRsp();
		Log.i("", "TrigSegmXferRsp value->"+tsXfR.getValue());		
		Log.i("", "ProcessTrigSegmDataXferRsp end");

	}

	public MDSObject(Hashtable<Long,Attribute> map)
	{
		super(map);
	}
	public Boolean getAttributesResponse(GetResultSimple grs)
	{
		Boolean retValue = false;
		byte[] attribVal = null;
		if(grs != null)
		{
			AttributeList aList = grs.getAttributeList();
			for(int i = 0; i<aList.getCount(); i++)
			{
				AVA_Type member = aList.getMember(i);
				int id = member.getAttributeId().getValue();		
				if(member.getAttributeValue() != null)
				{
					attribVal = member.getAttributeValue().getAnyDefinedByObject().getBytes();
				}
				org.continuaalliance.mcesl.dim.Attribute attrib = new 
						org.continuaalliance.mcesl.dim.Attribute(id,
								MessageFactory.getAttributeValueDecoded(id, 
										attribVal));
				attributeList.put((long)id, attrib);
			}
			retValue = true;
		}
		return retValue;
	}
	public void addPMStoreValue (PMStore pmData){
		if(pMObject == null)
		{
			Log.i("","pMObject was null");
			pMObject = new Hashtable<Long, PMStore>();
		}
		if(pmData == null)
		{
			Log.i("","Numericdata is also null");
		}
		ASN_Handle handle = (ASN_Handle)pmData.getAttribute(Nomenclature.MDC_ATTR_ID_HANDLE).getMainAttribute();
		Log.i("","Putting PM with key -->" +handle.getValue());
		pMObject.put((long)handle.getValue(), pmData);
	}
	public void addEpisodicScannerValue(EpisodicScanner scanner)
	{
		if(eScanner == null)
		{
			Log.i("","eScanner was null");
			eScanner = new Hashtable<Long, EpisodicScanner>();
		}
		ASN_Handle handle = (ASN_Handle)scanner.getAttribute(Nomenclature.MDC_ATTR_ID_HANDLE).getMainAttribute();
		Log.i("","Putting PM with key -->" +handle.getValue());
		eScanner.put((long)handle.getValue(), scanner);
	}
	public void addPeriodicScannerValue(PeriodicScanner scanner)
	{
		if(pScanner == null)
		{
			Log.e("","pScanner was null");
			pScanner = new Hashtable<Long, PeriodicScanner>();
		}
		ASN_Handle handle = (ASN_Handle)scanner.getAttribute(Nomenclature.MDC_ATTR_ID_HANDLE).getMainAttribute();
		Log.i("","Putting PM with key -->" +handle.getValue());
		pScanner.put((long)handle.getValue(), scanner);
	}
	public void addEnumValue (Enumeration enumData){
		if(enumObject == null)
		{
			Log.i("","NumericObject was null");
			enumObject = new Hashtable<Long, Enumeration>();
		}
		if(enumData == null)
		{
			Log.i("","Numericdata is also null");
		}
		ASN_Handle handle = (ASN_Handle)enumData.getAttribute(Nomenclature.MDC_ATTR_ID_HANDLE).getMainAttribute();
		Log.i("","Putting numeric with key -->" +handle.getValue());
		enumObject.put((long)handle.getValue(), enumData);
	}
	public void addNumericValue (Numeric numericData){
		if(numericObject == null)
		{
			Log.e("","NumericObject was null");
			numericObject = new Hashtable<Long, Numeric>();
		}
		if(numericData == null)
		{
			Log.e("","Numericdata is also null");
		}
		ASN_Handle handle = (ASN_Handle)numericData.getAttribute(Nomenclature.MDC_ATTR_ID_HANDLE).getMainAttribute();
		Log.i("","Putting numeric with key -->" +handle.getValue());
		numericObject.put((long)handle.getValue(), numericData);
	}
	@SuppressWarnings("unchecked")
	protected Measurement getMeasurementsFromObsScanFixed(List<ObservationScanFixed> obsList)
	{
		Log.i("", "in extended getMeasurement");
		DeviceMeasurement measurement = null;
		HashMap<Integer, Reading> values = null;
		int unit = -1;
		int partition = -1;
		for(int i = 0;i<obsList.size();i++)
		{
			Log.i("", "obsList with i = "+i);
			ObservationScanFixed obs = (ObservationScanFixed)obsList.get(i);
			DIMObject dimObj = getDIMObject((long)obs.getObjHandle().getValue());//numericObject.get((long)obs.getObjHandle().getValue());
			if(dimObj == null)
			{
				Log.e("", "numeric null-- "+ obs.getObjHandle().getValue());
			}
			if(dimObj.getAttribute(Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP).getMainAttribute() == null)
			{
				Log.e("", "Object null");
			}
			if(values == null)
			{
				values = new HashMap<Integer, Reading>();
			}
			Reading spotReading  = new Reading();
			ASN_Handle ObjHandle = (ASN_Handle) dimObj.getAttribute((int)Nomenclature.MDC_ATTR_ID_HANDLE).getMainAttribute();
			Type type_from_numeric = (Type)dimObj.getAttribute((int)Nomenclature.MDC_ATTR_ID_TYPE).getMainAttribute();
			if(type_from_numeric!=null)
			{
				partition = type_from_numeric.getCode().getValue();
				partition = (partition & 0x0000FFFF);
			}
			Attribute attrib = dimObj.getAttribute((int)Nomenclature.MDC_ATTR_UNIT_CODE);
			ASN_OIDType unitCode = null;
			if(attrib!=null)
			{
				unitCode = (ASN_OIDType)attrib.getMainAttribute();
			}
			if(unitCode!=null)
			{
				unit = unitCode.getValue();
			}
			Log.i("", "partition: "+partition);
			Log.i("", "Unit: "+unit);
			int ObjHandleValue = ObjHandle.getValue();
			AttrValMap avm = (AttrValMap)dimObj.getAttribute(Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP).getMainAttribute();
			List<AttrValMapEntry> avme = avm.getMembers();	
			spotReading.setUnitCode(unit);
			for(int j = 0; j <avme.size(); j++)
			{
				Log.i("", "in extended get measurement 3");
				AttrValMapEntry entry = (AttrValMapEntry)avme.get(j); 
				long id = entry.getAttributeId().getValue();
				byte[] data = obs.getObsValData().getOctetString();
				if(obs.getObjHandle().getValue() == (short)ObjHandleValue)
				{
					switch((int)id)
					{
					case Nomenclature.MDC_ATTR_NU_VAL_OBS_SIMP:
					{
						spotReading.setDataType(Nomenclature.MDC_ATTR_NU_VAL_OBS_SIMP);
						String obsData = "-";
						Log.i("", "Object handle is --> "+obs.getObjHandle().getValue());
						for(int k = 0; k<data.length;k++)
						{
							obsData = String.format("%s-%02x", obsData,data[k]);
						}
						Log.i("", "Obs Data --> "+obsData);
						DecoderMDER decoder = new DecoderMDER(data);
						SimpleNuObsValue dataVal = new SimpleNuObsValue(decoder);	
						
						spotReading.addData(dataVal.toDouble());
					}
					break;
					case Nomenclature.MDC_ATTR_NU_VAL_OBS_BASIC:
					{
						spotReading.setDataType(Nomenclature.MDC_ATTR_NU_VAL_OBS_BASIC);
						DecoderMDER decoder = new DecoderMDER(data);
						BasicNuObsValue dataVal = new BasicNuObsValue(decoder);	
						
						spotReading.addData(dataVal.toDouble());
					}
					break;
					case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_SIMP:
					{
						spotReading.setDataType(Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_SIMP);
						DecoderMDER decoder = new DecoderMDER(data);
						SimpleNuObsValueComp simpleCmpValues = new SimpleNuObsValueComp(decoder);
						for (int p = 0; p < simpleCmpValues.getCount(); p++)
						{
							SimpleNuObsValue element = simpleCmpValues.getMember(p);
							double val = element.toDouble();
							Log.i("", "MDC_ATTR_NU_CMPD_VAL_OBS_SIMP val->"+val);
				
						}
					}
					break;
					case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_BASIC:
					{
						spotReading.setDataType(Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_BASIC);
						DecoderMDER decoder = new DecoderMDER(data);
						
						BasicNuObsValueComp basicCmpValues = new BasicNuObsValueComp(decoder);
						MetricIdList mIDList = (MetricIdList)dimObj.getAttribute(Nomenclature.MDC_ATTR_ID_PHYSIO_LIST).getMainAttribute();
						for (int p = 0; p < basicCmpValues.getCount(); p++)
						{
							BasicNuObsValue element = basicCmpValues.getMember(p);
							double val = element.toDouble();
						
							spotReading.addData(val);
						
							spotReading.addMetricID((mIDList.getMember(p).getValue()));
							
						}
					}
					break;
					case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_OID:
					{
						spotReading.setDataType(Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_OID);
						DecoderMDER decoder = new DecoderMDER(data);
						ASN_OIDType dataVal = new ASN_OIDType(decoder);	
						Log.i("", "MDC_ATTR_ENUM_OBS_VAL_SIMP_OID in MDSObject: ");
						spotReading.addData(dataVal.getValue());
					}
					break;
					case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR:
					{
						spotReading.setDataType(Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR);
						DecoderMDER decoder = new DecoderMDER(data);
						ASN_BITS32 dataVal = new ASN_BITS32(decoder);
						Log.i("", "MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR in MDSObject: ");
						spotReading.addData(dataVal.getBytes());
					}
					break;
					case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_BASIC_BIT_STR:
					{
						spotReading.setDataType(Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR);
						DecoderMDER decoder = new DecoderMDER(data);
						ASN_BITS16 dataVal = new ASN_BITS16(decoder);	
						Log.i("", "MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR in MDSObject: ");
						spotReading.addData(dataVal.getBytes());
					}
					break;
					case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_STR:
					{
						spotReading.setDataType(Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_STR);
						DecoderMDER decoder = new DecoderMDER(data);
						EnumPrintableString dataVal = new EnumPrintableString(decoder);
						Log.i("", "MDC_ATTR_ENUM_OBS_VAL_SIMP_STR in MDSObject: ");
						spotReading.addData(dataVal.getOctetString());
					}
					break;
					case Nomenclature.MDC_ATTR_VAL_ENUM_OBS:
					{
						/*spotReading.setDataType(Nomenclature.MDC_ATTR_VAL_ENUM_OBS);
						DecoderMDER decoder = new DecoderMDER(data);
						//EnumObservedValue dataVal = new EnumObservedValue(decoder);	
						spotReading.addData(dataVal);*/
					}
					break;
					}

				}
			}
			values.put(partition, spotReading);
		}
		measurement = new DeviceMeasurement(this.getSpecialization(), values);

		return measurement;
	}
	private DIMObject getDIMObject(long id)
	{
		DIMObject retVal = null;

		if(numericObject!=null)
		{
			retVal = numericObject.get(id);
		}
		if(retVal == null)
		{
			if(enumObject!=null)
			{
				retVal = enumObject.get(id);
			}
			if(retVal == null)
			{
				if(pMObject!=null)
				{
					retVal = pMObject.get(id);
				}
				if(retVal == null)
				{
					if(eScanner!=null)
					{
						retVal = eScanner.get(id);
					}
					if(retVal == null)
					{
						if(pScanner!=null)
						{
							retVal = pScanner.get(id);
						}
					}
				}
			}
		}
		return retVal;
	}
	protected Measurement getMeasurementsFromObservationScan(List<ObservationScan> obsList)
	{
		Log.i("", "in extended getMeasurement");
		DeviceMeasurement measurement = null;
		HashMap<Integer, Reading> values = null;
		int unit = -1;
		int partition = -1;
		for(int i = 0;i<obsList.size();i++)
		{
			Log.i("", "obsList with i = "+i);
			ObservationScan obs = (ObservationScan)obsList.get(i);

			DIMObject dimObj = getDIMObject((long)obs.getObjHandle().getValue());//numericObject.get((long)obs.getObjHandle().getValue());
			Reading spotReading  = new Reading();

			Type type_from_numeric = (Type)dimObj.getAttribute((int)Nomenclature.MDC_ATTR_ID_TYPE).getMainAttribute();
			if(type_from_numeric!=null)
			{
				partition = type_from_numeric.getCode().getValue();
				partition = (partition & 0x0000FFFF);
			}
			ASN_OIDType unitCode = (ASN_OIDType)dimObj.getAttribute((int)Nomenclature.MDC_ATTR_UNIT_CODE).getMainAttribute();
			if(unitCode!=null)
			{
				unit = unitCode.getValue();
			}
			Log.i("", "partition: "+partition);
			Log.i("", "Unit: "+unit);
			if(values == null)
			{
				values = new HashMap<Integer, Reading>();
			}
			AttributeList aList = obs.getAttributes();
			spotReading.setUnitCode(unit);
			for(int j = 0; j< aList.getCount(); j++)
			{
				AVA_Type member = aList.getMember(j);
				long id = member.getAttributeId().getValue();				
				byte[]data = null;
				if(member.getAttributeValue() != null)
				{
					data = member.getAttributeValue().getAnyDefinedByObject().getBytes();
				}
				if(data != null)
				{
					switch((int)id)
					{

					case Nomenclature.MDC_ATTR_NU_VAL_OBS_SIMP:
					{
						spotReading.setDataType(Nomenclature.MDC_ATTR_NU_VAL_OBS_SIMP);
						String obsData = "-";
				
						for(int k = 0; k<data.length;k++)
						{
							obsData = String.format("%s-%02x", obsData,data[k]);
						} 
				
						DecoderMDER decoder = new DecoderMDER(data);
						SimpleNuObsValue dataVal = new SimpleNuObsValue(decoder);	
				
						spotReading.addData(dataVal.toDouble());
					}
					break;
					case Nomenclature.MDC_ATTR_NU_VAL_OBS_BASIC:
					{
						spotReading.setDataType(Nomenclature.MDC_ATTR_NU_VAL_OBS_BASIC);
						DecoderMDER decoder = new DecoderMDER(data);
						BasicNuObsValue dataVal = new BasicNuObsValue(decoder);	
				
						spotReading.addData(dataVal.toDouble());
					}
					break;
					case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_SIMP:
					{
						spotReading.setDataType(Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_SIMP);
						DecoderMDER decoder = new DecoderMDER(data);
						SimpleNuObsValueComp simpleCmpValues = new SimpleNuObsValueComp(decoder);
						for (int p = 0; p < simpleCmpValues.getCount(); p++)
						{
							SimpleNuObsValue element = simpleCmpValues.getMember(p);
							double val = element.toDouble();
							Log.i("", "MDC_ATTR_NU_CMPD_VAL_OBS_SIMP val->"+val);
						}
					}
					break;
					case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_BASIC:
					{
						spotReading.setDataType(Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_BASIC);
						DecoderMDER decoder = new DecoderMDER(data);
						BasicNuObsValueComp basicCmpValues = new BasicNuObsValueComp(decoder);
						MetricIdList mIDList = (MetricIdList)dimObj.getAttribute(Nomenclature.MDC_ATTR_ID_PHYSIO_LIST).getMainAttribute();
						for (int p = 0; p < basicCmpValues.getCount(); p++)
						{
							BasicNuObsValue element = basicCmpValues.getMember(p);
							double val = element.toDouble();
				
							spotReading.addData(val);
							Log.i("","Metric ID->"+mIDList.getMember(p).getValue());
							spotReading.addMetricID(mIDList.getMember(p).getValue());
						}
					}
					break;
					case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_OID:
					{
						spotReading.setDataType(Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_OID);
						DecoderMDER decoder = new DecoderMDER(data);
						ASN_OIDType dataVal = new ASN_OIDType(decoder);	
						
						spotReading.addData(dataVal.getValue());
					}
					break;
					case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR:
					{
						spotReading.setDataType(Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR);
						DecoderMDER decoder = new DecoderMDER(data);
						ASN_BITS32 dataVal = new ASN_BITS32(decoder);
						
						spotReading.addData(dataVal.getBytes());
					}
					break;
					case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_BASIC_BIT_STR:
					{
						spotReading.setDataType(Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR);
						DecoderMDER decoder = new DecoderMDER(data);
						ASN_BITS16 dataVal = new ASN_BITS16(decoder);	
						spotReading.addData(dataVal.getBytes());
					}
					break;
					case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_STR:
					{
						spotReading.setDataType(Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_STR);
						DecoderMDER decoder = new DecoderMDER(data);
						EnumPrintableString dataVal = new EnumPrintableString(decoder);	
						spotReading.addData(dataVal.getOctetString());
					}
					break;
					case Nomenclature.MDC_ATTR_VAL_ENUM_OBS:
					{
						/*spotReading.setDataType(Nomenclature.MDC_ATTR_VAL_ENUM_OBS);
						DecoderMDER decoder = new DecoderMDER(data);
						//EnumObservedValue dataVal = new EnumObservedValue(decoder);	
						spotReading.addData(dataVal);*/
					}
					break;

					}

					values.put(partition, spotReading);
				}
			}
			
		}
		measurement = new DeviceMeasurement(this.getSpecialization(), values);
		return measurement;
	}
	public Measurement MDS_Dynamic_Data_Update_MP_Fixed(List<ObservationScanFixed> Seq)
	{
		return getMeasurementsFromObsScanFixed(Seq);		 	
	}
	@SuppressWarnings("unchecked")
	public ArrayList<Measurement> MDS_Dynamic_Data_Update_MP_Var(ScanReportInfoMPVar srimv)
	{
		ArrayList<Measurement> retVal = null;
		Measurement measurement = null;
		Log.e("", "MDSObject MDS_Dynamic_Data_Update_MP_Var start");
		ScanReportPerVarSequence seq = srimv.getScanPerVar();
		for(int i = 0; i< seq.getCount(); i++)
		{
			ScanReportPerVar srpf = seq.getMember(i);
			ObservationScanSequence oSeq = srpf.getObsScanVar();
			measurement = getMeasurementsFromObservationScan(oSeq.getMembers());
			measurement.setPersonId(srpf.getPersonId().getValue());
			if(retVal == null)
			{
				retVal = new ArrayList<Measurement>();
			}
			retVal.add(measurement);
		}
		return retVal;
	}
	public Measurement MDS_Dynamic_Data_Update_Fixed(List<ObservationScanFixed> Seq)
	{
		Log.i("","In MDS_Dynamic_Data_Update_Fixed start");

		Measurement measurementObj = null;
		measurementObj = getMeasurementsFromObsScanFixed(Seq);//getMeasurements(obsSeq.getMembers());
		measurementObj.setPersonId(-1);
		return measurementObj;
	}
	@SuppressWarnings("unchecked")
	public Measurement MDS_Dynamic_Data_Update_Var(ScanReportInfoVar sRIV)
	{
		ObservationScanSequence obScanSeq = sRIV.getObsScanVar();

		return getMeasurementsFromObservationScan(obScanSeq.getMembers());

	}

	@SuppressWarnings("unchecked")
	public ConfigReportRsp MDS_CONFIG_NOTIFICATION_Event(EventReportArgumentSimple eras)
	{
		ConfigReportRsp retValue = null;
		Hashtable<Long,org.continuaalliance.mcesl.dim.Attribute> attribs = 
				new Hashtable<Long,org.continuaalliance.mcesl.dim.Attribute>();
		switch((int)eras.getObjHandle().getValue())
		{
		case 0:
			//MDS object
			System.out.println("Object handle is 0. MDS object");
			ConfigReport configReport =
					(ConfigReport) eras.getEventInfo().getAnyDefinedByObject();
			ConfigObjectList confObjList = configReport.getConfigObjList();
			List<ConfigObject> configObs = confObjList.getMembers();

			for(int i = 0; i < configObs.size(); i++)
			{
				ConfigObject obj = (ConfigObject) configObs.get(i);
				Log.i("","Object class-->"+obj.getObjClass().getValue());
				Log.i("","Object handle-->"+obj.getObjHandle().getValue());
				ASN_Handle handle = new ASN_Handle(obj.getObjHandle().getValue());
				Attribute attr = new Attribute(Nomenclature.MDC_ATTR_ID_HANDLE,
						handle);

				attribs.put((long)Nomenclature.MDC_ATTR_ID_HANDLE, attr);
				AttributeList aList = obj.getAttributes();
				@SuppressWarnings("rawtypes")
				List elements = aList.getMembers();
				Log.e("","attribute size -->"+elements.size());
				for(int j = 0; j < elements.size(); j++)
				{
					AVA_Type object = (AVA_Type) elements.get(j);
					if(object == null)
					{
						Log.e("","object is NULL!");
					}

					ASN_OIDType id= object.getAttributeId();
					if(id ==null)
					{
						Log.e("","id is NULL!");
					}
					Log.e("","id is not null "+id.getValue());

					Log.e("","object is not null "+object.getAttributeValue());


					byte[] attribVal = null;
					if(object.getAttributeValue() != null)
					{
						attribVal = object.getAttributeValue().getAnyDefinedByObject().getBytes();
					}
					org.continuaalliance.mcesl.dim.Attribute attrib = new org.continuaalliance.mcesl.dim.Attribute((int)id.getValue(),
							MessageFactory.getAttributeValueDecoded((int)id.getValue(),attribVal));
					attribs.put((long)id.getValue(), attrib);

				}						
				switch((int)obj.getObjClass().getValue())
				{
				case Nomenclature.MDC_MOC_VMO_METRIC_NU:
				{
					Log.i("","Creating Numeric object");
					Numeric num = new Numeric(attribs);
					addNumericValue(num);
					Log.i("","Numeric object created");
				}
				break;
				case Nomenclature.MDC_MOC_VMO_METRIC_ENUM:
				{
					Log.i("","Creating enum object");
					Enumeration enumData = new Enumeration(attribs);
					addEnumValue(enumData);
					Log.i("","Numeric enum created");
				}
				case Nomenclature.MDC_MOC_VMO_PMSTORE:
				{
					Log.i("","Creating pmstore object");
					PMStore pmData = new PMStore(attribs);
					addPMStoreValue(pmData);
					Log.i("","pmstore created");
				}
				break;
				case Nomenclature.MDC_MOC_SCAN_CFG_EPI:
				{
					Log.i("","Creating epi scanner object");
					EpisodicScanner scanner = new EpisodicScanner(attribs);
					addEpisodicScannerValue(scanner);
					Log.i("","epi scanner created");
				}
				break;
				case Nomenclature.MDC_MOC_SCAN_CFG_PERI:
				{
					Log.i("","Creating per scanner object");
					PeriodicScanner scanner = new PeriodicScanner(attribs);
					addPeriodicScannerValue(scanner);
					Log.i("","per scanner created");
				}
				break;
				};


			}
			retValue = new ConfigReportRsp(configReport.getConfigId(), ConfigResult.ACCEPTED_CONFIG);
		}
		return retValue;
	}
	public AppMDSObject createAppMDS()
	{
		AppMDSObject retValue = new AppMDSObject();

		if(numericObject!=null)
		{
			java.util.Enumeration<Numeric> obs = numericObject.elements();
			for(int i = 0;obs.hasMoreElements();i++)
			{
				AppNumeric obj = new AppNumeric();
				Numeric num = obs.nextElement();
				Log.e("","NUMERIC size-->"+numericObject.size());
				if(num == null)
				{
					Log.e("","NUM is null "+i);
				}
				ASN_Handle handle =(ASN_Handle)num.getAttribute((int)Nomenclature.MDC_ATTR_ID_HANDLE).getMainAttribute();
				obj.handle = handle.getValue();
				retValue.addNumeric(obj);
			}
		}
		if(pMObject!=null)
		{
			java.util.Enumeration<PMStore> obsP = pMObject.elements();
			for(int j = 0;obsP.hasMoreElements();j++)
			{
				AppPMStore obj = new AppPMStore();
				PMStore pms = obsP.nextElement();
				Log.i("","PMStore size-->"+pMObject.size());
				if(pms == null)
				{
					Log.e("","NUM is null "+j);
				}
				ASN_Handle handle =(ASN_Handle)pms.getAttribute((int)Nomenclature.MDC_ATTR_ID_HANDLE).getMainAttribute();
				obj.handle = handle.getValue();
				retValue.addPMStore(obj);
			}
		}
		if(eScanner!=null)
		{
			java.util.Enumeration<EpisodicScanner> obsP = eScanner.elements();
			for(int j = 0;obsP.hasMoreElements();j++)
			{
				AppScannerObject obj = new AppScannerObject();
				EpisodicScanner pms = obsP.nextElement();
				Log.i("","EpisodicScannersize-->"+eScanner.size());
				if(pms == null)
				{
					Log.e("","NUM is null "+j);
				}
				ASN_Handle handle =(ASN_Handle)pms.getAttribute((int)Nomenclature.MDC_ATTR_ID_HANDLE).getMainAttribute();
				OperationalState opState =(OperationalState)pms.getAttribute((int)Nomenclature.MDC_ATTR_OP_STAT).getMainAttribute();
				obj.opState = opState.getValue();
				obj.handle = handle.getValue();
				obj.type = Nomenclature.MDC_MOC_SCAN_CFG_EPI;
				retValue.addScannerObject(obj);
			}
		}
		if(pScanner!=null)
		{
			java.util.Enumeration<PeriodicScanner> obsP = pScanner.elements();
			for(int j = 0;obsP.hasMoreElements();j++)
			{
				AppScannerObject obj = new AppScannerObject();
				PeriodicScanner pms = obsP.nextElement();
				Log.i("","EpisodicScannersize-->"+pScanner.size());
				if(pms == null)
				{
					Log.e("","NUM is null "+j);
				}
				ASN_Handle handle =(ASN_Handle)pms.getAttribute((int)Nomenclature.MDC_ATTR_ID_HANDLE).getMainAttribute();
				OperationalState opState =(OperationalState)pms.getAttribute((int)Nomenclature.MDC_ATTR_OP_STAT).getMainAttribute();
				obj.opState = opState.getValue();
				obj.handle = handle.getValue();
				obj.type = Nomenclature.MDC_MOC_SCAN_CFG_PERI;
				retValue.addScannerObject(obj);
			}
		}
		return retValue;
	}
}
