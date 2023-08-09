package common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyUtils {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	//public MyUtils() {
	//}
	public static void SystemLogPrint(String msg) {
        System.out.printf("[%s]%s\n", sdf.format(new Date()), msg);
	}

	public static void SystemErrPrint(String msg) {
        System.err.printf("[%s]%s\n", sdf.format(new Date()), msg);
	}
	
	public static String getDate() {
		return sdf.format(new Date());
	}

	public static String getToday() {
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
		return sdf2.format(new Date());
	}

	public static String getToday(int day) {
        //日時を格納するためのDateクラスを宣言(現在時刻)
        Date date = new Date();
		if (day != 0) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTime(date);
	        //Date型の持つ日時を表示
	        //System.out.println(date);
	        //Date型の持つ日時の4年後を表示(日時の加算/減算)
	        calendar.add(Calendar.YEAR, day);
	        date = calendar.getTime();
	        //System.out.println(date);
		}
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
		return sdf2.format(date);
	}
	
	public static String getDateStr() {
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		return sdf2.format(new Date());
	}
}
