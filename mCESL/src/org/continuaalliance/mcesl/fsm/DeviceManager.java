package org.continuaalliance.mcesl.fsm;

import java.io.IOException;
import java.util.ArrayList;

import org.continuaalliance.mcesl.Event.*;
import org.continuaalliance.mcesl.appData.AppMDSObject;
import org.continuaalliance.mcesl.appData.DeviceObject;
import org.continuaalliance.mcesl.appData.MeasurementEvent;
import org.continuaalliance.mcesl.appData.StateEvent;
import org.continuaalliance.mcesl.communication.EConnectionType;
import org.continuaalliance.mcesl.communication.VirtualChannel;
import org.continuaalliance.mcesl.communication.hdp.HDPVirtualChannel;
import org.continuaalliance.mcesl.controller.ManagerController;
import org.continuaalliance.mcesl.dim.MDSObject;
import org.continuaalliance.mcesl.utils.Constants;
import org.continuaalliance.mcesl.utils.ManagerInfo;
import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ApduType;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;


/* 
 * DeviceManager.java : Manages the device / agent connected to the manager  
 * 
 * @author: Vignet
 */


public class DeviceManager extends Thread
{
	private VirtualChannel m_vChannel = null;
	private org.continuaalliance.mcesl.fsm.State m_state = null;
	private Handler m_handler = null;
	private String encodingType = "MDER";
	private final ManagerController m_parent_controller;
	public final ManagerInfo m_mgrInfo;
	private int m_mCIdentifier;
	private MDSObject agentMds = null;
	private final String TAG = "DEVICEMANAGER";
	private final int connectionType;
	private final int m_channelID;
	private final int m_dataType;
	/**
	 * Multiparameter Constructor    
	 * @param connType integer which defines type of the connection device has
	 * @param connectionVariable object which defines the communication media e.g in case of tcp it is Socket
	 * @param ManagerController object which refers the Manager's object 
	 */
	public DeviceManager (int connType, Object connectionVariable, final ManagerController 
			controller, final ManagerInfo mgr,int mCIdentifier, int channelID, int dataType)
	throws IOException,Exception
	{

		connectionType = connType;
		//m_state = new ConnectedState(this);
		m_parent_controller = controller;
		m_mgrInfo = mgr;
		m_mCIdentifier = mCIdentifier;
		m_channelID = channelID;
		m_dataType = dataType;
		switch(connType)
		{
		case EConnectionType.BT_HDP:
			ParcelFileDescriptor pFd = (ParcelFileDescriptor)connectionVariable;
			m_vChannel = new HDPVirtualChannel(pFd,this);
			break;

		}
		encodingType = "MDER";
		ChangeState(StateConstants.CONNECTED);
		this.start();

	}

	/**
	 * Returns the encoding type being used    
	 * @return string representing encoding being used. 
	 */
	public String getEncodingType()
	{
		return encodingType;
	}

	/**
	 * Changes the state according to state constant received and sends message to manager.    
	 * @param state int 
	 */
	public void ChangeState(int state)
	{
		switch(state)
		{
		case StateConstants.CONNECTED:
		case StateConstants.UNASSOCIATED:
			m_state = new ConnectedState(this);
			break;
		case StateConstants.OPERATING:
			m_state = new OperatingState(this);
			break;
		case StateConstants.ASSOCIATED:
			m_state = new AssociatedState(this);
			break;
		case StateConstants.ASSOCIATING:
			// m_state is not changed as Associating state is written within connected state class.
			// do nothing and allow sending change state broadcast
			break;
		case StateConstants.DISCONNECTED:
			// m_state is not changed to Disconnected state object. 
			// We nullify all the associated data once device is disconnected
			// do nothing and allow sending change state broadcast
			break;
		case StateConstants.CONFIGURING:
			m_state = new ConfiguringState(this);
			Log.i("StateChange", "changing state to configuring");
			break;
		case StateConstants.DISASSOCIATING:
			break;
		};
		sendMessageToManager(Event.DEVICE_STATE_CHANGED,(Object)state);
	}
	public void run()
	{

		//Create a handler to handle messages coming from other threads.
		//Start VirtualChannel inner threads for reads and writes
		Looper.prepare();
		Log.i("", "Device Manager started..." );

		m_vChannel.Initialize();
		m_handler  = new Handler() 
		{
			@Override
			public void handleMessage(Message msg) 
			{
				switch(msg.what)
				{
				case Event.DATA_RCVD_IN_STREAM:
					//Send it to state object to process the received data
					if(m_vChannel!=null && m_state!=null)
					{
						ApduType DataRcvd = m_vChannel.ReadFromInQueue();
						Log.i(TAG, "DeviceManager recvd the data");
						m_state.ProcessData(DataRcvd);
					}
					break;
				case Event.PROCESS_DATA_ON_STATE:
					if(m_state!=null)
					{
						Log.i("", "Reached PROCESS_DATA_ON_STATE ");
						ApduType Data = (ApduType)msg.obj;
						m_state.ProcessData(Data);
					}
					break;
				case Event.ADD_TO_OUT_QUEUE:
					Log.i("","Event.ADD_TO_OUT_QUEUE");
					ApduType sendData = (ApduType)msg.obj;
					addToWriteQueue(sendData);
					break;
				case Event.TRANSPORT_DISCONNECTED:
					if(m_state!=null)
					{
						m_state.ProcessEvent(Event.TRANSPORT_DISCONNECTED);
						cleanUpDeviceManager();
						Message mess = new Message();
						mess.what = Event.TRANSPORT_DISCONNECTED;
						mess.arg1 = m_mCIdentifier;
						//StateConstants.DISCONNECTED set because at this point device disconnects. This should be notified to Application
						ChangeState(StateConstants.DISCONNECTED); 
						m_parent_controller.m_libConfig.getMessanger().handleEvent(Constants.TRANSPORT_DISCONNECT);
						m_parent_controller.handler.sendMessage(mess);
					}
					break;
				case Event.ABORT_FROM_APP:
					if(m_state!=null)
					{
						m_state.ProcessEvent(Event.ABORT_FROM_APP);
						cleanUpDeviceManager();
						ChangeState(StateConstants.DISCONNECTED);
					}
					break;
				case Event.UNASSOCIATE_FROM_APP:
					if(m_state!=null)
					{
						m_state.ProcessEvent(Event.UNASSOCIATE_FROM_APP);
					}
					break;
				case Event.GET_REQUEST_FROM_APP:
					if(m_state!=null)
					{
						m_state.ProcessEvent(msg.what);
					}
					break;
				case Event.GET_PM_STORE_SEGMENTS_INFO:
				case Event.GET_PM_SEGMENT_DATA:
				case Event.ENABLE_SCANNER:
				{
					if(m_state!=null)
					{
						m_state.ProcessEventWithObject(msg.what,msg.obj);
					}
					break;
				}

				};
			}
		};
		Looper.loop();
	}
	
	/**
	 * Cleans up channel, mds and state object of the device manager.
	 * 
	 */
	public void cleanUpDeviceManager()
	{
		Log.i("","Releasing state, virtual channel and mds");
		m_handler.getLooper().quit();
		if(m_vChannel!=null)
		{
			m_vChannel.Destroy();
		}
		m_vChannel = null;
		agentMds = null;
		m_state = null;
	}
	/**
	 * Adds the ApduType packet to queue that needs to be written in out queue    
	 * @param ApduType packet 
	 */
	public void addToWriteQueue(ApduType dataToSend)
	{
		if(dataToSend != null)
		{
			if(m_vChannel!=null)
			{
				m_vChannel.AddToWriteQueue(dataToSend);
			}
		}
	}

	/**
	 * Creates the message object and sends it to manager    
	 * @param ApduType packet 
	 */
	public void sendMessageToManager(final int msgEventFromChannel)
	{
		switch(msgEventFromChannel)
		{
		case Event.DATA_RCVD_IN_STREAM:
			Message msg  = Message.obtain();
			msg.what = Event.DATA_RCVD_IN_STREAM;
			m_handler.sendMessage(msg);
			break;
		case Event.TCP_DISCONNECTED:
			Message processData  = Message.obtain();
			processData.what = Event.PROCESS_DATA_ON_STATE;
			processData.obj = (Object) this;
			m_handler.sendMessage(processData);
			break;
		case Event.DEVICE_STATE_CHANGED:
			Message msgA  = Message.obtain();
			msgA.what = Event.DEVICE_STATE_CHANGED;
			msgA.obj = (Object) m_mCIdentifier;
			//m_parent_controller.m_libConfig.getMessanger().handleEvent(Constants.DEVICE_STATE_CHANGED, )
			break;
		case Event.MEASUREMENT_RECVD:
			Message msgB  = Message.obtain();
			msgB.what = Event.MEASUREMENT_RECVD;
			msgB.obj = (Object) m_mCIdentifier;
			m_parent_controller.handler.sendMessage(msgB);
			break;
		case Event.TRANSPORT_DISCONNECTED:
			Message processD  = Message.obtain();
			processD.what = Event.TRANSPORT_DISCONNECTED;
			processD.obj = (Object) this;
			m_handler.sendMessage(processD);
			break;
		case Event.ABORT_FROM_APP:
			Message msgAbort = Message.obtain();
			msgAbort.what = Event.ABORT_FROM_APP;
			m_handler.sendMessage(msgAbort);
			break;
		case Event.UNASSOCIATE_FROM_APP:
			Message msgUnassoc = Message.obtain();
			msgUnassoc.what = Event.UNASSOCIATE_FROM_APP;
			m_handler.sendMessage(msgUnassoc);
			break;
		case Event.GET_REQUEST_FROM_APP:
			Message msgGet = Message.obtain();
			msgGet.what = Event.GET_REQUEST_FROM_APP;
			m_handler.sendMessage(msgGet);
			break;
		
		}

	}

	/**
	 * Creates the message object and sends it to manager
	 * 
	 * @param event from the channel
	 * @param ApduType packet 
	 */
	@SuppressWarnings("rawtypes")
	public void sendMessageToManager(final int msgEventFromChannel,Object data)
	{
		switch(msgEventFromChannel)
		{
		case Event.DATA_RCVD_IN_STREAM:
			Message msg  = Message.obtain();
			msg.what = Event.DATA_RCVD_IN_STREAM;
			msg.obj = (Object) data;
			m_handler.sendMessage(msg);
			break;

		case Event.PROCESS_DATA_ON_STATE:
			Message processData  = Message.obtain();
			processData.what = Event.PROCESS_DATA_ON_STATE;
			processData.obj = (Object) data;
			m_handler.sendMessage(processData);
			break;

		case Event.ADD_TO_OUT_QUEUE:
			Message sendData  = Message.obtain();
			sendData.what = Event.ADD_TO_OUT_QUEUE;
			sendData.obj = (Object) data;
			m_handler.sendMessage(sendData);
			break;

		case Event.MEASUREMENT_RECVD:

			MeasurementEvent objs = (MeasurementEvent)data;
			m_parent_controller.m_libConfig.getMessanger().handleEvent((Object)Constants.MEASUREMENT_RECVD, objs);
			break;

		case Event.DEVICE_STATE_CHANGED:
			Log.i("","Sending the state change");
			StateEvent sEvent = new StateEvent((Integer)data);
			m_parent_controller.m_libConfig.getMessanger().handleEvent((Object)Constants.DEVICE_STATE_CHANGED, (StateEvent)sEvent);
			break;

		case Event.DEVICE_INFO:
			Log.i("","Device Info sent");
			m_parent_controller.m_libConfig.getMessanger().handleEvent((Object)Constants.DEVICE_INFO, (DeviceObject)data);
			break;
		
		case Event.MDS_OBJECT_RECVD:
			Log.i("","MDS_OBJECT_RECVD event");
			m_parent_controller.m_libConfig.getMessanger().handleEvent((Object)Constants.MDS_OBJECT, (AppMDSObject)data);
			break;
		
		case Event.SEGMENT_INFO_RECEIVED:
			Log.i("","SEGMENT_INFO_RECEIVED event");
			m_parent_controller.m_libConfig.getMessanger().handleEvent((Object)Constants.SEGMENT_INFO, (ArrayList)data);
			break;
		case Event.GET_PM_STORE_SEGMENTS_INFO:
		case Event.GET_PM_SEGMENT_DATA:
		case Event.ENABLE_SCANNER:
			Message msgSInfo = Message.obtain();
			msgSInfo.what = msgEventFromChannel;
			msgSInfo.obj = (Object) data;
			m_handler.sendMessage(msgSInfo);
			break;
		
			
			
		}

	}
	
	/**
	 * Sets the agent MDS 
	 * @param object MDSObject
	 */
	public void setAgentMDS(MDSObject object)
	{
		agentMds = object;
	}
	
	/**
	 * Returns the agent MDS object
	 * @return MDSObject
	 */
	public MDSObject getAgentMDS()
	{
		return agentMds;
	}
	/**
	 * returns the type of the connection e.g. HDP/TCP etc
	 * @return connectionType int
	 */
	public int getConnectionType() {
		return connectionType;
	}

	/**
	 * Returns the channel id of the connection.
	 * @return channel id
	 */
	public int getChannelID() {
		return m_channelID;
	}

	/**
	 * Returns the device type of the connected device.
	 * @return device type.
	 */
	public int getDeviceDataType() {
		return m_dataType;
	}
	
	/**
	 * Returns unique identifier of the device manager.
	 * @return ID 
	 */
	public int getID()
	{
		return m_mCIdentifier;
	}
	public MDSObject checkInSpecDictionery(int configID)
	{
		return m_parent_controller.getExtendedDeviceFromDictionary(configID);
	}
	public void setInDeviceDictionary(int configID, MDSObject object)
	{
		m_parent_controller.setExtendedDeviceToDictionary(configID, object);
	}
}
