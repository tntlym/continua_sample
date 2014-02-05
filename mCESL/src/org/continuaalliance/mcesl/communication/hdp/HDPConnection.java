package org.continuaalliance.mcesl.communication.hdp;

import java.util.HashMap;

import org.continuaalliance.mcesl.Event.Event;
import org.continuaalliance.mcesl.communication.AConnection;
import org.continuaalliance.mcesl.controller.ManagerController;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHealth;
import android.bluetooth.BluetoothHealthAppConfiguration;
import android.bluetooth.BluetoothHealthCallback;
import android.bluetooth.BluetoothProfile;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;

/*
 * HDPConnection.java: Connection class for HDP profile. 
 * 					   Extended from AConnection class 
 *  
 * @author: Vignet
 */


public class HDPConnection extends AConnection {

	private BluetoothHealthAppConfiguration mHealthAppConfig;
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothHealth mBluetoothHealth;
	private HashMap<Integer, Integer> m_devSpecs = null;
	private static final String TAG = "HDPConnection";

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) //Will handle messages such as new connection and connection loss
		{
			switch(msg.what)
			{
			case Event.CONNECT_TO_DEVICE:
				
				connectToDevice((BluetoothDevice)msg.obj);
				Log.i("","Device connection done");
				break;
			}
		}
	};

	/**
	 * 
	 * Single parameter Constructor   
	 * @param ManagerController object used to notify events
	 * 
	 */
	public HDPConnection(ManagerController listener)
	{
		m_controller = listener;
		Log.i(TAG, "starting HDPConnection");
	}
	/**
	 * Bluetooth service listener
	 */
	private final BluetoothProfile.ServiceListener mBluetoothServiceListener =
		new BluetoothProfile.ServiceListener() {
		public void onServiceConnected(int profile, BluetoothProfile proxy) {
			if (profile == BluetoothProfile.HEALTH) {
				mBluetoothHealth = (BluetoothHealth) proxy;
				registerAgents();
			}
		}

		public void onServiceDisconnected(int profile) {
			if (profile == BluetoothProfile.HEALTH) {
				mBluetoothHealth = null;
			}
		}
	};

	/**
	 * 
	 * Sets the device specializations to register in HDP    
	 * @param HashMap which defines the device specializations
	 * 
	 */
	public void setSpecializations(final HashMap<Integer,Integer> devSpecs)
	{
		m_devSpecs = devSpecs;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */

	public void run()
	{		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			// Bluetooth adapter isn't available.  The client of the service is supposed to
			// verify that it is available and activate before invoking this service.
			// tell to manager
			return;
		}
		if (!mBluetoothAdapter.getProfileProxy(m_controller.getContext(), mBluetoothServiceListener,
				BluetoothProfile.HEALTH)) {
			//Toast.makeText(this, R.string.bluetooth_health_profile_not_available,Toast.LENGTH_LONG);
			// tell to manager
			return;
		}
		
		//registerAgents();
	}

	/**
	 * 
	 * Registers the agent specialization to HDP    
	 * 
	 */
	private void registerAgents()
	{
		Log.i(TAG,"Agent registration started");
		if(m_devSpecs!= null)
		{
			if(m_devSpecs.size()>0)
			{
				for(int i = 0; i < m_devSpecs.size(); i++)
				{
					mBluetoothHealth.registerSinkAppConfiguration(TAG, m_devSpecs.get(i), mHealthCallback);
				}
			}
		}
		Log.i(TAG,"Agent registration complete");
	}
	
	/**
	 * Allows to connect to HDP agent device
	 * @param device BluetoothDevice
	 */
	private void connectToDevice(BluetoothDevice device)
	{
		if(device!=null)
		{
			mBluetoothHealth.connectChannelToSource(device, mHealthAppConfig);
			Log.e("","device connectChannelToSource called "+device.getName());
		}
	}
	/**
	 * 
	 * Defines the callback to handle application registration and unregistration events.       
	 * 
	 */
	private final BluetoothHealthCallback mHealthCallback = new BluetoothHealthCallback() {

		public void onHealthAppConfigurationStatusChange(BluetoothHealthAppConfiguration config,
				int status) {
			if (status == BluetoothHealth.APP_CONFIG_REGISTRATION_FAILURE) {
				Log.e("","APP_CONFIG_REGISTRATION_FAILURE");
				mHealthAppConfig = null;

			} else if (status == BluetoothHealth.APP_CONFIG_REGISTRATION_SUCCESS) {
				mHealthAppConfig = config;
				Log.e("","APP_CONFIG_REGISTRATION_SUCCESS");

			} 
		}

		// Callback to handle channel connection state changes.
		public void onHealthChannelStateChange(BluetoothHealthAppConfiguration config,
				BluetoothDevice device, int prevState, int newState, ParcelFileDescriptor fd,
				int channelId) {
			Log.i(TAG,"entering callback");
			if (prevState == BluetoothHealth.STATE_CHANNEL_DISCONNECTED &&
					newState == BluetoothHealth.STATE_CHANNEL_CONNECTED) {
				if (config.equals(mHealthAppConfig)) {

					Log.i(TAG,"SENDING NEW CONN MSG TO MANAGER");
					HDPData hdpData = new HDPData(fd, channelId,config.getDataType());
					Log.e("","Datatype-->"+config.getDataType() +" -- Name "+config.getName() );
					m_controller.sendMessage(hdpData,Event.HDP_NEW_CONNECTION);
				} else {
					Log.e(TAG,"ERROR");
				}
			}else if (prevState == BluetoothHealth.STATE_CHANNEL_CONNECTING
                    && newState == BluetoothHealth.STATE_CHANNEL_CONNECTED) {
                
				if (config.equals(mHealthAppConfig)) {				                                    
					Log.e(TAG, "channel state - CONNECTED");
					Log.i(TAG,"SENDING NEW CONN MSG TO MANAGER");
					HDPData hdpData = new HDPData(fd, channelId,config.getDataType());
					Log.e("","Datatype-->"+config.getDataType() +" -- Name "+config.getName() );
					m_controller.sendMessage(hdpData,Event.HDP_NEW_CONNECTION);
				} 
			}
			else if (prevState == BluetoothHealth.STATE_CHANNEL_CONNECTING &&
					newState == BluetoothHealth.STATE_CHANNEL_DISCONNECTED) {
				Log.i(TAG,"Channel Disconnecting");
				m_controller.sendMessage(channelId, Event.HDP_DISCONNECTED);

			} else if (newState == BluetoothHealth.STATE_CHANNEL_DISCONNECTED) {
				if (config.equals(mHealthAppConfig)) {
					Log.i(TAG,"Channel Disconnected");
					m_controller.sendMessage(channelId, Event.HDP_DISCONNECTED);
				} 
			}
		}
	};

}
