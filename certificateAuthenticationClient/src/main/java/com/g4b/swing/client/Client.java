package com.g4b.swing.client;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

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
		
		SwingUtils.addJScrollPane(caCredentialCom, caCredentialTitled);
		
		JButton caCredentialJButton = SwingUtils.createJBotton(caCredentialTitled, "提交",null);
		SendDataUtils.getModuleDataAndSend(caCredentialData, caCredentialCom, caCredentialJButton,httpClientService);
		
		
        /**创建文件录入标题栏*/
		JPanel fileInputCom = new JPanel();
		SwingUtils.createTitledBorder(jTabbedPane,fileInputCom,FILE_INPUT_DATA);
        
		
		jFrame.add(jTabbedPane);
        jFrame.pack();  
        /**当点击窗口的关闭按钮时退出程序*/
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        /**显示窗口，前面创建的信息都在内存中，通过 jf.setVisible(true) 把内存中的窗口显示在屏幕上*/ 
        jFrame.setVisible(true);
    }

	
}
