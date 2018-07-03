package com.g4b.swing.client;

import java.awt.Dimension;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

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
	
	/**属性文件读取*/
	private static final String ORG_CREDENTIAL_TITLE = loadder("ORG_CREDENTIAL_TITLE");
	
	private static final String ORG_PERSON_CREDENTIAL_TITLE = loadder("ORG_PERSON_CREDENTIAL_TITLE");
	
	private static final String CA_PERSON_TITLE = loadder("CA_PERSON_TITLE");
	
	private static final String CA_CREDENTIAL_TITLE = loadder("CA_CREDENTIAL_TITLE");
	
	private static final String FILE_INPUT_DATA = loadder("FILE_INPUT_DATA");
	
	private static final JScrollPane jScrollPane = new JScrollPane();
	
	private static HttpClientService httpClientService = new HttpClientService();
	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
        /**创建顶层窗口*/
        JFrame jFrame = new JFrame("认证客户端");
        /**设置窗口大小*/
        jFrame.setPreferredSize(new Dimension(1024, 600));
        
    	/**定义窗格*/
    	JTabbedPane jTabbedPane = new JTabbedPane();;
    	
        /**设置窗口在屏幕中心*/
        SwingUtils.setScreenCenter(jFrame);
        
        
        /**获取机构证书属性文件Map集合*/
        Map<String, Object> orgCerdentialData = SwingUtils.getMap("orgCdtFiledNames.properties");
		
        /**创建证书机构平面组件*/
        JPanel orgCerdentialComp = new JPanel(false);
        JPanel fileJPanel = new JPanel();
        /**迭代机构证书属性文件的Value，循环创建Swing组件*/
		Collection<Object> values = orgCerdentialData.values();
		for (Object value : values) {
			SwingUtils.createJtextField(orgCerdentialComp,jScrollPane,(List<String>)value);
		}
        
        /**创建机构证书用户标题栏*/
        JPanel orgCerdentialTitle = SwingUtils.createTitledBorder(jTabbedPane, orgCerdentialComp,ORG_CREDENTIAL_TITLE);
        
        orgCerdentialTitle.add(orgCerdentialComp);
        
		JButton orgCerdentialJBotton = SwingUtils.createJBotton(orgCerdentialComp, "提交",null);
		
		/**请求*/
		SendDataUtils.getModuleDataAndSend(orgCerdentialData, orgCerdentialComp, orgCerdentialJBotton,httpClientService);
		
		
		
		
		JPanel orgPersionCredentialCom = new JPanel();
		
		Map<String, Object> orgPersionCredentialData = SwingUtils.getMap("orgPersionCdtFiledName.properties");
	        /**迭代机构证书属性文件的Value，循环创建Swing组件*/
			Collection<Object> opcdValue = orgPersionCredentialData.values();
			for (Object value : opcdValue) {
				SwingUtils.createJtextField(orgPersionCredentialCom,jScrollPane, (List<String>)value);
			}
		/**创建个人证书标题栏*/
		JPanel orgPersionCredentialTitle = SwingUtils.createTitledBorder(jTabbedPane,orgPersionCredentialCom,ORG_PERSON_CREDENTIAL_TITLE);
       
		SwingUtils.createJBotton(orgPersionCredentialCom, "提交",null);
       
		orgPersionCredentialTitle.add(orgPersionCredentialCom);
		
//        /**创建CA用户认证标题栏*/
//       // SwingUtils.createTitledBorder(jTabbedPane,CA_PERSON_TITLE);
//        /**创建CA认证证书标题栏*/
//       // SwingUtils.createTitledBorder(jTabbedPane,CA_CREDENTIAL_TITLE);
//        /**创建文件录入标题栏*/
//       // SwingUtils.createTitledBorder(jTabbedPane,FILE_INPUT_DATA);
        
        
        jFrame.add(jTabbedPane);
        jFrame.pack();  
        /**当点击窗口的关闭按钮时退出程序*/
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        /**显示窗口，前面创建的信息都在内存中，通过 jf.setVisible(true) 把内存中的窗口显示在屏幕上*/ 
        jFrame.setVisible(true);
    }


	
}
