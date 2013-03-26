package com.example.data;
import java.util.ArrayList;

public class Person
{
	private int id; //�û�ID
	private String name;//����
	private String phoneNumber = null;//�ֻ�����
	private int sex = 0;//true�������ԣ�false����Ů��
	private String group = null; //������
	private ArrayList<String> personTag = new ArrayList<String>();//���˱�ǩ
	private ArrayList<String>groupTag = new ArrayList<String>(); //���ǩ
	
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
	
	//��Ӹ��˱�ǩ
	public ArrayList<String> appendPersonTag(String tag)
	{
		this.personTag.add(tag);
		return personTag;
	}
	
	//������ǩ
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
