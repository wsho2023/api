package common.thspot;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.demo.ApiConfig;

public class ThSpotMikomiDAO {
	String DB_URL;
	String DB_USER;
	String DB_PASS;
	String DB_DRIVER;
	
	String header[];
	
	public ThSpotMikomiDAO(ApiConfig config) {
        //接続情報取得
		DB_URL = config.getDBUrl();
		DB_USER = config.getDBUsername();
		DB_PASS = config.getDBPassword();
		DB_DRIVER = config.getDBDriverClassName();
	}

	// インスタンスオブジェクトの生成->返却（コードの簡略化）
	public static ThSpotMikomiDAO getInstance(ApiConfig config) {
		return new ThSpotMikomiDAO(config);
	}
	
	public String getHeaderTsv() {
		String retStr = "";

		for (int c=0; c<header.length-1; c++) {
			retStr = retStr + header[c] + "\t";
		}
		retStr = retStr + header[header.length-1] + "\r\n";

		return retStr;
		//return "行\tID\t品名\tコード3\t購入先\t分類\t予定月\t数量\t単価\t金額\r\n";
	}

	public ArrayList<String> getDataTsv(String sql) throws SQLException {
		//接続処理
		Connection conn = null;
		ArrayList<String> list = new ArrayList<String>();
		try {
			Class.forName(DB_DRIVER);
			conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
			System.out.println(sql);	//for debug

			PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            //ヘッダ情報取得
    		ResultSetMetaData meta = rs.getMetaData();
    		int colNum = meta.getColumnCount();
    		System.out.println("カラム数：" + colNum);
    		
    		//ヘッダ格納
    		header = new String[colNum];
    		System.out.print("ヘッダ: ");
    		for (int h=0; h<colNum; h++) {
    			header[h] = meta.getColumnName(h+1);	//1始まり
        		System.out.print(header[h] + " ");
    		}
    		System.out.println();
    		list.add(getHeaderTsv());
            
    		//データ格納
            ThspotMikomiBean bean = null;
    		while (rs.next()) {
    			//Beanクラスを初期化
    			bean = new ThspotMikomiBean(colNum);
    			//Beanクラスへセット
				for (int d=0; d<bean.data.length; d++) {
					bean.data[d] = rs.getString(d+1);	//1始まり
				}
            	// リストにBeanクラスごと格納
    			list.add(bean.getDataTsv());
    		}			
		//} catch(SQLException e) {
		//	// エラーハンドリング
		//	System.out.println("sql実行失敗");
		//	e.printStackTrace();
		//	
		} catch(ClassNotFoundException e) {
			// エラーハンドリング
			System.out.println("JDBCドライバ関連エラー");
			e.printStackTrace();
		} finally {
			// DB接続を解除
			if (conn != null) {
				conn.close();
			}
		}
		
		// リストを返す
		return list;
	}
	
	//https://confrage.jp/javaからプロシージャを呼び出す方法/
	public void mikomiUpdate() throws SQLException {
        String sql = "CALL package.update";
    	
		//接続処理
		Connection conn = null;
		try {
			Class.forName(DB_DRIVER);
			conn = DriverManager.getConnection(this.DB_URL, this.DB_USER, this.DB_PASS);
			System.out.println("  sql: " + sql);
			conn.setAutoCommit(false);
			
			CallableStatement cs = conn.prepareCall(sql);
			// OUTパラメータ
			//cs.registerOutParameter(4, Types.INTEGER);  
			//cs.registerOutParameter(5, Types.VARCHAR);
			//cs.registerOutParameter(6, Types.ARRAY, "INFOARRAY");
			// INパラメータ
			//cs.setInt(1, 100);
			//cs.setString(2, "2");
			// プロシージャを実行する	
			cs.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
	   	} finally {
			// DB接続を解除
			if (conn != null) {
				conn.close();
			}
	   }
	}
}
