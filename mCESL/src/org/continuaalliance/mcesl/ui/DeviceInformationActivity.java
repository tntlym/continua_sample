package org.continuaalliance.mcesl.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

import org.continuaalliance.mcesl.appData.AppMDSObject;
import org.continuaalliance.mcesl.appData.AppNumeric;
import org.continuaalliance.mcesl.appData.AppPMStore;
import org.continuaalliance.mcesl.appData.DeviceObject;
import org.continuaalliance.mcesl.appData.MeasurementEvent;
import org.continuaalliance.mcesl.appData.StateEvent;
import org.continuaalliance.mcesl.communication.EConnectionType;
import org.continuaalliance.mcesl.controller.MCESLLib;
import org.continuaalliance.mcesl.measurement.DeviceMeasurement;
import org.continuaalliance.mcesl.measurement.Measurement;
import org.continuaalliance.mcesl.measurement.Reading;
import org.continuaalliance.mcesl.utils.Nomenclature;
import org.continuaalliance.mcesl.utils.Constants;
import org.continuaalliance.mcesl.wan.BodyVital;
import org.continuaalliance.mcesl.wan.UploaderThread;
import org.continuaalliance.mcesl.wan.WANConstants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceInformationActivity extends Activity {


	private final String TAG = "DeviceInformationActivity";

	final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	private ListView listOfDevices = null;
	private SimpleAdapter adapter = null;

	private UploaderThread uploaderThread;
	private LinkedBlockingQueue<BodyVital> uploadQueue = new LinkedBlockingQueue<BodyVital>();
	private int deviceCode = -1;

	private Button connectButton;

	Semaphore mutex = new Semaphore(1);

	Boolean connected = false;
	org.continuaalliance.mcesl.appData.Device dev = null;
	int ObjectID = -1;
	MCESLLib gLib = null;
	BluetoothAdapter mBluetoothAdapter;
	BluetoothDevice mDevice;
	private int mDeviceIndex = 0;
	private BluetoothDevice[] mAllBondedDevices;
	int mDeviceCode = -1;
	private ImageView vigImageView = null;
	private ImageView continuaImageView = null;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_data);

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "NO Bluetooth Available", Toast.LENGTH_LONG);
			finish();
			return;
		}
		/** Clicking on Continua logo, Continua website should be opened in browser **/
		continuaImageView  = (ImageView) findViewById(R.id.continua_logo);
		continuaImageView .setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        Context context = getApplicationContext();  
		    	String url = "http://www.continuaalliance.org/";  
		    	Intent i = new Intent(Intent.ACTION_VIEW);  
		    	Uri u = Uri.parse(url);  
		    	i.setData(u);  
		    	try {  
		    	  startActivity(i);  
		    	} catch (ActivityNotFoundException e) {  	    	    
		    	  Toast toast = Toast.makeText(context, "Browser not found.", Toast.LENGTH_SHORT);  
		    	  toast.show();
		    	}  
		    }
		});
		/** Clicking on Vignet logo, vignet website should be opened in browser **/
		vigImageView = (ImageView) findViewById(R.id.vignet_logo);
		vigImageView.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        Context context = getApplicationContext();  
		    	String url = "http://www.vignetcorp.com";  
		    	Intent i = new Intent(Intent.ACTION_VIEW);  
		    	Uri u = Uri.parse(url);  
		    	i.setData(u);  
		    	try {  
		    	  startActivity(i);  
		    	} catch (ActivityNotFoundException e) {  
		    	    
		    	  Toast toast = Toast.makeText(context, "Browser not found.", Toast.LENGTH_SHORT);  
		    	  toast.show();
		    	}  
		    }
		});
		
		// ****************************************************************
		// Retrieving the Device Code from Intent Extras
		String key = "Specialisation";

		Bundle bundle = this.getIntent().getExtras();
		deviceCode = bundle.getInt(key);

		Log.i("MyTAG", "key : " + deviceCode);

		setImage(deviceCode, false);

		// ****************************************************************
		// Creating and starting thread to upload data to the server
		uploaderThread = new UploaderThread(deviceCode, uploadQueue,
				getApplicationContext(), mutex);

		uploaderThread.start();

		
		listOfDevices = (ListView) findViewById(R.id.readings_list);
		listOfDevices.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listOfDevices.setStackFromBottom(true);

		int[] to = { R.id.reading_param, R.id.reading_value };// R.id.deviceImage,
		String[] from = { "param", "value" };// "icon",

		adapter = new SimpleAdapter(getApplicationContext(),
				(List<? extends Map<String, ?>>) list, R.layout.list_item,
				from, to);

		listOfDevices.setAdapter(adapter);

		adapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				super.onChanged();
				Log.i("TAG", "Data Set Changed");
				listOfDevices.setSelection(adapter.getCount() - 1);
			}
		});

		/************************ CODE RELATED TO mCESL *****************************/
		Log.e("", "starting ManagerController");
		System.out.println("starting ManagerController");
		
		
		gLib = new MCESLLib(this);
		gLib.addTransport(EConnectionType.BT_HDP);
		gLib.addDeviceSpecialization(deviceCode);
		gLib.start();

		
		this.registerReceiver(this.mMeasurementRcvdEvent, new IntentFilter(
				org.continuaalliance.mcesl.utils.Constants.MEASUREMENT_RECVD));
		
		this.registerReceiver(this.mStateChangeRcvd, new IntentFilter(
				Constants.DEVICE_STATE_CHANGED));
		
		this.registerReceiver(this.mDeviceInfoEvent, new IntentFilter(
				Constants.DEVICE_INFO));
		
		this.registerReceiver(this.mDeviceConnected, new IntentFilter(
				Constants.HDP_NEW_CONNECTION));
		
		this.registerReceiver(this.mMDSReceived, new IntentFilter(
				Constants.MDS_OBJECT));

		// ****************************************************************
		// Setting Listener to the Connect Button.
		

		connectButton = (Button) findViewById(R.id.connect_btn);
		connectButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				connectButton.setClickable(false);
				connectButton.setTextColor(R.color.Gray);
				setImage(deviceCode, true);

				if (!connected) {
					mAllBondedDevices = (BluetoothDevice[]) mBluetoothAdapter
							.getBondedDevices().toArray(new BluetoothDevice[0]);

					if (mAllBondedDevices.length > 0) {
						int deviceCount = mAllBondedDevices.length;
						if (mDeviceIndex < deviceCount)
							mDevice = mAllBondedDevices[mDeviceIndex];
						else {
							mDeviceIndex = 0;
							mDevice = mAllBondedDevices[0];
						}
						String[] deviceNames = new String[deviceCount];
						int i = 0;
						for (BluetoothDevice device : mAllBondedDevices) {
							deviceNames[i++] = device.getName();
						}
						SelectDeviceDialogFragment deviceDialog = SelectDeviceDialogFragment
								.newInstance(deviceNames, mDeviceIndex);
						deviceDialog.show(getFragmentManager(), "deviceDialog");
					}
				} else {
					org.continuaalliance.mcesl.appData.Device dev = gLib
							.getDevice(ObjectID);
					if (dev != null) {
						Log.e("", "Calling abort from APp");
						dev.abort();
						Log.e("", "abort called");
					}
				}
			}
		});


	}// onCreate ends

	
	private void setImage(int key, boolean colour) {
		ImageView icon = (ImageView) findViewById(R.id.device_icon);

		switch (key) {
		case 4100:
			if (colour)
				icon.setImageResource(R.drawable.pulseox);
			else {
				icon.setImageResource(R.drawable.pulse);
			}
			break;

		case 4103:
			if (colour)
				icon.setImageResource(R.drawable.bloodpressure);
			else {
				icon.setImageResource(R.drawable.blood_pressure_monitor);
			}
			break;

		case 4104:
			if (colour)
				icon.setImageResource(R.drawable.thermo);
			else {
				icon.setImageResource(R.drawable.thermometer);
			}
			break;

		case 4111:
			if (colour)
				icon.setImageResource(R.drawable.scale);
			else {
				icon.setImageResource(R.drawable.weight_scale);
			}
			break;

		case 4113:
			if (colour)
				icon.setImageResource(R.drawable.glucose);
			else {
				icon.setImageResource(R.drawable.glucometer);
			}
			break;
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

		uploaderThread.stopUploaderThread(true);
		uploaderThread.interrupt();
		
		gLib.stop();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		this.unregisterReceiver(this.mMeasurementRcvdEvent);
		this.unregisterReceiver(this.mStateChangeRcvd);
		this.unregisterReceiver(this.mDeviceInfoEvent);
		this.unregisterReceiver(this.mDeviceConnected);
		this.unregisterReceiver(this.mMDSReceived);
		//gLib.stop();

	}

	private BroadcastReceiver mDeviceConnected = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			Log.i("", "CONNECTION ESTABLISHED");
			int obj = (Integer) intent
					.getSerializableExtra(Constants.HDP_NEW_CONNECTION);
			ObjectID = obj;
			connected = true;

		}

	};

	private BroadcastReceiver mMDSReceived = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			Log.e("", "MDS Object Received");

			AppMDSObject obj = (AppMDSObject) intent
					.getSerializableExtra(Constants.MDS_OBJECT);

			Log.e("", "CID for device ->" + obj.getDeviceID());
			ArrayList<AppNumeric> nums = obj.getNumerics();
			if (nums != null) {
				for (int i = 0; i < obj.getNumerics().size(); i++) {
					AppNumeric d = nums.get(i);
					Log.e("", "AppNumeric handle ->" + d.handle);
				}
			}
			ArrayList<AppPMStore> pms = obj.getPMStores();
			if (pms != null) {
				for (int j = 0; j < pms.size(); j++) {
					AppPMStore d = pms.get(j);
					Log.e("", "AppPM handle ->" + d.handle);
				}
			}

		}

	};

	private BroadcastReceiver mDeviceInfoEvent = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {

			Log.e("", "Broadcast recevd -- " + intent.getAction());
			final TextView rcvData = (TextView) findViewById(R.id.system_ID_data);
			final TextView modelData = (TextView) findViewById(R.id.system_model_data);
			final TextView nameData = (TextView) findViewById(R.id.system_name_data);
			DeviceObject obj = (DeviceObject) intent
					.getSerializableExtra(Constants.DEVICE_INFO);

			byte[] arr = obj.getSystemID();
			String data = "";
			for (int i = 0; i < arr.length; i++) {
				if (i == 0) {
					data = String.format("%02x", arr[i]);
				} else {
					data = String.format("%s %02x", data, arr[i]);
				}
			}
			Log.e("", "SystemID-->" + data);
			rcvData.setText(data);
			arr = obj.getSysModel();
			data = "";
			for (int j = 0; j < arr.length; j++) {
				if (j == 0) {
					data = String.format("%02x", arr[j]);
				} else {
					data = String.format("%s %02x", data, arr[j]);
				}
			}
			Log.e("", "SystemModel-->" + data);
			modelData.setText(new String(arr));
			arr = obj.getManufacturer();
			nameData.setText(new String(arr));
		}
	};

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	private BroadcastReceiver mMeasurementRcvdEvent = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {

			Log.e("", "Broadcast recevd -- " + intent.getAction());
			MeasurementEvent obj = (MeasurementEvent) intent
					.getSerializableExtra(Constants.MEASUREMENT_RECVD);
			{
				org.continuaalliance.mcesl.appData.EventObject retVal = obj
						.getEventObject();
				ArrayList<Measurement> retData = retVal.getMeasurements();

				Log.e("", "Size of measurements - " + retData.size());
				// Object.
				for (int i = 0; i < retData.size(); i++) {
					DeviceMeasurement m = (DeviceMeasurement) retData.get(i);

					Log.e("", "Specialization @ APP->" + m.getSpecialization());
					Log.e("", "personID->" + m.getPersonId());

					HashMap<Integer, Reading> r = m.getValues();

					Log.e("", "size of hashmap - >" + r.size());

					Set<Integer> keys = r.keySet();
					Object[] akeys = keys.toArray();
					String dataVal = "";

					BodyVital vitalObj = new BodyVital();
					vitalObj.setSpecialization(m.getSpecialization());
					vitalObj.setPersonId(m.getPersonId());

					// Filling all elements from a hashMap into one BodyVital
					// object.
					for (int j = 0; j < keys.size(); j++) {
						Log.e("", "Partition-->" + (Integer) akeys[j]);

						Reading reading = r.get(akeys[j]);

						if (reading.getDataType() == Nomenclature.MDC_ATTR_NU_VAL_OBS_BASIC) {
							// Get actual readings into an Array.
							ArrayList<Object> values = reading.getData();
							for (int k = 0; k < values.size(); k++) {
								Double val = (Double) values.get(k);
								Log.e("", "int object val - " + val);
								dataVal = Constants
										.getStringonID((Integer) akeys[j]);
								if (dataVal != null) {
									String withUnit = ""
											+ round(val, 2)
											+ " "
											+ Constants.getUnit(reading
													.getUnitCode());
									Map<String, Object> mapSpo2 = new HashMap<String, Object>();
									mapSpo2.put("param", dataVal);
									mapSpo2.put("value", withUnit);
									list.add(mapSpo2);

									// sendData(dataVal, val);
									// vitalObj.readings.put(dataVal, val);
									HashMap<Integer, Object> valueUnit = new HashMap<Integer, Object>();
									valueUnit.put(WANConstants.VALUE, val);
									valueUnit.put(WANConstants.UNITCODE,
											reading.getUnitCode());

									vitalObj.readings.put((Integer) akeys[j],
											valueUnit);
									Log.i("Key : " + (Integer) akeys[j]
											+ " Data Value", dataVal);
								}
							}
						} else if (reading.getDataType() == Nomenclature.MDC_ATTR_NU_VAL_OBS_SIMP) {
							ArrayList<Object> values = reading.getData();
							for (int k = 0; k < values.size(); k++) {
								Double val = (Double) values.get(k);
								Log.e("", "double object val - " + val);

								dataVal = Constants
										.getStringonID((Integer) akeys[j]);
								if (dataVal != null) {
									String withUnit = ""
											+ round(val, 2)
											+ " "
											+ Constants.getUnit(reading
													.getUnitCode());
									Map<String, Object> mapSpo2 = new HashMap<String, Object>();
									mapSpo2.put("param", dataVal);
									mapSpo2.put("value", withUnit);
									list.add(mapSpo2);

									// sendData(dataVal, val);
									HashMap<Integer, Object> valueUnit = new HashMap<Integer, Object>();
									valueUnit.put(WANConstants.VALUE, val);
									valueUnit.put(WANConstants.UNITCODE,
											reading.getUnitCode());

									vitalObj.readings.put((Integer) akeys[j],
											valueUnit);

									Log.i("Key : " + (Integer) akeys[j]
											+ " Data Value", dataVal);
								}
							}

							// Log.e("","object val - "+val);
						} else if (reading.getDataType() == Nomenclature.MDC_ATTR_NU_CMPD_VAL_OBS_BASIC) {
							ArrayList<Object> values = reading.getData();
							ArrayList<Integer> metricIDs = reading
									.getMetricIDList();
							for (int k = 0; k < values.size(); k++) {
								Double val = (Double) values.get(k);
								int mID = (int) metricIDs.get(k);
								Log.e("", "double object val - " + val);
								Log.e("", "MetricID - " + mID);
								dataVal = Constants
										.getStringonID((Integer) mID);
								if (dataVal != null) {
									String withUnit = ""
											+ round(val, 2)
											+ " "
											+ Constants.getUnit(reading
													.getUnitCode());
									Map<String, Object> mapSpo2 = new HashMap<String, Object>();
									mapSpo2.put("param", dataVal);
									mapSpo2.put("value", withUnit);
									list.add(mapSpo2);

									// sendData(dataVal, val);
									HashMap<Integer, Object> valueUnit = new HashMap<Integer, Object>();
									valueUnit.put(WANConstants.VALUE, val);
									valueUnit.put(WANConstants.UNITCODE,
											reading.getUnitCode());

									vitalObj.readings.put(mID, valueUnit);

									Log.i("Key : " + (Integer) akeys[j]
											+ " Data Value", dataVal);
								}
							}
						}

					}
					uploadQueue.add(vitalObj);
				}
			}

			adapter.notifyDataSetChanged();
		}

		/**
		 * @param dataVal
		 * @param val
		 */

	};

	
	private void sendData() {
		/************* CREATING BODYVITAL OBJECT TO UPLOAD *************/

		// Interrupt the sleeping uploaderThread
		for (int a = 0; a < 3; a++) {
			if (uploaderThread != null && uploaderThread.isAlive()) {
				uploaderThread.interrupt();
				Log.i(TAG, "Interrupting uploader thread.");
				break;
			}
		}
	}

	private BroadcastReceiver mStateChangeRcvd = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent intent) {

			Log.e("", "Broadcast recevd -- " + intent.getAction());
			final TextView stateData = (TextView) findViewById(R.id.current_state_value);

			StateEvent obj = (StateEvent) intent
					.getSerializableExtra(Constants.DEVICE_STATE_CHANGED);
			Log.e("", "Device state is " + obj.State);

			if (obj.State == StateEvent.ASSOCIATED) {
				stateData.setText("Associated State\n");
			} else if (obj.State == StateEvent.CONNECTED) {
				stateData.setText("Connected State\n");
				Button connectButton = (Button) findViewById(R.id.connect_btn);

				connectButton.setClickable(true);
				connectButton.setTextColor(R.color.Gray);
				//
				setImage(mDeviceCode, true);
				connectButton.setText("Disconnect");
				connected = true;

			} else if (obj.State == StateEvent.OPERATING) {
				dev = gLib.getDevice(ObjectID);
				stateData.setText("Operating State\n");
				if (dev != null) {
					Log.e("", "SENDING GET");
					// dev.Get();
				} else {
					Log.e("", "DEV IS NULL");
				}
			} else if (obj.State == StateEvent.ASSOCIATING) {
				stateData.setText("Associating State\n");
			} else if (obj.State == StateEvent.DISCONNECTED) {

				stateData.setText("Disconnected State\n");

				Button connectButton = (Button) findViewById(R.id.connect_btn);

				connectButton.setClickable(true);
				connected = false;

				setImage(mDeviceCode, false);
				connectButton.setText("Connect");
				connected = false;

				sendData();

			} else if (obj.State == StateEvent.CONFIGURING) {
				stateData.setText("Configuring State\n");
			} else if (obj.State == StateEvent.UNASSOCIATED) {
				stateData.setText("Unassociated State\n");
			}
			stateData.setGravity(Gravity.CENTER_VERTICAL);
		}
	};

	/**
	 * Dialog to display a list of bonded Bluetooth devices for user to select
	 * from. This is needed only for channel connection initiated from the
	 * application.
	 */
	public static class SelectDeviceDialogFragment extends DialogFragment {

		public static SelectDeviceDialogFragment newInstance(String[] names,
				int position) {
			SelectDeviceDialogFragment frag = new SelectDeviceDialogFragment();
			Bundle args = new Bundle();
			args.putStringArray("names", names);
			args.putInt("position", position);
			frag.setArguments(args);
			return frag;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			String[] deviceNames = getArguments().getStringArray("names");
			int position = getArguments().getInt("position", -1);
			if (position == -1)
				position = 0;
			return new AlertDialog.Builder(getActivity())
					.setTitle("Select a device")
					.setPositiveButton("Okay",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									((DeviceInformationActivity) getActivity())
											.connectChannel();
								}
							})
					.setSingleChoiceItems(deviceNames, position,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									((DeviceInformationActivity) getActivity())
											.setDevice(which);
								}
							}).create();
		}
	}

	public void setDevice(int position) {
		mDevice = this.mAllBondedDevices[position];
		mDeviceIndex = position;
	}

	private void connectChannel() {
		gLib.connectToDevice(mDevice);
		Log.e("", "connect to device call to glib sent -->" + mDevice.getName());
	}

}
