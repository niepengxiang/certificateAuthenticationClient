package client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

public class SimpleWebBrowserExample extends JPanel {
	/**
	 * @Fields field:field:{todo}(用一句话描述这个变量表示什么)
	 */

	private static final long serialVersionUID = 2318287328168699184L;

	private JPanel webBrowserPanel;

	private JWebBrowser webBrowser;

	@SuppressWarnings("unused")
	private String url;

	public SimpleWebBrowserExample(String url) {
		super(new BorderLayout());
		this.url = url;
		webBrowserPanel = new JPanel(new BorderLayout());
		webBrowser = new JWebBrowser();
		webBrowser.navigate(url);
		webBrowser.setButtonBarVisible(false);
		webBrowser.setMenuBarVisible(false);
		webBrowser.setBarsVisible(false);
		webBrowser.setStatusBarVisible(false);
		webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
		add(webBrowserPanel, BorderLayout.CENTER);
		// webBrowser.executeJavascript("javascrpit:window.location.href='http://www.baidu.com'");
		//webBrowser.executeJavascript("alert('haha')"); // 执行Js代码
	}

	public static void main(String[] args) {
		final String url = "http://www.baidu.com";
		final String title = "DJ NativeSwiting Test";
		UIUtils.setPreferredLookAndFeel();
		NativeInterface.open();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame(title);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().add(new SimpleWebBrowserExample(url), BorderLayout.CENTER);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.setLocationByPlatform(true);
				frame.setVisible(true);
			}
		});
		NativeInterface.runEventPump();
	}

}