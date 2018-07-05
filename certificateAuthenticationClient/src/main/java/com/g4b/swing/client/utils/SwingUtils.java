package com.g4b.swing.client.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.log4j.Logger;

import com.g4b.swing.client.filter.ExcelFileFilter;

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
	 * @param  jFrame      顶层窗口
	 * @param  TitleBorder 标签栏
	 * @param  tabName     标签名称
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
	 * @param  jFrame 顶层窗口
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
	 * @param valuesSize 
	 * @Title: createJtextField  
	 * @Description: TODO 创建组件文本框靠右显示,根据属性配置文件显示不同的组件  
	 * @param @param orgCerdentialTitle 标签栏的水平图
	 * @param @param filedName    		字段名称
	 * @return void  
	 */
	public static void createJtextField(JPanel comp,List<String> filedNameList) {
		
		if(filedNameList != null && filedNameList.size() > 0) {
			
				GridLayout gridLayout = new GridLayout(0, 2);
				/**设置布局,网格布局*/
				comp.setLayout(gridLayout);
				createSwingModule(comp, filedNameList);
			}
	}
	public static void createJtextField(JPanel comp,List<String> filedNameList,int index) {
		int x = 30;
		int y = 30*index;
		int w = 200;
		int h = 25;
		comp.setLayout(null);
		/**判断是否是文本框组件*/
		if( filedNameList.get(0).equals("0")) {
			try {
				logger.info("创建"+filedNameList.get(1)+"文本框开始");
				
				/**创建标签*/
				JLabel jLabel = new JLabel(filedNameList.get(1)+":");
				/**绑定坐标*/
				jLabel.setBounds(x, y, w, h);
				comp.add(jLabel);
				
				/**创建文本框*/
				final JTextField field = new JTextField(50);
				/**绑定坐标*/
				field.setBounds(x+200, y, 2*w, h);
				comp.add(field);
				logger.info("创建"+filedNameList.get(1)+"文本框结束");
			}catch(Exception ex) {
				logger.error("创建"+filedNameList.get(1)+"文本框失败", ex);
			}
		}
		/**判断是否是文本域组件*/
		if( filedNameList.get(0).equals("1")) {
			try {
				logger.info("创建"+filedNameList.get(1)+"文本框开始");
				
				/**创建标签*/
				JLabel jLabel = new JLabel(filedNameList.get(1)+":");
				/**绑定坐标*/
				jLabel.setBounds(x, y, w, h);
				comp.add(jLabel);
				/**创建文本框*/
				final JTextArea jTextArea = new JTextArea();
				jTextArea.setLineWrap(true);
				jTextArea.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));
				/**绑定坐标*/
				jTextArea.setBounds(x+200, y, 2*w, h);
				comp.add(jTextArea);
				logger.info("创建"+filedNameList.get(1)+"文本框结束");
			}catch(Exception ex) {
				logger.error("创建"+filedNameList.get(1)+"文本框失败", ex);
			}
		}
		
		/**判断是否是单选按钮组件*/
		if(filedNameList.get(0).equals("2")) {
			String opt = null;
			try {
				logger.info("创建"+filedNameList.get(1)+"单选框开始");
				/**创建标签*/
				JLabel jLabel = new JLabel(filedNameList.get(1)+":");
				/**绑定坐标*/
				jLabel.setBounds(x, y, w, h);
				comp.add(jLabel);
				
				/**创建单选框的水平图*/
				JPanel raidaoButtonJPanel = new JPanel();
				/**绑定坐标*/
				raidaoButtonJPanel.setBounds(x+200, y, 2*w, h);
				comp.add(raidaoButtonJPanel);
				String reqOptStr = filedNameList.get(2);
				String[] reqOpt = reqOptStr.split("\\.");
				
				/**创建单选按钮管理器,控制单选*/
				ButtonGroup group = new ButtonGroup();
				
				for (String str : reqOpt) {
					opt = str.split("-")[1];
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
		
		if(filedNameList.get(0).equals("3")) {
			String opt = null;
			try {
				logger.info("创建"+filedNameList.get(1)+"下拉框开始");
				/**创建标签*/
				JLabel jLabel = new JLabel(filedNameList.get(1)+":");
				
				/**绑定坐标*/
				jLabel.setBounds(x, y, w, h);
				comp.add(jLabel);
				
				String credentialTypeStr = filedNameList.get(2);
				String[] credentialTypes = credentialTypeStr.split("\\.");
				JComboBox<String> jComboBox = new JComboBox<>(credentialTypes);
				/**绑定坐标*/
				jComboBox.setBounds(x+200, y, 2*w, h);
				comp.add(jComboBox);
				logger.info("创建"+filedNameList.get(1)+"下拉框结束");
			}catch(Exception ex) {
				logger.error("创建"+opt+"下拉框内容失败",ex);
			}
		}
		if(filedNameList.get(0).equals("4")) {
			try {
					logger.info("创建"+filedNameList.get(1)+"多选框开始");
					JLabel jLabel = new JLabel(filedNameList.get(1)+":");
					
					/**绑定坐标*/
					jLabel.setBounds(x, y, w, h);
					comp.add(jLabel);
					String[] split = filedNameList.get(2).split("\\.");
					
					JPanel jPanel = new JPanel();
					for (String str : split) {
						JCheckBox jCheckBox = new JCheckBox(str.split("-")[1]);
						jPanel.add(jCheckBox);
					}
					jPanel.setBounds(x+200, y, 2*w, h);
					comp.add(jPanel);
					logger.info("创建"+filedNameList.get(1)+"多选框结束");
				}catch (Exception ex) {
					logger.error("创建"+filedNameList.get(1)+"多选框失败", ex);
			}
		}
		
		if(filedNameList.get(0).equals("5")) {
				try {
					logger.info("创建"+filedNameList.get(1)+"文件上传按钮开始");
					JLabel jLabel = new JLabel(filedNameList.get(1)+":");
					
					/**绑定坐标*/
					jLabel.setBounds(x, y, w, h);
					comp.add(jLabel);
					String[] split = filedNameList.get(2).split("\\.");
					
					JPanel jPanel = new JPanel();
					for (String str : split) {
						JCheckBox jCheckBox = new JCheckBox(str.split("-")[1]);
						jPanel.add(jCheckBox);
					}
					jPanel.setBounds(x+200, y, 2*w, h);
					comp.add(jPanel);
					logger.info("创建"+filedNameList.get(1)+"多选框结束");
				}catch (Exception ex) {
					logger.error("创建"+filedNameList.get(1)+"多选框失败", ex);
			}
		}
	}
	
	
	/***
	 * @Title: createSwingModule  
	 * @Description: 抽取方法  
	 * @param  comp
	 * @param  filedNameList   
	 * @return void    
	 */
	private static void createSwingModule(JPanel comp, List<String> filedNameList) {
		/**判断是否是文本框组件*/
		if( filedNameList.get(0).equals("0")) {
			try {
				logger.info("创建"+filedNameList.get(1)+"文本框开始");
				
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
		/**判断是否是文本域组件*/
		if( filedNameList.get(0).equals("1")) {
			try {
				logger.info("创建"+filedNameList.get(1)+"文本框开始");
				
				/**创建标签*/
				JLabel jLabel = new JLabel(filedNameList.get(1)+":",SwingConstants.TRAILING);
				/**设置标签居中*/
				jLabel.setHorizontalAlignment(JLabel.CENTER);
				comp.add(jLabel);
				/**创建文本框*/
				final JTextArea jTextArea = new JTextArea();
				jTextArea.setLineWrap(true);
				jTextArea.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));
				comp.add(jTextArea);
				logger.info("创建"+filedNameList.get(1)+"文本框结束");
			}catch(Exception ex) {
				logger.error("创建"+filedNameList.get(1)+"文本框失败", ex);
			}
		}
		/**判断是否是单选按钮组件*/
		if(filedNameList.get(0).equals("2")) {
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
					opt = str.split("-")[1];
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
		
		if(filedNameList.get(0).equals("3")) {
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
	}
	
	/**
	 * @Title: createJBotton  
	 * @Description: TODO 常见按钮,按钮在最下方  
	 * @param  jFrame     顶层窗口
	 * @param  bottonName 按钮名称
	 * @param  color      按钮背景色,可为null背景为默认
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
	 * @param  fileName 文件名称
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
			throw new RuntimeException(ex);
		}
		return configMap;
	}
	
	
	/**
	 * @Title: verifyRequired  
	 * @Description: TODO 验证必填项
	 * @param  mapData     属性文件
	 * @param  component   组件父类
	 * @return void  
	 */
	@SuppressWarnings("unchecked")
	public static List<String> verifyRequired(Map<String,Object> mapData,Map<String,Object >moduleData) {
		
		List<String> errorlist = new ArrayList<>();
		logger.info("验证必填项开始");
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
								errorlist.add(list.get(1));
							}
						}
						
						if(list.get(0).equals("1") && list.get(2).equals("required")) {
							String value = (String)entry2.getValue();
							if("".equals(value)) {
								errorlist.add(list.get(1));
							}
						}
						
						if(list.get(0).equals("2") && list.get(3).equals("required")) {
							String value = (String)entry2.getValue();
							if("".equals(value)) {
								errorlist.add(list.get(1));
							}
						}
						
						if(list.get(0).equals("3") && list.get(3).equals("required")) {
							String value = (String)entry2.getValue();
							if("".equals(value)) {
								errorlist.add(list.get(1));
							}
						}
						if(list.get(0).equals("4")&& list.get(3).equals("required")) {
							String value = (String)entry2.getValue();
							if("".equals(value)) {
								errorlist.add(list.get(1));
							}
						}
					}
				}
			}
			logger.info("验证必填项结束");
		}catch (Exception ex) {
			logger.error("验证必填项错误",ex);
			throw new RuntimeException(ex);
		}
		return errorlist;
	}
	
	/**
	 * @Title: addJScrollPane  
	 * @Description: TODO 添加滾動條
	 * @param  orgCerdentialComp
	 * @param  orgCerdentialTitle    参数  
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
  		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
          //设置横向滚动条的显示: 当需要的时候显示
  		jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
  		//参数说明:
        //AS_NEEDED 需要的时候显示
        //ALWAYS    一直显示
        //NEVER     绝不显示
	}
	
	/**
	 * @Title: fileInputJbuttonClick
	 * @Description: TODO 文件上传按钮点击事件,封住数据参数
	 * @param  jFrame      	顶层容器
	 * @param  mapData	   	属性文件读取数据
	 * @param  jTextArea	文件上传名称显示框
	 * @param  jButton   	文件上传按钮
	 * @return void  
	 */
	public static void fileInputJbuttonClick(JFrame jFrame, Map<String, Object> mapData,
			JTextArea jTextArea, JButton jButton,Set<String> set,List<Map<String,Object>> ListModule,String classification) {
		/**为文件表单按钮组件添加点击事件*/
		jButton.addMouseListener(new MouseAdapter() {
        	
        	@SuppressWarnings("unchecked")
			@Override
        	public void mouseClicked(MouseEvent e) {
        		JFileChooser jFileChooser = new JFileChooser();
        		jFileChooser.setMultiSelectionEnabled(true);
        		ExcelFileFilter fileFilter = new ExcelFileFilter();
        		jFileChooser.setFileFilter(fileFilter);
        		
        		
				int dialog = jFileChooser.showOpenDialog(jFrame);
				if(dialog == JFileChooser.APPROVE_OPTION) {
					/** 得到选择的文件,支持批量上传**/
					File[] arrfiles = jFileChooser.getSelectedFiles();
					if (arrfiles == null || arrfiles.length == 0) {
						return;
					}
					
					try {
						logger.info("文件数据读取开始");
						for (File file : arrfiles) {
							String firstCellValue = ReadExcelTools.getFirstCellValue(file);
							if(classification != null && !classification.equals(firstCellValue)) {
								JOptionPane.showMessageDialog(null, "不是该证书类型", "提示框", JOptionPane.WARNING_MESSAGE);
								continue;
							}
							
							
							if(set.contains(file.getName())) {
								JOptionPane.showMessageDialog(null, "已添加该文件", "提示框", JOptionPane.WARNING_MESSAGE);
								continue;
							}
							
							/**添加到Set集合*/
							set.add(file.getName());
							
							List<String[]> readExcel = ReadExcelTools.getRowValue(file);
							Set<Entry<String,Object>> entrySet = mapData.entrySet();
							Map<String,Object> mapModule = new HashMap<>();
							for (Entry<String,Object> entry : entrySet) {
								for (String[] arrStr : readExcel) {
									if(entry.getValue() instanceof List) {
										if(((List<String>)entry.getValue()).get(1).equals(arrStr[0])) {
											mapModule.put(entry.getKey(), arrStr[1]);
										}
									}
								}
								
							}
							mapModule.put("fileName", file.getName());
							ListModule.add(mapModule);
						}
						Iterator<String> iterator = set.iterator();
						
						StringBuffer stringBuffer = new StringBuffer();
						int i = 0;
						while(iterator.hasNext()) {
							if(i > 0) {
								stringBuffer.append(",");
							}
							stringBuffer.append(iterator.next());
							i++;
						}
						jTextArea.setText(stringBuffer.toString());
						
						logger.info("文件数据读取结束");
					} catch (Exception ex) {
						logger.error("文件上传失败",ex);
						throw new RuntimeException(ex);
					} 
				}
        	}
		});
	}
}
