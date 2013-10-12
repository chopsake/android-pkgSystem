package com.countableset.pkgsystem;


public class PackageList
{
	protected String name;
	protected String id;
	protected String pkg_id;

	protected PackageList(String name, String id, String pkg_id)
	{
		this.name = name;
		this.id = id;
		this.pkg_id = pkg_id;
	}
	
	public String getID()
	{
		return pkg_id;
	}
	
	public String getName()
	{
		return name;
	}

	public String toString()
	{
		return name + "\nPkg ID: " + pkg_id;
	}

	public boolean equals(Object o)
	{
		return o instanceof FriendsList
				&& ((PackageList) o).name.compareTo(name) == 0;
	}
}