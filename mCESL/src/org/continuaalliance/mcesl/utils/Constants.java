package org.continuaalliance.mcesl.utils;

/*
 * Constants.java: A class which defines the constants. 
 * 				   This class would ideally store only the public final variables.  
 * 
 * @author: Vignet
 */

import org.continuaalliance.mcesl.utils.Nomenclature;
public class Constants {

	public final static int DATA_PROTO_ID_20601 = 20601;
	public final static int ASSOC_RESPONSE_ACCEPTED = 0; 
	public final static String MDER_ENCODING = "MDER";
	public final static int PROTOCOL_VERSION = 0x80000000;
	public final static int MDER_ENC_RULE = 0x8000;
	public final static int SYSTEM_TYPE_MANAGER = 0x80000000;
	public final static int SYSTEM_TYPE_AGENT = 0x00800000;
	public final static int NO_ERROR = 1;
	public final static int NONE = 0;

	public final static String TCP_DISCONNECTED = "TCP_DISCONNECTED";
	public final static String TCP_NEW_CONNECTION = "TCP_NEW_CONNECTION";
	public final static String HDP_NEW_CONNECTION =  "HDP_NEW_CONNECTION";
	public final static String DEVICE_STATE_CHANGED = "DEVICE_STATE_CHANGED";
	public final static String UNKNOWN_ERROR = "UNKNOWN_ERROR";
	public final static String ASSOC_RCVD = "ASSOC_RCVD";
	public final static String TRANSPORT_DISCONNECT = "TRANSPORT_DISCONNECT";
	public final static String MEASUREMENT_RECVD = "MEASUREMENT_RECVD";
	public final static String DEVICE_INFO = "DEVICE_INFO";
	public final static String MDS_OBJECT = "MDS_OBJECT";
	public final static String SEGMENT_INFO = "SEGMENT_INFO";

	public final static int STANDARD_CONFIGURATION = 0x01;
	public final static int EXTENDED_CONFIGURATION = 0x00;

	public static String getStringonID(int id)
	{
		String val = null;
		switch(id)
		{
		case Nomenclature.MDC_MASS_BODY_ACTUAL:
			val = "BODY MASS";
			break;
		case Nomenclature.MDC_BODY_FAT:
			val = "BODY FAT";
			break;
		case Nomenclature.MDC_LEN_BODY_ACTUAL:
			val = "BODY LEN";
			break;
		case Nomenclature.MDC_RATIO_MASS_BODY_LEN_SQ:
			val = "BODY LEN SQ";
			break;
		case Nomenclature.MDC_CONC_GLU_CAPILLARY_WHOLEBLOOD:
			val = "GLU CAPILLARY WHOLEBLOOD";
			break;
		case Nomenclature.MDC_CONC_GLU_CAPILLARY_PLASMA:
			val = "GLU CAPILLARY PLASMA";
			break;
		case Nomenclature.MDC_CONC_GLU_VENOUS_WHOLEBLOOD:
			val = "GLU VENOU WHOLEBLOOD";
			break;
		case Nomenclature.MDC_CONC_GLU_VENOUS_PLASMA:
			val = "GLU VENOUS PLASMA";
			break;
		case Nomenclature.MDC_CONC_GLU_ARTERIAL_WHOLEBLOOD :
			val = "GLU ARTERIAL WHOLEBLOOD";
			break;
		case Nomenclature.MDC_CONC_GLU_ARTERIAL_PLASMA :
			val = "GLU ARTERIAL PLASMA";
			break;
		case Nomenclature.MDC_CONC_GLU_CONTROL :
			val = "GLU CONTROL";
			break;
		case Nomenclature.MDC_CONC_GLU_ISF:
			val = "GLU ISF";
			break;
		case Nomenclature.MDC_TEMP_BODY:
			val = "BODY TEMP";
			break;
		case Nomenclature.MDC_PRESS_BLD_NONINV:
			val = "PRESS_BLD_NONINV";
			break;
		case Nomenclature.MDC_PRESS_BLD_NONINV_SYS:
			val = "SYSTOLIC";
			break;
		case Nomenclature.MDC_PRESS_BLD_NONINV_DIA:
			val = "DIASTOLIC";
			break;
		case Nomenclature.MDC_PRESS_BLD_NONINV_MEAN:
			val = "MEAN";
			break;
		case Nomenclature.MDC_PULS_OXIM_PULS_RATE:
			val="PULSE RATE";
			break;
		case Nomenclature.MDC_PULS_OXIM_SAT_O2:
			val = "SpO2";
			break;
		
		}
		return val;
	}
	public static String getUnit(int id)
	{
		String val = "";
		switch(id)
		{
		/*Units*/
		case Nomenclature.MDC_DIM_CENTI_M :  /*   cm    */
			val = "CM";
			break;
		case Nomenclature.MDC_DIM_INCH:  /*  in    */
			val = "INCH";
			break;
		case Nomenclature.MDC_DIM_LB:/*  lb      */
			val = "LB";
			break;
		case Nomenclature.MDC_DIM_KG_PER_M_SQ:
			val = "KG PER M SQ";
			break;
		case Nomenclature.MDC_DIM_MILLI_L:/* mL    */
			val = "mL";
			break;
		case Nomenclature.MDC_DIM_MILLI_G:/* mg    */
			val = "mg";
			break;
		case Nomenclature.MDC_DIM_MILLI_G_PER_DL:/* mg dL-1    */
			val = "mgDL";
			break;
		case Nomenclature.MDC_DIM_MILLI_MOLE_PER_L:/* mmol L-1    */
			val = "mmolL";
			break;
		case Nomenclature.MDC_DIM_X_G:  /* g*/
			val = "g";
			break;
		case Nomenclature.MDC_DIM_FAHR:
			val = "°F";
			break;
		case Nomenclature.MDC_DIM_BEAT_PER_MIN:  /* bpm  */
			val = "bpm";
			break;
		case Nomenclature.MDC_DIM_KILO_PASCAL:  /* kPa  */
			val = "kPa";
			break;
		case Nomenclature.MDC_DIM_MMHG: 
			val = "mmHg";
			break;
		case Nomenclature.MDC_DIM_PERCENT:
			val = "%";
			break;
		case Nomenclature.MDC_DIM_KILO_G:
			val = "Kg";
			break;
		case Nomenclature.MDC_DIM_MIN:
			val = "min";
			break;
		case Nomenclature.MDC_DIM_HR:
			val = "h"; 
			break;
		case Nomenclature.MDC_DIM_DAY:
			val = "d";
			break;
		case Nomenclature.MDC_DIM_DEGC:
			val = "ºC";
			break;
		};
		return val; 
	}
}
