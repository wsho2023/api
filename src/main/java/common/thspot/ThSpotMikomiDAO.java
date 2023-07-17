package common.thspot;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.demo.ApiConfig;

public class ThSpotMikomiDAO {
	String DB_URL;
	String DB_USER;
	String DB_PASS;
	String DB_DRIVER;
	
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
		return "行\tID\t品名\tコード3\t購入先\t分類\t予定月\t数量\t単価\t金額\r\n";
	}

	public ArrayList<ThspotMikomiBean> getData(String sql) {
		//接続処理
		Connection conn = null;
		ArrayList<ThspotMikomiBean> list = new ArrayList<ThspotMikomiBean>();
		try {
			Class.forName(DB_DRIVER);
			conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
			System.out.println(sql);	//for debug

			PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            ThspotMikomiBean bean = new ThspotMikomiBean();
            int line = 1;
    		while(rs.next()) {
    			// ユーザIDと名前をBeanクラスへセット
    			bean.line = line;
    			bean.id = rs.getString("ID");
    			bean.code3 = rs.getString("CODE3");
    			bean.torihikisaki = rs.getString("TORIHIKISAKI");
    			bean.bunrui = rs.getString("BUNRUI");
    			bean.yotei = rs.getString("YOTEI");
    			bean.suryo = rs.getString("SURYO");
    			bean.tanka = rs.getString("TANKA");
    			bean.kingaku = rs.getString("KINGAKU");
            	// リストにBeanクラスごと格納
    			list.add(bean);
    			//Beanクラスを初期化
    			bean = new ThspotMikomiBean();
    			line++;
    		}
		} catch(SQLException e) {
			// エラーハンドリング
			System.out.println("sql実行失敗");
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			// エラーハンドリング
			System.out.println("JDBCドライバ関連エラー");
			e.printStackTrace();
		} finally {
			// DB接続を解除
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		// リストを返す
		return list;
	}

	public ArrayList<String> getDataTsv(String sql) {
		//接続処理
		Connection conn = null;
		ArrayList<String> list = new ArrayList<String>();
		list.add(getHeaderTsv());
		try {
			Class.forName(DB_DRIVER);
			conn = DriverManager.getConnection(DB_URL,DB_USER,DB_PASS);
			System.out.println(sql);	//for debug

			PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            ThspotMikomiBean bean = new ThspotMikomiBean();
            int line = 1;
    		while (rs.next()) {
    			// ユーザIDと名前をBeanクラスへセット
    			bean.line = line;
    			bean.id = rs.getString("ID");
    			bean.code3 = rs.getString("CODE3");
    			bean.torihikisaki = rs.getString("TORIHIKISAKI");
    			bean.bunrui = rs.getString("BUNRUI");
    			bean.yotei = rs.getString("YOTEI");
    			bean.suryo = rs.getString("SURYO");
    			bean.tanka = rs.getString("TANKA");
    			bean.kingaku = rs.getString("KINGAKU");
            	// リストにBeanクラスごと格納
    			list.add(bean.getDataTsv());
    			//Beanクラスを初期化
    			bean = new ThspotMikomiBean();
    			line++;
    		}			
		} catch(SQLException e) {
			// エラーハンドリング
			System.out.println("sql実行失敗");
			e.printStackTrace();
			
		} catch(ClassNotFoundException e) {
			// エラーハンドリング
			System.out.println("JDBCドライバ関連エラー");
			e.printStackTrace();
		} finally {
			// DB接続を解除
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		// リストを返す
		return list;
	}
	
	
	//https://confrage.jp/javaからプロシージャを呼び出す方法/
	public void mikomiUpdate() {
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
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
	   }
	}
}
