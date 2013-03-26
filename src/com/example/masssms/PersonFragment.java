package com.example.masssms;

import com.example.data.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class PersonFragment extends Fragment
{
	private Contact db = null;
	private View view = null;
	private ListView blank = null;
	private int lastClick = 0;
	private List<HashMap<String, String>> list = null;
	private HashMap<Integer, Integer> pos = null;
	private SimpleAdapter adapter = null;

	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.person_fragment, container, false);
		db = new Contact(getActivity());
		
		initButton();
		initList();
		// TODO Auto-generated method stub
		return view;
	}

	private void initButton()
	{
		Button call_button = (Button) view
				.findViewById(R.id.people_fragment_call);
		Button add_button = (Button) view
				.findViewById(R.id.people_fragment_add);
		Button edit_button = (Button) view
				.findViewById(R.id.people_fragment_edit);
		Button delete_button = (Button) view
				.findViewById(R.id.people_fragment_delete);
		call_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				//КєНа
			}
		});
		add_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(getActivity(), PersonEditor.class);
				startActivityForResult(intent, 1);
			}
		});
		edit_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(getActivity(), PersonEditor.class);
				intent.putExtra("id", pos.get(lastClick));
				startActivityForResult(intent, 2);
			}
		});
		delete_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				Log.i("error", "" + lastClick);
				db.deletePerson((int)pos.get(lastClick));				
				list.remove(lastClick);
				adapter.notifyDataSetChanged();
			}
		});
	}

	private void initList()
	{
		pos = new HashMap<Integer, Integer>();
		list = new ArrayList<HashMap<String, String>>();
		blank = (ListView) view.findViewById(R.id.people_fragment_blank);
		
		ArrayList<Integer> idList = db.getIdList();// id-name

		int p = 0;
		for(int i = 0; i < idList.size(); i ++)
		{
			HashMap<String, String> map = new HashMap<String, String>();
			Person person = db.getPersonData(idList.get(i));
			map.put("name", person.getName());
			pos.put(p, idList.get(i));
			list.add(map);
			p ++;
		}
		adapter = new SimpleAdapter(getActivity(), list,
				R.layout.person_list, new String[] { "name" },
				new int[] { R.id.person_list_name });
		blank.setAdapter(adapter);
		
		blank.setOnItemClickListener(new OnItemClickListener()
		{
			 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			 {
				 arg0.getChildAt(lastClick).setBackgroundResource(android.R.color.white);
				 arg1.setBackgroundResource(android.R.color.holo_blue_bright);
				 lastClick = arg2;
			 }
		});			
	}
	
	public void modifyData(int requestCode, int resultCode, Intent data)
	{
		Person person = db.getPersonData(data.getIntExtra("id", -1));
		HashMap<String, String> map = new HashMap<String, String>();
		switch(resultCode)
		{
		case 1:
			{
				map.put("name", person.getName());
				pos.put(list.size(), person.getId());
				list.add(map);
				adapter.notifyDataSetChanged();
				break;
			}
		case 2:break;
		default:break;
		}
	}
}
