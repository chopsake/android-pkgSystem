package com.countableset.pkgsystem;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class Deliver extends Activity
{
	// Variables
	private ListView lv1;
	private PostData data = new PostData();
	// List array of friends
	protected ArrayList<PackageList> PackageList = new ArrayList<PackageList>();

	private static final String URL_GET = "http://dev.countableset.com/android/package.php";
	private static final String URL_REMOVE = "http://dev.countableset.com/android/package_delivered.php";
	private static final String URL_LOCATION = "http://dev.countableset.com/android/location.php";
	private static final String SERVER_KEY = "KbnLHqqqZLw60uctCYh1";

	// Menu Items
	private static final int MAPTO_ID = Menu.FIRST;
	private static final int DELIVERED_ID = Menu.FIRST + 1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_menu);

		// Action Bar Stuff
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle(R.string.title_bar_deliver);
		actionBar.setHomeAction(new IntentAction(this, MainMenu.createIntent(this), R.drawable.ic_title_home_default));
		display();
	} // end onCreate()

	@Override
	protected void onResume() {
		super.onResume();
		display();
	}

	protected void display() {
		// Generating the friends list to display
		generateDelivery();
		// List Activity Settings
		lv1 = (ListView) findViewById(R.id.ListView01);
		lv1.setAdapter(new ArrayAdapter<PackageList>(this, R.layout.friends_list_view, PackageList));
		registerForContextMenu(lv1);
		lv1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				view.showContextMenu();
              }
        });		
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		// When you hold down on an item
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.deliver_menu_header);
		menu.add(0, MAPTO_ID, 0, R.string.showmap);
		menu.add(0, DELIVERED_ID, 0, R.string.delivered);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case DELIVERED_ID:
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
				PackageList friendContext = (PackageList) lv1.getAdapter().getItem(info.position);
				// Removing friend
				removeFriend(friendContext.getID());
				generateDelivery();
				lv1.setAdapter(new ArrayAdapter<PackageList>(this, R.layout.friends_list_view, PackageList));
				Toast.makeText(Deliver.this, friendContext.toString() + " Delivered", Toast.LENGTH_SHORT).show();
				return true;
				
			case MAPTO_ID:
				AdapterContextMenuInfo info2 = (AdapterContextMenuInfo) item.getMenuInfo();
				PackageList friendContext2 = (PackageList) lv1.getAdapter().getItem(info2.position);

				Toast.makeText(Deliver.this, "Finding " + friendContext2.getName() + "...", Toast.LENGTH_SHORT).show();
				
				// query last known location for recipient
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("key", SERVER_KEY));
				nameValuePairs.add(new BasicNameValuePair("email", SystemInfo.getEmail()));
				nameValuePairs.add(new BasicNameValuePair("password", SystemInfo.getPassword()));
				nameValuePairs.add(new BasicNameValuePair("user", friendContext2.id));
				JSONObject result = data.post(nameValuePairs, URL_LOCATION);
				try
				{
					JSONArray ja = result.getJSONArray("location");
					
					// convert into proper lat, lng format
					double lat = Integer.parseInt(ja.getString(0).trim()) / 1E6;
					double lng = Integer.parseInt(ja.getString(1).trim()) / 1E6;
					
					String url = "http://maps.google.com/maps?daddr=" + lat + ",+" + lng + "+%28" + friendContext2.getName() + "%29";
					Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
					startActivity(browserIntent);
				} catch (JSONException ex)
				{
				}
				return true;
		}
		return super.onContextItemSelected(item);
	}

	
	public void generateDelivery()
	{
		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("key", SERVER_KEY));
		nameValuePairs.add(new BasicNameValuePair("email", SystemInfo.getEmail()));
		nameValuePairs.add(new BasicNameValuePair("password", SystemInfo.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("cond", "1"));
		
		// Get the post data to fill the friends array
		JSONObject result = data.post(nameValuePairs, URL_GET);
		
		PackageList = new ArrayList<PackageList>();
		
		try
		{
			// Create the array of names
			JSONArray names = result.getJSONArray("names");

			// Fill the friends array with the JSONObject
			for (int i = 0; i < names.length(); i++)
			{
				PackageList.add(new PackageList(names.getString(i++),
						names.getString(i++), names.getString(i)));
			}
		} catch (JSONException ex)
		{
		}
	} // end generateDelivery
	
	// remove package from friend
	public void removeFriend(String id)
	{
		// setting up variables for post
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("key", SERVER_KEY));
		nameValuePairs.add(new BasicNameValuePair("email", SystemInfo.getEmail()));
		nameValuePairs.add(new BasicNameValuePair("password", SystemInfo.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("removeid", id));
		// posting information
		data.post(nameValuePairs, URL_REMOVE);
	}

} // end class
