/*Copyright © 2011, Continua Health Alliance.
 *All rights reserved.
 *Terms and conditions of use are stated in the Reference Code License Agreement.
 */

package org.continuaalliance.mcesl.ui;

/*******************************
 Developed by Vignet for Continua.
 *******************************/
import org.continuaalliance.mcesl.settings.Settings;
import org.continuaalliance.mcesl.wan.Logger;
import org.continuaalliance.mcesl.wan.SettingItemNames;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity {
	/** Called when the activity is first created. */
	private static Context mainContext;

	private Button saveBtn;
	private Spinner spinner;
	private EditText settingsUrl;
	private EditText settingsPort;
	private EditText settingsActionName;
	private EditText settingsServiceName;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mainContext = this;
		setContentView(R.layout.upload_settings);

		// ***********************************************************
		// To determine if this activity has been called for the
		// first time since start of the application.
		String key = "Count";

		Bundle bundle = this.getIntent().getExtras();
		int callCnt = bundle.getInt(key);

		// ***********************************************************
		// Associating th UI components with local variables,
		// so that they can be modified later.
		settingsUrl = (EditText) findViewById(R.id.URLValue);
		settingsPort = (EditText) findViewById(R.id.portValue);
		settingsServiceName = (EditText) findViewById(R.id.serviceNameValue);
		settingsActionName = (EditText) findViewById(R.id.serviceActionValue);

		spinner = (Spinner) findViewById(R.id.presetValue);
		saveBtn = (Button) findViewById(R.id.saveBtn);


		System.setProperty(Logger.tag, "INFO");

		// ***********************************************************
		Settings.initSettings(this);

		// Display previously saved settings if this activity is not called for
		// the FIRST time.
		if (callCnt != 0) {
			settingsActionName.setText(Settings.getInstance()
					.get(SettingItemNames.serviceAction).toString());
			settingsServiceName.setText(Settings.getInstance()
					.get(SettingItemNames.serviceName).toString());
			settingsPort.setText(Settings.getInstance()
					.get(SettingItemNames.portNumber).toString());
			settingsUrl.setText(Settings.getInstance()
					.get(SettingItemNames.server_url).toString());
			spinner.setSelection(SettingItemNames.presetindex.ordinal());

		}

		// ***********************************************************
		// Populating the Spinner
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.presets, android.R.layout.simple_spinner_item);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		// Setting Click listener for spinner
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				String selected = arg0.getItemAtPosition(arg2).toString();

				
				if (selected.contentEquals("Other")) {
					settingsPort.setText((String) Settings.getInstance().get(
							SettingItemNames.portNumber));
					settingsUrl.setText((String) Settings.getInstance().get(
							SettingItemNames.server_url));
					settingsServiceName.setText((String) Settings.getInstance()
							.get(SettingItemNames.serviceName));
					settingsActionName.setText((String) Settings.getInstance()
							.get(SettingItemNames.serviceAction));

				} else if (selected.contentEquals("BXI Server")) {

					settingsUrl.setText("23.21.82.221");
					settingsPort.setText("8080");
					settingsServiceName
							.setText("/axis2/services/DeviceObservationConsumer_Service");
					settingsActionName
							.setText("CommunicatePCDData");
					/*settingsUrl.setText("continua.bxihealth.com");
					settingsPort.setText("8080");
					settingsServiceName
							.setText("/axis2/services/DeviceObservationConsumer_ServiceVanilla");
					settingsActionName
							.setText("urn:ihe:pcd:dec:2010:DeviceObservationConsumer_Service");*/
				}
				else if (selected.contentEquals("IBM Server")) {

					settingsUrl.setText("195.212.190.241");
					settingsPort.setText("9080");
					settingsServiceName
							.setText("/ConsumeDeviceObservationWeb/DeviceObservationConsumer_Service");
					settingsActionName
							.setText("urn:ihe:pcd:dec:2009:DeviceObservationConsumer_Service");
				}
				
				else if (selected.contentEquals("Local Test Tool")) {

					settingsUrl.setText("192.168.1.105");
					settingsPort.setText("8080");
					settingsServiceName
							.setText("/WanReceiver/services/DeviceObservationConsumer_Service");
					settingsActionName
							.setText("urn:ihe:pcd:dec:2009:DeviceObservationConsumer_Service");
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// Must be included to implement the interface.
				
			}

			
		});

		// ***********************************************************
		// Setting OnClickListener for save button
		OnClickListener btnClickListener = new OnClickListener() {

			public void onClick(View v) {
				

				// save the data in shared preferences of Settings object.

				Settings.getInstance().set(SettingItemNames.server_url,
						settingsUrl.getText().toString());
				Settings.getInstance().set(SettingItemNames.serviceName,
						settingsServiceName.getText().toString());
				Settings.getInstance().set(SettingItemNames.serviceAction,
						settingsActionName.getText().toString());
				Settings.getInstance().set(SettingItemNames.portNumber,
						settingsPort.getText().toString());
				Settings.getInstance().set(SettingItemNames.presetindex,
						spinner.getSelectedItemPosition());
				finish();

			}
		};

		saveBtn.setOnClickListener(btnClickListener);

	}

	public static Context getContext() {
		return mainContext;
	}
}
