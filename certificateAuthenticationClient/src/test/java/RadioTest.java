import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class RadioTest extends JFrame {
    public static void main(String[] args) {
        new RadioTest();
    }
 
    public RadioTest() {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JRadioButton button = new JRadioButton("apple");
        JRadioButton button2 = new JRadioButton("banana");
        ButtonGroup group = new ButtonGroup();
        group.add(button);
        group.add(button2);
        JPanel panel = new JPanel();
        panel.add(button);
        panel.add(button2);
        this.add(panel, BorderLayout.NORTH);
        this.setVisible(true);
    }
}