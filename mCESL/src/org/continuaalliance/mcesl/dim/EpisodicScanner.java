package org.continuaalliance.mcesl.dim;

import java.util.Hashtable;

import org.continuaalliance.mcesl.utils.Nomenclature;

public class EpisodicScanner extends DIMObject{

	public EpisodicScanner (Hashtable<Long,Attribute> attributeList) {
		super(attributeList);
	}
	@Override
	public int getNomenclatureCode() {

		return Nomenclature.MDC_MOC_SCAN_CFG_EPI;
	}

}
