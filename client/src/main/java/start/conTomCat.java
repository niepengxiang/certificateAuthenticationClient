package start;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class conTomCat {
	public void close() throws IOException {
		Process process = Runtime.getRuntime().exec("F:\\eclipse-workspace\\client\\tomcat\\apache-tomcat-7.0.70\\bin\\shutdown.bat"); // 调用外部程序
		final InputStream in = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		StringBuilder buf = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null)
			buf.append(line);
		System.out.println("输出结果为：" + buf);
	}

	public void start() throws IOException {
		System.out.println("1111111111111111111111111");
		Process process = Runtime.getRuntime().exec("F:\\eclipse-workspace\\client\\tomcat\\apache-tomcat-7.0.70\\bin\\startup.bat"); // 调用外部程序
		final InputStream in = process.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		StringBuilder buf = new StringBuilder();
		String line = null;
		System.out.println("1111111111111111111111111");
		while ((line = br.readLine()) != null)
			buf.append(line);
		System.out.println("输出结果为：" + buf);
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		conTomCat con = new conTomCat();
		con.start();

	}
}