package org.continuaalliance.mcesl.dim;
import java.util.Hashtable;
import java.util.Iterator;

import android.util.Log;

public abstract class DIMObject {
	protected Hashtable<Long,Attribute> attributeList;

	public DIMObject () {}

	public DIMObject (Hashtable<Long,Attribute> attributes)  {
		Log.i("DIM","In DIMObject");
		this.attributeList = new Hashtable<Long,Attribute>();
		addAttributes(attributes);
		Log.i("DIM","DIMObject end");
	}

	public Attribute getAttribute(int attributeId){
		Log.i("DIM","getAttribute start ");
		return attributeList.get(new Long(attributeId));
	}

	public Attribute getAttribute(String friendlyName){
		Iterator<Attribute> i =  attributeList.values().iterator();
		Attribute attr;
		while (i.hasNext()) {
			attr = (Attribute)i.next();
			if (attr.getAttributeName().equals(friendlyName))
				return attr;
		}
		return null;
	}

	public void addAttribute(Attribute attr){
		attributeList.put(new Long(attr.getAttributeID()), attr);
	}

	public abstract int getNomenclatureCode ();


	private void addAttributes(Hashtable<Long,Attribute> attributes){ 
		/* Add attributes to attribute list */
		Iterator<Attribute> i = attributes.values().iterator();
		while (i.hasNext()){
			addAttribute(i.next());
		}
	}
}

