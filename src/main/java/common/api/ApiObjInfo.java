package common.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import common.utils.MyUtils;

public class ApiObjInfo {
	String beseUrl;
	String fields;
	String filters;
	String sort;
	String url;
	String objFile;
	
	public ApiObjInfo(String sys, String obj) {
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
		MyUtils.SystemLogPrint("filters " + filters);
		MyUtils.SystemLogPrint("sort " + sort);
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
	
}
