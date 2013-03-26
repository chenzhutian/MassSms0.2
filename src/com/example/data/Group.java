package com.example.data;
import java.util.ArrayList;



public class Group
{
	private String name;//组名
	private int id; //组id
	private ArrayList<String> tag = new ArrayList<String>();//组标签
	private ArrayList<Integer> member = new ArrayList<Integer>(); //组成员
	
	public Group(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	public ArrayList<String>  appendTag(String tag)
	{
		this.tag.add(tag);
		return this.tag;
	}
	
	public ArrayList<Integer> appendMember(int id)
	{
		this.member.add(id);
		return this.member;
	}

	public String getName()
	{
		return name;
	}

	public int getId()
	{
		return id;
	}

	public ArrayList<String> getTag()
	{
		return tag;
	}

	public ArrayList<Integer> getMember()
	{
		return member;
	}
	
}
