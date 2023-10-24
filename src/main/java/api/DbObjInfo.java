package api;

import java.io.IOException;
import java.sql.SQLException;

import com.example.demo.SpringConfig;

import common.utils.MyFiles;
import common.utils.MyUtils;

public class DbObjInfo extends ApiSuper {
	
	public DbObjInfo(SpringConfig argConfig, String argSys, String argObj) {
		super(argConfig, argSys, argObj);
	}
	
	@Override
	public String makeObject() {
        sysName = "システム";
        if (obj.equals("juchu") == true) {
			//curl -X POST "http://localhost:8080/db?obj=juchu"
        	objName = "juchu集計";
        } else if (obj.equals("update") == true) {
 			//curl -X POST "http://localhost:8080/db?obj=update"
        	//try {
			//	DataBaseDAO.getInstance(config).update();
			//} catch (SQLException e) {
			//	e.printStackTrace();
			//	String msg = e.getMessage();
			//	return msg;
			//}
        	//return "OK";
        } else if (obj.equals("uriage") == true) {
 			//curl -X POST "http://localhost:8080/db?obj=uriage"
        	objName = "uriage集計";
	    } else if (obj.equals("faxlist") == true) {
				//curl -X POST "http://localhost:8080/db?obj=faxlist"
	    	objName = "FAXリスト";
	    }
		if (objName == null)
			return "対象Object処理なし";
		
		return null;
	}
	
	@Override
	public String execute() {
		String msg;
        msg = getData(obj);//データ取得
		if (msg != null) return msg;
		
		msg = makeExcel();			//Excelに書き出し
		if (msg != null) return msg;
		
		msg = sendMail();			//メール添付送信
		if (msg != null) return msg;
		
		return null;
	}
	
	//---------------------------------------
	//データ取得
	//---------------------------------------
	@Override
	public String getData(String obj) {
		//---------------------------------------
		//SQL取得
		//---------------------------------------
		String sqlPath =  templePath + obj + ".sql";
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
		if (list.size() < 2) {
			MyUtils.SystemErrPrint("抽出データなし");
		}
		
		//---------------------------------------
		//TSVファイル書き出し
		//---------------------------------------
		String saveTxtPath = outputPath + obj + ".tsv";
		try {
			MyFiles.writeList2File(list, saveTxtPath, "UTF-8");	//UTF-8 or SJIS
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
		//---------------------------------------
		//見込みデータBackUp
		//---------------------------------------
		/*ArrayList<String> list2 = null;
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
		}*/
		
		return null;
	}
	
	//---------------------------------------
	//Excelに書き出し
	//---------------------------------------
	@Override
	public String makeExcel() {
		saveXlsPath = "";
		
		return null;
	}
	
	//---------------------------------------
	//メール添付送信        
	//---------------------------------------
	public String sendMail() {
		String mailBody = "";
		String subject = "[" + sysName + "]連絡(" + objName + " " + MyUtils.getDate() + ")";
		
		return super.sendMail(subject, mailBody, saveXlsPath);
	}
}
