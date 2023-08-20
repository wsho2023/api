package dbchange;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StatusDataBean  {
	//DB接続情報
	String DB_URL;
	String DB_USER;
	String DB_PASS;
	String DB_DRIVER;
	
	int ID;
	String Name;
	int BirthYear;
	int BirthMonth;
	int BirthDay;
	String Sex;
	
	public StatusDataBean() {
		//接続情報取得
		ResourceBundle rb = ResourceBundle.getBundle("prop");
		DB_URL = rb.getString("DB_URL");
		DB_USER = rb.getString("DB_USER");
		DB_PASS = rb.getString("DB_PASS");
		DB_DRIVER = rb.getString("DB_DRIVER");
    }

	public String getTaskName() {
		return null;
	}

	public String getTaskStatus() {
		return null;
	}

	public String getUserId() {
		return null;
	}
	public int queryDB() {
		String sql = "select * from USER_DATA where rownum=1 order by ID desc";
		//接続処理
		Connection conn = null;
		try {
			Class.forName(DB_DRIVER);
			conn = DriverManager.getConnection(this.DB_URL, this.DB_USER, this.DB_PASS);
			//System.out.println(sql);

			PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
	            this.ID = rs.getInt("ID");
				this.Name = rs.getString("NAME");
				this.BirthYear = rs.getInt("BIRTH_YEAR");
				this.BirthMonth = rs.getInt("BIRTH_MONTH");
				this.BirthDay = rs.getInt("BIRTH_DAY");
				this.Sex = rs.getString("SEX");
    		}
		} catch(SQLException sql_e) {
			// エラーハンドリング
			System.out.println("sql実行失敗");
			sql_e.printStackTrace();
			
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
		
		return 0;
	}
	
    public String getDataText() {
		String line = this.ID + "\t" + this.Name + "\t" + this.BirthYear + "\t" +
					  this.BirthMonth + "\t" + this.BirthDay + "\t" + this.Sex;

		return line;
	}

}

