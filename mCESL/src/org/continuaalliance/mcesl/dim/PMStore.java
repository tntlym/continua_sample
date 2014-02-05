package org.continuaalliance.mcesl.dim;
import java.util.ArrayList;
import java.util.Hashtable;

import org.continuaalliance.mcesl.utils.Nomenclature;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.SegmentInfo;
public class PMStore extends DIMObject{
	private ArrayList<SegmentInfo> segmentInfoList = null;
	public PMStore (Hashtable<Long,Attribute> attributeList) {
		super(attributeList);
	}
	
	public int getNomenclatureCode (){
		return Nomenclature.MDC_MOC_VMO_PMSTORE;
	}
	
	public void addSegmentInfo(SegmentInfo segElement)
	{
		if(segmentInfoList == null)
		{
			segmentInfoList = new ArrayList<SegmentInfo>();
		}
		segmentInfoList.add(segElement);
	}
}



