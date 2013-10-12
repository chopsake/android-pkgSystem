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
import android.widget.Toast;

public class Register extends Activity
{
	ProgressDialog dialog;
	EditText fname, lname, email, password;
	Button submit;
	private PostData data = new PostData();
	int lat;
	int lng;
	
	// Server Stuff
	private static final String SERVER_KEY = "KbnLHqqqZLw60uctCYh1";
	private static final String URL = "http://dev.countableset.com/android/register.php";
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        // Getting the id for the text fields
        fname = (EditText) findViewById(R.id.txt_fname);
        lname = (EditText) findViewById(R.id.txt_lname);
        email = (EditText) findViewById(R.id.txt_email);
        password = (EditText) findViewById(R.id.txt_password);
        submit = (Button) findViewById(R.id.submit_button);
        
        submit.setOnClickListener(new View.OnClickListener()
        {
        	@Override
        	public void onClick(View v)
        	{
        		register();
        	}
        });
        
    } // end onCreate()
	
	public void register()
	{
		// Checks if everything is filled in
		if(fname.getText().toString().equals("") || lname.getText().toString().equals("") 
			|| email.getText().toString().equals("") || password.getText().toString().equals(""))
		{
			Toast.makeText(Register.this, "Missing information!", Toast.LENGTH_SHORT).show();
		}
		else
		{	
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("key", SERVER_KEY));
			nameValuePairs.add(new BasicNameValuePair("fname", fname.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("lname", lname.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString()));
			nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));
			
			// Get the post data to fill the friends array
			JSONObject result = data.post(nameValuePairs, URL);
			
			try
			{
				// Create the array of names
				JSONArray info = result.getJSONArray("response");
				
				if(info.getString(0).equals("0"))		// Register is a success go back to the login screen
				{
					Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
					Intent i = new Intent(this, PkgSystem.class);
	    	        startActivity(i);
				}
				else if(info.getString(0).equals("1"))	// Register is a failure
				{
					Toast.makeText(Register.this, "Error, Email Address Already Registered?", Toast.LENGTH_SHORT).show();
				}
				else if(info.getString(0).equals("2"))
				{
					Toast.makeText(Register.this, "Invalid Email Address!", Toast.LENGTH_SHORT).show();
				}
				
			} catch (JSONException ex)
			{
			}
		}
	} // end register()	
} // end class
