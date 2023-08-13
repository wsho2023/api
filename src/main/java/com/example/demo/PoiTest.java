package com.example.demo;

import java.io.FileOutputStream;
import java.util.stream.IntStream;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.DataConsolidateFunction;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFPivotTable;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//ttps://www.javalife.jp/2023/06/23/java-apache-poiを使って、excelでピボットテーブルを作る/
public class PoiTest {
	//ピボット集計用にデータを重複させる回数（データが重複しないと面白くないので、何回か作るｗ）
	private static final int REPEAT = 3;
	
	public void run() {
		try (XSSFWorkbook wb = new XSSFWorkbook();
			FileOutputStream fileOut = new FileOutputStream(".\\pivot.xlsx")) {
			//シート作成
			XSSFSheet sheet = wb.createSheet("data");
			//データ作成
			createCellData(sheet);

			XSSFSheet sheet2 = wb.createSheet("pivot");
//ピボットテーブル作成
			createPivotTable(sheet2);
			//Excelファイル書き込み
			wb.write(fileOut);
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
			 
	private void createPivotTable(XSSFSheet sheet) {
			 
		 //ピボットテーブルを作成する
		 XSSFPivotTable pivotTable = sheet.createPivotTable(
			 //データ範囲
			 //new AreaReference(new CellReference(0, 0), new CellReference(3 * 12 * REPEAT, 2), SpreadsheetVersion.EXCEL2007),
			 new AreaReference("data!A1:C109", SpreadsheetVersion.EXCEL2007),
			 //ピボットテーブルの左上の座標
			 new CellReference(0, 0)
		 );
		 
		 //行フィールドを追加
		 pivotTable.addRowLabel(0);
		 //列フィールドを追加
		 pivotTable.addColLabel(1);
		 //値データを追加
		 pivotTable.addColumnLabel(DataConsolidateFunction.SUM, 2, "合計");
	 }
	 
	 private void createCellData(XSSFSheet sheet) {
		 
		//見出し
		Row row1 = sheet.createRow(0);
		row1.createCell(0).setCellValue("年");
		row1.createCell(1).setCellValue("月");
		row1.createCell(2).setCellValue("売上");
		 
		//リピート回数分だけ売上データ作成
		IntStream.range(0, REPEAT).forEach(i -> createCellSalesData(sheet, i));
	}
	 
	private void createCellSalesData(XSSFSheet sheet, int repeat) {
		 
		 //とりあえず３年間の売上データを乱数で作成
		IntStream.range(0, 3 * 12).forEach(i -> {
		 
			Row row = sheet.createRow(i + 1 + repeat * 3 * 12);
			
			//年(2020-2022)
			row.createCell(0).setCellValue(2020 + i / 12);
			//月(1-12)
			row.createCell(1).setCellValue(i % 12 + 1);
			//売上（上下30%ぶれの乱数）
			row.createCell(2).setCellValue((int)(1000000 * (0.7 + Math.random() * 0.6)));
		 });
	}
}
