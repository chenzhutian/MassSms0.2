package com.example.masssms;

import com.example.data.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SmsSender extends Activity
{
	private MessageAdapter adapter = null;
	private ArrayList<Integer> choice = null;
	Person contact = null;
	Group group = null;
	DataBase db = null;
	SmsManager smsManager = SmsManager.getDefault();
	// 编辑器初始化
	private SmsEditor editor = new SmsEditor(null,
			SmsEditor.SPRING_FESTIVAL);
	EditText message = null;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_send);
		db = new DataBase(this);
		message = (EditText) findViewById(R.id.sms_send_content);
		registerReceiver(sendMessage, new IntentFilter("SENT_SMS_ACTION"));
		registerReceiver(receiver, new IntentFilter("DELIVERED_SMS_ACTION"));
		initButton();
		initList();
	}
//初始化按钮
	private void initButton()
	{
		Button send_button = (Button) findViewById(R.id.sms_send_send);
		Button return_button = (Button) findViewById(R.id.sms_send_return);
		Button generate_button = (Button) findViewById(R.id.sms_send_generate);
		send_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				for (int i = 0; i < choice.size(); i++)
				{
					SmsEditor.setBase(message.getText().toString());
					contact = db.getDataById(choice.get(i));
					editor = new SmsEditor(contact, SmsEditor.DEFAULT);
					sendSms(contact, editor.getContent());
				}

			}
		});
		return_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				finish();
			}
		});
		generate_button.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				message.setText(SmsEditor.getBase());
			}
		});
	}
//填充联系人部分
	private void initList()
	{
		Bundle bundle = getIntent().getExtras();
		choice = bundle.getIntegerArrayList("choice");
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		ListView blank = (ListView) findViewById(R.id.sms_send_contact);
		for (int i = 0; i < choice.size(); i++)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("tv", db.getDataById(choice.get(i)).getName());
			map.put("cb", true);
			list.add(map);
		}
		adapter = new MessageAdapter(this, list, R.layout.list, new String[] {
				"tv", "cb" }, new int[] { R.id.list_tv, R.id.list_cb });
		blank.setAdapter(adapter);
	}
//反馈广播接受
	private BroadcastReceiver sendMessage = new BroadcastReceiver()
	{
		public void onReceive(Context context, Intent intent)
		{
			// 判断短信是否发送成功
			switch (getResultCode())
			{
			case Activity.RESULT_OK:
				Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(context, "发送失败", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
	private BroadcastReceiver receiver = new BroadcastReceiver()
	{
		public void onReceive(Context context, Intent intent)
		{
			// 表示对方成功收到短信
			Toast.makeText(context, "对方接收成功", Toast.LENGTH_LONG).show();
		}
	};
//发送短信
	private void sendSms(Person contact, String sms)
	{
		Intent sentIntent = new Intent("SENT_SMS_ACTION");
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sentIntent,
				0);
		Intent deliverIntent = new Intent("DELIVERED_SMS_ACTION");
		PendingIntent deliverPI = PendingIntent.getBroadcast(this, 0,
				deliverIntent, 0);
		if (sms.length() > 70)
		{
			ArrayList<String> msgs = smsManager.divideMessage(sms);
			for (String msg : msgs)
			{
				smsManager.sendTextMessage(contact.getPhoneNumber(), null, msg,
						sentPI, deliverPI);
			}
		}
		else
		{
			smsManager.sendTextMessage(contact.getPhoneNumber(), null, sms, sentPI, deliverPI);
		}
	}
}
