package cn.eviadc.cloaker;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class GUIAbout {

	private JFrame frmcloaker;

	/**
	 * Create the application.
	 */
	public GUIAbout() {
		initialize();
	}
	
	public void show() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIAbout window = new GUIAbout();
					window.frmcloaker.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmcloaker = new JFrame();
		frmcloaker.setResizable(false);
		frmcloaker.setTitle("关于Cloaker");
		frmcloaker.setBounds(100, 100, 450, 211);
		frmcloaker.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frmcloaker.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Cloaker是一个使用Java编写的可以本地运行的文章查重");
		lblNewLabel.setBounds(14, 13, 404, 25);
		frmcloaker.getContentPane().add(lblNewLabel);
		
		JLabel label = new JLabel("开发工具，若您有论文或其他文档查重的需求，就可以使");
		label.setBounds(14, 37, 404, 25);
		frmcloaker.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("用本工具进行简单的查重自测。使用本工具进行查重或开");
		label_1.setBounds(14, 62, 404, 25);
		frmcloaker.getContentPane().add(label_1);
		
		JLabel label_2 = new JLabel("发，您可以获得您的论文与文献库中文档的总体相似度、");
		label_2.setBounds(14, 85, 404, 25);
		frmcloaker.getContentPane().add(label_2);
		
		JLabel label_3 = new JLabel("论文中与文献库相似度较高的语句、被您的文章所引用的");
		label_3.setBounds(14, 111, 404, 25);
		frmcloaker.getContentPane().add(label_3);
		
		JLabel lblhtml = new JLabel("文章和语句等信息，它们会被显示在一个HTML页面中。");
		lblhtml.setBounds(14, 134, 404, 25);
		frmcloaker.getContentPane().add(lblhtml);
	}

}
