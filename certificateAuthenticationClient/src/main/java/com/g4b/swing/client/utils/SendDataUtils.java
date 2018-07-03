package com.g4b.swing.client.utils;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
/**
 * @ClassName: SendDataUtils  
 * @Description: TODO 发送数据的工具类  
 * @author niepengxiang
 * @date 2018年7月3日  
 *
 */
public class SendDataUtils {
	
	/**定义操作的常量*/
	private static final String REQOPT = "reqOpt";
	
	private static final String ADD = "i";
	
	private static final String UPDATE = "u";
	
	private static final String DELETE = "d";
	
	private static Logger logger = Logger.getLogger(SendDataUtils.class);
	/***
	 * @Title: getModuleDataAndSend  
	 * @Description: TODO 点击提交按钮获取数据并发送请求  
	 * @param @param mapData    		读取属性文件的Map集合
	 * @param @param jpanl				平面组件
	 * @param @param jBotton			按钮组件
	 * @param @param httpClientService  发送请求服务
	 * @return void  
	 */
	public static void getModuleDataAndSend(Map<String, Object> mapData, JPanel jpanl, JButton jBotton,
			HttpClientService httpClientService) {
		/** 机构证书认证提交按钮 */
		jBotton.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent event) {
				
				List<String> jTextList = new ArrayList<>();
				List<String> jRadioList = new ArrayList<>();
				List<String> jComboBoxList = new ArrayList<>();  
				Map<String, Object> moduleData = new HashMap<>();
				Set<String> keySet = mapData.keySet();
				/** 获去窗格类所有的组件 */
				int count = jpanl.getComponentCount();
				for (int i = 0; i < count; i++) {
					Component component = jpanl.getComponent(i);

					/** 判断类型是否是文本框 */
					if (component instanceof JTextField) {
						JTextField jTextField = (JTextField) component;
						String text = jTextField.getText().trim();
						jTextList.add(text);
					}

					/** 判断是否是平面组件 */
					if (component instanceof JPanel) {
						JPanel jRadioPanel = (JPanel) component;
						int componentCount = jRadioPanel.getComponentCount();
						for (int j = 0; j < componentCount; j++) {
							Component comp = jRadioPanel.getComponent(j);
							/** 判断是否是单选框组件 */
							if (comp instanceof JRadioButton) {
								JRadioButton jRadioButton = (JRadioButton) comp;
								/** 判断是否选中 */
								if (jRadioButton.isSelected()) {
									String text = jRadioButton.getText();
									jRadioList.add(text);
								}
							}
						}
					}
					
					/**判断下拉组件*/
					if (component instanceof JComboBox) {
						JComboBox<String> jComboBox = (JComboBox<String>) component;
						String item = (String) jComboBox.getSelectedItem();
						jComboBoxList.add(item);
					}
				}

				/** 定义索引 */
				int jTextIndex = 0;
				int jRadioIndex = 0;
				int jComboBoxIndex = 0;
				try {
					logger.info("封装请求参数");
					/** 迭代所有的key */
					for (String key : keySet) {
						List<String> temp = (List<String>) mapData.get(key);
						/** 判断Jtext */
						if (temp.get(0).equals("0")) {
							moduleData.put(key, jTextList.get(jTextIndex));
							jTextIndex++;
						}
	
						/** 判断JRadio */
						if (temp.get(0).equals("1")) {
							if (jRadioIndex < jRadioList.size()) {
								moduleData.put(key, jRadioList.get(jRadioIndex));
								jRadioIndex++;
							} else {
								moduleData.put(key, "");
							}
						}
	
						/** 判断JComboBox */
						if (temp.get(0).equals("2")) {
							moduleData.put(key, jComboBoxList.get(jComboBoxIndex));
							jComboBoxIndex++;
						}
					}
				}catch (Exception ex) {
					logger.error("封装请求参数失败",ex);
				}
				/**验证必填项，有一项未填都不发送请求*/
				boolean verifyRequired = SwingUtils.verifyRequired(mapData, moduleData);
				
				if(verifyRequired) {
					
					/**获取操作项*/
					String value = (String)moduleData.get(REQOPT);
					if(value.equals(ADD)) {
						
						/**发送Post请求*/
						httpClientService.sendPost("", moduleData, true);
					}
					
					if(value.equals(UPDATE)) {
						
						/**发送Get请求*/
						httpClientService.sendPut("", moduleData, true);			
					}
					
					if(value.equals(DELETE)) {
						
						/**发送Delete请求*/
						httpClientService.sendDelete("", moduleData);
					}
				}
			}
		});
	}
}
