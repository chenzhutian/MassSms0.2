package com.example.data;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

public class Contact
{
	private DataBase db;
	private Context context;
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer,String>  id_name_List = new HashMap<Integer,String>();
	private HashMap<String,ArrayList<Integer>> name_id_List = new HashMap<String,ArrayList<Integer>>();
	private ArrayList<Integer> idList = new ArrayList<Integer>();
	public Contact(Context context)
	{
		this.context = context;
		db = new DataBase(this.context);
		this.getSystemContact();
	}
	
	private void makeIdNameList()
	{
		Cursor cursor = db.getNameList();
		ArrayList<Integer> tempList ;
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		{
			this.id_name_List.put(cursor.getInt(cursor.getColumnIndex("personId")), cursor.getString(cursor.getColumnIndex("name")));
			this.idList.add(cursor.getInt(cursor.getColumnIndex("personId")));
			tempList = this.name_id_List.get(cursor.getString(cursor.getColumnIndex("name")));
			if(tempList == null)
				tempList = new ArrayList<Integer>();
			tempList.add(cursor.getInt(cursor.getColumnIndex("personId")));
			this.name_id_List.put(cursor.getString(cursor.getColumnIndex("name")),tempList);
		}
	}
	public boolean getSystemContact()
	{
		try
		{
			db.getSystemContact();
			this.makeIdNameList();
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
    public long craeteNewPerson(Person person)
    {
    	return db.insertData(person);
    }
    
	public boolean deletePerson(Person person)
	{
		return db.deleteDataById(person.getId());
	}
	
	public boolean deletePerson(int id)
	{
		return db.deleteDataById(id);
	}
	
	public boolean modifyPerson(Person person)
	{
		return db.modifyData(person);
	}
	
	public Person getPersonData(int id)
	{
		return db.getDataById(id);
	}
	
    public ArrayList<Person> searchPersonByName(String name)
    {
    	ArrayList<Integer> tempList ;
    	ArrayList<Person> personList = new ArrayList<Person>();
    	tempList = name_id_List.get(name);
    	for(int i  = 0;i < tempList.size();i++)
    	{
    		int id = tempList.get(i);
    		personList.add(db.getDataById(id));
    	}
    	return personList;
    }
    
    public ArrayList<Integer> getIdList()
    {
    	return this.idList;
    }
}
