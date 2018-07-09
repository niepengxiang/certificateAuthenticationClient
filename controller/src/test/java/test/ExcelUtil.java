package test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

    public static void main(String[] args) {
        String path = "G:\\新建 XLS 工作表.xls";
        try {
            List<List<Object>> result = new ExcelUtil().readXls(path);
            System.out.println(result.size());
            for (int i = 0; i < result.size(); i++) {
                List<Object> model = result.get(i);
                System.out.println("orderNum:" + model.get(0) + "--> orderAmount:" + model.get(3));
                	
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
    * @Title: readXls 
    * @Description: 处理xls文件
    * @param @param path
    * @param @return
    * @param @throws Exception    设定文件 
    * @return List<List<String>>    返回类型 
    * @throws
    * 
    * 从代码不难发现其处理逻辑：
    * 1.先用InputStream获取excel文件的io流
    * 2.然后穿件一个内存中的excel文件HSSFWorkbook类型对象，这个对象表示了整个excel文件。
    * 3.对这个excel文件的每页做循环处理
    * 4.对每页中每行做循环处理
    * 5.对每行中的每个单元格做处理，获取这个单元格的值
    * 6.把这行的结果添加到一个List数组中
    * 7.把每行的结果添加到最后的总结果中
    * 8.解析完以后就获取了一个List<List<String>>类型的对象了
    * 
     */
    private List<List<Object>> readXls(String path) throws Exception {
        InputStream is = new FileInputStream(path);
        // HSSFWorkbook 标识整个excel
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        List<List<Object>> result = new ArrayList<List<Object>>();
        int size = hssfWorkbook.getNumberOfSheets();
        // 循环每一页，并处理当前循环页
        for (int numSheet = 0; numSheet < size; numSheet++) {
            // HSSFSheet 标识某一页
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            
            int firstRowNum = hssfSheet.getFirstRowNum();
            
            HSSFRow row = hssfSheet.getRow(firstRowNum);
            
            if(row == null) {
            	continue;
            }
            int cells = row.getPhysicalNumberOfCells();
            
            // 处理当前页，循环读取每一行
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                // HSSFRow表示行
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                int minColIx = hssfRow.getFirstCellNum();
                List<Object> rowList = new ArrayList<Object>();
                // 遍历改行，获取处理每个cell元素
                for (int colIx = minColIx; colIx < cells; colIx++) {
                    // HSSFCell 表示单元格
                    Cell cell = hssfRow.getCell(colIx);
                    rowList.add(getStringVal(cell));
                }
                result.add(rowList);
            }
        }
        return result;
    }

    /**
    * @Title: readXlsx 
    * @Description: 处理Xlsx文件
    * @param @param path
    * @param @return
    * @param @throws Exception    设定文件 
    * @return List<List<String>>    返回类型 
    * @throws
     */
    private List<List<String>> readXlsx(String path) throws Exception {
        InputStream is = new FileInputStream(path);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        List<List<String>> result = new ArrayList<List<String>>();
        // 循环每一页，并处理当前循环页
        for (XSSFSheet xssfSheet : xssfWorkbook) {
            if (xssfSheet == null) {
                continue;
            }
            // 处理当前页，循环读取每一行
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                int minColIx = xssfRow.getFirstCellNum();
                int maxColIx = xssfRow.getLastCellNum();
                List<String> rowList = new ArrayList<String>();
                for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                    XSSFCell cell = xssfRow.getCell(colIx);
                    if (cell == null) {
                        continue;
                    }
                    rowList.add(cell.toString());
                }
                result.add(rowList);
            }
        }
        return result;
    }

    // 存在的问题
    /*
     * 其实有时候我们希望得到的数据就是excel中的数据，可是最后发现结果不理想
     * 如果你的excel中的数据是数字，你会发现Java中对应的变成了科学计数法。
     * 所以在获取值的时候就要做一些特殊处理来保证得到自己想要的结果
     * 网上的做法是对于数值型的数据格式化，获取自己想要的结果。
     * 下面提供另外一种方法，在此之前，我们先看一下poi中对于toString()方法:
     * 
     * 该方法是poi的方法，从源码中我们可以发现，该处理流程是：
     * 1.获取单元格的类型
     * 2.根据类型格式化数据并输出。这样就产生了很多不是我们想要的
     * 故对这个方法做一个改造。
     */
    /*public String toString(){
        switch(getCellType()){
            case CELL_TYPE_BLANK:
                return "";
            case CELL_TYPE_BOOLEAN:
                return getBooleanCellValue() ? "TRUE" : "FALSE";
            case CELL_TYPE_ERROR:
                return ErrorEval.getText(getErrorCellValue());
            case CELL_TYPE_FORMULA: 
                return getCellFormula();
            case CELL_TYPE_NUMERIC:
                if(DateUtil.isCellDateFormatted(this)){
                    DateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy")
                    return sdf.format(getDateCellValue());
                }
                return getNumericCellValue() + "";
            case CELL_TYPE_STRING:  
                return getRichStringCellValue().toString();
            default :
                return "Unknown Cell Type:" + getCellType();
        }
    }*/

    /**
     * 改造poi默认的toString（）方法如下
    * @Title: getStringVal 
    * @Description: 1.对于不熟悉的类型，或者为空则返回""控制串
    *               2.如果是数字，则修改单元格类型为String，然后返回String，这样就保证数字不被格式化了
    * @param @param cell
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public static Object getStringVal(Cell cell) {
    	Object cellValue = "";
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
}