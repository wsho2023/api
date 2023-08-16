package common.api;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.demo.ApiConfig;

import common.utils.MyExcel;
import common.utils.MyFiles;
import common.utils.MyUtils;
import jakarta.servlet.http.HttpServletResponse;

public class ShukeiObjInfo {
	ApiConfig config;
	String sysName;
	String obj;
	String objName;
	String[][] colFormat;
	ArrayList<ArrayList<String>> list = null;
	String templePath;
	String outputPath;
    String saveXlsPath;
	
	public ShukeiObjInfo(ApiConfig argConfig, String argSys, String argObj) {
		config = argConfig;
        sysName = argSys;
		obj = argObj;
		objName = null;
		colFormat = null;
		System.out.println("/" + sysName + " obj: " + obj);
	}
	
	public String makeObject() {
        if (obj.equals("jisseki") == true) {
			//curl -X POST "http://localhost:8080/shukei?obj=jisseki"
			objName = "jisseki";
        } else if (obj.equals("meisai") == true) {
			//curl -X POST "http://localhost:8080/shukei?obj=meisai"
			objName = "meisai";
        } else if (obj.equals("tonyu") == true) {
			//curl -X POST "http://localhost:8080/shukei?obj=tonyu"
			objName = "tonyu";
        } else if (obj.equals("daicho") == true) {
        	//curl -X POST "http://localhost:8080/shukei?obj=daicho"
			objName = obj;
	        String[][] format = {
	        		{"9", "SURYO"},
	        		{"10", "TANKA"},
	        		{"11", "KINGAKU"},
	        };
			colFormat = new String[format.length][];
			colFormat = format;
        }
		if (objName == null)
			return "対象Object処理なし";
		
		return null;
	}
	
	public String execute() {
        String msg = getDataDB();	//データ取得
		if (msg != null) return msg;
		
		msg = makeExcel();			//Excelに書き出し
		if (msg != null) return msg;
		
		msg = sendMail();			//メール添付送信
		if (msg != null) return msg;
		
		return null;
	}
	
	public String download(HttpServletResponse response) {
        String msg = getDataDB();	//データ取得
		if (msg != null) return msg;
		
		msg = makeExcel();			//Excelに書き出し
		if (msg != null) return msg;
		
        try (OutputStream os = response.getOutputStream();) {
        	Path p = Paths.get(saveXlsPath);
        	String fileName = MyFiles.getFileName(saveXlsPath);
            byte[] fb1 = Files.readAllBytes(p);
            
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentLength(fb1.length);
            os.write(fb1);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return null;
	}
	
	//---------------------------------------
	//データ取得
	//---------------------------------------
	public String getDataDB() {
		//---------------------------------------
		//SQL取得
		//---------------------------------------
		String sqlPath =  config.getPathTempletePath() + obj + ".sql";
		String sql = "";
		try {
			sql = MyFiles.readAllText(sqlPath);
		} catch (IOException e) {
			e.printStackTrace();
        	return e.toString();
		}
		
		//---------------------------------------
		//データ取得
		//---------------------------------------
		DataBaseDAO dao = new DataBaseDAO(config);
		try {
			list = dao.getData(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return e.toString();
		}
		if (list == null) {
			String msg = "抽出データなし";
			MyUtils.SystemErrPrint(msg);
			return msg;
		}
		
		//---------------------------------------
		//TSVファイル読み込み
		//---------------------------------------
		/*outputPath = config.getPathOutputPath();
		String saveTxtPath = outputPath + obj + ".tsv";
        list = null;
		try {
			list = MyFiles.parseTSV(saveTxtPath, "UTF-8");	//or "SJIS"
		} catch (IOException e) {
			e.printStackTrace();
        	return e.toString();
		}
		if (list == null) {
			String msg = "抽出データなし";
			MyUtils.SystemErrPrint(msg);
			return msg;
		}*/
		
		return null;
	}
	
	//---------------------------------------
	//Excelに書き出し
	//---------------------------------------
	public String makeExcel() {
		templePath = config.getPathTempletePath();
		outputPath = config.getPathOutputPath();
        String defXlsPath = templePath + objName + ".xlsx";
        String tmpXlsPath = null;
		if (MyFiles.exists(defXlsPath) != true) {
			tmpXlsPath = null;	//新規作成
			MyUtils.SystemLogPrint("  Excel新規オープン... 種別: " + objName);
		} else {
	        tmpXlsPath = templePath + objName + "_tmp.xlsx";
			try {
				MyFiles.copyOW(defXlsPath, tmpXlsPath);	//上書き
			} catch (IOException e) {
				e.printStackTrace();
				return e.getMessage();
			}
			MyUtils.SystemLogPrint("  Excelオープン...: " + tmpXlsPath + " 種別: " + objName);
		}
		
		MyExcel xlsx = new MyExcel();
		try {
			//Excelファイルオープン(tmpXlsPath=nullなら、新規作成)
			xlsx.open(tmpXlsPath, objName);
			//データ転記、データ転記した範囲をテーブル化
			xlsx.setColFormat(colFormat);
			xlsx.writeData(objName, list, true);
			xlsx.refreshPivot("pivot");
			//Excelファイル保存
			//saveXlsPath = outputPath + objName + "_" + MyUtils.getDateStr() +".xlsx";
			saveXlsPath = outputPath + objName + "_test.xlsx";
			MyUtils.SystemLogPrint("  XLSXファイル保存: " + saveXlsPath);
			xlsx.save(saveXlsPath);
			xlsx.close();
		} catch (IOException e) {
			e.printStackTrace();
			return e.toString();
		}
		
		return null;
	}
	
	//---------------------------------------
	//メール添付送信        
	//---------------------------------------
	public String sendMail() {
		String mailBody = "";
		String subject = "[" + sysName + "]完了連絡(" + objName + " " + MyUtils.getDate() + ")";
		SendMail.execute(config, subject, mailBody, saveXlsPath);
		
		return null;
	}
}
