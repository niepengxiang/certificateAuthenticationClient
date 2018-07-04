import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FrameTest {  
    private JFrame mainFrame = null;  
  
    public void launchFrame(final String title) {  
          
        //创建panel，为了便于区分，将panel设置了不同的颜色。  
        JPanel panel1 = new JPanel();  
        panel1.setBackground(Color.DARK_GRAY);  
        JPanel panel2 = new JPanel();  
        panel2.setBackground(Color.CYAN);  
        panel2.setPreferredSize(new Dimension(200, 200));//想将panel 2的大小固定为200×200，但未达到效果  
        JPanel panel3 = new JPanel();  
        panel3.setBackground(Color.LIGHT_GRAY);  
        panel3.setPreferredSize(new Dimension(200, 400));//想将panel 3的宽度固定为200，高度随窗口变化而变化  
  
        mainFrame = new JFrame(title);  
        GridBagLayout gridbag = new GridBagLayout();  
        GridBagConstraints c = new GridBagConstraints();  
        mainFrame.setLayout(gridbag);  
        mainFrame.setSize(800, 600);  
        //panel 1放置在表格(0,0)位置  
        c.gridx = 0;  
        c.gridy = 0;  
        c.gridheight = 2;//占用两行  
        c.gridwidth = 1;//占用一列  
        c.weightx = 1.0;  
        c.weighty = 1.0;  
        c.fill = GridBagConstraints.BOTH;  
        addComponent(mainFrame, panel1, gridbag, c);  
        //panel 2放置在表格(1,0)位置  
        c.gridx = 1;  
        c.gridy = 0;  
        c.gridheight = 1;//占用一行  
        c.weightx = 0.0;//固定大小，无需额外的填充空间  
        c.weighty = 0.0;//固定大小，无需额外的填充空间  
        c.fill = GridBagConstraints.NONE;  
        //panel 3放置在表格(1,1)位置  
        addComponent(mainFrame, panel2, gridbag, c);  
        c.weighty = 1.0;  
        c.gridx = 1;  
        c.gridy = 1;  
        c.fill = GridBagConstraints.VERTICAL;//垂直方向重绘  
        addComponent(mainFrame, panel3, gridbag, c);  
        //mainFrame.pack();  
        mainFrame.setLocationRelativeTo(null);  
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        mainFrame.setVisible(true);  
    }  
  
    private void addComponent(JFrame frame, JComponent comp,  
            GridBagLayout gridbag, GridBagConstraints c) {  
        gridbag.setConstraints(comp, c);  
        frame.add(comp);  
    }  
  
    public static void main(String[] args) {  
        new FrameTest().launchFrame("Test");  
    }  
}  