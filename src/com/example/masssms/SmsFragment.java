package com.example.masssms;

import com.example.data.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class SmsFragment extends Fragment
{
	private int kind = 1;
	private MessageAdapter adapter = null;
	private ArrayList<Integer> choice = new ArrayList<Integer>();
	private HashMap<Integer, Integer> pos = new HashMap<Integer, Integer>();//position-id
	private Contact db = null;
	private View view = null;
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.sms_fragment, container, false);
		db = new Contact(getActivity());
		SharedPreferences settings = getActivity().getSharedPreferences("settings",0);
		kind = settings.getInt("KIND", 1);
		initButton();
		initList();
		// TODO Auto-generated method stub
		return view;
	}
	private void initButton()
	{
		Button add_button = (Button)view.findViewById(R.id.sms_fragment_add);
		Button send_button = (Button)view.findViewById(R.id.sms_fragment_send);
		add_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent();
				switch(kind)
				{
				case 1:
					{
						db.getSystemContact();
						initList();
						break;
					}
				case 2:intent.setClass(getActivity(), SmsSender.class);break;
				}
				
			}
		});
		send_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				intent.setClass(getActivity(), SmsSender.class);
				bundle.putIntegerArrayList("choice", choice);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	private void initList()
	{
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>(); 
		ListView blank = (ListView)view.findViewById(R.id.sms_fragment_blank);	
		
		ArrayList<Integer> idList = db.getIdList();
		int p = 0;
		for(int i = 0; i < idList.size(); i ++)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			Person person = db.getPersonData(idList.get(i));
			map.put("tv", person.getName());
			map.put("cb", false);
			list.add(map);
			pos.put(p, idList.get(i));
			p++;
		}
		adapter = new MessageAdapter(getActivity(), list, R.layout.list, 
				new String[]{"tv", "cb"}, new int[]{R.id.list_tv, R.id.list_cb});
		blank.setAdapter(adapter);
		blank.setOnItemClickListener(new ListView.OnItemClickListener() 
		{ 
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) 
			{ 
				ViewHolder holder = (ViewHolder) view.getTag(); 
				holder.cb.toggle();// 在每次获取点击的item时改变checkbox的状态 
				adapter.isSelected.put(position, holder.cb.isChecked());
				int id = pos.get(position);
				Log.i("error", id +"");
				if(holder.cb.isChecked())
				{
					choice.add(id);
				}
				else
				{
					choice.remove((Integer)id);
				}
			}
		});
	}
}
