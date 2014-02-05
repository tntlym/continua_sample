package org.continuaalliance.mcesl.dim;

import org.continuaalliance.mcesl.utils.Nomenclature;

public class Attribute {
	
	private String attributeName; 	
	private int attributeId; 		
	private Object mainAttribute; 	
	
	public Attribute(int id, Object attrib)
	{
		attributeId = id;
		mainAttribute = attrib;
		attributeName = getAttributeNameFromID(id);
	}
	
	/**
	 * Returns the Attribute name
	 * @return String attribute name
	 */
	public final String getAttributeName()
	{
		return attributeName;
	}
	/**
	 * Returns the ID of the Attribute.
	 * @return int Attribute ID
	 */
	public int getAttributeID()
	{
		return attributeId;
	}
	
	/**
	 * Returns the main object of the attribute.
	 * @return Object 
	 */
	public Object getMainAttribute()
	{
		return mainAttribute;
	}
	
	/**
	 * Returns the String representation based on ID of the attribute
	 * @param id of the attribute
	 * @return String
	 */
	private String getAttributeNameFromID (int id){
		switch (id){
		case Nomenclature.MDC_ATTR_CONFIRM_MODE : return "Confirm-Mode";
		case Nomenclature.MDC_ATTR_CONFIRM_TIMEOUT : return "Confirm-Timeout";
		case Nomenclature.MDC_ATTR_ID_HANDLE : return "Handle";
		case Nomenclature.MDC_ATTR_ID_INSTNO : return "Instance-Number";
		case Nomenclature.MDC_ATTR_ID_LABEL_STRING : return "Label-String";
		case Nomenclature.MDC_ATTR_ID_MODEL : return "System-Model";
		case Nomenclature.MDC_ATTR_ID_PHYSIO : return "Metric-Id";
		case Nomenclature.MDC_ATTR_ID_PROD_SPECN : return "Production-Specification";
		case Nomenclature.MDC_ATTR_ID_TYPE : return "Type";
		case Nomenclature.MDC_ATTR_METRIC_STORE_CAPAC_CNT : return "Store-Capacity-Count";
		case Nomenclature.MDC_ATTR_METRIC_STORE_SAMPLE_ALG : return "Store-Sample-Algorithm";
		case Nomenclature.MDC_ATTR_METRIC_STORE_USAGE_CNT : return "Store-Usage-Count";
		case Nomenclature.MDC_ATTR_MSMT_STAT : return "Measurement-Status";
		case Nomenclature.MDC_ATTR_NU_ACCUR_MSMT : return "Accuracy";
		case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS : return "Compound-Nu-Observed-Value";
		case Nomenclature.MDC_ATTR_NU_VAL_OBS : return "Nu-Observed-Value";
		case Nomenclature.MDC_ATTR_NUM_SEG : return "Number-Of-Segments";
		case Nomenclature.MDC_ATTR_OP_STAT : return "Operational-State";
		case Nomenclature.MDC_ATTR_POWER_STAT : return "Power-Status";
		case Nomenclature.MDC_ATTR_SA_SPECN : return "Sa-Specification";
		case Nomenclature.MDC_ATTR_SCALE_SPECN_I16 : return "Scale-and-Range-Specification_I16";
		case Nomenclature.MDC_ATTR_SCALE_SPECN_I32 : return "Scale-and-Range-Specification_I32";
		case Nomenclature.MDC_ATTR_SCALE_SPECN_I8 : return "Scale-and-Range-Specification_I8";
		case Nomenclature.MDC_ATTR_SCAN_REP_PD : return "Reporting-Interval";
		case Nomenclature.MDC_ATTR_SEG_USAGE_CNT : return "Segment-Usage-Count";
		case Nomenclature.MDC_ATTR_SYS_ID : return "System-Id";
		case Nomenclature.MDC_ATTR_SYS_TYPE : return "System-Type";
		case Nomenclature.MDC_ATTR_TIME_ABS : return "Date-and-Time";
		case Nomenclature.MDC_ATTR_TIME_BATT_REMAIN : return "Remaining-Battery-Time";
		case Nomenclature.MDC_ATTR_TIME_END_SEG : return "Segment-End-Abs-Time";
		case Nomenclature.MDC_ATTR_TIME_PD_SAMP : return "Sample-Period";
		case Nomenclature.MDC_ATTR_TIME_REL : return "Relative-Time";
		case Nomenclature.MDC_ATTR_TIME_STAMP_ABS : return "Absolute-Time-Stamp";
		case Nomenclature.MDC_ATTR_TIME_STAMP_REL : return "Relative-Time-Stamp";
		case Nomenclature.MDC_ATTR_TIME_START_SEG : return "Segment-Start-Abs-Time";
		case Nomenclature.MDC_ATTR_TX_WIND : return "Transmit-Window";
		case Nomenclature.MDC_ATTR_UNIT_CODE: return "Unit-Code";
		case Nomenclature.MDC_ATTR_UNIT_LABEL_STRING : return "Unit-LabelString";
		case Nomenclature.MDC_ATTR_VAL_BATT_CHARGE : return "Battery-Level";
		case Nomenclature.MDC_ATTR_VAL_ENUM_OBS : return "Enum-Observed-Value";
		case Nomenclature.MDC_ATTR_TIME_REL_HI_RES : return "HiRes-Relative-Time";
		case Nomenclature.MDC_ATTR_TIME_STAMP_REL_HI_RES : return "HiRes-Time-Stamp";
		case Nomenclature.MDC_ATTR_DEV_CONFIG_ID: return "Dev-Configuration-Id";
		case Nomenclature.MDC_ATTR_MDS_TIME_INFO : return "Mds-Time-Info";
		case Nomenclature.MDC_ATTR_METRIC_SPEC_SMALL: return "Metric-Spec-Small";
		case Nomenclature.MDC_ATTR_SOURCE_HANDLE_REF : return "Source-Handle-Reference";
		case Nomenclature.MDC_ATTR_SIMP_SA_OBS_VAL : return "Simple-Sa-Observed-Value";
		case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_OID : return "Enum-Observed-Value-Simple-OID";
		case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_STR : return "Enum-Observed-Value-Simple-Str";
		case Nomenclature.MDC_ATTR_REG_CERT_DATA_LIST : return "Reg-Cert-Data-List";
		case Nomenclature.MDC_ATTR_NU_VAL_OBS_BASIC : return "Basic-Nu-Observed-Value";
		case Nomenclature.MDC_ATTR_PM_STORE_CAPAB : return "PM-Store-Capab";
		case Nomenclature.MDC_ATTR_PM_SEG_MAP : return "PM-Segment-Entry-Map";
		case Nomenclature.MDC_ATTR_PM_SEG_PERSON_ID : return "PM-Seg-Person-Id";
		case Nomenclature.MDC_ATTR_SEG_STATS : return "Segment-Statistics";
		case Nomenclature.MDC_ATTR_SCAN_HANDLE_ATTR_VAL_MAP : return "Scan-Handle-Attr-Val-Map";
		case Nomenclature.MDC_ATTR_SCAN_REP_PD_MIN : return "Min-Reporting-Interval";
		case Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP: return "Attribute-Value-Map";
		case Nomenclature.MDC_ATTR_NU_VAL_OBS_SIMP : return "Simple-Nu-Observed-Value";
		case Nomenclature.MDC_ATTR_PM_STORE_LABEL_STRING : return "PM-Store-Label";
		case Nomenclature.MDC_ATTR_PM_SEG_LABEL_STRING : return "Segment-Label";
		case Nomenclature.MDC_ATTR_TIME_PD_MSMT_ACTIVE : return "Measure-Active-Period";
		case Nomenclature.MDC_ATTR_SYS_TYPE_SPEC_LIST: return "System-Type-Spec-List";
		case Nomenclature.MDC_ATTR_METRIC_ID_PART : return "Metric-Id-Partition";
		case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_PART : return "Enum-Observed-Value-Partition";
		case Nomenclature.MDC_ATTR_SUPPLEMENTAL_TYPES : return "Supplemental-Types";
		case Nomenclature.MDC_ATTR_TIME_ABS_ADJUST : return "Date-and-Time-Adjustment";
		case Nomenclature.MDC_ATTR_CLEAR_TIMEOUT : return "Clear-Timeout";
		case Nomenclature.MDC_ATTR_TRANSFER_TIMEOUT : return "Transfer-Timeout";
		case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_SIMP_BIT_STR : return "Enum-Observed-Value-Simple-Bit-Str";
		case Nomenclature.MDC_ATTR_ENUM_OBS_VAL_BASIC_BIT_STR : return "Enum-Observed-Value-Basic-Bit-Str";
		case Nomenclature.MDC_ATTR_METRIC_STRUCT_SMALL : return "Metric-Structure-Small";
		case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_SIMP : return "Compund-Simple-Nu-Observed-Value";
		case Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_BASIC : return "Compund-Basic-Nu-Observed-Value";
		case Nomenclature.MDC_ATTR_ID_PHYSIO_LIST : return "Metric-Id";
		case Nomenclature.MDC_ATTR_SCAN_HANDLE_LIST : return "Scan-Handle-List";
		default: return null;
		}
	}
}
