package com.g4b.swing.client;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

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
	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
        /**创建顶层窗口*/
        JFrame jFrame = new JFrame("认证客户端");
        /**设置窗口大小*/
        jFrame.setPreferredSize(new Dimension(1000, 850));
        
        /**设置窗口在屏幕中心*/
        SwingUtils.setScreenCenter(jFrame);
        
        /**创建机构证书用户标题栏*/
        JPanel orgCerdentialTitle = SwingUtils.createTitledBorder(jFrame,ORG_CREDENTIAL_TITLE);
        
        /**获取机构证书属性文件Map集合*/
        Map<String, Object> map = SwingUtils.getMap("orgCdtFiledNames.properties");
		
        /**迭代机构证书属性文件的Value，循环创建Swing组件*/
		Collection<Object> values = map.values();
		for (Object value : values) {
			SwingUtils.createJtextField(orgCerdentialTitle,(List<String>)value);
		}
		/**获去窗格类所有的组件*/
		int count = orgCerdentialTitle.getComponentCount();
		for (int i = 0; i < count; i++) {
			Component component = orgCerdentialTitle.getComponent(i);
			if(component instanceof JTextField) {
				
			}
		}
		
		SwingUtils.createJBotton(jFrame, "提交",null);
        /**创建个人证书标题栏*/
        SwingUtils.createTitledBorder(jFrame,ORG_PERSON_CREDENTIAL_TITLE);
        /**创建CA用户认证标题栏*/
        SwingUtils.createTitledBorder(jFrame,CA_PERSON_TITLE);
        /**创建CA认证证书标题栏*/
        SwingUtils.createTitledBorder(jFrame,CA_CREDENTIAL_TITLE);
        /**创建文件录入标题栏*/
        SwingUtils.createTitledBorder(jFrame,FILE_INPUT_DATA);
        
        jFrame.pack();  
        /**当点击窗口的关闭按钮时退出程序*/
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        /**显示窗口，前面创建的信息都在内存中，通过 jf.setVisible(true) 把内存中的窗口显示在屏幕上*/ 
        jFrame.setVisible(true);
    }
}
