package com.example.data;
import java.util.ArrayList;

public class Person
{
	private int id; //用户ID
	private String name;//姓名
	private String phoneNumber = null;//手机号码
	private int sex = 0;//true代表男性，false代表女性
	private String group = null; //所在组
	private ArrayList<String> personTag = new ArrayList<String>();//个人标签
	private ArrayList<String>groupTag = new ArrayList<String>(); //组标签
	
	public Person(String name,String phoneNumber)
	{
		this.name = name;
		this.phoneNumber = phoneNumber;
	}
	public Person(String name, int sex, String group,String phoneNumber,ArrayList<String> tag)
	{
		this.name = name;
		this.sex = sex;
		this.group = group;
		this.phoneNumber = phoneNumber;
		this.personTag.addAll(tag);
	}
	
	public Person(int id, String name, int sex, String group,String phoneNumber)
	{
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.group = group;
		this.phoneNumber = phoneNumber;
	}
	
	//添加个人标签
	public ArrayList<String> appendPersonTag(String tag)
	{
		this.personTag.add(tag);
		return personTag;
	}
	
	//添加组标签
	public ArrayList<String> appendGroupTag(String tag)
	{
		this.groupTag.add(tag);
		return groupTag;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public int getSex()
	{
		return sex;
	}

	public String getGroup()
	{
		return group;
	}

	public ArrayList<String> getPersonTag()
	{
		return personTag;
	}

	public ArrayList<String> getGroupTag()
	{
		return groupTag;
	}
	
}
