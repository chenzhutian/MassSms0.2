package com.example.masssms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter
{
	public HashMap<Integer, Boolean> isSelected;
	private LayoutInflater inflater = null;
	private Context context = null;
	private List<HashMap<String, Object>> list = null;
	private String keyString[] = null;
	private String itemString = null; // ��¼ÿ��item��textview��ֵ
	private int idValue[] = null;// idֵ
	private int resource;

	public MessageAdapter(Context context, List<HashMap<String, Object>> list,
			int resource, String[] from, int[] to)
	{
		this.context = context;
		this.list = list;
		this.resource = resource;
		keyString = new String[from.length];
		idValue = new int[to.length];
		System.arraycopy(from, 0, keyString, 0, from.length);
		System.arraycopy(to, 0, idValue, 0, to.length);
		inflater = LayoutInflater.from(context);
		init();
	}

	public void init()
	{
		isSelected = new HashMap<Integer, Boolean>();
		for (int i = 0; i < list.size(); i++)
		{
			boolean click = (Boolean)list.get(i).get(keyString[1]);
			isSelected.put(i, click);
		}
	}

	public int getCount()
	{
		return list.size();
	}

	public Object getItem(int arg0)
	{
		return list.get(arg0);
	}

	public long getItemId(int arg0)
	{
		return arg0;

	}

	public View getView(int arg0, View arg1, ViewGroup arg2)
	{
		ViewHolder holder = null;
		if (arg1 == null)
		{
			arg1 = inflater.inflate(resource, null);
			holder = new ViewHolder();
			holder.tv = (TextView) arg1.findViewById(idValue[0]);
			holder.cb = (CheckBox) arg1.findViewById(idValue[1]);
			arg1.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) arg1.getTag();
		}
		HashMap<String, Object> map = list.get(arg0);
		if (map != null)
		{
			itemString = (String) map.get(keyString[0]);
			holder.tv.setText(itemString);
		}
		holder.cb.setChecked(isSelected.get(arg0));
		return arg1;
	}
}

class ViewHolder
{
	public TextView tv = null;
	public CheckBox cb = null;
}