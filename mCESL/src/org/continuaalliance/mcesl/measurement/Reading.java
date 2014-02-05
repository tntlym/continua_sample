package org.continuaalliance.mcesl.measurement;

/**
 * Reading.java: Encapsulates measurement data, datatype and its unit to send it to application 
 * @author Vignet
 */
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Reading implements Serializable
{
	private int dataType;
	private ArrayList<Object> data;
	private int unitCode;
	private ArrayList<Integer> metricIDList;
	
	/**
	 * Returns the data associated to the Reading
	 * @return ArrayList<Object>
	 */
	public ArrayList<Object> getData() {
		return data;
	}
	
	/**
	 * Sets data type related to reading
	 * @param dataType int
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	
	/**
	 * Returns MetricID list if available else null
	 * @return ArrayList<Integer> 
	 */
	public ArrayList<Integer> getMetricIDList()
	{
		return metricIDList;
	}
	
	/**
	 * Returns the data type related to reading 
	 * @return int
	 */
	public int getDataType() {
		return dataType;
	}
	
	/**
	 * Sets unit code for the Reading
	 * @param unitCode int
	 */
	public void setUnitCode(int unitCode) {
		this.unitCode = unitCode;
	}
	/**
	 * Returns the unit code for the reading.
	 * @return int
	 */
	public int getUnitCode() {
		return unitCode;
	}
	
	/**
	 * Adds the object to measurement
	 * @param obj
	 */
	public void addData(Object obj)
	{
		if(data == null)
		{
			data = new ArrayList<Object>();
		}
		data.add(obj);
	}
	
	/**
	 * Adds metric id to list of metric ids.
	 * @param metricID
	 */
	public void addMetricID(int metricID)
	{
		if(metricIDList == null)
		{
			metricIDList = new ArrayList<Integer>();
		}
		metricIDList.add(metricID);
	}
}
