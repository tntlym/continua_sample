package com.nousco.bluetoothsample;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int REQUEST_ENABLE_BT = 88;

	private TextView mBtAvailableTextView;
	private TextView mBtEnabledTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mBtAvailableTextView = (TextView) findViewById(R.id.bt_available);
		mBtEnabledTextView = (TextView) findViewById(R.id.bt_enabled);
		
		// Initiate Bluetooth
		initiateBluetooth();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	private void initiateBluetooth()
	{
		// Step 1 : get default adapter
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth Not Available", Toast.LENGTH_LONG).show();
			finish();
			return;
		} else {
			mBtAvailableTextView.setText("Bluetooth Available");
		}
		
		// Step 2 : check if enabled
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			mBtEnabledTextView.setText("Bluetooth Enabled");
		}
	}

}



























