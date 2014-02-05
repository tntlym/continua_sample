package org.continuaalliance.mcesl.wan;

import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import org.continuaalliance.mcesl.utils.Nomenclature;

import android.content.Context;
import android.util.Log;

public class UploaderThread extends Thread {

	private final String TAG = "UploaderThread";
	private boolean stopUploaderThread = false;
	private LinkedBlockingQueue<BodyVital> uploadQueue;
	private int deviceCode = -1;
	private Context uiContext;
	private Semaphore mutex;

	public void stopUploaderThread(boolean status) {
		stopUploaderThread = status;
	}

	public UploaderThread(int code, LinkedBlockingQueue<BodyVital> queue,
			Context context, Semaphore uiMutex) {

		uploadQueue = queue;
		deviceCode = code;
		uiContext = context;
		mutex = uiMutex;
	}

	public void run() {
		Log.i(TAG + " : Uploader Thread", "Uploader thread started.");

		while (!stopUploaderThread) {
				try {
					if (!uploadQueue.isEmpty()) {
						
						mutex.acquire();
						Log.i(TAG, "Semaphore Acquired.");
						Log.i(TAG, "Elements in QUEUE : "+ uploadQueue.size());
						sendMeasurement(deviceCode, uploadQueue.remove());
						mutex.release();
						Log.i(TAG, "Semaphore Released.");
						
					}
					else{
						Log.i(TAG, "Uploader thread going to sleep.");
						sleep(10000);
					}

				} catch (InterruptedException e) {
		
					e.printStackTrace();
					Log.i(TAG, "Uploader thread INTERRUPTED.");
				}

		}

		Log.i(TAG + " : Uploader Thread", "Exiting Uploader Thread.");
	}

	private final int MS_PER_HOUR = 60 * 60 * 1000;
	private final int MS_PER_MINUTE = 60 * 1000;
	private final int MAX_LENGTH = 24;

	private String getCurrentTime() {
		StringBuffer buff = new StringBuffer(MAX_LENGTH);
		Calendar calendar = Calendar.getInstance();
		buff.append(pad(calendar.get(Calendar.YEAR), 4));
		buff.append(pad(calendar.get(Calendar.MONTH) + 1, 2));
		buff.append(pad(calendar.get(Calendar.DATE), 2));
		buff.append(pad(calendar.get(Calendar.HOUR_OF_DAY), 2));
		buff.append(pad(calendar.get(Calendar.MINUTE), 2));
		buff.append(pad(calendar.get(Calendar.SECOND), 2));
		if (calendar.get(Calendar.MILLISECOND) != 0) {
			buff.append('.');
			buff.append(calendar.get(Calendar.MILLISECOND));
		}
		int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
		if (zoneOffset < 0) {
			buff.append('-');
			zoneOffset = zoneOffset * -1;
		} else {
			buff.append('+');
		}
		int zoneOffsetHours = zoneOffset / MS_PER_HOUR;
		int zoneOffsetMins = (zoneOffset - (zoneOffsetHours * MS_PER_HOUR))
				/ MS_PER_MINUTE;
		buff.append(pad(zoneOffsetHours, 2));
		buff.append(pad(zoneOffsetMins, 2));
		return buff.toString();
	}

	private String pad(int value, int span) {
		String valueString = Integer.toString(value);
		int padAmount = span - valueString.length();
		if (padAmount <= 0) {
			return valueString;
		}
		return "0000000000".substring(0, span - valueString.length())
				+ valueString;
	}

	public void sendMeasurement(int deviceCode, BodyVital vital) {

		Log.i(TAG, "Entered sendMeasurement().");

		// Confirming that the registered device code and
		// specialization are same.

		if(vital == null){
			Log.i(TAG, "BodyVital object is NULL.");
			return;
		}
		
		if (deviceCode == vital.getSpecialization()) {

			Log.i(TAG, "Device code is same as specialization.");

			switch (deviceCode) {
			case 4100: // Pulse Oximeter
				sendPulse(vital);
				break;

			case 4103: // Blood Pressure
				sendBP(vital);
				break;

			case 4104: // Thermometer
				sendTemperature(vital);
				break;

			case 4111: // Weight Scale
				sendWeight(vital);
				break;

			case 4113: // Blood Glucose
				sendGlucose(vital);
				break;
			}
		} else {
			Log.e(TAG, "Device code does not the specialization.");
		}

	}

	/**
	 * @param value
	 */
	private void sendWeight(BodyVital value) {

		Log.i(TAG, "In sendWeight...");

		Properties observationproperties = new Properties();

		observationproperties.setProperty(WANConstants.TIMESTAMP,
				getCurrentTime());

		
		if(value.readings.containsKey(Nomenclature.MDC_MASS_BODY_ACTUAL)){
		
			observationproperties.setProperty(WANConstants.BODY_WEIGHT,
				value.readings.get(Nomenclature.MDC_MASS_BODY_ACTUAL).get(WANConstants.VALUE).toString());
			
			observationproperties.setProperty(WANConstants.WEIGHT_UNIT,
					value.readings.get(Nomenclature.MDC_MASS_BODY_ACTUAL).get(WANConstants.UNITCODE).toString());
		}
		
		ObservationHelperWeight weightHelper = new ObservationHelperWeight(
				observationproperties);
		
		String successMsg = "Sent Weight : " + value;
		ObservationSender sender = new ObservationSender(weightHelper,
				successMsg, uiContext);
		sender.SendSync();
	}

	private void sendBP(BodyVital value) {

		
		Log.i(TAG, "METHOD : In sendBP...");
		if(value == null){
			Log.i(TAG +" : sensBP", "BodyVital object is NULL.");
		}
		Properties observationproperties = new Properties();

		observationproperties.setProperty(WANConstants.TIMESTAMP,
				getCurrentTime());
		if(value.readings.containsKey(Nomenclature.MDC_PRESS_BLD_NONINV_SYS)){

			observationproperties.setProperty(WANConstants.SYSTOLIC, value.readings
				.get(Nomenclature.MDC_PRESS_BLD_NONINV_SYS).get(WANConstants.VALUE).toString());
			
		
			Log.i("SYSTOLIC", "SYSTOLIC: "+value.readings
					.get(Nomenclature.MDC_PRESS_BLD_NONINV_SYS).get(WANConstants.VALUE).toString());
			
			Log.i("UNIT-CODE", "UNIT-CODE: "+value.readings
					.get(Nomenclature.MDC_PRESS_BLD_NONINV_SYS).get(WANConstants.UNITCODE).toString());
			
			observationproperties.setProperty(WANConstants.UNIT_CODE,
					value.readings
					.get(Nomenclature.MDC_PRESS_BLD_NONINV_SYS).get(WANConstants.UNITCODE).toString());
			
		
		}

		if(value.readings.containsKey(Nomenclature.MDC_PRESS_BLD_NONINV_DIA)){
		
			Log.i("SYSTOLIC", "DIASTOLIC: "+value.readings
					.get(Nomenclature.MDC_PRESS_BLD_NONINV_DIA).get(WANConstants.VALUE).toString());
			observationproperties.setProperty(WANConstants.DIASTOLIC,
				value.readings.get(Nomenclature.MDC_PRESS_BLD_NONINV_DIA).get(WANConstants.VALUE).toString());
			
		}
		
		if(value.readings.containsKey(Nomenclature.MDC_PRESS_BLD_NONINV_MEAN)){
		
			Log.i("SYSTOLIC", "MEAN: "+value.readings
					.get(Nomenclature.MDC_PRESS_BLD_NONINV_MEAN).get(WANConstants.VALUE).toString());
			observationproperties.setProperty(WANConstants.MEAN, value.readings
				.get(Nomenclature.MDC_PRESS_BLD_NONINV_MEAN).get(WANConstants.VALUE).toString());
			
		}
		
		ObservationHelperBloodPressure bpHelper = new ObservationHelperBloodPressure(
				observationproperties);

		
		String successMsg = "Sent Systolic : " + value;
		
		ObservationSender sender = new ObservationSender(bpHelper,
				successMsg, uiContext);
		sender.SendSync();
	}

	private void sendPulse(BodyVital value) {

		
		Properties observationproperties = new Properties();

		observationproperties.setProperty(WANConstants.TIMESTAMP,
				getCurrentTime());
	
		if(value.readings.containsKey(Nomenclature.MDC_PULS_OXIM_SAT_O2)){
		observationproperties.setProperty(WANConstants.SPO2, value.readings
				.get(Nomenclature.MDC_PULS_OXIM_SAT_O2).get(WANConstants.VALUE).toString());
		
		
		observationproperties.setProperty(WANConstants.SPO2_UNIT, value.readings
				.get(Nomenclature.MDC_PULS_OXIM_SAT_O2).get(WANConstants.UNITCODE).toString());
		}
		
		if(value.readings.containsKey(Nomenclature.MDC_PULS_OXIM_PULS_RATE)){
		observationproperties.setProperty(WANConstants.PULSERATE,
				value.readings.get(Nomenclature.MDC_PULS_OXIM_PULS_RATE).get(WANConstants.VALUE).toString());
		
		
		observationproperties.setProperty(WANConstants.PULSERATE_UNIT,
				value.readings.get(Nomenclature.MDC_PULS_OXIM_PULS_RATE).get(WANConstants.UNITCODE).toString());
		
		Log.i("", "pulse unit code"+value.readings.get(Nomenclature.MDC_PULS_OXIM_PULS_RATE).get(WANConstants.UNITCODE).toString());
		
		}
		
		
		ObservationHelperPulseOx pulseoxHelper = new ObservationHelperPulseOx(
				observationproperties);

		
		String successMsg = "";
		ObservationSender sender = new ObservationSender(pulseoxHelper,
				successMsg, uiContext);
		sender.SendSync();
		// }

	}

	private void sendGlucose(BodyVital value) {

		Properties observationproperties = new Properties();

		observationproperties.setProperty(WANConstants.TIMESTAMP,
				getCurrentTime());
		
		if(value.readings.containsKey(Nomenclature.MDC_CONC_GLU_CAPILLARY_WHOLEBLOOD)){
		observationproperties.setProperty(
				WANConstants.GLU_CAPILLARY_WHOLEBLOOD,
				value.readings.get(Nomenclature.MDC_CONC_GLU_CAPILLARY_WHOLEBLOOD)
				.get(WANConstants.VALUE).toString());
		
		observationproperties.setProperty(
				WANConstants.GLU_UNIT,
				value.readings.get(Nomenclature.MDC_CONC_GLU_CAPILLARY_WHOLEBLOOD)
				.get(WANConstants.UNITCODE).toString());
		}
		
		
		ObservationHelperGlucose glucoseHelper = new ObservationHelperGlucose(
				observationproperties);
		
		
		String successMsg = "Sent Blood Glucose : " + value;
		ObservationSender sender = new ObservationSender(glucoseHelper,
				successMsg, uiContext);
		sender.SendSync();

	}

	private void sendTemperature(BodyVital value) {

		Properties observationproperties = new Properties();
		
		observationproperties.setProperty(WANConstants.TIMESTAMP,
				getCurrentTime());
		
		if(value.readings.containsKey(Nomenclature.MDC_TEMP_BODY)){
		observationproperties.setProperty(WANConstants.BODY_TEMPERATURE,
				value.readings.get(Nomenclature.MDC_TEMP_BODY).get(WANConstants.VALUE).toString());
		
		observationproperties.setProperty(WANConstants.TEMPERATURE_UNIT,
				value.readings.get(Nomenclature.MDC_TEMP_BODY).get(WANConstants.UNITCODE).toString());
		}
		
		/*if(value.readings.containsKey(WANConstants.UNITCODE)){
		observationproperties.setProperty(WANConstants.UNIT_CODE,
				String.valueOf(value.readings.get(WANConstants.UNITCODE)));
		}*/
		
		ObservationHelperThermometer thermoHelper = new ObservationHelperThermometer(
				observationproperties);
		
		String successMsg = "Sent Temeprature : " + value;
		ObservationSender sender = new ObservationSender(thermoHelper,
				successMsg, uiContext);
		sender.SendSync();

	}

}
