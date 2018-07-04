import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class LoginDemo extends JFrame {

	// 声明组件
	private JPanel p;
	private JLabel lbName, lblPwd, lbRePwd, lbAddress, lbIMsg;

	// 声明文本框
	private JTextField txtName;
	// 声明两个密码框
	private JPasswordField txtPwd, txtRePwd;
	// 声明一个文本域
	private JTextArea txtAddress;
	private JButton btnReg, btnCancel;

	public LoginDemo() {

		super("注册新用户");
		// 创建面板，面板布局为NULL
		p = new JPanel(null);
		// 实例化5个标签
		lbName = new JLabel("用户名");
		lblPwd = new JLabel("密   码");
		lbRePwd = new JLabel("确认密码");
		lbAddress = new JLabel("地址");
		// 显示信息的标签
		lbIMsg = new JLabel();
		// 设置标签的文字是红色
		lbIMsg.setForeground(Color.RED);
		// 创建一个长度为20 的文本框
		txtName = new JTextField(20);
		// 创建两个密码框长度为20
		txtPwd = new JPasswordField(20);
		txtRePwd = new JPasswordField(20);
		// 设置密码框显示的字符为*
		txtPwd.setEchoChar('*');
		txtRePwd.setEchoChar('*');
		// 创建一个文本域 20,2
		txtAddress = new JTextArea(20, 2);
		// 创建两个按钮
		btnReg = new JButton("注册");
		btnCancel = new JButton("清空");

		// 注册监听
		btnReg.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 设置信息标签为空 清楚原来的历史信息
				lbIMsg.setText("");
				// 获取用户输入的用户名
				String strName = txtName.getText();
				if (strName == null || strName.equals("")) {

					lbIMsg.setText("用户名不能为空");
					return;
				}
				// 获取用户名密码
				String strPwd = new String(txtPwd.getPassword());
				if (strPwd == null || strPwd.equals("")) {

					lbIMsg.setText("密码不能为空");
					return;
				}
				String strRePwd = new String(txtRePwd.getPassword());
				if (strRePwd == null || strRePwd.equals("")) {

					lbIMsg.setText("确认密码不能为空");
					return;
				}

				// 判断确认密码是否跟密码相同
				if (!strRePwd.equals(strPwd)) {

					lbIMsg.setText("确认密码跟密码不同");
					return;
				}

				// 获取用户地址
				String strAddress = new String(txtAddress.getText());
				if (strAddress == null || strAddress.equals("")) {

					lbIMsg.setText("地址不能为空");
					return;
				}
				lbIMsg.setText("注册成功");

			}
		});

		// 取消按钮的事件处理
		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 清空所有文本信息
				txtName.setText("");
				txtPwd.setText("");
				txtRePwd.setText("");
				txtAddress.setText("");
				// 设置信息标签为空
				lbIMsg.setText("");

			}
		});

		lbName.setBounds(30, 30, 60, 25);
		txtName.setBounds(95, 30, 120, 25);
		lblPwd.setBounds(30, 60, 60, 25);
		txtPwd.setBounds(95, 60, 120, 25);
		lbRePwd.setBounds(30, 90, 60, 25);
		txtRePwd.setBounds(95, 90, 120, 25);
		lbAddress.setBounds(30, 120, 60, 25);
		txtAddress.setBounds(95, 120, 120, 25);
		lbIMsg.setBounds(60, 185, 180, 25);
		btnReg.setBounds(60, 215, 60, 25);
		btnCancel.setBounds(125, 215, 60, 25);

		// 添加所有组件
		p.add(lbName);
		p.add(txtName);
		p.add(txtPwd);
		p.add(lblPwd);
		p.add(txtRePwd);
		p.add(lbRePwd);
		p.add(txtAddress);
		p.add(lbAddress);
		p.add(lbIMsg);
		p.add(btnReg);
		p.add(btnCancel);

		this.add(p);
		this.setSize(1000, 800);
		this.setLocation(200, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new LoginDemo();
	}
}