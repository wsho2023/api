package common.thspot;

public class ThspotMikomiBean {

	public int line;
	public String id;
	public String code3;
	public String torihikisaki;
	public String bunrui;
	public String yotei;
	public String suryo;
	public String tanka;
	public String kingaku;

	public String getDataTsv() {
		return this.line + "\t"
				+ this.id + "\t"
				+ this.code3 + "\t"
				+ this.torihikisaki + "\t"
				+ this.bunrui + "\t"
				+ this.yotei + "\t"
				+ this.suryo + "\t"
				+ this.tanka + "\t"
				+ this.kingaku + "\t"
				+ this.suryo + "\r\n";
	}

}
