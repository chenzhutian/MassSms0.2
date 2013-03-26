package com.example.masssms;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.data.*;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;

public class PersonEditor extends Activity
{
	private Contact db = null;
	private EditText name_et = null;
	private Spinner sex_s = null;
	private EditText number_et = null;
	private ListView tag_lv = null;
	private int id = 0;
	private int lastClick = 0;
	private Person person = null;
	private ArrayAdapter<String> tagAdapter = null;
	private EditText tag_add_et = null;
	private ArrayList<String> tag = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		db = new Contact(this);
		id = this.getIntent().getIntExtra("id", -1);
		setContentView(R.layout.person_editor);
		initButton();
		initList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.person_editor, menu);
		return true;
	}

	private void initButton()
	{
		Button tag_add_button = (Button) findViewById(R.id.person_editor_tag_add);
		Button tag_delete_button = (Button) findViewById(R.id.person_editor_tag_delete);
		Button save_button = (Button) findViewById(R.id.person_editor_save);
		Button cancle_button = (Button) findViewById(R.id.person_editor_cancle);
		tag_add_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				tag_add_et = new EditText(PersonEditor.this);
				new AlertDialog.Builder(PersonEditor.this).setTitle("请输入")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(tag_add_et)
						.setNegativeButton("取消", null)
						.setPositiveButton("确定", new OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int which)
							{
								tag.add(tag_add_et.getText().toString());
								tagAdapter.notifyDataSetChanged();
								dialog.dismiss();
							}
						}).show();
			}
		});
		tag_delete_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{				
				tag.remove(lastClick);
				tagAdapter.notifyDataSetChanged();
			}
		});
		save_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent();
				person = new Person(name_et.getText().toString(), 1 - sex_s.getSelectedItemPosition(), null, number_et.getText().toString(), tag);
				
				if(id == -1)
				{
					intent.putExtra("id", (int)db.craeteNewPerson(person));
					setResult(11, intent);
				}
				else
				{
					db.modifyPerson(person);
					intent.putExtra("id", id);
					setResult(12, intent);
				}
				finish();
			}
		});
		cancle_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
			}
		});
	}

	private void initList()
	{
		name_et = (EditText) findViewById(R.id.person_editor_name);
		sex_s = (Spinner) findViewById(R.id.person_editor_sex);
		number_et = (EditText) findViewById(R.id.person_editor_number);
		tag_lv = (ListView) findViewById(R.id.person_editor_tag_list);
		tagAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1 , tag);

		ArrayList<String> choice = new ArrayList<String>();
		
		choice.add("男");
		choice.add("女");
		ArrayAdapter<String> sexAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, choice);
		sexAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sex_s.setAdapter(sexAdapter);

		tag_lv.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				arg0.getChildAt(lastClick).setBackgroundResource(
						android.R.color.white);
				arg1.setBackgroundResource(android.R.color.holo_blue_bright);
				lastClick = arg2;
			}
		});

		if (id != -1)
		{
			person = db.getPersonData(id);
			name_et.setText(person.getName());
			sex_s.setSelection(1 - person.getSex());
			number_et.setText(person.getPhoneNumber());
			tag = person.getPersonTag();
			tagAdapter.notifyDataSetChanged();			
		}
		tag_lv.setAdapter(tagAdapter);
	}

}
