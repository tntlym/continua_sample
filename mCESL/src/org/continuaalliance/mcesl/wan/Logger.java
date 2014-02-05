/*Copyright © 2011, Continua Health Alliance.
 *All rights reserved.
 *Terms and conditions of use are stated in the Reference Code License Agreement.
 */

package org.continuaalliance.mcesl.wan;

/*******************************
Developed by Vignet for Continua.
*******************************/
import org.ca.cesl.wan.encoding.api.IWanLogger;
import org.continuaalliance.mcesl.settings.Settings;

import android.util.Log;

public class Logger implements IWanLogger{

	public static final String tag = "MCESL";
	public static boolean getLoggingEnabled()
	{
		/*For temp use*/
		/*Object o = Settings.getInstance().get(SettingItemNames.loggingenabled);
		if(o instanceof Integer)
		{
			Integer retInt = (Integer)Settings.getInstance().get(SettingItemNames.loggingenabled);
			if(retInt != 0)
				return true;
			return false;
		}
		/*For temp use*/
		return Settings.getInstance().get(SettingItemNames.loggingenabled) == null? false :(Boolean) Settings.getInstance().get(SettingItemNames.loggingenabled);
	}
	public boolean isDebugLogLevel() {
		// Auto-generated method stub
		return getLoggingEnabled(); //android.util.Log.isLoggable(tag, LOGLEVEL_DEBUG);
	}

	public boolean isErrorLogLevel() {
		// Auto-generated method stub
		return getLoggingEnabled();// android.util.Log.isLoggable(tag, LOGLEVEL_ERROR);
	}

	public boolean isInfoLogLevel() {
		// Auto-generated method stub
		return getLoggingEnabled(); // Log.isLoggable(tag, LOGLEVEL_INFO);
	}

	public void logDebug(String arg0) {
		// Auto-generated method stub
		if (isDebugLogLevel()) {
			Log.d(tag, arg0);
		}
		
		
	}
	void functionEntered(String functionName)
	{
		logInfo("Entered function : " + functionName );
		
	}
	void functionLeaving(String functionName)
	{
		logInfo("Leaving function : " + functionName );
		
	}
	public void logDebugException(Exception arg0) {
		// Auto-generated method stub
		if (isDebugLogLevel()) {
			Log.d(tag, "", arg0);
		}
	}

	public void logError(String arg0) {
		// Auto-generated method stub
		if (isDebugLogLevel()) {
			Log.e(tag, arg0);
		}
	}

	public void logErrorException(Exception arg0) {
		// Auto-generated method stub
		if (isErrorLogLevel()) {
			Log.e(tag, "", arg0);
		}
	}

	public void logErrorWithException(String arg0, Exception arg1)
			throws Exception {
		// Auto-generated method stub
		if (isErrorLogLevel()) {
			Log.e(tag, arg0, arg1);
			throw arg1;
		}
		
	}

	public void logInfo(String arg0) {
		// Auto-generated method stub
		if (isInfoLogLevel()) {
			Log.i(tag, arg0);
		}
	}

	public void logInfoException(Exception arg0) {
		// Auto-generated method stub
		if (isInfoLogLevel()) {
			Log.i(tag, "", arg0);
			
		}
	}

	public void setLogLevel(int arg0) {
		// Auto-generated method stub
		
	}

}
