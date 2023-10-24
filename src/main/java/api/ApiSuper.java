package api;

import java.util.ArrayList;

import com.example.demo.SpringConfig;

import common.utils.MyMail;
import common.utils.MyUtils;

public class ApiSuper {
	SpringConfig config;
	String sys;
	String sysName;
	String obj;
	String objName;
	String[][] colFormat;
	ArrayList<ArrayList<String>> list = null;
	String templePath;
	String outputPath;
    String saveXlsPath;
    String saveCharSet;
	MyMail mailConf;
	
	public ApiSuper(SpringConfig argConfig, String argSys, String argObj) {
		config = argConfig;
        sys = argSys;
		obj = argObj;
        sysName = null;
		objName = null;
		colFormat = null;
		System.out.println(sys + " obj: " + obj);
		templePath = config.getPathTempletePath();
		outputPath = config.getPathOutputPath();
		
		//Mail Object初期化
		mailConf = new MyMail();
		mailConf.host = config.getMailHost();
		mailConf.port = config.getMailPort();
		mailConf.username = config.getMailUsername();
		mailConf.password = config.getMailPassword();
		mailConf.smtpAuth = config.getMailSmtpAuth();
		mailConf.starttlsEnable = config.getMailSmtpStarttlsEnable();
	}
	
	public String makeObject() {
		return null;
	}
	
	public String execute() {
		return null;
	}
	
	public String download(String[] filePath) {
		return null;
	}
	
	public String getData(String obj) {
		return null;
	}
	
	//---------------------------------------
	//Excelに書き出し
	//---------------------------------------
	public String makeExcel() {
		return null;
	}
	
	//---------------------------------------
	//メール添付送信        
	//---------------------------------------
	public String sendMail(String subject, String body, String attach) {
        mailConf.fmAddr = mailConf.username;
        mailConf.toAddr = mailConf.fmAddr;	//for debug
		mailConf.ccAddr = "";				//for debug
		mailConf.bccAddr = "";				//for debug
		mailConf.subject = subject;
		mailConf.body = body;
		mailConf.attach = attach;
		
		MyUtils.SystemLogPrint("  メール送信...");
		System.out.println("  MAIL FmAddr: " + mailConf.fmAddr);
		System.out.println("  MAIL ToAddr: " + mailConf.toAddr);
		System.out.println("  MAIL CcAddr: " + mailConf.ccAddr);
		System.out.println("  MAIL BcAddr: " + mailConf.bccAddr);
		System.out.println("  MAIL Subject: " + mailConf.subject);
		System.out.println("  MAIL Body: \n" + mailConf.body);
		System.out.println("  MAIL Attach: " + mailConf.attach);
		mailConf.sendRawMail();
		
		return null;
	}
}
