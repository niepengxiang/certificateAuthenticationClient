import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.g4b.swing.client.utils.ReadExcelTools;

public class ExcelText {
	public static void main(String[] args) {
		
		Workbook workBook = ReadExcelTools.getWorkBook(new File("G:\\新建 XLS 工作表.xls"));
		
		List<String[]> data = new ArrayList<>();
		
		List<List<String>> dataList = new ArrayList<>();

		/** 得到第一个shell */
		Sheet sheet = workBook.getSheetAt(0);

		/** 得到Excel的行数 */
		int totalRows = sheet.getPhysicalNumberOfRows();

		/** 得到Excel的列数 */
		int totalCells = 0;
		if (totalRows >= 1 && sheet.getRow(0) != null) {
			totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
		}

		for (int lie = 1; lie <= totalCells; lie++) {
			List<String> list = new ArrayList<String>();
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
				String cellValue = ReadExcelTools.getCellValue(cell);
				dataList.get(c).add(cellValue);
			}
		}
		for (List<String> strList : dataList) {
			String[] strArr = new String[strList.size()];
			String[] array = strList.toArray(strArr);
 			data.add(array);
		}
	}
}
