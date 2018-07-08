package start;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class EntityWebServer {
	private static Log logger = LogFactory.getLog(EntityWebServer.class);

	public static void main(String[] args) throws InterruptedException, IOException {

		startTomcat();

		System.out.println("EntityWebServer is running!");
		logger.info("EntityWebServer is running!");

		synchronized (EntityWebServer.class) {
			try {
				EntityWebServer.class.wait();
			} catch (Throwable e) {
				System.exit(1);
			}
		}

	}

	private static void startTomcat() throws IOException {
		Properties config = PropertiesLoaderUtils.loadAllProperties("config.properties");
		int port = Integer.parseInt(getProperty(config, "ENTITY.WEB.PORT"));
		String context = getProperty(config, "ENTITY.WEB.CONTEXT");
		if (!context.startsWith("/")) {
			context = "/" + context;
		}
		String webapp = new File(getProperty(config, "ENTITY.WEB.APP")).getAbsolutePath();
		new TomcatHttpServer.Builder().setPort(port).setContext(context).setWebapp(webapp).build().startup();
	}

	private static String getProperty(Properties config, String propertyName) {
		String propertyValue = config.getProperty(propertyName);
		if (StringUtils.isBlank(propertyValue)) {
			throw new IllegalArgumentException("property: " + propertyName + " not set!");
		}
		return propertyValue;
	}
}
