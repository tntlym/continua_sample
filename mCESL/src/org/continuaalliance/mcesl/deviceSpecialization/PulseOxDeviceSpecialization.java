package org.continuaalliance.mcesl.deviceSpecialization;


/**
 * PulseOxDeviceSpecialization.java: This class represents Pulse Oximeter device specialization
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

public class PulseOxDeviceSpecialization extends MDSObject{

	public PulseOxDeviceSpecialization(Hashtable<Long, Attribute> map)
	{
		super(map);
		createNumericValues();
	}

	@Override
	public int getNomenclatureCode() {
		return 0;
	}
	private void createNumericSPO2()
	{
		Hashtable<Long,Attribute> mandatoryAttributes = new Hashtable<Long,Attribute>();

		//from Part 10408: Handle=1
		ASN_Handle handle = new ASN_Handle(1);
		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_ID_HANDLE, 
				new Attribute(Nomenclature.MDC_ATTR_ID_HANDLE,
						handle));



		ASN_OIDType oid = new ASN_OIDType(Nomenclature.MDC_PULS_OXIM_SAT_O2);

		Type type = new Type(new ASN_INTU16((short)Nomenclature.MDC_PART_SCADA),oid);

		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_ID_TYPE, 
				new Attribute( Nomenclature.MDC_ATTR_ID_TYPE,
						type));

		short mask = 0;

		mask = (short) (mask |  ASN1Constants.MSS_AVAIL_STORED_DATA | ASN1Constants.MSS_ACC_AGENT_INITIATED);
		MetricSpecSmall metricSpec;

		metricSpec = new MetricSpecSmall(mask);
		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_METRIC_SPEC_SMALL, 
				new Attribute(Nomenclature.MDC_ATTR_METRIC_SPEC_SMALL,
						metricSpec));



		/* Mandatory attributes for standard configuration: */				
		//from Part 10408: Unit-Code=MDC_DIM_DEGC
		ASN_OIDType unitOid = new ASN_OIDType(Nomenclature.MDC_DIM_PERCENT);
		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_UNIT_CODE, 
				new Attribute(Nomenclature.MDC_ATTR_UNIT_CODE,
						unitOid));

		//from Part 10408: Attribute-Value-Map {(MDC_ATTR_NU_VAL_OBS_BASIC,2),(MDC_ATTR_TIME_STAMP_ABS,8)}
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

	private void createNumericPulse()
	{
		Hashtable<Long,Attribute> mandatoryAttributesForPulse = new Hashtable<Long,Attribute>();


		ASN_Handle handlePulse = new ASN_Handle(10);
		mandatoryAttributesForPulse.put((long)Nomenclature.MDC_ATTR_ID_HANDLE, 
				new Attribute(Nomenclature.MDC_ATTR_ID_HANDLE,
						handlePulse));



		ASN_OIDType oidPulse = new ASN_OIDType(Nomenclature.MDC_PULS_OXIM_PULS_RATE);

		Type typePulse = new Type(new ASN_INTU16(Nomenclature.MDC_PART_SCADA),oidPulse);
		mandatoryAttributesForPulse.put((long)Nomenclature.MDC_ATTR_ID_TYPE, 
				new Attribute( Nomenclature.MDC_ATTR_ID_TYPE,
						typePulse));

		short pulseMask = 0;
		pulseMask = (short) (pulseMask |  ASN1Constants.MSS_AVAIL_STORED_DATA | ASN1Constants.MSS_ACC_AGENT_INITIATED);
		
		MetricSpecSmall metricSpecPulse = new MetricSpecSmall(pulseMask);

		//metricSpecPulse = new MetricSpecSmall(16,bytes);
		mandatoryAttributesForPulse.put((long)Nomenclature.MDC_ATTR_METRIC_SPEC_SMALL, 
				new Attribute(Nomenclature.MDC_ATTR_METRIC_SPEC_SMALL,
						metricSpecPulse));


		/* Mandatory attributes for standard configuration: */				
		//from Part 10408: Unit-Code=MDC_DIM_DEGC
		ASN_OIDType unitOidPulse = new ASN_OIDType(Nomenclature.MDC_DIM_BEAT_PER_MIN);
		mandatoryAttributesForPulse.put((long)Nomenclature.MDC_ATTR_UNIT_CODE, 
				new Attribute(Nomenclature.MDC_ATTR_UNIT_CODE,
						unitOidPulse));

		//from Part 10408: Attribute-Value-Map {(MDC_ATTR_NU_VAL_OBS_BASIC,2),(MDC_ATTR_TIME_STAMP_ABS,8)}
		AttrValMap avmPulse = new AttrValMap();

		AttrValMapEntry []entriesPulse = new AttrValMapEntry[2];
		ASN_OIDType attrId1Pulse = new ASN_OIDType(Nomenclature.MDC_ATTR_NU_VAL_OBS_BASIC);


		entriesPulse[0] = new AttrValMapEntry(attrId1Pulse,new ASN_INTU16(2));


		ASN_OIDType attrId2 = new ASN_OIDType(Nomenclature.MDC_ATTR_TIME_STAMP_ABS);
		entriesPulse[1] = new AttrValMapEntry(attrId2,new ASN_INTU16(8));

		avmPulse.addMember(entriesPulse[0]);
		avmPulse.addMember(entriesPulse[1]);

		mandatoryAttributesForPulse.put((long)Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP, 
				new Attribute(Nomenclature.MDC_ATTR_ATTRIBUTE_VAL_MAP,
						avmPulse));

		Numeric numericDataPulse = new Numeric(mandatoryAttributesForPulse);
		addNumericValue(numericDataPulse);

	}
	private void createNumericValues()
	{
		createNumericSPO2();
		createNumericPulse();
	}
}
