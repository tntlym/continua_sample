package org.continuaalliance.mcesl.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Semaphore;

import org.continuaalliance.mcesl.Event.*;
import org.continuaalliance.mcesl.communication.AConnection;
import org.continuaalliance.mcesl.communication.ConnectionFactory;
import org.continuaalliance.mcesl.communication.EConnectionType;
import org.continuaalliance.mcesl.communication.hdp.HDPConnection;
import org.continuaalliance.mcesl.communication.hdp.HDPData;
import org.continuaalliance.mcesl.dim.MDSObject;
import org.continuaalliance.mcesl.fsm.*;
import org.continuaalliance.mcesl.utils.Constants;
import org.continuaalliance.mcesl.utils.ManagerInfo;
import org.continuaalliance.mcesl.appData.Device;
import android.util.Log;

/*
 * ManagerController.java: Acts as a main manager. 
 * Keeps track on Devices connected. 
 * 
 * @author: Vignet
 */

public class ManagerController {

	private AConnection m_tcpConnObj = null;
	private AConnection m_hdpConnObj = null;
	private ManagerController self = null; // this is useful to send through
											// handler to
	private HashMap<Integer, DeviceManager> deviceManagers = null;
	private ManagerInfo mgrInfo = new ManagerInfo();
	public static Context context = null; // Application context
	public LibConfig m_libConfig = null;
	private String TAG = "ManagerController";

	/**
	 * ExtendedSpecDictionary saves the newly found extended device
	 * specialisations. The device manager should first check if the Extended
	 * Config ID is present in dictionary. If not, then only request for
	 * MDS_NOTI_CONFIG.
	 */

	private HashMap<Integer, MDSObject> ExtendedSpecDictionary = null;
	private Semaphore eSDSemaphore = null;

	/**
	 * Allows to connect to specific device.
	 * 
	 * @param device
	 *            object device to connect
	 */
	public void connectToDevice(Object device) {
		HDPConnection hConn = (HDPConnection) m_hdpConnObj;
		Message msg = Message.obtain();
		msg.what = Event.CONNECT_TO_DEVICE;
		msg.obj = device;
		hConn.handler.sendMessage(msg);
		Log.i("", "MSG SENT TO CONNECT TO device");
	}

	/**
	 * Returns MDS object with the specific config id from the MDS dictionary
	 * 
	 * @param configID
	 *            integer
	 * @return MDSObject 
	 */
	public MDSObject getExtendedDeviceFromDictionary(int configID) {
		MDSObject retObj = null;
		if (ExtendedSpecDictionary != null
				&& ExtendedSpecDictionary.containsKey(configID)) {
			try {
				eSDSemaphore.acquire();
				retObj = ExtendedSpecDictionary.get(configID);
				eSDSemaphore.release();
			} catch (InterruptedException e) {
				// something wrong
				retObj = null;
			}

		}
		return retObj;
	}
	
	/**
	 * sets MDS object with the specific config id in MDS dictionary
	 * 
	 * @param configID
	 *            integer
	 * @param object 
	 * 			  MDSObject
	 * 
	 */
	public void setExtendedDeviceToDictionary(int configID, MDSObject object) {
		if (ExtendedSpecDictionary == null) {
			ExtendedSpecDictionary = new HashMap<Integer, MDSObject>();
		}
		try {
			eSDSemaphore.acquire();
			ExtendedSpecDictionary.put(configID, object);
			eSDSemaphore.release();
		} catch (InterruptedException e) {
			// something wrong

		}

	}

	/**
	 * Returns the device according to the key passed.
	 * @param key int
	 */
	public Device getDevice(int key) {
		Device retDevice = null;
		Log.i("", "GetDevice received ID ->" + key);
		Log.i("", "no of device managers ->" + deviceManagers.size());
		if (deviceManagers.containsKey(key)) {
			Log.i("", "Device managers contains the ID");
			DeviceManager dev = deviceManagers.get(key);
			retDevice = new Device(this, dev.getID(), dev.getDeviceDataType());
		}
		return retDevice;
	}

	/*
	 * Handler which handles messages such as device connection / disconnection.
	 */
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) // Will handle messages such as
												// new connection and connection
												// loss
		{
			switch (msg.what) {
			case Event.TCP_NEW_CONNECTION: {
				Socket soc = (Socket) msg.obj;
				if (deviceManagers == null) {
					deviceManagers = new HashMap<Integer, DeviceManager>();
				}
				try {
					int cID = deviceManagers.size();
					deviceManagers.put(cID, (new DeviceManager(
							EConnectionType.TCP, soc, self, mgrInfo, cID, -1,
							-1)));
					m_libConfig.getMessanger().handleEvent(
							(Object) Constants.TCP_NEW_CONNECTION);

				} catch (IOException exception) {
					// handle exception
				} catch (Exception exception) {
					// handle exception
				}
			}
				break;
			case Event.HDP_NEW_CONNECTION: {
				Log.i("MANAGER CONTROLLER", "in mgr HDP new conn handler");
				HDPData hdpData = (HDPData) msg.obj;
				ParcelFileDescriptor soc = hdpData.getFd();
				if (deviceManagers == null) {
					deviceManagers = new HashMap<Integer, DeviceManager>();
				}
				try {
					int cID = deviceManagers.size() + 1;
					deviceManagers.put(cID, (new DeviceManager(
							EConnectionType.BT_HDP, soc, self, mgrInfo, cID,
							hdpData.getChannelId(), hdpData.getDataType())));
					Log.i("", "SENDING HDP NEW CONNECTION BROADCAST");
					m_libConfig.getMessanger().handleEvent(
							(Object) Constants.HDP_NEW_CONNECTION, cID);
				} catch (IOException exception) {
					// exceptions are thrown as unknown as error to be sent on
					// app level
					// is still to be defined
					m_libConfig.getMessanger().handleEvent(
							(Object) Constants.UNKNOWN_ERROR);

				} catch (Exception exception) {
					// exceptions are thrown as unknown as error to be sent on
					// app level
					// is still to be defined
					m_libConfig.getMessanger().handleEvent(
							(Object) Constants.UNKNOWN_ERROR);
				}
			}

			case Event.TRANSPORT_DISCONNECTED: {
				Log.i("",
						"removing the device manager -->"
								+ deviceManagers.size());
				deleteDeviceManager(msg.arg1);
				Log.i("",
						"removed the device manager -->"
								+ deviceManagers.size());
			}
				break;

			case Event.HDP_DISCONNECTED: {
				int channelID = (Integer) msg.obj;
				if (deviceManagers != null) {
					for (int i = 0; i <= deviceManagers.size(); i++) {
						if (deviceManagers.containsKey(i)) {
							DeviceManager dm = deviceManagers.get(i);
							if (dm.getConnectionType() == EConnectionType.BT_HDP) {
								if (dm.getChannelID() == channelID) {
									Log.i("",
											"Sending trans_disc to deviceMgr from MngrCntrl");
									dm.sendMessageToManager(Event.TRANSPORT_DISCONNECTED);
								}
							}
						}
					}
				}
			}
				break;
			case Event.DEVICE_STATE_CHANGED: {
				m_libConfig.getMessanger().handleEvent(
						(Object) Constants.DEVICE_STATE_CHANGED);
			}
				break;
			case Event.DATA_RCVD_IN_STREAM: {
				Intent i_login = new Intent();
				i_login.setAction(Constants.ASSOC_RCVD);
				i_login.putExtra("DATA_RCVD_IN_STREAM", (String) msg.obj);
				context.sendBroadcast(i_login);
				m_libConfig.getMessanger().handleEvent(
						(Object) Constants.ASSOC_RCVD);
			}
				break;
			case Event.ABORT_FROM_APP: {
				Device devFromApp = (Device) msg.obj;
				if (devFromApp != null) {
					if (deviceManagers.containsKey(devFromApp
							.getDeviceManagerId())) {
						DeviceManager toAbortDM = deviceManagers.get(devFromApp
								.getDeviceManagerId());
						toAbortDM.sendMessageToManager(Event.ABORT_FROM_APP);
					}
				}
			}
			}
		}

	};

	/**
	 * Delete particular device manager 
	 * @param deviceID int
	 */
	private void deleteDeviceManager(int deviceID) {
		if (deviceManagers.containsKey(deviceID)) {
			deviceManagers.remove(deviceID);
			Log.i("", "Contains the Device Manager");
		}
	}

	/**
	 * Get the device manager according to ID passed
	 * @param devMgrId int
	 * @return
	 */
	public DeviceManager getDeviceManager(int devMgrId) {
		DeviceManager retDM = null;
		if (deviceManagers.containsKey(devMgrId)) {
			retDM = deviceManagers.get(devMgrId);
		}
		return retDM;
	}

	/**
	 * Single parameter Constructor
	 * 
	 * @param libConfig
	 *            Library configurations
	 */
	public ManagerController(final LibConfig libConfig) {
		Log.i(TAG, "ManagerControll() start");
		this.m_libConfig = libConfig;
		context = libConfig.getConfigContext();
		self = this;
		eSDSemaphore = new Semaphore(1);
		initializeTransports(); // initiate connection for all the available
								// transports. e.g BT, ANT etc.
		Log.i(TAG, "ManagerControll() end");

	}

	/**
	 * Used to send message through ManagerController handler
	 * 
	 * @param soc
	 *            Object that needs to be sent through message
	 * @param event
	 *            flag to indicate the event occured.
	 */
	public void sendMessage(Object soc, int event) {
		Log.i(TAG, "ManagerControl: Sendmessgae() start");
		Message message = Message.obtain();
		message.obj = (Object) soc;
		message.what = event;
		handler.sendMessage(message);
		Log.i(TAG, "ManagerControl: Sendmessgae() end");
	}

	/**
	 * Returns the application context that is received from application
	 * 
	 * @return Context application context
	 */
	public Context getContext() {
		Log.i(TAG, "ManagerControl: getContext() start - end");
		return context;
	}

	/**
	 * private function to initialize the transports.
	 * 
	 * @return void
	 */
	private void initializeTransports() {
		Log.i(TAG, "ManagerControl: initializeTransports() start");
		HashMap<Integer, Integer> transportMap = m_libConfig.GetTransports();
		HashMap<Integer, Integer> devSpecsMap = m_libConfig
				.GetDevSpecialization();

		for (int i = 0; i < transportMap.size(); i++) {
			switch (transportMap.get(i)) {
			case EConnectionType.TCP:
				m_tcpConnObj = ConnectionFactory.CreateConnection(
						EConnectionType.TCP, this);
				m_tcpConnObj.start();
				break;
			case EConnectionType.BT_HDP:
				m_hdpConnObj = ConnectionFactory.CreateConnection(
						EConnectionType.BT_HDP, this);
				m_hdpConnObj.setSpecializations(devSpecsMap);
				m_hdpConnObj.start();
				break;
			}
		}
		Log.i(TAG, "ManagerControl: initializeTransports() end");
	}

	public void stop() {
		if(deviceManagers!=null && !deviceManagers.isEmpty())
		{
			Set<Integer> keySet = deviceManagers.keySet();
			Iterator<Integer> iterator = keySet.iterator();
			while (iterator.hasNext()) {
				DeviceManager d = deviceManagers.get(iterator.next());
				d.cleanUpDeviceManager();
				
			}
		}
	}
}
