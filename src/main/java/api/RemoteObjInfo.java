package api;

import com.example.demo.SpringConfig;

import common.utils.MyUtils;

public class RemoteObjInfo {
	SpringConfig config;
	String sys;
	String sysName;
	String obj;
	String objName;

	public RemoteObjInfo(SpringConfig argConfig, String argSys, String argObj) {
		config = argConfig;
        sys = argSys;
        sysName = null;
		obj = argObj;
		objName = null;
		System.out.println(sys + " obj: " + obj);
	}
	
	public String makeObject() {
        sysName = "Remote";
        //curl -X POST http://localhost:8080/api/remote
		return null;
	}

	public String execute() {
		String[] cmdList;
		cmdList = new String[1];
		cmdList[0]	=	obj;
		try {
		    MyUtils.exeCmd(cmdList);
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
		cmdList = null;
		
		MyUtils.SystemLogPrint("完了");
		
		return null;
	}
}
