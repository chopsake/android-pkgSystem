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

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class AddFriend extends Activity
{
	// Variables
	private ListView lv1;
	private PostData data = new PostData();
	
	protected ArrayList<FriendsList> friendsList = new ArrayList<FriendsList>();
	
	// Server Stuff
	private static final String URL_ADD = "http://dev.countableset.com/android/add_friend.php";
	private static final String SERVER_KEY = "KbnLHqqqZLw60uctCYh1";
	
	// Context Menu Stuff
	private static final int FRIEND_ID = Menu.FIRST;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friends_menu);
		
		// Action Bar Stuff
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle(R.string.title_bar_addfriend);
		actionBar.setHomeAction(new IntentAction(this, MainMenu.createIntent(this), R.drawable.ic_title_home_default));
		
		generateList();
		
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
		
	} // end onCreate()
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo)
	{
		// When you hold down on an item
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.friends_menu_header);
		menu.add(0, FRIEND_ID, 0, R.string.friends_menu_add);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case FRIEND_ID:
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
				FriendsList friendContext = (FriendsList) lv1.getAdapter().getItem(info.position);
				addFriend(friendContext.getId());
				Toast.makeText(AddFriend.this, friendContext.toString() + " Added", Toast.LENGTH_SHORT).show();
    	        finish();
				return true;
		}
		return super.onContextItemSelected(item);
	}
	
	public void addFriend(String id)
	{
		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("key", SERVER_KEY));
		nameValuePairs.add(new BasicNameValuePair("email", SystemInfo.getEmail()));
		nameValuePairs.add(new BasicNameValuePair("password", SystemInfo.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("cond", "2"));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		
		// Get the post data to fill the friends array
		data.post(nameValuePairs, URL_ADD);
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
		JSONObject result = data.post(nameValuePairs, URL_ADD);
		
		friendsList = new ArrayList<FriendsList>();
		
		try
		{
			// Create the array of names
			JSONArray names = result.getJSONArray("names");

			// Fill the friends array with the JSONObject
			for (int i = 0; i < names.length(); i++)
			{
				friendsList.add(new FriendsList(names.getString(i++), names.getString(i)));
			}
		} catch (JSONException ex)
		{
		}
	} // end generateList()
}
