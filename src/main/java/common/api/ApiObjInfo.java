package common.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import common.utils.MyUtils;

public class ApiObjInfo {
	final String serv1 = "kkk";
	final String serv2 = "hantei";
	final String serv3 = "kyaku";

	String sys;
	String obj;
	String beseUrl;
	String fields;
	String filter;
	String sort;
	String url;
	String objFile;
	String[][] colFormat;
	
	public ApiObjInfo(String argSys, String argObj) {
		sys = argSys;
		obj = argObj;
		fields = "";
		filter = "";
		sort = "";
		url = null;
		objFile = null;
		colFormat = null;
		if (sys.equals(serv1) == true) {
	        if (obj.equals("juchzn") == true) {
				//curl -X POST "http://localhost:8080/kkk?obj=juchzn"
				String today = MyUtils.getToday();
				beseUrl = "http://localhost/api/juchzn?";
				filter = "date='" + today + "'";
				sort = "";
				objFile = "juchzn";
	        } else if (obj.equals("seisan") == true) {
				//curl -X POST "http://localhost:8080/kkk?obj=seisan"
				String today = MyUtils.getToday();
				beseUrl = "http://localhost/api/seisan?";
				filter = "date='" + today + "'";
				sort = "";
				objFile = "seisan";
	        } else if (obj.equals("uriage") == true) {
				//curl -X POST "http://localhost:8080/kkk?obj=uriage"
				String kinou = MyUtils.getToday(-1);	//前日
				beseUrl = "http://localhost/api/uriage?";
				filter = "date='" + kinou + "'";
				sort = "";
				objFile = "uriage";
	        } else if (obj.equals("kaigai") == true) {
				//curl -X POST "http://localhost:8080/kkk?obj=kaigai"
				String today = MyUtils.getToday();
				today = today.replace("/", "");	//YYYYMMDDへ変換
				beseUrl = "http://localhost/api/kaigai?";
				filter = "date='" + today + "'";
				sort = "";
				objFile = "kaigai";
	        } else if (obj.equals("daicho") == true) {
	        	//curl -X POST "http://localhost:8080/kkk?obj=daicho"
	        	beseUrl = "http://localhost/api/test?";
				objFile = obj;
		        String[][] format = {
		        		{"9", "SURYO"},
		        		{"10", "TANKA"},
		        		{"11", "KINGAKU"},
		        };
				colFormat = new String[format.length][];
				colFormat = format;
	        } else if (obj.equals("new") == true) {
	        	//curl -X POST "http://localhost:8080/kkk?obj=new"
	        	beseUrl = "http://localhost/api/new?";
				objFile = obj;
	        }
		} else if (sys.equals(serv2) == true) {
	    	if (obj.equals("2") == true) {
				String today = MyUtils.getToday();
				beseUrl = "http://localhost/api/2?";
				filter = "today='" + today + "'";
				sort = "";
				objFile = "hantei";
			}
		} else if (sys.equals(serv3) == true) {
	    	if (obj.equals("1") == true) {
				String today = MyUtils.getToday();
				beseUrl = "http://localhost/api/1?";
				filter = "today='" + today + "'";
				sort = "";
				objFile = "kokunai";
			} else if (obj.equals("2") == true) {
				String today = MyUtils.getToday();
				beseUrl = "http://localhost/api/2?";
				filter = "today='" + today + "'";
				sort = "";
				objFile = "kaigai";
			}
		}
	}
	
	public String getUrl() {
		MyUtils.SystemLogPrint("fields " + fields);
		MyUtils.SystemLogPrint("filter " + filter);
		MyUtils.SystemLogPrint("sort " + sort);
		url = beseUrl;
		if (fields.equals("") != true) {
			String encoded;
			try {
				encoded = URLEncoder.encode(fields, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				String msg = e.toString();
				return msg;
			}
			System.out.println("fieldsエンコード結果: " + encoded);
			url = url + "&fields=" + encoded;
		}
		if (filter.equals("") != true) {
			String encoded;
			try {
				encoded = URLEncoder.encode(filter, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				String msg = e.toString();
				return msg;
			}
			System.out.println("filterエンコード結果: " + encoded);
			url = url + "&filter=" + encoded;
		}
		if (sort.equals("") != true) {
			String encoded;
			try {
				encoded = URLEncoder.encode(sort, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				String msg = e.toString();
				return msg;
			}
			System.out.println("filterエンコード結果: " + encoded);
			url = url + "&sort=" + sort;	//ソート(空白を含まないこと)
		}
		return this.url;
	}
	
	public String getObjeFile() {return this.objFile;}

	public String[][] getColFormat() {return colFormat;	}

    public String getGroupShukei(ArrayList<ArrayList<String>> list) {
		int colIdx = 0;
    	String msg = "";
		if (sys.equals(serv1) == true) {
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

