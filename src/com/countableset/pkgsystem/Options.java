package com.countableset.pkgsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class Options extends Activity
{	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options_menu);
		// Action Bar Stuff
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(R.string.options);
        actionBar.setHomeAction(new IntentAction(this, MainMenu.createIntent(this), R.drawable.ic_title_home_default));
        
        // PERSONAL INFO BUTTON
        final Button buttonInfo = (Button) findViewById(R.id.button_info);
        buttonInfo.setOnClickListener(new OnClickListener() {
        	public void onClick(View v)
        	{
        		Intent i = new Intent(Options.this, OptionsInfo.class);
        		startActivity(i);
        	}
        });
        
        // LOCATION BUTTON
        final Button buttonLocation = (Button) findViewById(R.id.button_location);
        buttonLocation.setOnClickListener(new OnClickListener() {
        	public void onClick(View v)
        	{
        		Intent i = new Intent(Options.this, OptionsLocation.class);
        		startActivity(i);
        	}
        });
	} // end onCreate()
}
