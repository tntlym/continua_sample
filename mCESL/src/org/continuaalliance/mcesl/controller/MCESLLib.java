package org.continuaalliance.mcesl.controller;

import org.continuaalliance.mcesl.appData.Device;

import android.content.Context;
import android.util.Log;

/*
 * Interface to the application. 
 * Stores the configurations for MCESL library  
 * Note: Application developer is supposed to create object of this class 
 * and start the library using start() function  
 * 
 * @author: Vignet
 */

public class MCESLLib {

	private ManagerController m_controller = null;
	private LibConfig m_LibConfig = null;
	private String TAG = "MCESLLIB";

	/**
	 * Single parameter Constructor
	 * 
	 * @param Context
	 *            application context
	 * 
	 */
	public MCESLLib(final Context context) {
		Log.i(TAG, "MCESLLib() start");
		m_LibConfig = new LibConfig();
		m_LibConfig.setConfigContext(context);
		m_LibConfig.setMessanger(new BroadcastMessanger(context));
		Log.i(TAG, "MCESLLib() End");
	}

	/**
	 * Adds the single transport
	 * 
	 * @param transport
	 *            constant
	 * @return void
	 */
	public void addTransport(int transportConstant) {
		Log.i(TAG, "MCESLLib - addTransport() start");
		m_LibConfig.addTransport(transportConstant);
		Log.i(TAG, "MCESLLib - addTransport() end");
	}

	/**
	 * Returns the specified device according to key sent.
	 * @param deviceKey integer
	 * @return Device object
	 */
	public Device getDevice(int deviceKey) {
		return m_controller.getDevice(deviceKey);
	}

	/**
	 * Adds single device specialization
	 * 
	 * @param device
	 *            Specialization constant
	 * @return void
	 */
	public void addDeviceSpecialization(int devSpec) {
		Log.i(TAG, "MCESLLib - addDeviceSpecialization() start");
		m_LibConfig.addDeviceSpecialization(devSpec);
		Log.i(TAG, "MCESLLib - addDeviceSpecialization() end");
	}

	/**
	 * Starts the library execution. Application developer is not supposed to
	 * use MCESlLib object from now on. It returns ManagerController object. Use
	 * ManagerController object for further computation
	 * 
	 * @param
	 * @return ManagerController object
	 */
	public ManagerController start() {
		Log.i(TAG, "MCESLLib - start() start");
		m_controller = new ManagerController(m_LibConfig);
		Log.i(TAG, "MCESLLib - start() end");
		return m_controller;
	}

	/**
	 * Allows to connect to specific device. 
	 * Gives application the interface to connect.
	 * 
	 * @param device object device to connect
	 */
	public void connectToDevice(Object device) {
		m_controller.connectToDevice(device);
	}

	public void stop() {
		m_controller.stop();
	}
}
