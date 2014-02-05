/*Copyright © 2011, Continua Health Alliance.
 *All rights reserved.
 *Terms and conditions of use are stated in the Reference Code License Agreement.
 */

package org.continuaalliance.mcesl.wan;

/*******************************
Developed by Vignet for Continua.
*******************************/
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

public class RangeChecker 
{
	EditText edittext; 
	double minRange;
	double maxRange;
	String alertMessage;
	Context context;
	public RangeChecker(EditText edittext, double minRange, double maxRange,
			String alertMessage, Context context) {
		this.edittext = edittext;
		this.minRange = minRange;
		this.maxRange = maxRange;
		this.alertMessage = alertMessage;
		this.context = context;
	}
	static public boolean isValueNull(EditText edittext,Context context,String name)
	{
		String sValue= edittext.getText().toString();
		if(sValue == null || sValue.compareTo("") == 0)
		{
			Toast.makeText(context,name + " cannot be empty!" ,Toast.LENGTH_LONG).show();//, Toast.LENGTH_LONG).show();
			return true;
		}
		if(sValue.compareTo("0") == 0)
		{
			Toast.makeText(context,"Value cannot be 0!" ,Toast.LENGTH_LONG).show();//, Toast.LENGTH_LONG).show();
			return true;
		}
		return false;
	}
	public boolean isValueInRange()
	{
		String sValue= edittext.getText().toString();
		
		double value = Double.parseDouble(sValue);
		if ((Double.compare(value, minRange)>=0) &&(Double.compare(maxRange, value)>=0)) {
			return true;
		}		
		edittext.setText("");
		Toast.makeText(context,alertMessage ,Toast.LENGTH_LONG).show();//, Toast.LENGTH_LONG).show();		
		return false;
		
	}
	
}
