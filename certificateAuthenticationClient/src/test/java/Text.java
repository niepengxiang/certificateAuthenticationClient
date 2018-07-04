import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class Text {
	public static void main(String[] args) {
		JFrame jFrame = new JFrame("文件上传");

		JFileChooser jFileChooser = new JFileChooser();

		jFrame.add(jFileChooser);
		jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jFrame.pack();
		/** 当点击窗口的关闭按钮时退出程序 */
		jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		/** 显示窗口，前面创建的信息都在内存中，通过 jf.setVisible(true) 把内存中的窗口显示在屏幕上 */
		jFrame.setVisible(true);
		
	}

}
