package com.countableset.pkgsystem;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class AddDelivery extends Activity
{
	// Variables
	private ListView lv1;
	private PostData data = new PostData();
	
	protected ArrayList<FriendsList> userList = new ArrayList<FriendsList>();
	protected String TestVal = "0";
	
	// Server Stuff
	private static final String URL = "http://dev.countableset.com/android/add_package.php";
	private static final String SERVER_KEY = "KbnLHqqqZLw60uctCYh1";
	
	// Context Menu Stuff
	private static final int SEND_DIRECT = Menu.FIRST;
	private static final int SEND_ROUTE = Menu.FIRST + 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_menu);
		
		// Action Bar Stuff
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle(R.string.title_bar_adddeliver);
		actionBar.setHomeAction(new IntentAction(this, MainMenu.createIntent(this), R.drawable.ic_title_home_default));
		
		generateList();
		
		// List Activity Settings
		lv1 = (ListView) findViewById(R.id.ListView01);
		lv1.setAdapter(new ArrayAdapter<FriendsList>(this, R.layout.friends_list_view, userList));
		registerForContextMenu(lv1);
		lv1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				view.showContextMenu();
              }
        });
		
	} // end onCreate()
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		// When you hold down on an item
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.deliver_menu_header);
		menu.add(0, SEND_DIRECT, 0, R.string.delivery_menu_add_direct);
		menu.add(0, SEND_ROUTE, 0, R.string.delivery_menu_add_route);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case SEND_DIRECT:
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
				FriendsList friendContext = (FriendsList) lv1.getAdapter().getItem(info.position);
				sendDirect(friendContext.getId());
				if (TestVal.equalsIgnoreCase("1")) {
					TestVal = "0";
					sendToPostOffice();
				}
				else {
				    Toast.makeText(AddDelivery.this, friendContext.toString() + " Added Successful", Toast.LENGTH_SHORT).show();
				}
				finish();
				return true;
			case SEND_ROUTE:
				AdapterContextMenuInfo infoRoute = (AdapterContextMenuInfo) item.getMenuInfo();
				FriendsList userContext = (FriendsList) lv1.getAdapter().getItem(infoRoute.position);
				sendRoute(userContext.getId());
				if (TestVal.equalsIgnoreCase("1")) {
					TestVal = "0";
					sendToPostOffice();
				}
				else {
					Toast.makeText(AddDelivery.this, userContext.toString() + " Added Successful", Toast.LENGTH_SHORT).show();
				}
    	        finish();
				return true;
				
		}
		return super.onContextItemSelected(item);
	}
	
	public void sendDirect(String id)
	{
		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("key", SERVER_KEY));
		nameValuePairs.add(new BasicNameValuePair("email", SystemInfo.getEmail()));
		nameValuePairs.add(new BasicNameValuePair("password", SystemInfo.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("cond", "2"));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		
		// Get the post data to fill the friends array
		JSONObject result = data.post(nameValuePairs, URL);
		
		userList = new ArrayList<FriendsList>();
		
		try
		{
			// Create the array of names
			JSONArray names = result.getJSONArray("names");
			TestVal = names.getString(0);
		} catch (JSONException ex) {}
	}

	public void sendRoute(String id)
	{
		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("key", SERVER_KEY));
		nameValuePairs.add(new BasicNameValuePair("email", SystemInfo.getEmail()));
		nameValuePairs.add(new BasicNameValuePair("password", SystemInfo.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("cond", "3"));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		
		// Get the post data to fill the friends array
		JSONObject result = data.post(nameValuePairs, URL);
		
		userList = new ArrayList<FriendsList>();
		
		try
		{
			// Create the array of names
			JSONArray names = result.getJSONArray("names");
			TestVal = names.getString(0);
		} catch (JSONException ex) {}
		
	}
	
	public void generateList()
	{
		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("key", SERVER_KEY));
		nameValuePairs.add(new BasicNameValuePair("email", SystemInfo.getEmail()));
		nameValuePairs.add(new BasicNameValuePair("password", SystemInfo.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("cond", "1"));
		
		// Get the post data to fill the friends array
		JSONObject result = data.post(nameValuePairs, URL);
		
		userList = new ArrayList<FriendsList>();
		
		try
		{
			// Create the array of names
			JSONArray names = result.getJSONArray("names");

			// Fill the friends array with the JSONObject
			for (int i = 0; i < names.length(); i++)
			{
				userList.add(new FriendsList(names.getString(i++), names.getString(i)));
			}
		} catch (JSONException ex)
		{
		}
	} // end generateList()
	
	// method will pull up Map with "post office" query
	public void sendToPostOffice()
	{
		Toast.makeText(AddDelivery.this, "Sorry, No Delivery Path.\nHow about the Post Office?", Toast.LENGTH_LONG).show();
		String url = "http://maps.google.com/maps?q=post+office";
		Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
		startActivity(browserIntent);
	} // end sendToPostOffice()
}
