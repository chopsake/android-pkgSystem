package com.countableset.pkgsystem;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class OptionsLocation extends Activity
{
	private LocationManager locationManager;
	ProgressDialog dialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options_location);
		// Action Bar Stuff
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(R.string.location);
        actionBar.setHomeAction(new IntentAction(this, MainMenu.createIntent(this), R.drawable.ic_title_home_default));
		
		// Get Location
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000,
				100f, new GeoUpdateHandler());
		
		dialog = ProgressDialog.show(this, "", "Getting Location. Please wait...", true);
	} // end onCreate()
	
	
	public class GeoUpdateHandler implements LocationListener 
	{
		private PostData data = new PostData();
		
		// Server Stuff
		private static final String SERVER_KEY = "KbnLHqqqZLw60uctCYh1";
		private static final String URL = "http://dev.countableset.com/android/options_info.php";

		@Override
		public void onLocationChanged(Location location) 
		{
			int lat = (int) (location.getLatitude() * 1E6);
			int lng = (int) (location.getLongitude() * 1E6);
			
			Toast.makeText(OptionsLocation.this, lat + " " + lng, Toast.LENGTH_SHORT).show();
			
			updateLocation(lat, lng);
			
			// Turning off GPS
			locationManager.removeUpdates(this);
			locationManager = null;
		
			finish();
		}
		
		public void updateLocation(int lat, int lng)
		{
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("key", SERVER_KEY));
			nameValuePairs.add(new BasicNameValuePair("email", SystemInfo.getEmail()));
			nameValuePairs.add(new BasicNameValuePair("password", SystemInfo.getPassword()));
			nameValuePairs.add(new BasicNameValuePair("action", "3"));
			nameValuePairs.add(new BasicNameValuePair("lat", Integer.toString(lat)));
			nameValuePairs.add(new BasicNameValuePair("lng", Integer.toString(lng)));
			
			data.post(nameValuePairs, URL);
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	} // end GeoUpdateHandler Class

} // end class
