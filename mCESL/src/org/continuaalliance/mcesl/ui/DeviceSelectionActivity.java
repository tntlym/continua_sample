package org.continuaalliance.mcesl.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.continuaalliance.mcesl.settings.Settings;
import org.continuaalliance.mcesl.wan.SettingItemNames;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class DeviceSelectionActivity extends Activity {
	/** Called when the activity is first created. */

	// private Bitmap icon;
	private Map<String, Object> selectedMap = null;
	private Map<String, Object> map = null;

	private Map<String, Integer> codeMap = null;

	private ListView listOfDevices;
	private List<Map<String, Object>> list;
	private CompoundButton uploadChkbox;
	private Button settingsBtn;
	private int activityCallCount = 0;
	private Settings settingsObj;
	private ImageView vigImageView = null;
	private ImageView continuaImageView = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		codeMap = new HashMap<String, Integer>();
		codeMap.put("Pulse Oximeter", 4100);
		codeMap.put("Blood Pressure", 4103);
		codeMap.put("Thermometer", 4104);
		codeMap.put("Weight Scale", 4111);
		codeMap.put("Blood Glucose", 4113);

		/************************ POPULATING LISTVIEW AND SETTING ADAPTER ***************************/

		populateDeviceList();

		int[] to = { R.id.deviceImage, R.id.deviceName };// R.id.deviceImage,
		String[] from = { "icon", "name" };// "icon",

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
		
		listOfDevices = (ListView) findViewById(R.id.deviceList);

		SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),
				(List<? extends Map<String, ?>>) list, R.layout.item_in_list,
				from, to);

		if (listOfDevices != null) {
			listOfDevices.setAdapter(adapter);
			Log.i("tag", "list not null");
		}

		/*********** SETTING AN ITEM CLICK LISTENER TO THE LISTVIEW ***********/
		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> listView, View view,
					int position, long rowId) {

				view.setSelected(true);
				selectedMap = list.get(position);

				String text = (String) selectedMap.get("name");
				int value = (int) codeMap.get(text);

				Intent intent = new Intent(getApplicationContext(),
						DeviceInformationActivity.class);
				intent.setAction(Intent.ACTION_VIEW);

				intent.putExtra("Specialisation", value);

				startActivity(intent);

			}
		};

		listOfDevices.setOnItemClickListener(clickListener);

		/******************** UPLOAD SERVER SETTINGS *******************/

		settingsBtn = (Button) findViewById(R.id.setingsBtn);
		uploadChkbox = (CompoundButton) findViewById(R.id.uploadChkbox);

		// ////////// Upload check box callback ///////////

		OnCheckedChangeListener uploadCheckedListener = new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isChecked) {
					settingsBtn.setEnabled(true);
				} else
					settingsBtn.setEnabled(false);
			}
		};
		uploadChkbox.setOnCheckedChangeListener(uploadCheckedListener);

		// /////////// Settings button callback ////////////

		OnClickListener settingsClickListener = new OnClickListener() {

			public void onClick(View v) {

				Intent settingsIntent = new Intent(getApplicationContext(),
						SettingsActivity.class);
				settingsIntent.setAction(Intent.ACTION_VIEW);

				settingsIntent.putExtra("Count",
						activityCallCount);

				activityCallCount++;
				startActivityForResult(settingsIntent, 0);
			}
		};
		settingsBtn.setOnClickListener(settingsClickListener);

	}// End of OnCreate

	/**
	 * 
	 */
	private void populateDeviceList() {
		/*************** POPULATING THE LIST VIEW ******************/
		list = new ArrayList<Map<String, Object>>();

		// Pulse Oximeter
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.pulseox);
		map.put("name", "Pulse Oximeter");
		list.add(map);

		// Blood Pressure
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.bloodpressure);
		map.put("name", "Blood Pressure");
		list.add(map);

		// Thermometer
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.thermo);
		map.put("name", "Thermometer");
		list.add(map);

		// Weight Scale
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.scale);
		map.put("name", "Weight Scale");
		list.add(map);

		// Blood Glucose
		map = new HashMap<String, Object>();
		map.put("icon", R.drawable.glucose);
		map.put("name", "Blood Glucose");
		list.add(map);

		Log.i("tag", "Count in list : " + list.size());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		settingsObj = Settings.getInstance();

		Log.i("Settings Object Data",
				"SERVER PORT : " + settingsObj.get(SettingItemNames.portNumber));
		Log.i("Settings Object Data",
				"ACTION NAME : "
						+ settingsObj.get(SettingItemNames.serviceAction));
		Log.i("Settings Object Data",
				"SERVER NAME : "
						+ settingsObj.get(SettingItemNames.serviceName));
		Log.i("Settings Object Data",
				"SERVER URL : " + settingsObj.get(SettingItemNames.server_url));

	}

}
