package org.continuaalliance.mcesl.dim;

import java.util.Hashtable;

import org.continuaalliance.mcesl.utils.Nomenclature;

public abstract class Metric extends DIMObject {
		
	public Metric(Hashtable<Long,Attribute> attributes) {
		super(attributes);
	}
	
	public int getNomenclatureCode (){
		return Nomenclature.MDC_MOC_VMO_METRIC;
	}
	
}