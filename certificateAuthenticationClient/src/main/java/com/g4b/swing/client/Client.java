package com.g4b.swing.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;

import com.g4b.swing.client.utils.HttpClientService;
import com.g4b.swing.client.utils.SendDataUtils;
import com.g4b.swing.client.utils.SwingUtils;
/***
 * @ClassName: Client  
 * @Description: TODO 客户单入口  
 * @author niepengxiang
 * @date 2018年6月29日  
 *
 */
public class Client extends ConfigProperties {
	
	private static Logger logger = Logger.getLogger(Client.class);
	
	/**属性文件读取*/
	private static final String ORG_CREDENTIAL_TITLE = loadder("ORG_CREDENTIAL_TITLE");
	
	private static final String ORG_PERSON_CREDENTIAL_TITLE = loadder("ORG_PERSON_CREDENTIAL_TITLE");
	
	private static final String CA_PERSON_TITLE = loadder("CA_PERSON_TITLE");
	
	private static final String CA_CREDENTIAL_TITLE = loadder("CA_CREDENTIAL_TITLE");
	
	private static final String FILE_INPUT_DATA = loadder("FILE_INPUT_DATA");
	
	private static HttpClientService httpClientService = new HttpClientService();
	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
        /**创建顶层窗口*/
        JFrame jFrame = new JFrame("认证客户端");
        /**设置窗口大小*/
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
    	/**定义窗格*/
    	JTabbedPane jTabbedPane = new JTabbedPane();;
    	
        /**设置窗口在屏幕中心*/
        SwingUtils.setScreenCenter(jFrame);
        
        logger.info("创建机构证书认证表单开始");
        
        /**获取机构证书属性文件Map集合*/
        Map<String, Object> orgCerdentialData = SwingUtils.getMap("orgCdtFiledNames.properties");
		
        /**创建证书机构平面组件*/
        JPanel orgCerdentialComp = new JPanel(false);
        
        /**创建机构证书用户标题栏*/
        JPanel orgCerdentialTitle = SwingUtils.createTitledBorder(jTabbedPane, orgCerdentialComp,ORG_CREDENTIAL_TITLE);
        
        
        /**迭代机构证书属性文件的Value，循环创建Swing组件*/
		Collection<Object> values = orgCerdentialData.values();
		for (Object value : values) {
			SwingUtils.createJtextField(orgCerdentialComp,(List<String>)value);
		}
		
        
        SwingUtils.addJScrollPane(orgCerdentialComp, orgCerdentialTitle);
        
		JButton orgCerdentialJBotton = SwingUtils.createJBotton(orgCerdentialTitle, "提交",null);
		logger.info("创建机构证书认证表单结束");
		
		
		/**提交机构证书按钮请求*/
		SendDataUtils.getModuleDataAndSend(orgCerdentialData, orgCerdentialComp, orgCerdentialJBotton,httpClientService);
		
		
		logger.info("创建个人非机构证书认证表单开始");
		 /**获取个人非机构证书属性文件Map集合*/
		Map<String, Object> orgPersionCredentialData = SwingUtils.getMap("orgPersionCdtFiledName.properties");
		JPanel orgPersionCredentialCom = new JPanel(false);
	       
		/**创建个人证书标题栏*/
		JPanel orgPersionCredentialTitle = SwingUtils.createTitledBorder(jTabbedPane,orgPersionCredentialCom,ORG_PERSON_CREDENTIAL_TITLE);
		
		/**迭代机构证书属性文件的Value，循环创建Swing组件*/
		Collection<Object> opcdValue = orgPersionCredentialData.values();
		for (Object value : opcdValue) {
				SwingUtils.createJtextField(orgPersionCredentialCom, (List<String>)value);
		}
       
		SwingUtils.addJScrollPane(orgPersionCredentialCom, orgPersionCredentialTitle);
		
		JButton orgPersionCredentialJBotton = SwingUtils.createJBotton(orgPersionCredentialTitle, "提交",null);
		logger.info("创建个人非机构证书认证表单结束");
		
		/**提交个人非机构按钮请求*/
		SendDataUtils.getModuleDataAndSend(orgPersionCredentialData, orgPersionCredentialCom, orgPersionCredentialJBotton,httpClientService);
		
		
		
		
        logger.info("创建CA用户认证表单开始");
        /**获取CA用户属性文件Map集合*/
		Map<String, Object> caPersonData = SwingUtils.getMap("caPersonFiledNames.properties");
		JPanel caPersonCom = new JPanel(false);
	       
		 /**创建CA用户认证标题栏*/
        JPanel caPersonTitled = SwingUtils.createTitledBorder(jTabbedPane,caPersonCom,CA_PERSON_TITLE);
		
		/**迭代CA用户属性文件的Value，循环创建Swing组件*/
		Collection<Object> caPersonValue = caPersonData.values();
		int caPersionIndex = 1;
		for (Object value : caPersonValue) {
				SwingUtils.createJtextField(caPersonCom, (List<String>)value,caPersionIndex);
				caPersionIndex ++;
		}
       
		SwingUtils.addJScrollPane(caPersonCom, caPersonTitled);
		
		JButton caPersonJBotton = SwingUtils.createJBotton(caPersonTitled, "提交",null);
		logger.info("创建CA用户认证表单结束");
       
		/**提交个人非机构按钮请求*/
		SendDataUtils.getModuleDataAndSend(caPersonData, caPersonCom, caPersonJBotton,httpClientService);
		
       
		
		 /**获取CA证书属性文件Map集合*/
		Map<String, Object> caCredentialData = SwingUtils.getMap("caCredentialFiledNames.properties");
		
		JPanel caCredentialCom = new JPanel(false);
        /**创建CA认证证书标题栏*/
		JPanel caCredentialTitled = SwingUtils.createTitledBorder(jTabbedPane,caCredentialCom,CA_CREDENTIAL_TITLE);
		/**迭代CA证书属性文件的Value，循环创建Swing组件*/
		Collection<Object> caCredentialValues = caCredentialData.values();
		int caCredentialIndex = 1;
		for (Object value : caCredentialValues) {
				SwingUtils.createJtextField(caCredentialCom, (List<String>)value,caCredentialIndex);
				caCredentialIndex ++;
		}
		
		/**创建垂直滚动条，在需要的时候的显示*/
		SwingUtils.addJScrollPane(caCredentialCom, caCredentialTitled);
		
		/**创建提交按钮*/
		JButton caCredentialJButton = SwingUtils.createJBotton(caCredentialTitled, "提交",null);
		
		/**发送请求*/
		SendDataUtils.getModuleDataAndSend(caCredentialData, caCredentialCom, caCredentialJButton,httpClientService);
		
		
        /**创建文件录入标题栏*/
		JPanel fileInputCom = new JPanel();
		JPanel filedInputTitled = SwingUtils.createTitledBorder(jTabbedPane,fileInputCom,FILE_INPUT_DATA);
        Map<String, Object> fileInputData = SwingUtils.getMap("fileInputFiledNames.properties");
		fileInputCom.setLayout(null);
		/**创建表单域*/
        JTextArea orgCdtJtextArea = new JTextArea();
        JButton orgCdtFiledButton = new JButton((String)fileInputData.get("orgCdtFiledButton"));
        JTextArea orgPersionJtextArea = new JTextArea();
        JButton orgPersionCdtButton = new JButton((String)fileInputData.get("orgPersionCdtButton"));
        JTextArea caPersionJtextArea = new JTextArea();
        JButton caPersionButtion = new JButton((String)fileInputData.get("caPersionButtion"));
        JTextArea caCredentialJtextArea = new JTextArea();
        JButton caCredentialFiledButton = new JButton((String)fileInputData.get("caCredentialFiledButton"));
		
        /**设置文件上传表单的组件的属性*/
        setFileInputFormModuleProperties(fileInputCom, orgCdtJtextArea, orgCdtFiledButton, orgPersionJtextArea,
				orgPersionCdtButton, caPersionJtextArea, caPersionButtion, caCredentialJtextArea,
				caCredentialFiledButton);
        
        /**为文件表单按钮组件添加点击事件*/
        orgCdtFiledButton.addMouseListener(new MouseAdapter() {
        	
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		JFileChooser jFileChooser = new JFileChooser();
        		jFileChooser.setMultiSelectionEnabled(true);
				int dialog = jFileChooser.showOpenDialog(jFrame);
				if(dialog == JFileChooser.APPROVE_OPTION) {
					/** 得到选择的文件* */
					File[] arrfiles = jFileChooser.getSelectedFiles();
					if (arrfiles == null || arrfiles.length == 0) {
						return;
					}
					FileInputStream input = null;
					try {
						for (File f : arrfiles) {
							/** 目标文件夹 **/
							HashSet<String> set = new HashSet<String>();
							/** 判断是否已有该文件* */
							if (set.contains(f.getName())) {
								JOptionPane.showMessageDialog(new JDialog(), "已有该文件！");
								return;
							}
							input = new FileInputStream(f);
						}
					} catch (Exception ex) {
						logger.error("文件上传失败",ex);
					} 
				}
        	}
        	
		});
        
        
        
        
        
        orgPersionCdtButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						
					}
				});
        caPersionButtion.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
        caCredentialFiledButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        /**创建垂直滚动条，在需要的时候的显示*/
		SwingUtils.addJScrollPane(fileInputCom, filedInputTitled);
        
		/**创建提交按钮*/
		JButton fileInputJButton = SwingUtils.createJBotton(filedInputTitled, "提交",null);
        
		jFrame.add(jTabbedPane);
        jFrame.pack();  
        /**当点击窗口的关闭按钮时退出程序*/
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        /**显示窗口，前面创建的信息都在内存中，通过 jf.setVisible(true) 把内存中的窗口显示在屏幕上*/ 
        jFrame.setVisible(true);
    }


	/**
	 * @Title: setFileInputFormModuleProperties  
	 * @Description: TODO 设置文件上传表单的组件的属性
	 * @param  fileInputCom 			文件上传表单平面组件
	 * @param  orgCdtJtextArea			机构证书录入文件名称显示框
	 * @param  orgCdtFiledButton		机构证书录入文件上传按钮
	 * @param  orgPersionJtextArea		个人非机构录入文件名称显示框
	 * @param  orgPersionCdtButton		个人非机构录入文件上传按钮
	 * @param  caPersionJtextArea		CA用户录入文件名称显示框
	 * @param  caPersionButtion			CA用户录入上传按钮
	 * @param  caCredentialJtextArea	CA证书链录入文件显示框
	 * @param  caCredentialFiledButton  CA证书链录入文件上传按钮
	 * @return void 
	 */
	private static void setFileInputFormModuleProperties(JPanel fileInputCom, JTextArea orgCdtJtextArea,
			JButton orgCdtFiledButton, JTextArea orgPersionJtextArea, JButton orgPersionCdtButton,
			JTextArea caPersionJtextArea, JButton caPersionButtion, JTextArea caCredentialJtextArea,
			JButton caCredentialFiledButton) {
		/**设置坐标*/
        orgCdtJtextArea.setBounds(30, 30, 400, 25);
        orgCdtFiledButton.setBounds(530, 30, 180, 25);
        
        orgPersionJtextArea.setBounds(30, 60, 400, 25);
        orgPersionCdtButton.setBounds(530, 60, 180, 25);
        
        caPersionJtextArea.setBounds(30, 90, 400, 25);
        caPersionButtion.setBounds(530, 90, 180, 25);
        
        caCredentialJtextArea.setBounds(30, 120, 400, 25);
        caCredentialFiledButton.setBounds(530, 120, 180, 25);
        
        orgCdtJtextArea.setLineWrap(true);
        orgCdtJtextArea.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));
        
        orgPersionJtextArea.setLineWrap(true);
        orgPersionJtextArea.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));
        
        caPersionJtextArea.setLineWrap(true);
        caPersionJtextArea.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));
        
        caCredentialJtextArea.setLineWrap(true);
        caCredentialJtextArea.setBorder(new LineBorder(new java.awt.Color(127,157,185), 1, false));
        /**添加组件*/
        fileInputCom.add(orgCdtJtextArea);
        fileInputCom.add(orgCdtFiledButton);
        fileInputCom.add(orgPersionJtextArea);
        fileInputCom.add(orgPersionCdtButton);
        fileInputCom.add(caPersionJtextArea);
        fileInputCom.add(caPersionButtion);
        fileInputCom.add(caCredentialJtextArea);
        fileInputCom.add(caCredentialFiledButton);
	}

	
}
