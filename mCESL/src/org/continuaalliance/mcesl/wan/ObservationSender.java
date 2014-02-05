/*Copyright © 2011, Continua Health Alliance.
 *All rights reserved.
 *Terms and conditions of use are stated in the Reference Code License Agreement.
 */

package org.continuaalliance.mcesl.wan;

/*******************************
 Developed by Vignet for Continua.
 *******************************/
import org.continuaalliance.mcesl.ui.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class ObservationSender {

	ObservationHelper helper;
	String successMessage;
	private String errorMessage;
	private Context Context;
	private boolean sendsuccess = false;

	public ObservationSender(ObservationHelper helper, String successMessage,
			Context context) {
		this.helper = helper;
		this.Context = context;
		this.successMessage = successMessage;

	}

	boolean sendData(ObservationHelper helper) {
		Log.i("ObservationSender", "In sendData of ObservationSender...");
		boolean retVal = false;
		errorMessage = null;

		if (isNetworkConnected()) {
			try {
				if (helper.send())
					retVal = true;
			} catch (Exception e) {
				errorMessage = e.getMessage();
				e.printStackTrace();
			}

		} else {
			errorMessage = Context.getString(R.string.nointerneterror);
			// Toast.makeText(this,R.string.nointerneterror
			// ,Toast.LENGTH_LONG).show();//, Toast.LENGTH_LONG).show();
		}

		return retVal;

	}

	public void SendSync() {
		Log.i("ObservationSender", "In sendSync...");
		sendsuccess = sendData(helper);
		if (sendsuccess)
			Log.i("Upload Success", "Measurement Uploaded Successfully");
		else
			Log.e("Upload Failure", "Measurement Upload Failed");

	}

	public void DoPostSend() {
		if (sendsuccess)
			Toast.makeText(Context, successMessage, Toast.LENGTH_LONG).show();// ,
																				// Toast.LENGTH_LONG).show();
		else {

			if (errorMessage == null)

				Toast.makeText(Context, "Failed to Send Data!",
						Toast.LENGTH_LONG).show();
			else
				Toast.makeText(Context, errorMessage, Toast.LENGTH_LONG).show();
		}
	}


	void Send() {
		new SenderTask().execute(this);
	}

	boolean isNetworkConnected() {
		@SuppressWarnings("static-access")
		ConnectivityManager connectivityMgr = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
		return (networkInfo != null) ? networkInfo.isConnected() : false;
		// return true;
	}

	public Context getApplicationContext() {

		return Context;
	}


	class SenderTask extends AsyncTask<ObservationSender, Void, Void> {

		ProgressDialog progressDlg;

		ObservationSender[] params;

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			int count = params.length;

			for (int i = 0; i < count; i++) {
				params[i].DoPostSend();

			}

			// progressDlg.cancel();
		}

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			/*
			 * progressDlg = new ProgressDialog(main.getContext());
			 * progressDlg.setTitle("Sending..");
			 * progressDlg.setMessage("Sending data..please wait!");
			 * //progressDlg.show();
			 */}

		@Override
		protected Void doInBackground(ObservationSender... params) {

			this.params = params;
			int count = params.length;

			for (int i = 0; i < count; i++) {
				params[i].SendSync();

			}

			return null;
		}

	}
}
