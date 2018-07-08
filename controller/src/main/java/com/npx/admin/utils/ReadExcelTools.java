package com.npx.admin.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcelTools {
	private final static String xls = "xls";
	private final static String xlsx = "xlsx";
	private static Logger logger = Logger.getLogger(ReadExcelTools.class);

	/**
	 * 读入excel文件，解析后返回忽略第一行
	 * 
	 * @param file
	 * @throws IOException
	 */
	public static List<Object[]> readExcel(InputStream inputStream, String fileName) throws IOException {
		List<Object[]> list = null;
		int rowNum = 0;
		int cellNum = 0;
		try {
			/** 获得Workbook工作薄对象 */
			Workbook workbook = getWorkBook(inputStream, fileName);
			/** 创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回 */
			list = new ArrayList<Object[]>();
			if (workbook != null) {
				for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
					/** 获得当前sheet工作表 */
					Sheet sheet = workbook.getSheetAt(sheetNum);
					if (sheet == null) {
						continue;
					}
					/** 获得当前sheet的开始行 */
					int firstRowNum = sheet.getFirstRowNum();
					/** 获得当前sheet的结束行 */
					int lastRowNum = sheet.getLastRowNum();
					/** 循环除了第一行的所有行 */
					for (rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
						// 获得当前行
						Row row = sheet.getRow(rowNum);
						if (row == null) {
							continue;
						}
						/** 获得当前行的开始列 */
						int firstCellNum = row.getFirstCellNum();
						/** 获得当前行的列数 */
						int lastCellNum = row.getLastCellNum();// 为空列获取
						// int lastCellNum = row.getPhysicalNumberOfCells();//为空列不获取
						// String[] cells = new String[row.getPhysicalNumberOfCells()];
						Object[] cells = new Object[row.getLastCellNum()];
						/** 循环当前行 */
						for (cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
							Cell cell = row.getCell(cellNum);
							cells[cellNum] = getCellValue(cell);
						}
						list.add(cells);
					}
				}
			}
		} catch (Exception e) {
			logger.error(rowNum + "行" + cellNum + "列读取错误！", e);
		}
		return list;
	}

	public static Workbook getWorkBook(InputStream inputStream, String fileName) {
		/** 创建Workbook工作薄对象，表示整个excel */
		Workbook workbook = null;
		try {
			/** 根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象 */
			if (fileName.endsWith(xls)) {
				// 2003
				workbook = new HSSFWorkbook(inputStream);
			} else if (fileName.endsWith(xlsx)) {
				// 2007
				workbook = new XSSFWorkbook(inputStream);
			}
		} catch (IOException e) {
		}
		return workbook;
	}

	public static Object getCellValue(Cell cell) {
		Object cellValue = null;
		if (cell == null) {
			return cellValue;
		}
		/** 把数字当成String来读，避免出现1读成1.0的情况 */
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		/** 判断数据的类型 */
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC: // 数字
			cellValue = Integer.parseInt(String.valueOf(cell.getNumericCellValue()));
			break;
		case Cell.CELL_TYPE_STRING: // 字符串
			cellValue = String.valueOf(cell.getStringCellValue());
			break;
		case Cell.CELL_TYPE_BOOLEAN: // Boolean
			cellValue = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_BLANK: // 空值
			cellValue = "";
			break;
		case Cell.CELL_TYPE_ERROR: // 故障
			cellValue = "非法字符";
			break;
		default:
			cellValue = "未知类型";
			break;
		}
		return cellValue;
	}

	public static Object getFirstCellValue(InputStream inputStream, String fileName) {
		Object cellValue = null;
		/** 获得Workbook工作薄对象 */
		Workbook workbook = getWorkBook(inputStream, fileName);
		if (workbook != null) {
			for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
				/** 获得当前sheet工作表 */
				Sheet sheet = workbook.getSheetAt(sheetNum);
				if (sheet == null) {
					continue;
				}
				int firstRowNum = sheet.getFirstRowNum();
				// 获得当第一列
				Row row = sheet.getRow(firstRowNum);
				if (row == null) {
					logger.error("未获取表头信息");
					continue;
				}
				int firstCellNum = row.getFirstCellNum();

				Cell cell = row.getCell(firstCellNum);
				cellValue = getCellValue(cell);
			}
		}
		return cellValue;
	}

	public static List<String[]> getRowValue(InputStream inputStream, String fileName) {

		List<String[]> data = new ArrayList<>();
		Workbook workbook = getWorkBook(inputStream, fileName);
		List<List<Object>> dataList = new ArrayList<>();

		/** 得到第一个shell */
		Sheet sheet = workbook.getSheetAt(0);

		/** 得到Excel的行数 */
		int totalRows = sheet.getPhysicalNumberOfRows();

		/** 得到Excel的列数 */
		int totalCells = 0;
		if (totalRows >= 1 && sheet.getRow(0) != null) {
			totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
		}

		for (int lie = 1; lie <= totalCells; lie++) {
			List<Object> list = new ArrayList<Object>();
			dataList.add(list);
		}
		/** 循环Excel的行 */
		for (int r = 0; r < totalRows; r++) {
			Row row = sheet.getRow(r);
			if (row == null) {
				continue;

			}
			/** 循环Excel的列 */
			for (int c = 0; c < totalCells; c++) {
				Cell cell = row.getCell(c);
				Object cellValue = ReadExcelTools.getCellValue(cell);
				dataList.get(c).add(cellValue);
			}
		}
		for (List<Object> strList : dataList) {
			String[] strArr = new String[strList.size()];
			String[] array = strList.toArray(strArr);
			data.add(array);
		}
		return data;
	}

	public static List<List<Object>> readCellsValue(InputStream inputStream,String fileName) throws Exception {
		// HSSFWorkbook 标识整个excel
		Workbook workBook = getWorkBook(inputStream, fileName);
		List<List<Object>> result = new ArrayList<List<Object>>();
		int size = workBook.getNumberOfSheets();
		// 循环每一页，并处理当前循环页
		for (int numSheet = 0; numSheet < size; numSheet++) {
			// HSSFSheet 标识某一页
			Sheet sheet = workBook.getSheetAt(numSheet);
			if (sheet == null) {
				continue;
			}

			int firstRowNum = sheet.getFirstRowNum();

			Row row = sheet.getRow(firstRowNum);

			if (row == null) {
				continue;
			}
			int cells = row.getPhysicalNumberOfCells();

			// 处理当前页，循环读取每一行
			for (int rowNum = 2; rowNum <= sheet.getLastRowNum(); rowNum++) {
				// HSSFRow表示行
				Row hssfRow = sheet.getRow(rowNum);
				int minColIx = hssfRow.getFirstCellNum();
				List<Object> rowList = new ArrayList<Object>();
				// 遍历改行，获取处理每个cell元素
				for (int colIx = minColIx; colIx < cells; colIx++) {
					// HSSFCell 表示单元格
					Cell cell = hssfRow.getCell(colIx);
					rowList.add(getCellValue(cell));
				}
				result.add(rowList);
			}
		}
		return result;
	}
}