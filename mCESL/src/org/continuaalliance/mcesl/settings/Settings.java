/*Copyright © 2011, Continua Health Alliance.
 *All rights reserved.
 *Terms and conditions of use are stated in the Reference Code License Agreement.
 */

package org.continuaalliance.mcesl.settings;

/*******************************
 Developed by Vignet for Continua.
 *******************************/
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.continuaalliance.mcesl.wan.SettingItemNames;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class Settings {

	private static SharedPreferences mceslPreferences = null;
	private static Settings instance = null;

	Map<SettingItemNames, Object> defaultSettings = new HashMap<SettingItemNames, Object>();

	// Called only in First run of the app
	public void createDefaultSettings() throws InvalidSettingsException {
		defaultSettings.put(SettingItemNames.SharedPreference_Name,
				"MCSELRefAppPreferences");
		// Set values from map to SharedPref
		/*
		 * defaultSettings.put(SettingItemNames.server_url,"continua.bxihealth.com"
		 * ); defaultSettings.put(SettingItemNames.portNumber,"8080");
		 * defaultSettings.put(SettingItemNames.serviceName,
		 * "/axis2/services/DeviceObservationConsumer_ServiceVanilla");
		 * defaultSettings.put(SettingItemNames.serviceAction,
		 * "urn:ihe:pcd:dec:2010:DeviceObservationConsumer_Service");
		 */
		defaultSettings.put(SettingItemNames.server_url, "195.212.190.241");
		defaultSettings.put(SettingItemNames.portNumber, "9080");
		defaultSettings
				.put(SettingItemNames.serviceName,
						"/ConsumeDeviceObservationWeb/DeviceObservationConsumer_Service");
		defaultSettings.put(SettingItemNames.serviceAction,
				"urn:ihe:pcd:dec:2009:DeviceObservationConsumer_Service");

		defaultSettings.put(SettingItemNames.presetindex, 1);
		defaultSettings.put(SettingItemNames.loggingenabled, false);
		defaultSettings.put(SettingItemNames.runfirsttime, 1);
		SharedPreferences.Editor editor = mceslPreferences.edit();
		for (SettingItemNames e : SettingItemNames.values()) {
			if (defaultSettings.get(e) == null)
				throw new InvalidSettingsException("Invalid Settings Exception");
			set(e, defaultSettings.get(e), editor);
		}
		editor.commit();
		// ApplicationLogger.getApplicationLogger()
		// .writeSettingsToApplicationLogs();
	}

	private Settings() { // So that this class cannot be initialised outside
	}

	public static Settings getInstance() {
		if (instance == null)
			instance = new Settings();
		return instance;
	}

	public static void initSettings(Context context) {
		mceslPreferences = context.getSharedPreferences(
				SettingItemNames.SharedPreference_Name.toString(), 0);

	}

	public Object get(SettingItemNames key) {
		try {
			int integerVal = mceslPreferences.getInt(key.toString(), 0);
			return integerVal;
		} catch (ClassCastException e) {
		}
		try {
			long longVal = mceslPreferences.getLong(key.toString(), 0);
			return longVal;
		} catch (ClassCastException e) {
		}
		try {
			float floatVal = mceslPreferences.getFloat(key.toString(), 0);
			return floatVal;
		} catch (ClassCastException e) {
		}
		try {
			String stringVal = mceslPreferences.getString(key.toString(), "");
			return stringVal;
		} catch (ClassCastException e) {
		}
		try {
			boolean booleanVal = mceslPreferences.getBoolean(key.toString(),
					false);
			return booleanVal;
		} catch (ClassCastException e) {
		}
		return null; // Should not be reached
	}

	public void set(SettingItemNames key, Object valueToSet) {
		SharedPreferences.Editor editor = mceslPreferences.edit();
		set(key, valueToSet, editor);
		editor.commit();
		// ApplicationLogger.getApplicationLogger()
		// .writeSettingsToApplicationLogs();
	}

	public void set(String key, String valueToSet) {
		SharedPreferences.Editor editor = mceslPreferences.edit();
		editor.putString(key, valueToSet);
		editor.commit();
		// ApplicationLogger.getApplicationLogger()
		// .writeSettingsToApplicationLogs();
	}

	private void set(SettingItemNames key, Object valueToSet,
			SharedPreferences.Editor editor) {
		if (valueToSet instanceof Integer) {
			editor.putInt(key.toString(), (Integer) valueToSet);
		} else if (valueToSet instanceof Float) {
			editor.putFloat(key.toString(), (Float) valueToSet);
		} else if (valueToSet instanceof Long) {
			editor.putLong(key.toString(), (Long) valueToSet);
		} else if (valueToSet instanceof String) {
			editor.putString(key.toString(), (String) valueToSet);
		} else if (valueToSet instanceof Boolean) {
			editor.putBoolean(key.toString(), (Boolean) valueToSet);
		}
		return;
	}

	public boolean backup(ObjectOutputStream oos) {
		boolean retVal = false;
		if (oos == null)
			return retVal;
		try {
			Hashtable<SettingItemNames, Object> preferencesHashTable = new Hashtable<SettingItemNames, Object>();
			for (SettingItemNames e : SettingItemNames.values()) {
				preferencesHashTable.put(e, get(e));
			}
			oos.writeObject(preferencesHashTable);
			retVal = true;
		} catch (IOException e) {
			Log.e("MCesl",
					"Error while writing the Shared Preference to the file!! "
							+ e.toString());
		}
		return retVal;
	}

	public boolean restore(ObjectInputStream ois) {
		boolean retVal = false;
		try {
			@SuppressWarnings("unchecked")
			Hashtable<SettingItemNames, Object> preferencesHashTable = (Hashtable<SettingItemNames, Object>) ois
					.readObject();
			if (preferencesHashTable == null
					|| preferencesHashTable.size() <= 0)
				return retVal;
			Enumeration<SettingItemNames> enumerator = preferencesHashTable
					.keys();
			SharedPreferences.Editor editor = mceslPreferences.edit();
			while (enumerator.hasMoreElements()) {
				SettingItemNames key = enumerator.nextElement();
				set(key, preferencesHashTable.get(key), editor);
			}
			editor.commit();
			retVal = true;
		} catch (ClassNotFoundException e) {
			Log.e("MCesl",
					"Error while reading the Shared Preference from the file!! "
							+ e.toString());
		} catch (IOException e) {
			Log.e("MCesl",
					"Error while reading the Shared Preference from the file!! "
							+ e.toString());
		}
		return retVal;
	}

}
