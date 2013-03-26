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
	// �༭����ʼ��
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
//��ʼ����ť
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
//�����ϵ�˲���
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
//�����㲥����
	private BroadcastReceiver sendMessage = new BroadcastReceiver()
	{
		public void onReceive(Context context, Intent intent)
		{
			// �ж϶����Ƿ��ͳɹ�
			switch (getResultCode())
			{
			case Activity.RESULT_OK:
				Toast.makeText(context, "���ŷ��ͳɹ�", Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(context, "����ʧ��", Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
	private BroadcastReceiver receiver = new BroadcastReceiver()
	{
		public void onReceive(Context context, Intent intent)
		{
			// ��ʾ�Է��ɹ��յ�����
			Toast.makeText(context, "�Է����ճɹ�", Toast.LENGTH_LONG).show();
		}
	};
//���Ͷ���
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
