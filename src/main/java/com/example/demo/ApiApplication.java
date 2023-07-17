package com.example.demo;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import common.thspot.ThSpotMikomiDAO;
import common.utils.MyExcel;
import common.utils.MyFiles;
import common.utils.MyMail;
import common.utils.MyUtils;
import common.utils.WebApi;

@SpringBootApplication
@RestController	//★これが絶対必要！
public class ApiApplication extends SpringBootServletInitializer {

    @Autowired
    private ApiConfig config;

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApiApplication.class);
	}

    @PostMapping("kkk")
    public String kkkPost(@RequestParam("obj") String obj) {
		String mailBody = "";
		System.out.println("/kkk obj: " + obj);
		String url = "";
        if (obj == null) {
			String msg = "Object指定なし";
        	return msg;
        } else if (obj.equals("daicho") == true) {
			//curl -X POST "http://localhost:8080/kkk?obj=daicho"
			url = "http://localhost/api/daicho";
        } else if (obj.equals("jchzn") == true) {
			//curl -X POST "http://localhost:8080/kkk?obj=jchzn"
			url = "http://localhost/api/jchzn";
        } else if (obj.equals("seisan") == true) {
			//curl -X POST "http://localhost:8080/kkk?obj=seisan"
			url = "http://localhost/api/seisan";
        } else if (obj.equals("uriage") == true) {
			//curl -X POST "http://localhost:8080/kkk?obj=uriage"
			url = "http://localhost/api/uriage";
        } else if (obj.equals("kaigai") == true) {
			//curl -X POST "http://localhost:8080/kkk?obj=kaigai"
			url = "http://localhost/api/kaigai";
        } else {
			String msg = "対象Object処理なし";
        	return msg;
        }

		String outputPath = config.getPathOutputPath();
		String saveTxtPath = outputPath + obj + ".tsv";

		//---------------------------------------
        //既存ファイルがあれば削除
		//---------------------------------------
        try {
			MyFiles.existsDelete(saveTxtPath);
		} catch (IOException e) {
			e.printStackTrace();
			String msg = e.getMessage();
        	return msg;
		}
        
		//---------------------------------------
		//HTTP request process
		//---------------------------------------
		WebApi api = new WebApi();
		api.setUrl("GET", url);
		int res = -1;
		try {
			res = api.download(saveTxtPath);
		} catch (IOException e) {
			e.printStackTrace();
			String msg = e.getMessage();
        	return msg;
		}
        if (res != 200) {	//HttpURLConnection.HTTP_OK: 200
			String msg = "HTTP Connection Failed: " + res;
	        MyUtils.SystemErrPrint(msg);
        	return msg;
        } 

		//---------------------------------------
		//TSVファイル読み込み
		//---------------------------------------
        ArrayList<ArrayList<String>> list = null;
		try {
			list = MyFiles.parseTSV(saveTxtPath, "UTF-8");	//or "SJIS"
		} catch (IOException e) {
			e.printStackTrace();
			String msg = e.getMessage();
        	return msg;
		}
        int maxRow = list.size();
		int maxCol = list.get(0).size();

		//---------------------------------------
		//1. Excelに書き出し
		//---------------------------------------
		String templePath = config.getPathTempletePath();
        String defXlsPath = templePath + obj + ".xlsx";
        String saveXlsPath = "";
		if (MyFiles.exists(defXlsPath) != true) {
			String msg = "  " + obj + "テンプレートファイルが見つかりませんでした";
			MyUtils.SystemErrPrint(msg);
			mailBody = mailBody + "\n" + msg;
		} else {
			//テンプレートから出力ファイル生成
	        String tmpXlsPath = templePath + obj + "_tmp.xlsx";
			try {
				MyFiles.copyOW(defXlsPath, tmpXlsPath);	//上書き
			} catch (IOException e) {
				e.printStackTrace();
				String msg = e.getMessage();
				return msg;
			}
			//---------------------------------------
			//Excelオープン(XLSXのファイル読込)
			//---------------------------------------
			MyUtils.SystemLogPrint("  Excelオープン...: " + tmpXlsPath + " 種別: " + obj);
			MyExcel xlsx = new MyExcel();
			try {
				xlsx.open(tmpXlsPath, null, false);
				String strValue;
				for (int rowIdx=0; rowIdx<maxRow; rowIdx++) {
					xlsx.createRow(rowIdx);					//行の生成
					for (int colIdx=0; colIdx<maxCol; colIdx++) {
						strValue = list.get(rowIdx).get(colIdx);
						xlsx.setCellValue(colIdx, strValue);//セルの生成
					} //colIdx
				} //rowIdx

				//1行目ヘッダをセンタリング
				xlsx.getRow(0);
				for (int colIdx=0; colIdx<maxCol; colIdx++) {
					xlsx.setCellAlignCenter(colIdx);
				} //colIdx
				
				//---------------------------------------
				//XLSXのファイル保存
				//---------------------------------------
				saveXlsPath = outputPath + obj + "_" + MyUtils.getDateStr() +".xlsx";
				MyUtils.SystemLogPrint("  XLSXファイル保存: " + saveXlsPath);
				xlsx.save(saveXlsPath);
				xlsx.close();
			} catch (IOException e) {
				e.printStackTrace();
				String msg = e.getMessage();
				return msg;
			}
		}
		
		//---------------------------------------
		//メール本文作成
		//---------------------------------------
		if (obj.equals("daicho") == true) {
	        if (maxRow > 1) {
				//index:8
				String code;
				String name;
				ArrayList<ClassMstInfo> classMstList = new ArrayList<ClassMstInfo>();
	        	boolean matching = false;
		        for (int rowIdx=1; rowIdx<maxRow; rowIdx++) {
		        	code = list.get(rowIdx).get(8);
		        	name = list.get(rowIdx).get(8);
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
		        	code = list.get(rowIdx).get(8);
		        	for (ClassMstInfo cm : classMstList) {
		        		if (cm.code.equals(code) == true) {
		        			cm.cnt++;
		        			break;
		        		}
		        	}
		        } //rowIdx
				mailBody = "件数: " + (maxRow-1);
	        	for (ClassMstInfo cm : classMstList) {
	        		mailBody = mailBody + "\n" + cm.name + ": " + cm.cnt;
	        	}
	        } else {
	        	mailBody = "件数: 0";
	        }
		}

		//---------------------------------------
		//2. メール添付送信        
		//---------------------------------------
		String sysName = "kkk";
		String subject = "[" + sysName + "]" + MyUtils.getDate();
		if (saveXlsPath.equals("") == true) {
    		mailBody = mailBody + "\n添付ファイルなし";
		}
		sendnMailProcess(subject, mailBody, saveXlsPath);
		
        return null;
    }

	//------------------------------------------------------
	//ファイル添付メール送信
	//------------------------------------------------------
	int sendnMailProcess(String subject, String body, String attach) {
		MyMail mailConf;
		mailConf = new MyMail();
		mailConf.host = config.getMailHost();
		mailConf.port = config.getMailPort();
		mailConf.username = config.getMailUsername();
		mailConf.password = config.getMailPassword();
		mailConf.smtpAuth = config.getMailSmtpAuth();
		mailConf.starttlsEnable = config.getMailSmtpStarttlsEnable();

        mailConf.fmAddr = mailConf.username;
        mailConf.toAddr = mailConf.fmAddr;	//for debug
		mailConf.ccAddr = "";				//for debug
		mailConf.bccAddr = "";				//for debug
		mailConf.subject = subject;
		mailConf.body = body;
		mailConf.attach = attach;
		
		MyUtils.SystemLogPrint("  メール送信...");
		MyUtils.SystemLogPrint("  MAIL FmAddr: " + mailConf.fmAddr);
		MyUtils.SystemLogPrint("  MAIL ToAddr: " + mailConf.toAddr);
		MyUtils.SystemLogPrint("  MAIL CcAddr: " + mailConf.ccAddr);
		MyUtils.SystemLogPrint("  MAIL BcAddr: " + mailConf.bccAddr);
		MyUtils.SystemLogPrint("  MAIL Subject: " + mailConf.subject);
		MyUtils.SystemLogPrint("  MAIL Body: \n" + mailConf.body);
		MyUtils.SystemLogPrint("  MAIL Attach: " + mailConf.attach);
		//mailConf.sendRawMail();            
		
		return 0;
	}
    
    @PostMapping("thspot")
    public String thspotPost(@RequestParam("obj") String obj) {
		String mailBody = "";
		System.out.println("/thspot obj: " + obj);
        if (obj == null) {
			String msg = "Object指定なし";
        	return msg;
        } else if (obj.equals("juchu") == true) {
			//curl -X POST "http://localhost:8080/thspot?obj=juchu"
        } else if (obj.equals("update") == true) {
 			//curl -X POST "http://localhost:8080/thspot?obj=update"
        	ThSpotMikomiDAO.getInstance(config).mikomiUpdate();
        	return null;
        } else if (obj.equals("uriage") == true) {
 			//curl -X POST "http://localhost:8080/thspot?obj=uriage"
        } else {
			String msg = "対象Object処理なし";
        	return msg;
        }
	    String sqlPath =  config.getPathTempletePath() + obj + ".sql";
		String savePath = config.getPathOutputPath() + obj + ".tsv";
		
		//---------------------------------------
		//SQL取得
		//---------------------------------------
		String sql = "";
		try {
			sql = MyFiles.readAllText(sqlPath);
		} catch (IOException e) {
			e.printStackTrace();
			String msg = e.getMessage();
        	return msg;
		}
		
		//---------------------------------------
		//データ取得
		//---------------------------------------
		ThSpotMikomiDAO dao = new ThSpotMikomiDAO(config);
		ArrayList<String> list = dao.getDataTsv(sql);
		if (list != null) {
			try {
			    MyFiles.WriteList2File(list, savePath);
			} catch (IOException e) {
				e.printStackTrace();
				String msg = e.getMessage();
				return msg;
			}
		} else {
			String msg = "抽出データなし";
			MyUtils.SystemErrPrint(msg);
        	return msg;
		}

		//---------------------------------------
		//見込みデータBackUp
		//---------------------------------------
		ArrayList<String> list2 = null;
		if (obj.equals("juchu") == true) {
			list2 = new ArrayList<String>();
			list2.add(list.get(0));		//ヘッダ
	        int maxRow = list.size();
			String bunruiFlag;
			for (int rowIdx=1; rowIdx<maxRow; rowIdx++) {
				bunruiFlag = list.get(rowIdx).substring(0, 1);
				if (bunruiFlag.equals("0") == true || bunruiFlag.equals("1") == true) {
					list2.add(list.get(rowIdx));
				}
			}
		} else if (obj.equals("uriage") == true) {
			list2 = new ArrayList<String>();
			list2.add(list.get(0));		//ヘッダ
	        int maxRow = list.size();
			String bunruiFlag;
			for (int rowIdx=1; rowIdx<maxRow; rowIdx++) {
				bunruiFlag = list.get(rowIdx).substring(0, 1);
				if (bunruiFlag.equals("0") == true || bunruiFlag.equals("1") == true) {
					list2.add(list.get(rowIdx));
				}
			}
		}
		if (list2 != null) {
			if (list2.size() != 0) {
				String backPath = config.getPathOutputPath() + obj + "_bak_" + MyUtils.getDateStr() + ".tsv";
				try {
					MyFiles.WriteList2File(list2, backPath);
				} catch (IOException e) {
					e.printStackTrace();
					String msg = e.getMessage();
					return msg;
				}
			}
		}
		
		//---------------------------------------
		//2. メール添付送信        
		//---------------------------------------
		String sysName = "システム";
		String subject = "[" + sysName + "]" + MyUtils.getDate();
		sendnMailProcess(subject, mailBody, "");
		
        return null;
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
