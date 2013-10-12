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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class OptionsInfo extends Activity
{
	private EditText fname;
	private EditText lname;
	private PostData data = new PostData();
	
	// Server Stuff
	private static final String SERVER_KEY = "KbnLHqqqZLw60uctCYh1";
	private static final String URL = "http://dev.countableset.com/android/options_info.php";
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options_info);
		
		// Action Bar Stuff
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(R.string.options);
        actionBar.setHomeAction(new IntentAction(this, MainMenu.createIntent(this), R.drawable.ic_title_home_default));
        
        fname = (EditText) findViewById(R.id.txt_first_name);
        lname = (EditText) findViewById(R.id.txt_last_name);
        
        populateFields();
        
        // SUBMIT BUTTON
        final Button buttonSubmit = (Button) findViewById(R.id.submit_button);
        buttonSubmit.setOnClickListener(new OnClickListener() {
        	public void onClick(View v)
        	{
        		updateInfo();
        		Toast.makeText(OptionsInfo.this, "Information Updated", Toast.LENGTH_SHORT).show();
        		finish();
        	}
        });
        
	} // end onCreate()
	
	public void populateFields()
	{
		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("key", SERVER_KEY));
		nameValuePairs.add(new BasicNameValuePair("email", SystemInfo.getEmail()));
		nameValuePairs.add(new BasicNameValuePair("password", SystemInfo.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("action", "1"));
		
		// Get the post data to fill the friends array
		JSONObject result = data.post(nameValuePairs, URL);
		
		try
		{
			// Create the array of names
			JSONArray info = result.getJSONArray("info");
			
			fname.setText(info.getString(0));	// First Name is at position 0
			lname.setText(info.getString(1));	// Last Name is at position 1
			
		} catch (JSONException ex)
		{
		}
	} // end populateFields()
	
	public void updateInfo()
	{
		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("key", SERVER_KEY));
		nameValuePairs.add(new BasicNameValuePair("email", SystemInfo.getEmail()));
		nameValuePairs.add(new BasicNameValuePair("password", SystemInfo.getPassword()));
		nameValuePairs.add(new BasicNameValuePair("action", "2"));
		nameValuePairs.add(new BasicNameValuePair("fname", fname.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("lname", lname.getText().toString()));
		
		data.post(nameValuePairs, URL);
	} // end updateInfo()

} // end class
