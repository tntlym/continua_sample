package org.continuaalliance.mcesl.deviceSpecialization;


/**
 * GlucoseMeterDeviceSpecialization.java: This class represents Glucose meter device specialization
 * 
 * @author Vignet
 */
import java.util.Hashtable;

import org.continuaalliance.mcesl.dim.Attribute;
import org.continuaalliance.mcesl.dim.MDSObject;
import org.continuaalliance.mcesl.dim.Numeric;
import org.continuaalliance.mcesl.utils.ASN1Constants;
import org.continuaalliance.mcesl.utils.Nomenclature;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_Handle;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_INTU16;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_OIDType;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.AttrValMap;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.AttrValMapEntry;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.MetricSpecSmall;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.Type;

public class GlucoseMeterDeviceSpecialization extends MDSObject{

	public GlucoseMeterDeviceSpecialization(Hashtable<Long, Attribute> map) {
		super(map);
		createNumericValues();
	}

	private void createNumericValues()
	{
		Hashtable<Long,Attribute> mandatoryAttributes = new Hashtable<Long,Attribute>();

		ASN_Handle handle = new ASN_Handle(1);
		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_ID_HANDLE, 
				new Attribute(Nomenclature.MDC_ATTR_ID_HANDLE,
						handle));

		ASN_OIDType oid = new ASN_OIDType(Nomenclature.MDC_CONC_GLU_CAPILLARY_WHOLEBLOOD);

		Type type = new Type(new ASN_INTU16((short)Nomenclature.MDC_PART_SCADA),oid);

		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_ID_TYPE, 
				new Attribute( Nomenclature.MDC_ATTR_ID_TYPE,
						type));

		short mask = 0;
		mask = (short) (mask | ASN1Constants.MSS_AVAIL_INTERMITTENT|ASN1Constants.MSS_UPD_APREIODIC
				|ASN1Constants.MSS_MSMT_APREIODIC|ASN1Constants.MSS_AVAIL_STORED_DATA | 
				ASN1Constants.MSS_ACC_AGENT_INITIATED);
		MetricSpecSmall metricSpec;

		metricSpec = new MetricSpecSmall(mask);
		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_METRIC_SPEC_SMALL, 
				new Attribute(Nomenclature.MDC_ATTR_METRIC_SPEC_SMALL,
						metricSpec));

		ASN_OIDType unitOid = new ASN_OIDType(Nomenclature.MDC_DIM_MILLI_G_PER_DL);
		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_UNIT_CODE, 
				new Attribute(Nomenclature.MDC_ATTR_UNIT_CODE,
						unitOid));

		AttrValMap avm = new AttrValMap();

		AttrValMapEntry []entries = new AttrValMapEntry[2];
		ASN_OIDType attrId1 = new ASN_OIDType(Nomenclature.MDC_ATTR_NU_VAL_OBS_BASIC);


		entries[0] = new AttrValMapEntry(attrId1, new ASN_INTU16(2));

		ASN_OIDType attrId2 = new ASN_OIDType(Nomenclature.MDC_ATTR_TIME_STAMP_ABS);
		entries[1] = new AttrValMapEntry(attrId2, new ASN_INTU16(8));

		avm.addMember(entries[0]);
		avm.addMember(entries[1]);

		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP, 
				new Attribute(Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP,
						avm));

		Numeric numericData = new Numeric(mandatoryAttributes);
		addNumericValue(numericData);

	}
	@Override
	public int getNomenclatureCode() {
		return 0;
	}

}
