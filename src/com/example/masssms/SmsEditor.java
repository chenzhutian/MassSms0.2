package com.example.masssms;
import java.util.ArrayList;
import com.example.data.*;


public class SmsEditor
{
	//��������base����
	public static final int DEFAULT = 0;
	public static final int SPRING_FESTIVAL = 1;
	
	//���������־����
	private final static String CALL = "<call>";
	
	
	static private String base;//���ɶ��Ż�������
	private String content;//��������
	private Person person;//��ϵ��
	
	public SmsEditor(Person c, int b)
	{
		person = c;
		switch(b)
		{
		case DEFAULT:break;
		case SPRING_FESTIVAL:base = CALL + "������֣�";break;
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
	//�Զ����ɶ��Ų�����
	public String getContent()
	{
		//����base������Ӧ��������ģ��
		content = getBase();
		content = content.replaceAll(CALL, getCall());
		return content;
	}
	//���ɳƺ�
	private String getCall()
	{
		String call = person.getName();
		if(person.getSex() == 1)
		{
			call += "����";
		}
		else
		{
			call += "Ůʿ";
		}
		return call;
	}
	
}
