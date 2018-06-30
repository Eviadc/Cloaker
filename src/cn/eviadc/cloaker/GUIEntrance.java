package cn.eviadc.cloaker;

import java.awt.Cursor;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import cn.eviadc.cloaker.analyzer.filter.CloakerFilter;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 程序的入口。
 * 
 * @author Shuolin Yang
 *
 */
public class GUIEntrance {

	/*
	 * 声明各个组件。
	 */
	private JFrame frmCloaker;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JFileChooser fileChooser_1;
	private JFileChooser fileChooser_2;
	private JFileChooser fileChooser_3;
	private JButton button;
	private JButton button_1;
	private JButton button_2;
	private JButton button_3;
	private JButton button_4;
	private JButton button_5;
	private JLabel label;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;
	private JLabel lblNote;
	private JCheckBox checkBox;
	
	/*
	 * 声明查重使用的各个参数。
	 */
	private String art;
	private String ref;
	private float thres;
	private String output;
	boolean debug;

	/**
	 * 启动应用程序。
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIEntrance window = new GUIEntrance();
					window.frmCloaker.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 初始化各个参数并创建窗口。
	 */
	public GUIEntrance() {
		String currentPath = System.getProperty("user.dir");
		art = currentPath + "\\assets\\article.txt";
		ref = currentPath + "\\assets\\ref\\";
		thres = .4f;
		output = currentPath + "\\result.html";
		debug = false;
		initialize();
	}

	/**
	 * 初始化窗口的内容。
	 */
	private void initialize() {
		frmCloaker = new JFrame();
		frmCloaker.setResizable(false);
		frmCloaker.setTitle("Cloaker");
		frmCloaker.setBounds(100, 100, 593, 248);
		frmCloaker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCloaker.getContentPane().setLayout(null);
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			System.out.println("系统不支持的外观。");
			e.printStackTrace();
		}
		
		fileChooser_1 = new JFileChooser("打开");
		fileChooser_1.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser_1.setFileFilter(new FileNameExtensionFilter("文本文件(.txt)", "txt"));
		fileChooser_2 = new JFileChooser("打开");
		fileChooser_2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser_3 = new JFileChooser("打开");
		fileChooser_3.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser_3.setFileFilter(new FileNameExtensionFilter("网页文件(.html)", "html"));
		
		button = new JButton("关于");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				GUIAbout aboutWindow = new GUIAbout();
				aboutWindow.show();
			}
		});
		button.setBounds(460, 170, 113, 27);
		frmCloaker.getContentPane().add(button);
		
		button_1 = new JButton("开始查重");
		button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frmCloaker.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
				/*
				 * 使用用户设置的参数内容。
				 */
				art = textField_1.getText();
				ref = textField_2.getText();
				output = textField_3.getText();
				
				/*
				 * 实例化Cloaker，此对象将被用来控制整个
				 * 程序的运行。
				 */
				Cloaker cloaker = new Cloaker();
				
				/*
				 * 实例化过滤器，并设置过滤器的相似度阈值。
				 */
				CloakerFilter cf = new CloakerFilter();
				cf.setThreshold(thres);
				
				/*
				 * 向Cloaker类中添加过滤器cf。
				 */
				cloaker.addFilter(cf);
				
				/*
				 * 开始使用指定的art（目标文章），ref（文献库）
				 * ，debug（是否为开发模式），output（输出文件
				 * 路径）参数对目标文章进行相似度分析。
				 */
				File o = null;
				try
				{
					o = cloaker.analysis(art, ref, debug, output);
				
					frmCloaker.setCursor(null);
					
					if(JOptionPane.showOptionDialog(frmCloaker, "分析完成\n是否现在打开生成结果？", "Cloaker",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null)
							== JOptionPane.OK_OPTION)
					{
					    final Runtime runtime = Runtime.getRuntime();  
					    final String cmd = "rundll32 url.dll FileProtocolHandler file://" + o.getAbsolutePath();
						
						runtime.exec(cmd);
					}
				} catch(Exception e1) {
					e1.printStackTrace();
				}
				
				if(debug)
					return ;
			}
		});
		button_1.setBounds(14, 130, 559, 27);
		frmCloaker.getContentPane().add(button_1);
		
		label = new JLabel("阈值");
		label.setBounds(114, 174, 72, 18);
		frmCloaker.getContentPane().add(label);
		
		lblNote = new JLabel("");
		lblNote.setBounds(292, 171, 72, 18);
		frmCloaker.getContentPane().add(lblNote);
		
		textField = new JTextField();
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				lblNote.setText("");
			}
		});
		textField.setBounds(14, 171, 86, 24);
		frmCloaker.getContentPane().add(textField);
		textField.setColumns(10);
		
		button_2 = new JButton("设置阈值");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				thres = Float.parseFloat(textField.getText());
				lblNote.setText("成功");
			}
		});
		button_2.setBounds(165, 170, 113, 27);
		frmCloaker.getContentPane().add(button_2);
		
		textField_1 = new JTextField();
		textField_1.setBounds(114, 13, 388, 24);
		frmCloaker.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		label_1 = new JLabel("待查文献路径");
		label_1.setBounds(14, 16, 100, 18);
		frmCloaker.getContentPane().add(label_1);
		
		label_2 = new JLabel("文献库路径");
		label_2.setBounds(14, 53, 86, 18);
		frmCloaker.getContentPane().add(label_2);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(114, 50, 388, 24);
		frmCloaker.getContentPane().add(textField_2);
		
		label_3 = new JLabel("结果保存路径");
		label_3.setBounds(14, 90, 100, 18);
		frmCloaker.getContentPane().add(label_3);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(114, 87, 388, 24);
		frmCloaker.getContentPane().add(textField_3);
		
		checkBox = new JCheckBox("调试模式");
		checkBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				debug = !debug;
			}
		});
		checkBox.setBounds(350, 170, 96, 27);
		frmCloaker.getContentPane().add(checkBox);
		
		textField_1.setText(art);
		textField_2.setText(ref);
		textField_3.setText(output);
		textField.setText(String.valueOf(thres));
		
		button_3 = new JButton("...");
		button_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (fileChooser_1.showOpenDialog(frmCloaker) 
						== JFileChooser.APPROVE_OPTION) {
					textField_1.setText(fileChooser_1.getSelectedFile().getAbsolutePath());
				}
			}
		});
		button_3.setBounds(516, 12, 57, 25);
		frmCloaker.getContentPane().add(button_3);
		
		button_4 = new JButton("...");
		button_4.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (fileChooser_2.showOpenDialog(frmCloaker) 
						== JFileChooser.APPROVE_OPTION) {
					textField_2.setText(fileChooser_2.getSelectedFile().getAbsolutePath());
				}
			}
		});
		button_4.setBounds(516, 49, 57, 25);
		frmCloaker.getContentPane().add(button_4);
		
		button_5 = new JButton("...");
		button_5.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (fileChooser_3.showOpenDialog(frmCloaker) 
						== JFileChooser.APPROVE_OPTION) {
					textField_3.setText(fileChooser_3.getSelectedFile().getAbsolutePath());
				}
			}
		});
		button_5.setBounds(516, 86, 57, 25);
		frmCloaker.getContentPane().add(button_5);
	}
}
