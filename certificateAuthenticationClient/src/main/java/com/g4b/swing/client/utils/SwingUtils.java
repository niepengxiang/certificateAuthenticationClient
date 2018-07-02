package com.g4b.swing.client.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.log4j.Logger;

/**
 * @ClassName: SwingUtils  
 * @Description: TODO 图像界面工具类  
 * @author NiePengXiang
 * @date 2018年6月29日  
 *
 */
public class SwingUtils{
	
	/**定义窗格*/
	private static JTabbedPane jTabbedPane;
	
	/**定义文本平面*/
	private static JPanel filedJpanl;
	
	/**定义按钮的平面*/
	private static JPanel buttonPanel;
	
    private static LinkedHashMap<String, Object> configMap = new LinkedHashMap<String, Object>();
	
    private static Logger logger = Logger.getLogger(SwingUtils.class);
	
	private static FileChangedReloadingStrategy reloadingStrategy ;
	
	private static PropertiesConfiguration fileNamesProeprties;
	
	private static final String SRCPATH = SwingUtils.class.getClassLoader().getResource("//").getPath();
	
	/**初始化类*/
	static {
		filedJpanl = new JPanel();
		
		buttonPanel = new JPanel();
		
		jTabbedPane = new JTabbedPane();
		try {
			reloadingStrategy = new FileChangedReloadingStrategy();
			reloadingStrategy.setRefreshDelay(Long.parseLong("1000"));
			fileNamesProeprties = new PropertiesConfiguration();
			fileNamesProeprties.setReloadingStrategy(reloadingStrategy);
		} catch (Exception ex) {
			logger.error("SwingUtils 初始化失败！", ex);
		}
	}

	
	/**
	 * @Title: createTitledBorder  
	 * @Description: TODO 创建标题栏
	 * @param @param jFrame      顶层窗口
	 * @param @param TitleBorder 标签栏
	 * @param @param tabName     标签名称
	 */
	public static JPanel createTitledBorder(JFrame jFrame,String tabName){
			JPanel titleBorder = new JPanel();
		try {
			logger.info("添加"+tabName+"标签栏开始");
			/**创建布局*/
			
			/**设置间距*/
			Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
			
			/**添加样式*/
			titleBorder.setBorder(emptyBorder);
			
			/**索引添加名称*/
			jTabbedPane.addTab(tabName, titleBorder);
			
			/**添加窗格*/
			jFrame.add(jTabbedPane);
			logger.info("添加"+tabName+"标签栏结束");
		}catch (Exception ex) {
			logger.error("添加"+tabName+"标签栏失败");
		}
		return titleBorder;
	}
	
	/**
	 * @Title: setScreenCenter  
	 * @Description: TODO 设置弹出框在屏幕中心  
	 * @param @param jFrame 顶层窗口
	 * @return void      
	 */
	public static void setScreenCenter(JFrame jFrame){
		/**定义工具包*/
		Toolkit kit = Toolkit.getDefaultToolkit(); 
		
		/**获取屏幕的尺寸*/
		Dimension screenSize = kit.getScreenSize(); 
		
		/**获取屏幕的宽*/
		int screenWidth = screenSize.width/2; 
		
		/** 获取屏幕的高*/
		int screenHeight = screenSize.height/2; 
		
		/**获得窗口高*/
		int height = jFrame.getHeight();
		
		/**获得窗口宽*/
		int width = jFrame.getWidth();
		
		/**设置居中*/
		jFrame.setLocation(screenWidth/2-width/2, screenHeight/2-height/2);
	}
	
	/**
	 * @Title: createJtextField  
	 * @Description: TODO 创建组件文本框靠右显示,根据属性配置文件显示不同的组件  
	 * @param @param orgCerdentialTitle 标签栏的水平图
	 * @param @param filedName    		字段名称
	 * @return void  
	 */
	public static void createJtextField(JPanel titleJpanl,List<String> filedNameList) {
		if(filedNameList != null && filedNameList.size() > 0) {
			/**设置布局为流式布局,在北间*/
			titleJpanl.add(filedJpanl, BorderLayout.NORTH);
			/**判断是否是文本框组件*/
			if( filedNameList.get(0).equals("0")) {
				try {
					logger.info("创建"+filedNameList.get(1)+"文本框开始");
					/**设置布局,网格布局*/
					filedJpanl.setLayout(new GridLayout(0, 2));
					
					/**创建标签*/
					JLabel jLabel = new JLabel(filedNameList.get(1)+":",SwingConstants.TRAILING);
					
					/**设置标签居中*/
					jLabel.setHorizontalAlignment(JLabel.CENTER);
					filedJpanl.add(jLabel);
					
					/**创建文本框*/
					final JTextField field = new JTextField(50);
					filedJpanl.add(field);
					logger.info("创建"+filedNameList.get(1)+"文本框结束");
				}catch(Exception ex) {
					logger.error("创建"+filedNameList.get(1)+"文本框失败", ex);
				}
			}
			
			/**判断是否是单选按钮组件*/
			if(filedNameList.get(0).equals("1")) {
				String opt = null;
				try {
					logger.info("创建"+filedNameList.get(1)+"单选框开始");
					/**创建标签*/
					JLabel jLabel = new JLabel(filedNameList.get(1)+":",SwingConstants.TRAILING);
					
					/**设置标签居中*/
					jLabel.setHorizontalAlignment(JLabel.CENTER);
					filedJpanl.add(jLabel);
					
					/**创建单选框的水平图*/
					JPanel raidaoButtonJPanel = new JPanel();
					filedJpanl.add(raidaoButtonJPanel);
					String reqOptStr = filedNameList.get(2);
					String[] reqOpt = reqOptStr.split("\\.");
					
					/**创建单选按钮管理器,控制单选*/
					ButtonGroup group = new ButtonGroup();
					
					for (String str : reqOpt) {
						opt = str.split("_")[1];
						logger.info("创建"+opt+"单选框内容开始");
						final JRadioButton jRadioButton = new JRadioButton(opt);
						group.add(jRadioButton);
						raidaoButtonJPanel.add(jRadioButton);
						logger.info("创建"+opt+"单选框内容结束");
					}
					
					logger.info("创建"+filedNameList.get(1)+"单选框结束");
				}catch(Exception ex) {
					logger.error("创建"+opt+"内容单选框内容失败",ex);
				}
			}
			
			
		}
	}
	
	/**
	 * @Title: createJBotton  
	 * @Description: TODO 常见按钮,按钮在最下方  
	 * @param @param jFrame     顶层窗口
	 * @param @param bottonName 按钮名称
	 * @param @param color    	按钮背景色,可为null背景为默认
	 * @return void
	 */
	public static void createJBotton(JFrame jFrame,String bottonName,Color color) {
		try {
			logger.info("创建按钮"+bottonName+"开始");
			jFrame.add(buttonPanel, BorderLayout.SOUTH);
			JButton jButton = new JButton(bottonName);
			jButton.setBackground(color);
			buttonPanel.add(jButton);
			logger.info("创建按钮"+bottonName+"结束");
		}catch (Exception ex) {
			logger.error("创建"+bottonName+"按钮失败",ex);
		}
	}
	
	/**
	 * @Title: getMap  
	 * @Description: TODO 获取内容
	 * @param @param fileName 文件名称
	 * @return Map<String,Object>  Map
	 */
	public static Map<String,Object> getMap(String fileName) {
		String path = new File(SRCPATH).getParentFile().getParentFile().getPath() + "/src/main/resources/filedNames/"+fileName;
		try {
			fileNamesProeprties.load(new InputStreamReader(new FileInputStream(path), "UTF-8"));
			Iterator<String> keys = fileNamesProeprties.getKeys();
			while(keys.hasNext()) {
				String key = keys.next();
				configMap.put(key, fileNamesProeprties.getProperty(key));
			}
		}catch (Exception ex) {
			logger.error(ex);
			return null;
		}
		return configMap;
	}
}
