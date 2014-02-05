
/*
 * mcsel protocol implementation
 * @author Vignet
 */

package org.continuaalliance.mcesl.deviceSpecialization;

import java.util.Hashtable;

import org.continuaalliance.mcesl.dim.Attribute;
import org.continuaalliance.mcesl.utils.Nomenclature;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_Handle;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_INTU16;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_OIDType;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_Octet_String_Var;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ConfigId;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SystemModel;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.TypeVer;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.TypeVerList;

import android.util.Log;

public class DeviceSpecializationFactory {

	public static PulseOxDeviceSpecialization createPulseOximeterSpecialization(byte[] system_id, ConfigId devConfig_id)
	{
		PulseOxDeviceSpecialization poDevSpec = null;

		Hashtable<Long,Attribute> mandatoryAttributes = new Hashtable<Long,Attribute>();


		ASN_Handle handle = new ASN_Handle(0);

		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_ID_HANDLE, 
				new Attribute(Nomenclature.MDC_ATTR_ID_HANDLE,
						handle));

		SystemModel systemModel = new SystemModel(new ASN_Octet_String_Var("Manufacturer".getBytes()),new ASN_Octet_String_Var("Model".getBytes()));
		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_ID_MODEL, 
				new Attribute(Nomenclature.MDC_ATTR_ID_MODEL,
						systemModel));

		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_SYS_ID, 
				new Attribute(Nomenclature.MDC_ATTR_SYS_ID,
						system_id));

		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_DEV_CONFIG_ID, 
				new Attribute(Nomenclature.MDC_ATTR_DEV_CONFIG_ID,
						devConfig_id));

		TypeVerList syst_type_data_list = new TypeVerList();
		TypeVer []item = new TypeVer[1];
		ASN_OIDType oid = new ASN_OIDType(Nomenclature.MDC_DEV_SPEC_PROFILE_PULS_OXIM);

		if(item[0]== null)
		{
			Log.i("","null with len "+item.length);
		}
		item[0] = new TypeVer(oid, new ASN_INTU16(1));

		for(int j = 0 ; j < item.length; j++)
		{
			syst_type_data_list.addMember(item[j]);
		}
		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_SYS_TYPE_SPEC_LIST, 
				new Attribute(Nomenclature.MDC_ATTR_SYS_TYPE_SPEC_LIST,
						syst_type_data_list));
		poDevSpec = new PulseOxDeviceSpecialization(mandatoryAttributes);
		

		return poDevSpec;

	}
	
	public static ExtendedConfDeviceSpecialization createExtendedSpecialization(byte[] system_id, ConfigId devConfig_id)
	{
		ExtendedConfDeviceSpecialization exDevSpec = null;
		Hashtable<Long,Attribute> mandatoryAttributes = new Hashtable<Long,Attribute>();


		ASN_Handle handle = new ASN_Handle(0);

		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_ID_HANDLE, 
				new Attribute(Nomenclature.MDC_ATTR_ID_HANDLE,
						handle));

		SystemModel systemModel = new SystemModel(new ASN_Octet_String_Var("Manufacturer".getBytes()),new ASN_Octet_String_Var("Model".getBytes()));
		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_ID_MODEL, 
				new Attribute(Nomenclature.MDC_ATTR_ID_MODEL,
						systemModel));

		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_SYS_ID, 
				new Attribute(Nomenclature.MDC_ATTR_SYS_ID,
						system_id));

		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_DEV_CONFIG_ID, 
				new Attribute(Nomenclature.MDC_ATTR_DEV_CONFIG_ID,
						devConfig_id));

		exDevSpec = new ExtendedConfDeviceSpecialization(mandatoryAttributes);
		return exDevSpec;

	}
}
