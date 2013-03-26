package com.example.masssms;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class MainActivity extends FragmentActivity
{
	private TabHost tabHost = null;
	private DailFragment dailFragment = null;
	private PersonFragment personFragment = null;
	private SmsFragment smsFragment = null;
	private int currentTab = 0;
	FragmentTransaction ft = null;
	RelativeLayout tabIndicator1,tabIndicator2,tabIndicator3;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		tabHost = (TabHost)findViewById(android.R.id.tabhost);
		findTabView();
		tabHost.setup();
		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener()
		{			
			@Override
			public void onTabChanged(String tabId)
			{
				// TODO Auto-generated method stub
				FragmentManager fm =  getSupportFragmentManager();
				dailFragment = (DailFragment) fm.findFragmentByTag("dail");
				personFragment = (PersonFragment) fm.findFragmentByTag("person");
				smsFragment = (SmsFragment) fm.findFragmentByTag("sms");
				ft = fm.beginTransaction();
				if(dailFragment != null)
				{
					ft.detach(dailFragment);
				}
				if(personFragment != null)
				{
					ft.detach(personFragment);
				}
				if(smsFragment != null)
				{
					ft.detach(smsFragment);
				}
				if(tabId.equalsIgnoreCase("dail"))
				{
					if(dailFragment == null)
						ft.add(android.R.id.tabcontent, new DailFragment(), "dail");
					else
						ft.attach(dailFragment);
					currentTab = 0;
				}
				else if(tabId.equalsIgnoreCase("person"))
				{
					if(personFragment == null)
						ft.add(android.R.id.tabcontent, new PersonFragment(), "person");
					else
						ft.attach(personFragment);
					currentTab = 1;
				}
				else if(tabId.equalsIgnoreCase("sms"))
				{
					if(smsFragment == null)
						ft.add(android.R.id.tabcontent, new SmsFragment(), "sms");
					else
						ft.attach(smsFragment);
					currentTab = 2;
				}
				else 
					switch(currentTab)
					{
					case 0:ft.attach(dailFragment);break;
					case 1:ft.attach(personFragment);break;
					case 2:ft.attach(smsFragment);break;
					default:ft.attach(dailFragment);break;
					}
				ft.commit();
			}
		});
		initTab();
		tabHost.setCurrentTab(0);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch(resultCode / 10)
		{
		case 1:
		{
			FragmentManager fm =  getSupportFragmentManager();
			personFragment = (PersonFragment) fm.findFragmentByTag("person");
			personFragment.modifyData(requestCode, resultCode % 10, data);
			break;
		}
			
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity, menu);
		return true;
	}

	private void findTabView()
	{
		LinearLayout layout = (LinearLayout)tabHost.getChildAt(0);
        TabWidget tw = (TabWidget)layout.getChildAt(1);
        
		tabIndicator1 = (RelativeLayout) LayoutInflater.from(this)
         		.inflate(R.layout.tab, tw, false);
		TextView tvTab1 = (TextView)tabIndicator1.getChildAt(0);
		tvTab1.setText("拨号盘");
		
		tabIndicator2 = (RelativeLayout) LayoutInflater.from(this)
         		.inflate(R.layout.tab, tw, false);
		TextView tvTab2 = (TextView)tabIndicator2.getChildAt(0);
		tvTab2.setText("联系人");
		
		tabIndicator3 = (RelativeLayout) LayoutInflater.from(this)
         		.inflate(R.layout.tab, tw, false);
		TextView tvTab3 = (TextView)tabIndicator3.getChildAt(0);
		tvTab3.setText("信息");
	}
	
	private void initTab()
	{
		TabHost.TabSpec tSpecDail = tabHost.newTabSpec("dail");
		tSpecDail.setIndicator(tabIndicator1).setContent(new DummyTabContent(this));
		tabHost.addTab(tSpecDail);
		
		TabHost.TabSpec tSpecPerson = tabHost.newTabSpec("person");
		tSpecPerson.setIndicator(tabIndicator2).setContent(new DummyTabContent(this));
		tabHost.addTab(tSpecPerson);
		
		TabHost.TabSpec tSpecMessage = tabHost.newTabSpec("sms");
		tSpecMessage.setIndicator(tabIndicator3).setContent(new DummyTabContent(this));
		tabHost.addTab(tSpecMessage);
	}
}
