package com.npx.admin.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelReader {

	// 创建文件输进流

	private BufferedReader reader = null;

	// 文件类型

	private String filetype;

	// 文件二进制输进流

	private InputStream is = null;

	// 当前的Sheet

	private int currSheet;

	// 当前位置

	private int currPosition;

	// Sheet数目

	private int numOfSheets;

	// HSSFWorkbook

	HSSFWorkbook workbook = null;

	// 设置Cell之间以空格分割

	private static String EXCEL_LINE_DELIMITER = ",";

	// 设置最大列数

	private static int MAX_EXCEL_COLUMNS = 64;

	// 构造函数创建一个ExcelReader

	public ExcelReader(String inputfile) throws IOException, Exception {

		// 判定参数是否为空或没有意义

		if (inputfile == null || inputfile.trim().equals("")) {

			throw new IOException("no input file specified");

		}

		// 取得文件名的后缀名赋值给filetype

		this.filetype = inputfile.substring(inputfile.lastIndexOf(".") + 1);

		// 设置开始行为0

		currPosition = 0;

		// 设置当前位置为0

		currSheet = 0;

		// 创建文件输进流

		is = new FileInputStream(inputfile);

		// 判定文件格式

		if (filetype.equalsIgnoreCase("txt")) {

			// 假如是txt则直接创建BufferedReader读取

			reader = new BufferedReader(new InputStreamReader(is));

		}

		else if (filetype.equalsIgnoreCase("xls")) {

			// 假如是Excel文件则创建HSSFWorkbook读取

			workbook = new HSSFWorkbook(is);

			// 设置Sheet数

			numOfSheets = workbook.getNumberOfSheets();

		}

		else {

			throw new Exception("File Type Not Supported");

		}

	}

	// 函数readLine读取文件的一行

	public String readLine() throws IOException {

		// 假如是txt文件则通过reader读取

		if (filetype.equalsIgnoreCase("txt")) {

			String str = reader.readLine();

			// 空行则略往，直接读取下一行
			while (str.trim().equals("")) {
				str = reader.readLine();
			}

			return str;

		}

		// 假如是XLS文件则通过POI提供的API读取文件

		else if (filetype.equalsIgnoreCase("xls")) {

			// 根据currSheet值获得当前的sheet

			HSSFSheet sheet = workbook.getSheetAt(currSheet);

			// 判定当前行是否到但前Sheet的结尾

			if (currPosition > sheet.getLastRowNum()) {

				// 当前行位置清零

				currPosition = 0;

				// 判定是否还有Sheet

				while (currSheet != numOfSheets - 1) {

					// 得到下一张Sheet

					sheet = workbook.getSheetAt(currSheet + 1);

					// 当前行数是否已经到达文件末尾

					if (currPosition == sheet.getLastRowNum()) {

						// 当前Sheet指向下一张Sheet

						currSheet++;

						continue;

					} else {

						// 获取当前行数

						int row = currPosition;

						currPosition++;

						// 读取当前行数据

						return getLine(sheet, row);

					}

				}

				return null;

			}

			// 获取当前行数

			int row = currPosition;

			currPosition++;

			// 读取当前行数据

			return getLine(sheet, row);

		}

		return null;

	}

	// 函数getLine返回Sheet的一行数据

	private String getLine(HSSFSheet sheet, int row) {

		// 根据行数取得Sheet的一行

		HSSFRow rowline = sheet.getRow(row);

		// 创建字符创缓冲区

		StringBuffer buffer = new StringBuffer();

		// 获取当前行的列数

		int filledColumns = rowline.getLastCellNum();

		HSSFCell cell = null;

		// 循环遍历所有列

		for (int i = 0; i < filledColumns; i++) {

			// 取得当前Cell

			cell = rowline.getCell((short) i);

			String cellvalue = null;

			if (cell != null) {

				// 判定当前Cell的Type

				switch (cell.getCellType()) {

				// 假如当前Cell的Type为NUMERIC

				case HSSFCell.CELL_TYPE_NUMERIC: {

					// 判定当前的cell是否为Date

					if (HSSFDateUtil.isCellDateFormatted(cell)) {

						// 假如是Date类型则，取得该Cell的Date值

						Date date = cell.getDateCellValue();

						// 把Date转换本钱地格式的字符串

						cellvalue = cell.getDateCellValue().toLocaleString();

					}

					// 假如是纯数字

					else {

						// 取得当前Cell的数值

						Integer num = new Integer((int) cell

								.getNumericCellValue());

						cellvalue = String.valueOf(num);

					}

					break;

				}

				// 假如当前Cell的Type为STRIN

				case HSSFCell.CELL_TYPE_STRING:

					// 取得当前的Cell字符串

					cellvalue = cell.getStringCellValue().replaceAll("'", "''");

					break;

				// 默认的Cell值

				default:

					cellvalue = " ";

				}

			} else {

				cellvalue = "";

			}

			// 在每个字段之间插进分割符

			buffer.append(cellvalue).append(EXCEL_LINE_DELIMITER);

		}

		// 以字符串返回该行的数据

		return buffer.toString();

	}

	// close函数执行流的封闭操纵

	public void close() {

		// 假如is不为空，则封闭InputSteam文件输进流

		if (is != null) {

			try {

				is.close();

			} catch (IOException e) {

				is = null;

			}

		}

		// 假如reader不为空则封闭BufferedReader文件输进流

		if (reader != null) {

			try {

				reader.close();

			} catch (IOException e) {

				reader = null;

			}

		}

	}

}