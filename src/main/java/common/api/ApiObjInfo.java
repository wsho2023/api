package common.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import common.utils.MyUtils;

public class ApiObjInfo {
	String sys;
	String obj;
	String beseUrl;
	String fields;
	String filters;
	String sort;
	String url;
	String objFile;
	
	public ApiObjInfo(String argSys, String argObj) {
		sys = argSys;
		obj = argObj;
		fields = "";
		filters = "";
		sort = "";
		url = null;
		objFile = null;
		if (sys.equals("kkk") == true) {
	        if (obj.equals("juchzn") == true) {
				//curl -X POST "http://localhost:8080/kkk?obj=juchzn"
				String today = MyUtils.getToday();
				beseUrl = "http://localhost/api/juchzn?";
				filters = "today='" + today + "'";
				sort = "";
				objFile = "juchzn";
	        } else if (obj.equals("seisan") == true) {
				//curl -X POST "http://localhost:8080/kkk?obj=seisan"
				String today = MyUtils.getToday();
				beseUrl = "http://localhost/api/seisan?";
				filters = "today='" + today + "'";
				sort = "";
				objFile = "seisan";
	        } else if (obj.equals("uriage") == true) {
				//curl -X POST "http://localhost:8080/kkk?obj=uriage"
				String today = MyUtils.getToday();
				beseUrl = "http://localhost/api/uriage?";
				filters = "today='" + today + "'";
				sort = "";
				objFile = "uriage";
	        } else if (obj.equals("kaigai") == true) {
				//curl -X POST "http://localhost:8080/kkk?obj=kaigai"
				String today = MyUtils.getToday();
				today = today.replace("/", "");	//YYYYMMDDへ変換
				beseUrl = "http://localhost/api/kaigai?";
				filters = "today='" + today + "'";
				sort = "";
				objFile = "kaigai";
	        }
		} else if (sys.equals("hantei") == true) {
	    	if (obj.equals("2") == true) {
				String today = MyUtils.getToday();
				beseUrl = "http://localhost/api/2?";
				filters = "today='" + today + "'";
				sort = "";
				objFile = "hantei";
			}
		}
	}
	
	public String getUrl() {
		MyUtils.SystemLogPrint("fields " + fields);
		MyUtils.SystemLogPrint("filters " + filters);
		MyUtils.SystemLogPrint("sort " + sort);
		if (fields.equals("") != true) {
			String encoded;
			try {
				encoded = URLEncoder.encode(fields, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				String msg = e.toString();
				return msg;
			}
			System.out.println("エンコード結果: " + encoded);
			url = url + "&fields=" + encoded;
		}
		if (filters.equals("") != true) {
			String encoded;
			try {
				encoded = URLEncoder.encode(filters, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				String msg = e.toString();
				return msg;
			}
			System.out.println("エンコード結果: " + encoded);
			url = url + "&filter=" + encoded;
		}
		if (sort.equals("") != true) {
			url = url + "&sort=" + sort;	//ソート(空白を含まないこと)
		}
		return this.url;
	}
	
	public String getObjeFile() {return this.objFile;}

    public String getGroupShukei(ArrayList<ArrayList<String>> list) {
		int colIdx = 0;
    	String msg = "";
		if (sys.equals("kkk") == true) {
	        if (obj.equals("seisan") == true) {
				colIdx = 28;
			}
		}
		if (colIdx == 0) {
			return msg;
		}
        int maxRow = list.size();
		String code;
		String name;
		ArrayList<ClassMstInfo> classMstList = new ArrayList<ClassMstInfo>();
    	boolean matching = false;
        for (int rowIdx=1; rowIdx<maxRow; rowIdx++) {
        	code = list.get(rowIdx).get(colIdx);
        	name = list.get(rowIdx).get(colIdx+1);
			matching  = false;
        	for (ClassMstInfo cm : classMstList) {
        		if (cm.code.equals(code) == true) {
        			matching  = true;
        			break;
        		}
        	}
        	if (matching == false) {
	        	ClassMstInfo classMst = new ClassMstInfo(code, name);
        		classMstList.add(classMst);
        	}
        } //rowIdx
        for (int rowIdx=1; rowIdx<maxRow; rowIdx++) {
        	code = list.get(rowIdx).get(colIdx);
        	for (ClassMstInfo cm : classMstList) {
        		if (cm.code.equals(code) == true) {
        			cm.cnt++;
        			break;
        		}
        	}
        } //rowIdx
    	for (ClassMstInfo cm : classMstList) {
    		msg = "\n" + cm.name + "("+ cm.code + "): " + cm.cnt;
    	}
		return msg;
    }
}

class ClassMstInfo {
	String code;
	String name;
	int cnt;
	
	ClassMstInfo() {cnt = 0;}
	
	ClassMstInfo(String cd, String nm) {
		code = cd;
		name = nm;
		cnt = 0;
	}
}

