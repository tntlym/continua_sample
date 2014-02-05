package org.continuaalliance.mcesl.deviceSpecialization;

/**
 * BloodPressureDeviceSpecialization.java : This class represents blood pressure device
 * 											Specialization
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
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_INTU8;
import org.openhealthtools.stepstone.phd.core.asn1.base.ASN_OIDType;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.AttrValMap;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.AttrValMapEntry;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.MetricIdList;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.MetricSpecSmall;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.MetricStructureSmall;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.Type;

public class BloodPressureDeviceSpecialization extends MDSObject{

	public BloodPressureDeviceSpecialization(Hashtable<Long, Attribute> map) {
		super(map);
		createNumericValues();
	}

	private void createNumericValues()
	{
		Hashtable<Long,Attribute> mandatoryAttributes = new Hashtable<Long,Attribute>();

		//from Part 10408: Handle=1
		ASN_Handle handle = new ASN_Handle(1);
		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_ID_HANDLE, 
				new Attribute(Nomenclature.MDC_ATTR_ID_HANDLE,
						handle));



		ASN_OIDType oid = new ASN_OIDType(Nomenclature.MDC_PRESS_BLD_NONINV);

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

		MetricStructureSmall mStructSmall;
		mStructSmall = new MetricStructureSmall(new ASN_INTU8(Nomenclature.MS_STRUCT_COMPOUND_FIX),
												new ASN_INTU8(3));

		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_METRIC_STRUCT_SMALL,
				new Attribute(Nomenclature.MDC_ATTR_METRIC_STRUCT_SMALL, mStructSmall));
		
		MetricIdList mIdList = new MetricIdList();
		mIdList.addMember(new ASN_OIDType(Nomenclature.MDC_PRESS_BLD_NONINV_SYS));
		mIdList.addMember(new ASN_OIDType(Nomenclature.MDC_PRESS_BLD_NONINV_DIA));
		mIdList.addMember(new ASN_OIDType(Nomenclature.MDC_PRESS_BLD_NONINV_MEAN));
		/* Mandatory attributes for standard configuration: */				
		//from Part 10408: Unit-Code=MDC_DIM_DEGC
		ASN_OIDType unitOid = new ASN_OIDType(Nomenclature.MDC_DIM_MMHG);
		mandatoryAttributes.put((long)Nomenclature.MDC_ATTR_UNIT_CODE, 
				new Attribute(Nomenclature.MDC_ATTR_UNIT_CODE,
						unitOid));

		//from Part 10408: Attribute-Value-Map {(MDC_ATTR_NU_VAL_OBS_BASIC,2),(MDC_ATTR_TIME_STAMP_ABS,8)}
		AttrValMap avm = new AttrValMap();

		AttrValMapEntry []entries = new AttrValMapEntry[2];
		ASN_OIDType attrId1 = new ASN_OIDType(Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_BASIC);


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
