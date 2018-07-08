package start;

import java.io.File;

import org.apache.catalina.startup.Bootstrap;


public class TomcatStart {
	private static final String SRCPATH = TomcatStart.class.getClassLoader().getResource("//").getPath();
	// 创建Bootstarap实例
	Bootstrap bt = new Bootstrap();
	
	// 测试方法

	public static void main(String[] args) throws Exception {
		
		TomcatStart ts = new TomcatStart();
		
		ts.startOrStopTomat("start", args);

	}

	/**
	 * @Title: startOrStopTomat  
	 * @Description: java程序实现tomcat的启动和关闭
	 * @param @param id
	 * @param @param args   
	 */
	@SuppressWarnings("static-access")
	public void startOrStopTomat(String id, String[] args) {

		if (id.equals("start")) {
			File parentFile = new File(SRCPATH).getParentFile().getParentFile();
			String path  = parentFile.getPath() + "\\tomcat\\apache-tomcat-7.0.70\\";
			bt.setCatalinaHome(path);
			bt.main(args);

		}
		if (id.equals("stop") && bt != null) {

			try {

				bt.stopServer();

			} catch (Exception e) {

				e.printStackTrace();

			}

		} else {

			return;

		}

	}

}