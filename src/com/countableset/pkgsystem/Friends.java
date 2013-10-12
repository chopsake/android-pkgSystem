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

public class Friends extends Activity
{
	// Variables
	private ListView lv1;
	private PostData data = new PostData();
	private int opt = 0;
	// List array of friends
	protected ArrayList<FriendsList> friendsList = new ArrayList<FriendsList>();

	private static final String URL_GET = "http://dev.countableset.com/android/friends.php";
	private static final String URL_REMOVE = "http://dev.countableset.com/android/remove_friend.php";
	private static final String URL_BLACKLIST = "http://dev.countableset.com/android/blacklist_friend.php";
	private static final String URL_REMOVEWILL = "http://dev.countableset.com/android/remove_will.php";
	private static final String SERVER_KEY = "KbnLHqqqZLw60uctCYh1";

	// Menu Items
	private static final int DELETE_ID = Menu.FIRST;
	private static final int BLACK_ID = Menu.FIRST + 1;
	private static final int INSERT_ID = Menu.FIRST + 2;
	private static final int Will_ID = Menu.FIRST + 3;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_menu);

		// Action Bar Stuff
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle(R.string.title_bar_friends);
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
		generateFriends();
		// List Activity Settings
		lv1 = (ListView) findViewById(R.id.ListView01);
		lv1.setAdapter(new ArrayAdapter<FriendsList>(this, R.layout.friends_list_view, friendsList));
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
		menu.setHeaderTitle(R.string.friends_menu_header);
		menu.add(0, DELETE_ID, 0, R.string.friends_menu_delete);
		menu.add(0, BLACK_ID, 0, R.string.friends_menu_blacklist);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case DELETE_ID:
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
				FriendsList friendContext = (FriendsList) lv1.getAdapter().getItem(info.position);
				// Removing friend
				removeFriend(friendContext.getId());
				generateFriends();
				lv1.setAdapter(new ArrayAdapter<FriendsList>(this, R.layout.friends_list_view, friendsList));
				Toast.makeText(Friends.this, friendContext.toString() + " Removed", Toast.LENGTH_SHORT).show();
				return true;
			case BLACK_ID:
				AdapterContextMenuInfo infoBlack = (AdapterContextMenuInfo) item.getMenuInfo();
				FriendsList blackContext = (FriendsList) lv1.getAdapter().getItem(infoBlack.position);
				// Removing friend
				removeFriend(blackContext.getId());
				blacklist(blackContext.getId());
				generateFriends();
				lv1.setAdapter(new ArrayAdapter<FriendsList>(this, R.layout.friends_list_view, friendsList));
				Toast.makeText(Friends.this, blackContext.toString() + " Blacklisted", Toast.LENGTH_SHORT).show();
				return true;
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
	{
        super.onCreateOptionsMenu(menu);
        // When you press the menu button
        menu.add(0, INSERT_ID, 0, R.string.friends_menu_add);
        menu.add(0, Will_ID, 0, R.string.WillDeliver);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) 
    {
        switch(item.getItemId()) 
        {
            case INSERT_ID:
            	Intent i = new Intent(Friends.this, AddFriend.class);
    	        startActivityForResult(i, opt);
    	        if (opt == 1)
    	        	finish();
                return true;
            case Will_ID:
            	Intent j = new Intent(Friends.this, WillDeliver.class);
            	startActivityForResult(j, opt);
            	if (opt == 1)
            		finish();
            	return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
	
    public void blacklist(String id)
    {
    	// setting up variables for post
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("key", SERVER_KEY));
		nameValuePairs.add(new BasicNameValuePair("email", SystemInfo.getEmail()));
		nameValuePairs.add(new BasicNameValuePair("password", SystemInfo.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("removeid", id));
		// posting information
		data.post(nameValuePairs, URL_BLACKLIST);
		data.post(nameValuePairs, URL_REMOVEWILL);
    }
	
	public void generateFriends()
	{
		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("key", SERVER_KEY));
		nameValuePairs.add(new BasicNameValuePair("email", SystemInfo.getEmail()));
		nameValuePairs.add(new BasicNameValuePair("password", SystemInfo.getPassword()));
		
		// Get the post data to fill the friends array
		JSONObject result = data.post(nameValuePairs, URL_GET);
		
		friendsList = new ArrayList<FriendsList>();
		
		try
		{
			// Create the array of names
			JSONArray names = result.getJSONArray("names");

			// Fill the friends array with the JSONObject
			for (int i = 0; i < names.length(); i++)
			{
				friendsList.add(new FriendsList(names.getString(i++),
						names.getString(i)));
			}
		} catch (JSONException ex)
		{
		}
	} // end generateFriends
	
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
