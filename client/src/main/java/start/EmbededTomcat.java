package start;

import java.io.File;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EmbededTomcat {
	
	public static void main(String[] args) throws Exception{
		new EmbededTomcat().start();
	}
	
	private final Log log = LogFactory.getLog(getClass());
	
	public void start() throws Exception{
		Tomcat tomcat= new Tomcat();
		String projectPath=new File("").getAbsolutePath();
		tomcat.setBaseDir(projectPath); //Embeded tomcat存放路径
		tomcat.setPort(8089);
		try {
			tomcat.addWebapp("", projectPath+"\\src\\main\\webapp");//应用存放路径
		} catch (ServletException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw e;
		}
		try {
			tomcat.start(); // 启动
		} catch (LifecycleException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw e;
		}
		log.info("Tomcat started.");
		tomcat.getServer().await(); // 这个一定要
	}
}