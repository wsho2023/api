package common.thspot;

public class ThspotMikomiBean {

	public String data[];

	ThspotMikomiBean(int colNum) {
		data = new String[colNum];
	}
	
	//タブ区切り1行文字列を作成
	public String getDataTsv() {
		String retStr = "";

		for (int c=0; c<data.length-1; c++) {
			retStr = retStr + data[c] + "\t";
		}
		retStr = retStr + data[data.length-1] + "\r\n";

		return retStr;
	}
}
