package com.example.masssms;
import java.util.ArrayList;
import com.example.data.*;


public class SmsEditor
{
	//基础根据base代号
	public static final int DEFAULT = 0;
	public static final int SPRING_FESTIVAL = 1;
	
	//内容替代标志代号
	private final static String CALL = "<call>";
	
	
	static private String base;//生成短信基础根据
	private String content;//短信内容
	private Person person;//联系人
	
	public SmsEditor(Person c, int b)
	{
		person = c;
		switch(b)
		{
		case DEFAULT:break;
		case SPRING_FESTIVAL:base = CALL + "新年快乐！";break;
		}
	}
	public static String getBase()
	{
		return base;
	}
	public static void setBase(String base)
	{
		SmsEditor.base = base;
	}
	//自动生成短信并返回
	public String getContent()
	{
		//根据base生成相应基础短信模版
		content = getBase();
		content = content.replaceAll(CALL, getCall());
		return content;
	}
	//生成称呼
	private String getCall()
	{
		String call = person.getName();
		if(person.getSex() == 1)
		{
			call += "先生";
		}
		else
		{
			call += "女士";
		}
		return call;
	}
	
}
