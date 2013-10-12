package com.countableset.pkgsystem;


public class PendingList
{
	protected String name;
	protected String id;
	protected String pkg_id;
	protected String current_name;
	protected String current_id;

	protected PendingList(String name, String id, String pkg_id, String current_name, String current_id)
	{
		this.name = name;
		this.id = id;
		this.pkg_id = pkg_id;
		this.current_name = current_name;
		this.current_id = current_id;
	}
	
	public String getID()
	{
		return pkg_id;
	}
	
	public String getCurrentID()
	{
		return current_id;
	}

	public String getCurrentName()
	{
		return current_name;
	}
	
	public String toString()
	{
		return "Destination: " + name + "\nCurrent Location: " + current_name + "\nPkg ID: " + pkg_id;
	}

	public boolean equals(Object o)
	{
		return o instanceof FriendsList
				&& ((PendingList) o).name.compareTo(name) == 0;
	}
}