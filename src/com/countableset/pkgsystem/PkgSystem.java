// Rachel Aurand, Ryan Fritz, Mark Mayfield
// UC Davis ECS 160 Android Project

package com.countableset.pkgsystem;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PkgSystem extends Activity 
{
	EditText un, pw;
	TextView error;
	Button ok;
	Button reg;
	ProgressDialog dialog;
	
	private PostData data = new PostData();
	private static final int ACTIVITY_CREATE=0;
	
	// Post Variables
	private static final String URL = "http://dev.countableset.com/android/login.php";
	private static final String SERVER_KEY = "KbnLHqqqZLw60uctCYh1";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Getting the id for the text fields
        un = (EditText) findViewById(R.id.txt_username);
        pw = (EditText) findViewById(R.id.txt_password);
        ok = (Button) findViewById(R.id.login_button);
        reg = (Button) findViewById(R.id.register_button);
        
        ok.setOnClickListener(new View.OnClickListener()
        {
        	@Override
        	public void onClick(View v)
        	{
        		postData();
        	}
        });
        
        reg.setOnClickListener(new View.OnClickListener()
        {
        	@Override
        	public void onClick(View v)
        	{
        		Intent i = new Intent(PkgSystem.this, Register.class);
    	        startActivity(i);
        	}
        });
    } // end onCreate()
    
    public void postData()
	{
    	// So you can tell it's doing something
    	dialog = ProgressDialog.show(this, "", "Signing In...", true);	
    	
    	// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("key", SERVER_KEY));
		nameValuePairs.add(new BasicNameValuePair("email", un.getText().toString()));
		nameValuePairs.add(new BasicNameValuePair("password", pw.getText().toString()));
		
		// Get the post data to fill the friends array
		JSONObject result = data.post(nameValuePairs, URL);
		
		try
		{
			// Create the array of names
			JSONArray res = result.getJSONArray("response");
			
			if(res.getString(0).equals("0"))
			{
				Toast.makeText(PkgSystem.this, "Error, Try Again!", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
			else if(res.getString(0).equals("1"))
			{
				SystemInfo.setEmail(un.getText().toString());
				SystemInfo.setPassword(pw.getText().toString());
				Toast.makeText(PkgSystem.this, "Success!", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(this, MainMenu.class);
		        startActivityForResult(i, ACTIVITY_CREATE);
				dialog.dismiss();
			}
			else
			{
				Toast.makeText(PkgSystem.this, "You Are Blacklisted!", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
			
		}catch (JSONException ex)
		{
		}
	} // end Post Data
}