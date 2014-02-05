package org.continuaalliance.mcesl.dim;

import java.util.Hashtable;

import org.continuaalliance.mcesl.utils.Nomenclature;

public class Numeric extends Metric{

	public Numeric (Hashtable<Long,Attribute> attributeList) {
		super(attributeList);
	}
	
	public int getNomenclatureCode (){
		return Nomenclature.MDC_MOC_VMO_METRIC_NU;
	}
}
