import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Login extends JFrame implements ActionListener {
	JButton login = new JButton("登录");
	JButton exit = new JButton("退出");
	JLabel name = new JLabel("用户名：");
	JLabel password = new JLabel("密码：");
	JTextField JName = new JTextField(10); // 明文账号输入
	JPasswordField JPassword = new JPasswordField(10); // 非明文密码输入；

	public Login() {
		JPanel jp = new JPanel();
		jp.setLayout(new GridLayout(3, 2)); // 3行2列的面板jp（网格布局）

		name.setHorizontalAlignment(SwingConstants.RIGHT); // 设置该组件的对齐方式为向右对齐
		password.setHorizontalAlignment(SwingConstants.RIGHT);

		jp.add(name); // 将内容加到面板jp上
		jp.add(JName);
		jp.add(password);
		jp.add(JPassword);
		jp.add(login);
		jp.add(exit);

		login.addActionListener(this); // 登录增加事件监听
		exit.addActionListener(this); // 退出增加事件监听

		this.add(jp, BorderLayout.CENTER); // 将整块面板定义在中间

		this.setTitle("登录窗口");
		this.setLocation(500, 300); // 设置初始位置
		this.pack(); // 表示随着面板自动调整大小
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) // 对时间进行处理
	{
		if (e.getSource() == exit) {
			int i = JOptionPane.showConfirmDialog(null, "确认要退出吗？", "确认", JOptionPane.YES_NO_OPTION);
			// 显示选择对话框
			if (i == JOptionPane.YES_OPTION)
				;
			System.exit(0);
		} else {
			if (JName.getText().equals("admin") && String.valueOf(JPassword.getPassword()).equals("admin")) {
				JOptionPane.showMessageDialog(null, "登录成功，欢迎到来！");
				// 显示信息提示框
				System.exit(0);
			} else {
				JOptionPane.showMessageDialog(null, "用户或密码错误！请从新登录！");
				// 显示信息提示框
				JName.setText("");
				JPassword.setText("");
			}
		}
	}

	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
		new Login();
	}
}
