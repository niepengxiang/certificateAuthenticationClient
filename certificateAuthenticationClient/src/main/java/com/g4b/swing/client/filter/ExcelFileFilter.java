package com.g4b.swing.client.filter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @ClassName: ExcelFileFilter  
 * @Description: Excel文件过滤器
 * @author NiePengXiang
 * @date 2018年7月5日  
 *
 */
public class ExcelFileFilter extends FileFilter {  
	
	
    public String getDescription() {  
        return "*.xls;*.xlsx";  
    }  
  
    public boolean accept(File file) {  
        String name = file.getName();  
        return file.isDirectory() || name.toLowerCase().endsWith(".xls") || name.toLowerCase().endsWith(".xlsx");  // 仅显示目录和xls、xlsx文件
    }  
}