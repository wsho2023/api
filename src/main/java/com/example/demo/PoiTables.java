package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.function.BiConsumer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTableStyleInfo;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//https://devlog.arksystems.co.jp/2021/10/19/15344/#
public class PoiTables {
	
	public void run() throws IOException {
        record TableRowValues(String productId, LocalDate datePurchased, int unitPrice, int numOfUnits) {
        }
        
        record TableColumn(String columnName, String styleDataFormat, BiConsumer<Cell, TableRowValues> setter) {
            void setRowValueToCell(final TableRowValues rowValues, final Cell cell) {
                setter.accept(cell, rowValues);
            }
        }
 
        final var tableName = "製品購入";
        final TableColumn[] tableColumns = {
            new TableColumn(
                    "製品番号",
                    "text",
                    (cell, rowValues) -> cell.setCellValue(rowValues.productId())
            ),
            new TableColumn(
                    "購入日",
                    "yyyy\\-mm\\-dd;@",
                    (cell, rowValues) -> cell.setCellValue(rowValues.datePurchased())
            ),
            new TableColumn(
                    "単価",
                    "\"¥\"#,##0;\"¥\"\\-#,##0",
                    (cell, rowValues) -> cell.setCellValue(rowValues.unitPrice())
            ),
            new TableColumn(
                    "購入個数",
                    "#,##0",
                    (cell, rowValues) -> cell.setCellValue(rowValues.numOfUnits())
            ),
            new TableColumn(
                    "購入額",
                    "\"¥\"#,##0;\"¥\"\\-#,##0",
                    (cell, rowValues) -> cell.setCellFormula("製品購入[[#This Row],[単価]] * 製品購入[[#This Row],[購入個数]]")
            )
        };
        final TableRowValues[] tableRows = {
            new TableRowValues("ARK-H", LocalDate.of(2020, 9, 29), 137, 2),
            new TableRowValues("ARK-O", LocalDate.of(2020, 7, 22), 140, 5),
            new TableRowValues("ARK-G", LocalDate.of(2020, 11, 6), 149, 3),
            new TableRowValues("ARK-E", LocalDate.of(2020, 8, 1), 145, 2),
                // ……
        };
        
        final var colLength = tableColumns.length;
        final var rowLength = tableRows.length;
 
        final var top = 0;
        final var left = 0;
        final var bottom = top + 1 + rowLength - 1;
        final var right = left + colLength - 1;
 
        try (var book = new XSSFWorkbook()) {
            final var sheet = (XSSFSheet) book.createSheet();
            final var creationHelper = book.getCreationHelper();
            final var dataFormat = book.createDataFormat();
 
            /* テーブルの見出し行 */
            {
                final var row = sheet.createRow(top);
                for (var c = 0; c < colLength; c++) {
                    final var columnIndex = left + c;
                    final var cell = row.createCell(columnIndex);
                    cell.setCellValue(tableColumns[c].columnName());
 
                    // sheet.autoSizeColumn(columnIndex);
                    sheet.setColumnWidth(columnIndex, 12 * 0x100);
                }
            }
 
            /* テーブルとして書式設定 */
            final var tableArea = creationHelper.createAreaReference(
                    new CellReference(top, left),
                    new CellReference(bottom, right)
            );
            final var table = sheet.createTable(tableArea);
            table.setName(tableName);
            table.setDisplayName(tableName); // テーブル名
            table.setStyleName("TableStyleLight6"); // スタイル: TableStyleLight## | TableStyleMedium## | TableStyleDark##
            final var style = (XSSFTableStyleInfo) table.getStyle();
            style.setShowRowStripes(true); // 縞模様 (行)
            final var ctAutoFilter = table.getCTTable().addNewAutoFilter();
            ctAutoFilter.setRef(tableArea.formatAsString()); // フィルター ボタン
 
            /* テーブルのデータ行 */
            final var cellStyles = Arrays.stream(tableColumns)
                    .map(TableColumn::styleDataFormat)
                    .mapToInt(dataFormat::getFormat)
                    .mapToObj(formatIndex -> {
                        final var cellStyle = book.createCellStyle();
                        cellStyle.setDataFormat(formatIndex);
                        return cellStyle;
                    })
                    .toArray(XSSFCellStyle[]::new);
            for (var r = 0; r < rowLength; r++) {
                final var row = sheet.createRow(top + 1 + r);
                final var rowValues = tableRows[r];
 
                for (var c = 0; c < colLength; c++) {
                    final var cell = row.createCell(left + c);
                    cell.setCellStyle(cellStyles[c]);
                    tableColumns[c].setRowValueToCell(rowValues, cell);
                }
            }
 
            /* 保存 */
            try (var stream = Files.newOutputStream(Path.of("table.xlsx"))) {
                book.write(stream);
            }
        }
    }

}
