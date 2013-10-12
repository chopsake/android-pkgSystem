package com.countableset.pkgsystem;

import android.app.Application;


public class SystemInfo extends Application
{
	private static String email;
	private static String password;
	
	@Override
	public void onCreate() 
	{
	    super.onCreate();
	    email="";
	    password="";
	}

	public static String getEmail() {
	    return email;
	}

	public static void setEmail(String username) {
	    SystemInfo.email = username;
	}

	public static String getPassword() {
	    return password;
	}

	public static void setPassword(String password) {
	    SystemInfo.password = password;
	}
}
