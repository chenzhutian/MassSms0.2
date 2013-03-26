package com.example.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.ContactsContract;
import android.util.Log;

public class DataBase
{
	private MyDatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private Context context;

	public DataBase(Context context)
	{
		this.context = context;
		dbHelper = new MyDatabaseHelper(this.context, "Contact_db.db");
		try
		{
			db = dbHelper.getWritableDatabase();
		}
		catch (SQLiteException e)
		{
			Log.i("error", e.getMessage());
		}

	}

	public void getSystemContact()
	{
		ContentValues values = new ContentValues();
		db.delete("user", null, null);
		db.delete("userGroup", null, null);
		db.delete("userTag", null, null);
		db.delete("groupTag", null, null);
		values.put("seq", 0);
		db.update("sqlite_sequence", values, "name=?", new String[] { "user" });

		int id = 1;
		ContentResolver contentResolver = this.context.getContentResolver();
		// Cursor cursor = contentResolver.query(Data.CONTENT_URI, null,
		// "mimetype='vnd.android.cursor.item/phone_v2'", null,
		// Data.DISPLAY_NAME);
		Cursor cursor = contentResolver
				.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						new String[] {
								ContactsContract.CommonDataKinds.Phone.NUMBER,
								ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME },
						null, null,
						ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
		values.clear();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
		{
			values.put("personId", id);
			values.put(
					"name",
					cursor.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
			values.put(
					"phoneNumber",
					cursor.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
			db.insert("user", null, values);
			values.clear();
			id++;
		}
	}

	public Person getDataById(int personId)
	{
		String name = "";
		int sex = 0;
		String phoneNumber = "";
		String groupName = "";

		Cursor cursor = db.query("user", new String[] { "name", "sex",
				"phoneNumber" }, "personId=?", new String[] { personId + "" },
				null, null, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
		{
			name = cursor.getString(cursor.getColumnIndex("name"));
			sex = cursor.getInt(cursor.getColumnIndex("sex"));
			phoneNumber = cursor
					.getString(cursor.getColumnIndex("phoneNumber"));
		}
		cursor = db.query("userGroup", new String[] { "groupName" },
				"personId=?", new String[] { personId + "" }, null, null, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
		{
			groupName = cursor.getString(cursor.getColumnIndex("groupName"));
		}

		Person person = new Person(personId, name, sex, groupName, phoneNumber);

		cursor = db.query("userTag", new String[] { "userTag" }, "personId=?",
				new String[] { personId + "" }, null, null, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
		{
			String tag = cursor.getString(cursor.getColumnIndex("userTag"));
			person.appendPersonTag(tag);
		}
		cursor = db.query("groupTag", new String[] { "groupTag" },
				"groupName=?", new String[] { groupName }, null, null, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
		{
			String tag = cursor.getString(cursor.getColumnIndex("groupTag"));
			person.appendGroupTag(tag);
		}

		return person;
	}

	// public Person getDataByName(String name)
	// {
	// int personId = 0;
	// int sex = 0;
	// String phoneNumber = "";
	// String groupName = "";
	// int groupId = 0;
	//
	// Cursor cursor = db.query("user", new
	// String[]{"personId","sex","phoneNumber"}, "name=?", new String[]{name},
	// null, null, null);
	// for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
	// {
	// personId = cursor.getInt(cursor.getColumnIndex("personId"));
	// sex = cursor.getInt(cursor.getColumnIndex("sex"));
	// phoneNumber = cursor.getString(cursor.getColumnIndex("phoneNumber"));
	// }
	// cursor = db.query("userGroup", new String[]{"groupId", "groupName"},
	// "personId=?", new String[]{personId+""},null, null, null);
	// for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
	// {
	// groupName = cursor.getString(cursor.getColumnIndex("groupName"));
	// groupId = cursor.getInt(cursor.getColumnIndex("groupId"));
	// }
	// Person person = new Person(personId,name,sex,groupName,phoneNumber);
	//
	// cursor = db.query("userTag", new String[]{"userTag"}, "personId=?", new
	// String[]{personId+""}, null, null, null);
	// for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
	// {
	// String tag = cursor.getString(cursor.getColumnIndex("userTag"));
	// person.appendPersonTag(tag);
	// }
	// cursor = db.query("groupTag", new String[]{"groupTag"}, "groupId=?", new
	// String[]{groupId+""}, null,null,null);
	// for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
	// {
	// String tag = cursor.getString(cursor.getColumnIndex("groupTag"));
	// person.appendGroupTag(tag);
	// }
	// return person;
	// }

	public boolean deleteDataById(int personId)
	{
		if (db.delete("user", "personId=?", new String[] { personId + "" }) == 0)
			return false;
		if (db.delete("userTag", "personId=?", new String[] { personId + "" }) == 0)
			return false;
		if (db.delete("userGroup", "personId=?", new String[] { personId + "" }) == 0)
			return false;
		return true;
	}

	public boolean modifyData(Person person)
	{
		ContentValues values = new ContentValues();
		values.put("personId", person.getId());
		values.put("sex", person.getSex());
		values.put("phoneNumber", person.getPhoneNumber());
		if (db.update("user", values, "personId=?",
				new String[] { person.getId() + "" }) != 1)
			return false;
		values.clear();

		String tag;
		db.delete("userTag", "personId=?", new String[] { person.getId() + "" });
		for (int i = 0; i < person.getPersonTag().size(); i++)
		{
			tag = person.getPersonTag().get(i);
			values.put("personId", person.getId());
			values.put("userTag", tag);
			try
			{
				db.insertOrThrow("userTag", null, values);
			}
			catch (SQLException e)
			{
				return false;
			}
			values.clear();
		}

		values.put("personId", person.getId());
		values.put("groupName", person.getGroup());
		if (db.update("userGroup", values, "personId=?",
				new String[] { person.getId() + "" }) != 1)
			return false;
		values.clear();

		return true;
	}

	public long insertData(Person person)
	{
		long personId;
		String tag;
		ContentValues values = new ContentValues();
		try
		{
			values.put("name", person.getName());
			values.put("sex", person.getSex());
			values.put("phoneNumber", person.getPhoneNumber());
			personId = db.insert("user", null, values);
			values.clear();
			values.put("personId", personId);
			db.update("user", values, "_id=?", new String[] { personId + "" });
			values.clear();

			for (int i = 0; i < person.getPersonTag().size(); i++)
			{
				tag = person.getPersonTag().get(i);
				values.put("personId", personId);
				values.put("userTag", tag);
				db.insert("userTag", null, values);
				values.clear();
			}

			if (person.getGroup() != null)
			{
				values.put("personId", personId);
				values.put("groupName", person.getGroup());
				db.insert("userGroup", null, values);
				values.clear();
			}

		}
		catch (SQLiteException e)
		{
			Log.i("error", e.getMessage());
			return -1;
		}

		return personId;
	}

	public Cursor getNameList()
	{
		Cursor cursor = db.query("user", new String[] { "personId", "name" },
				null, null, null, null, null);
		return cursor;
	}

	public Cursor getUserTable()
	{
		Cursor cursor = db.query("user", new String[] { "personId", "name",
				"sex", "phoneNumber" }, null, null, null, null, null);
		return cursor;
	}

	public Cursor getuserTagTable()
	{
		Cursor cursor = db.query("userTag", new String[] { "personId",
				"userTag" }, null, null, null, null, null);
		return cursor;
	}

	public Cursor getUserGroupTable()
	{
		Cursor cursor = db.query("userGroup", new String[] { "personId",
				"groupId", "groupName" }, null, null, null, null, null);
		return cursor;
	}

	public Cursor getGroupTagTable()
	{
		Cursor cursor = db.query("groupTag", new String[] { "groupID",
				"groupTag" }, null, null, null, null, null);
		return cursor;
	}

}
