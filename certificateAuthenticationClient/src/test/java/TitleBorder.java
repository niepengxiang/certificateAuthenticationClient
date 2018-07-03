import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class TitleBorder {

	private static void addCompForBorder(String lable, Container container) {
		JPanel comp = new JPanel(false);
		JTextField jTextField = new JTextField(lable);
		comp.add(jTextField);
		container.add(comp);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(500, 500));

		JPanel simoleTitleBorder = new JPanel();
		
		JPanel comp = new JPanel(false);
		JTextField jTextField = new JTextField("title Border with line Border");
		comp.add(jTextField);
		simoleTitleBorder.add(comp);
		

		JPanel customTitleBorder = new JPanel();
		JPanel com = new JPanel(false);
		
		JTextField TextField = new JTextField("title Border  line Border");
		com.add(TextField);
		customTitleBorder.add(com);

		JTabbedPane jTabbedPane = new JTabbedPane();
		jTabbedPane.setToolTipText("simple");
		jTabbedPane.addTab("simpleTitleBorde", simoleTitleBorder);
		jTabbedPane.addTab("customTitleBorde", customTitleBorder);
		frame.add(jTabbedPane);
		// this.getContentPane().add(jTabbedPane);
		frame.pack();
		frame.setVisible(true);
	}
}
