package org.continuaalliance.mcesl.communication.hdp;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import org.continuaalliance.mcesl.Event.Event;
import org.continuaalliance.mcesl.communication.VirtualChannel;
import org.continuaalliance.mcesl.fsm.DeviceManager;

import org.openhealthtools.stepstone.phd.core.asn1.classes.generated.ApduType;
import org.openhealthtools.stepstone.phd.encoding.IDecoderService;
import org.openhealthtools.stepstone.phd.encoding.IEncoderService;
import org.openhealthtools.stepstone.phd.encoding.mder.DecoderMDER;
import org.openhealthtools.stepstone.phd.encoding.mder.EncoderMDER;

import android.os.ParcelFileDescriptor;
import android.util.Log;

/*
 * HDPVirtualChannel.java:  Virtual channel implementation for HDP bluetooth profile.
 * 							
 * @author: Vignet
 */

public class HDPVirtualChannel extends VirtualChannel {

	private ParcelFileDescriptor m_pFileDescriptor = null;
	private ReaderThread rTh = null;
	private WriterThread wTh = null;
	private FileInputStream fIn = null;
	private FileOutputStream fOut = null;
	private final String TAG = "HDPVirtualChannel";
	private final DeviceManager m_devMgr;

	/**
	 * 
	 * Single parameter Constructor
	 * 
	 * @param ParcelFileDescriptor
	 *            object which is used for reads and writes
	 * @param DeviceManager
	 *            object which suggests the device manager to which it belongs.
	 * 
	 */
	public HDPVirtualChannel(ParcelFileDescriptor pFd,
			final DeviceManager manager) throws IOException, Exception {
		m_devMgr = manager;
		m_pFileDescriptor = pFd;
		fIn = new FileInputStream(m_pFileDescriptor.getFileDescriptor());
		fOut = new FileOutputStream(m_pFileDescriptor.getFileDescriptor());

		rTh = new ReaderThread();
		wTh = new WriterThread();
		outQueue = new LinkedList<ApduType>();
		inQueue = new LinkedList<ApduType>();
		semaphoreOutQueue = new Semaphore(1);
		semaphoreInQueue = new Semaphore(1);
	}

	/**
	 * Adds the packet to the write queue to send message to agent.
	 * 
	 * @param ApduType
	 *            object which holds the outgoing data
	 */
	@Override
	public void AddToWriteQueue(ApduType dataToSend) {
		// This is kept empty as ASN1 data types and encoding is not yet
		// available.
		try {
			semaphoreOutQueue.acquire();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		outQueue.add(dataToSend);
		semaphoreOutQueue.release();
	}

	/**
	 * Starts the read and write threads.
	 * 
	 */
	@Override
	public void Initialize() {
		rTh.start();
		wTh.start();
	}

	/**
	 * Adds the apdu read from stream to read queue for state machine
	 * 
	 * @param apdu ApduType
	 */
	public void AddToReadQueue(ApduType apdu) {
		Log.i(TAG, "AddToReadQueue start");
		try {
			semaphoreInQueue.acquire();
		} catch (InterruptedException e) {
			Log.i(TAG,"add to write queue semaphore error");
			e.printStackTrace();
			return;
		}
		inQueue.add(apdu);
		semaphoreInQueue.release();
		// tell it to respective device manager
		m_devMgr.sendMessageToManager(Event.DATA_RCVD_IN_STREAM);
		Log.i(TAG, "AddToReadQueue end");

	}

	/**
	 * ReaderThread class runs as a thread which continuously listens to in
	 * stream
	 * 
	 */
	public class ReaderThread extends Thread {
		public void run() {

			byte data[] = new byte[8192];
			String str = "";
			/**
			 * After reading from the inStream decode it to apduType and add it
			 * to inQueue
			 */
			while (true) {

				int ret = -1;
				try {
					// Log.e(TAG, "READ THREAD READING.. ");
					ret = fIn.read(data);
					if (ret > 0) {
						Log.i(TAG, "Data received in read thread with size "
								+ ret);
						for (int i = 0; i < ret; i++) {
							if (i == 0) {
								str = String.format("%x", data[0]);
							} else {
								str = String.format("%s %x", str, data[i]);
							}
						}
						Log.i(TAG, "returned -->" + str + "  and ret is-->"
								+ ret);

						Log.i(TAG, "About to create decoder");
						IDecoderService decoder = new DecoderMDER(data);
						// ApduType apduMsg = new ApduType(decoder);
						ApduType apdudata = new ApduType(decoder);
						Log.i(TAG, "Apdu created");
						if (apdudata != null) {
							int choice = apdudata.getChoiceTag().getValue() & 0x0000FFFF;

							Log.i(TAG, "choice - " + choice);
							AddToReadQueue(apdudata);

						}
					}

				}
				catch (IOException e) {
					Log.e(TAG, "returned -->");
					m_devMgr.sendMessageToManager(Event.TRANSPORT_DISCONNECTED);
					return;
				} 
				
			}
		}

	}

	/**
	 * WriterThread class runs as a thread which continuously listens to out
	 * queue and sends the data to out stream
	 * 
	 */
	class WriterThread extends Thread {
		public void run() {
			// This is kept empty as ASN1 data types and encoding is not yet
			// available.
			while (outQueue!=null) {
				try {
					semaphoreOutQueue.acquire();
				} catch (InterruptedException e1) {
					Log.e("", "Semaphore error -in out run");
					e1.printStackTrace();
					return;
				}
				if (outQueue!=null && outQueue.size() > 0) {
					ApduType toSend = outQueue.remove();
					IEncoderService encoder = new EncoderMDER();
					encoder.encode(toSend);
					try {
						
						fOut.write(encoder.getBytes());
						
					} catch (IOException e) {

						m_devMgr.sendMessageToManager(Event.TRANSPORT_DISCONNECTED);
						return;
					}
				}
				semaphoreOutQueue.release();
			}
		}
	}

	@Override
	public void Destroy() {
		Log.i(TAG, "Calling interrupt for read and write thread");
		rTh.interrupt();
		wTh.interrupt();
		rTh = null;
		wTh = null;
		try {
			semaphoreOutQueue.acquire();
		} catch (InterruptedException e1) {
			Log.e("", "Semaphore error out thread");
			e1.printStackTrace();
		}
		outQueue.clear();
		outQueue = null;
		try {
			semaphoreInQueue.acquire();
		} catch (InterruptedException e1) {
			Log.e("", "Semaphore error in thread");
			e1.printStackTrace();
		}
		inQueue.clear();
		inQueue = null;
	}
}
