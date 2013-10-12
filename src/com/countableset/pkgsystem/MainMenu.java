package com.countableset.pkgsystem;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class MainMenu extends Activity
{
	private PostData data = new PostData();
	
	// Server Stuff
	private static final String SERVER_KEY = "KbnLHqqqZLw60uctCYh1";
	private static final String URL_CHECK = "http://dev.countableset.com/android/check_location.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		// Action Bar Stuff
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(R.string.title_bar);
        actionBar.setHomeAction(new IntentAction(this, createIntent(this), R.drawable.ic_title_home_default));
        
        checkLocation();
        
        // SEND BUTTON
        final Button buttonSend = (Button) findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new OnClickListener() {
        	public void onClick(View v)
        	{
        		Intent i = new Intent(MainMenu.this, AddDelivery.class);
        		startActivity(i);
        		// Toast.makeText(MainMenu.this, "Send Package", Toast.LENGTH_SHORT).show();
        	}
        });
        
        // DELIVER BUTTON
        final Button buttonDeliver = (Button) findViewById(R.id.button_deliver);
        buttonDeliver.setOnClickListener(new OnClickListener() {
        	public void onClick(View v)
        	{
        		Intent i = new Intent(MainMenu.this, Deliver.class);
        		startActivity(i);
        		// Toast.makeText(MainMenu.this, "Deliver Package", Toast.LENGTH_SHORT).show();
        	}
        });
        
        // PENDING BUTTON
        final Button buttonPending = (Button) findViewById(R.id.button_pending);
        buttonPending.setOnClickListener(new OnClickListener() {
        	public void onClick(View v)
        	{
        		Intent i = new Intent(MainMenu.this, Pending.class);
        	    startActivity(i);
        		// Toast.makeText(MainMenu.this, "Pending Packages", Toast.LENGTH_SHORT).show();
        	}
        });
        
        // FRIENDS BUTTON
        final Button buttonFriends = (Button) findViewById(R.id.button_friends);
        buttonFriends.setOnClickListener(new OnClickListener() {
        	public void onClick(View v)
        	{
        		Intent i = new Intent(MainMenu.this, Friends.class);
        		startActivity(i);
        	}
        });
        
        // OPTIONS BUTTON
        final Button buttonOptions = (Button) findViewById(R.id.button_options);
        buttonOptions.setOnClickListener(new OnClickListener() {
        	public void onClick(View v)
        	{
        		Intent i = new Intent(MainMenu.this, Options.class);
        		startActivity(i);
        	}
        });
        
    } // end onCreate()
    
    public static Intent createIntent(Context context) 
    {
        Intent i = new Intent(context, MainMenu.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }
    
    public void checkLocation()
    {
    	// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("key", SERVER_KEY));
		nameValuePairs.add(new BasicNameValuePair("email", SystemInfo.getEmail()));
		nameValuePairs.add(new BasicNameValuePair("password", SystemInfo.getPassword()));
		
		// Get the post data to fill the friends array
		JSONObject result = data.post(nameValuePairs, URL_CHECK);
		
		try
		{
			// Create the array of names
			JSONArray info = result.getJSONArray("response");

			if(info.getString(0).equals("0"))
			{
				Intent i = new Intent(this, OptionsLocation.class);
        		startActivity(i);
			}
			
		} catch (JSONException ex)
		{
		}
    }

}
