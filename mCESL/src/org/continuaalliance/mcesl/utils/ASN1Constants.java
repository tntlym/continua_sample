package org.continuaalliance.mcesl.utils;

/**
 * 
 * ASN1Constants.java: Stores required ASN1 constants.
 * @author Vignet
 *
 */
public interface ASN1Constants {
		
	/* AbortReason */
	public static final int ABRT_RE_UNDEFINED						= 0;
	public static final int ABRT_RE_BUFFER_OVERFLOW					= 1;
	public static final int ABRT_RE_RESPONSE_TIMEOUT				= 2;
	public static final int ABRT_RE_CONFIGURATION_TIMEOUT			= 3;
	
	/* AssociateResult */
	public static final int AR_ACCEPTED 							= 0;
	public static final int AR_REJECTED_PERMANENT 					= 1;
	public static final int AR_REJECTED_TRANSIENT 					= 2;
	public static final int AR_ACCEPTED_UNKNOWN_CONFIG				= 3;
	public static final int AR_REJECTED_NO_COMMON_PROTOCOL			= 4;
	public static final int AR_REJECTED_NO_COMMON_PARAMETER			= 5;
	public static final int AR_REJECTED_UNKNOWN	 					= 6;
	public static final int AR_REJECTED_UNAUTHORIZED				= 7;
	public static final int AR_REJECTED_UNSUPPORTED_ASSOC_VERSION	= 8;
	
	/* ConfigId */
	public static final int CONF_ID_MANAGER_CONFIG_RESPONSE			= 0;
	public static final int CONF_ID_STANDARD_CONFIG_START			= 1;
	public static final int CONF_ID_STANDARD_CONFIG_END				= 16383;
	public static final int CONF_ID_EXTENDED_CONFIG_START			= 16384;
	public static final int CONF_ID_EXTENDED_CONFIG_END				= 32767;
	public static final int CONF_ID_RESERVED_START					= 32768;
	public static final int CONF_ID_RESERVED_END					= 65535;

	/* DataReqModeFlags */
	public static final int DATA_REQ_SUPP_STOP						= 0x8000;
	public static final int DATA_REQ_SUPP_SCOPE_ALL					= 0x0800;
	public static final int DATA_REQ_SUPP_SCOPE_CLASS				= 0x0400;
	public static final int DATA_REQ_SUPP_SCOPE_HANDLE				= 0x0200;
	public static final int DATA_REQ_SUPP_MODE_SINGLE_RESP			= 0x0080;
	public static final int DATA_REQ_SUPP_MODE_TIME_PERIOD			= 0x0040;
	public static final int DATA_REQ_SUPP_MODE_TIME_NO_LIMIT		= 0x0020;
	public static final int DATA_REQ_SUPP_PERSON_ID					= 0x0010;
	public static final int DATA_REQ_SUPP_INIT_AGENT				= 0x0001;
	
	/*DataProto */
	public static final int DATA_PROTO_ID_EMPTY						= 0;
	public static final int DATA_PROTO_ID_20601						= 20601;	
	public static final int DATA_PROTO_ID_EXTERNAL					= 65535;
	
	/* EncodingRules */
	public static final int ENC_MDER								= 0x8000;
	public static final int ENC_XER									= 0x4000;
	public static final int ENC_PER									= 0x2000;
	
	/* FunctionalUnits */
	public static final int FUN_UNITS_UNIDIRECTIONAL				= 0x80000000;
	public static final int FUN_UNITS_HAVETESTCAP					= 0x40000000;
	public static final int FUN_UNITS_CREATETESTASSOC				= 0x20000000;
	
	/* NomenclatureVersion */
	public static final int NOMENCLATURE_VERSION1					= 0x80000000;
	
	/* ReleaseRequestReason */
	public static final int REL_REQ_RE_NORMAL						= 0;
	public static final int REL_REQ_RE_NO_MORE_CONFIGURATIONS		= 1;
	public static final int REL_REQ_RE_CONFIGURATION_CHANGED		= 2;
	
	/* ReleaseResponseReason */
	public static final int REL_RES_RE_NORMAL						= 0;
	
	/* SystemType */
	public static final int SYS_TYPE_MANAGER						= 0x80000000;
	public static final int SYS_TYPE_AGENT							= 0x00800000;	
	
	/* ProtocolVersion */
	public static final int PROTOCOL_VERSION1						= 0x80000000;
	
	/* MetricSpecSmall */
	public static final int MSS_AVAIL_INTERMITTENT					= 0x8000;
	public static final int MSS_AVAIL_STORED_DATA					= 0x4000;
	public static final int MSS_UPD_APREIODIC						= 0x2000;
	public static final int MSS_MSMT_APREIODIC						= 0x1000;
	public static final int MSS_MSMT_PHYS_EV_ID						= 0x0800;
	public static final int MSS_MSMT_BTB_METRIC						= 0x0400;
	public static final int MSS_ACC_MANAGER_INITIATED				= 0x0080;
	public static final int MSS_ACC_AGENT_INITIATED					= 0x0040;
	public static final int MSS_CAT_MANUAL							= 0x0008;
	public static final int MSS_CAT_SETTING							= 0x0004;
	public static final int MSS_CAT_CALCULATION						= 0x0002;
	
	/*RoerErrorValue*/
	public static final int ROER_NO_SUCH_OBJECT_INSTANCE			= 1;
	public static final int ROER_NO_SUCH_ACTION						= 9;
	public static final int ROER_INVALID_OBJECT_INSTANCE			= 17;
	public static final int ROER_PROTOCOL_VIOLATION					= 23;
	public static final int ROER_NOT_ALLOWED_BY_OBJECT				= 24;
	public static final int ROER_ACTION_TIMEOUT						= 25;
	public static final int ROER_ACTION_ABORTED						= 26;
	
	/*Config Result*/
	public static final short CONF_RESULT_ACCEPTED_CONFIG				= 0;
	public static final short CONF_RESULT_UNSUPPORTED_CONFIG			= 1;
	public static final short CONF_RESULT_STANDARD_CONFIG_UNKNOWN		= 2;
	
	public static final byte[] NomenclatureVManager = new byte[]{(byte)0x80,0x00,0x00,0x00};
	
}
