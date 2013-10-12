package com.countableset.pkgsystem;


public class FriendsList
{
	protected String name;
	protected String id;

	protected FriendsList(String name, String id)
	{
		this.name = name;
		this.id = id;
	}

	public String getId()
	{
		return id;
	}

	public String toString()
	{
		return name;
	}

	public boolean equals(Object o)
	{
		return o instanceof FriendsList
				&& ((FriendsList) o).name.compareTo(name) == 0;
	}
}
