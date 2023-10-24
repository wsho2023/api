package api;

import com.example.demo.SpringConfig;

import common.utils.MyUtils;

public class RemoteObjInfo extends ApiSuper {

	public RemoteObjInfo(SpringConfig argConfig, String argSys, String argObj) {
		super(argConfig, argSys, argObj);
        sysName = null;
		objName = null;
		System.out.println(sys + " obj: " + obj);
	}
	
	@Override
	public String makeObject() {
        sysName = "Remote";
        //curl -X POST http://localhost:8080/api/remote
		return null;
	}

	@Override
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
