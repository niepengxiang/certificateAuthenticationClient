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
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

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
	
    private static Logger logger = Logger.getLogger(SwingUtils.class);
	
	/**获取文件的系统目录*/
	private static final String SRCPATH = SwingUtils.class.getClassLoader().getResource("//").getPath();
	
	
	/**
	 * @Title: createTitledBorder  
	 * @Description: TODO 创建标题栏
	 * @param @param jFrame      顶层窗口
	 * @param @param TitleBorder 标签栏
	 * @param @param tabName     标签名称
	 */
	public static JPanel createTitledBorder(JTabbedPane jTabbedPane,JPanel comp,String tabName){
			JPanel titleBorder = new JPanel();
		try {
			logger.info("添加"+tabName+"标签栏开始");
			/**设置间距*/
			Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
			/**添加样式*/
			titleBorder.setBorder(emptyBorder);
			titleBorder.add(comp);
			jTabbedPane.addTab(tabName,titleBorder);
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
	public static void createJtextField(JPanel comp,List<String> filedNameList) {
		if(filedNameList != null && filedNameList.size() > 0) {
			/**设置布局为流式布局,在北间*/
			//comp.setBorder(new EmptyBorder(5, 5, 5, 5));
			//comp.setLayout(new BorderLayout(0, 0));
			
			//comp.add(jScrollPane, BorderLayout.CENTER);
			//jPanel.add(jScrollPane, BorderLayout.CENTER);
			/**判断是否是文本框组件*/
			if( filedNameList.get(0).equals("0")) {
				try {
					logger.info("创建"+filedNameList.get(1)+"文本框开始");
					GridLayout gridLayout = new GridLayout(0, 2);
					
					/**设置布局,网格布局*/
					comp.setLayout(gridLayout);
					
					/**创建标签*/
					JLabel jLabel = new JLabel(filedNameList.get(1)+":",SwingConstants.TRAILING);
					
					/**设置标签居中*/
					jLabel.setHorizontalAlignment(JLabel.CENTER);
					comp.add(jLabel);
					
					/**创建文本框*/
					final JTextField field = new JTextField(50);
										
					comp.add(field);
					 
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
					comp.add(jLabel);
					
					/**创建单选框的水平图*/
					JPanel raidaoButtonJPanel = new JPanel();
					comp.add(raidaoButtonJPanel);
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
			
			if(filedNameList.get(0).equals("2")) {
				String opt = null;
				try {
					logger.info("创建"+filedNameList.get(1)+"下拉框开始");
					/**创建标签*/
					JLabel jLabel = new JLabel(filedNameList.get(1)+":",SwingConstants.TRAILING);
					
					/**设置标签居中*/
					jLabel.setHorizontalAlignment(JLabel.CENTER);
					comp.add(jLabel);
					
					String credentialTypeStr = filedNameList.get(2);
					String[] credentialTypes = credentialTypeStr.split("\\.");
					JComboBox<String> jComboBox = new JComboBox<>(credentialTypes);
					comp.add(jComboBox);
					logger.info("创建"+filedNameList.get(1)+"下拉框结束");
				}catch(Exception ex) {
					logger.error("创建"+opt+"下拉框内容失败",ex);
				}
			}
			
			
			//jScrollPane.setViewportView(comp);
			//设置垂直滚动条的显示: 一直显示
			//jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        //设置横向滚动条的显示: 当需要的时候显示
			//jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			//参数说明:
	        //AS_NEEDED 需要的时候显示
	        //ALWAYS    一直显示
	        //NEVER     绝不显示
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
	public static JButton createJBotton(JPanel jpanl,String bottonName,Color color) {
		
		JButton jButton = null;
		try {
			logger.info("创建按钮"+bottonName+"开始");
			JPanel buttonPanel = new JPanel();
			jpanl.add(buttonPanel, BorderLayout.SOUTH);
			jButton = new JButton(bottonName);
			jButton.setBackground(color);
			buttonPanel.add(jButton);
			logger.info("创建按钮"+bottonName+"结束");
		}catch (Exception ex) {
			logger.error("创建"+bottonName+"按钮失败",ex);
		}
		return jButton;
	}
	
	/**
	 * @Title: getMap  
	 * @Description: TODO 获取内容
	 * @param @param fileName 文件名称
	 * @return Map<String,Object>  Map
	 */
	public static Map<String,Object> getMap(String fileName) {
		LinkedHashMap<String, Object> configMap = new LinkedHashMap<String, Object>();
		String path = new File(SRCPATH).getParentFile().getParentFile().getPath() + "/src/main/resources/filedNames/"+fileName;
		try {
			
			FileChangedReloadingStrategy reloadingStrategy = new FileChangedReloadingStrategy();
			reloadingStrategy.setRefreshDelay(Long.parseLong("1000"));
			
			PropertiesConfiguration	fileNamesProeprties = new PropertiesConfiguration();
			
			fileNamesProeprties.setReloadingStrategy(reloadingStrategy);
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
	
	
	/**
	 * @Title: verifyRequired  
	 * @Description: TODO 验证必填项
	 * @param @param mapData     属性文件
	 * @param @param component   组件父类
	 * @return void  
	 */
	@SuppressWarnings("unchecked")
	public static boolean verifyRequired(Map<String,Object> mapData,Map<String,Object >moduleData) {
		logger.info("验证必填项开始");
		boolean flag = true;
		try {
			Set<Entry<String,Object>> entrySet = mapData.entrySet();
			
			Set<Entry<String,Object>> entrySet2 = moduleData.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				for (Entry<String, Object> entry2 : entrySet2) {
					if(entry.getKey().equals(entry2.getKey())) {
						List<String> list = (List<String>)entry.getValue();
						if(list.get(0).equals("0") && list.get(2).equals("required")) {
							String value = (String)entry2.getValue();
							if("".equals(value)) {
								JOptionPane.showMessageDialog(null, list.get(1)+"不能为空", "提示框", JOptionPane.WARNING_MESSAGE);
								flag = false;
							}
						}
						
						if(list.get(0).equals("1") && list.get(3).equals("required")) {
							String value = (String)entry2.getValue();
							if("".equals(value)) {
								JOptionPane.showMessageDialog(null, list.get(1)+"不能为空", "提示框", JOptionPane.WARNING_MESSAGE);
								flag = false;
							}
						}
						
						if(list.get(0).equals("2") && list.get(3).equals("required")) {
							String value = (String)entry2.getValue();
							if("".equals(value)) {
								JOptionPane.showMessageDialog(null, list.get(1)+"不能为空", "提示框", JOptionPane.WARNING_MESSAGE);
								flag = false;
							}
						}
					}
				}
			}
			logger.info("验证必填项结束");
		}catch (Exception ex) {
			logger.error("验证必填项错误",ex);
		}
		return flag;
	}
	
	/**
	 * @Title: addJScrollPane  
	 * @Description: TODO 添加滾動條
	 * @param @param orgCerdentialComp
	 * @param @param orgCerdentialTitle    参数  
	 * @return void    返回类型  
	 * @throws
	 */
	public static void addJScrollPane(JPanel orgCerdentialComp, JPanel orgCerdentialTitle) {
		orgCerdentialTitle.add(orgCerdentialComp);
        orgCerdentialTitle.setBorder(new EmptyBorder(5, 5, 5, 5));
        orgCerdentialTitle.setLayout(new BorderLayout(0, 0));
        JScrollPane jScrollPane = new JScrollPane(orgCerdentialComp);
        jScrollPane.setPreferredSize(new Dimension(1000, 800));
        orgCerdentialTitle.add(jScrollPane,BorderLayout.CENTER);
        //设置垂直滚动条的显示: 一直显示
  		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
          //设置横向滚动条的显示: 当需要的时候显示
  		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
}
