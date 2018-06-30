package cn.eviadc.cloaker;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import cn.eviadc.cloaker.analyzer.filter.CloakerFilter;

public class SwingEntrance extends JFrame implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton okBtn;
	private JLabel artLbl, refLbl, outputLbl;
	private JTextField artTF, refTF, outputTF;
	private JButton artBtn, refBtn, outputBtn;
	private JFileChooser artFC, refFC, outputFC;
	
	private String art, ref, output;
	
	public void start()
	{
		this.setTitle("Cloaker");
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
		
		JPanel p = new JPanel();
		GroupLayout layout = new GroupLayout(p);
		p.setLayout(layout);
		
		okBtn = new JButton();
		okBtn.addActionListener(this);
		okBtn.setText("确认");
		okBtn.setName("okBtn");
		
		artLbl = new JLabel("选择待检测文章：");
		refLbl = new JLabel("选择参考文献路径：");
		outputLbl = new JLabel("输出位置：");
		
		artTF = new JTextField();
		artTF.setEditable(false);
		artTF.setPreferredSize(new Dimension(400, 16));
		
		refTF = new JTextField();
		refTF.setEditable(false);
		refTF.setPreferredSize(new Dimension(400, 16));

		outputTF = new JTextField();
		outputTF.setEditable(false);
		outputTF.setPreferredSize(new Dimension(400, 16));
		
		artBtn = new JButton();
		artBtn.addActionListener(this);
		artBtn.setText("...");
		artBtn.setName("artBtn");
		
		refBtn = new JButton();
		refBtn.addActionListener(this);
		refBtn.setText("...");
		refBtn.setName("refBtn");
		
		outputBtn = new JButton();
		outputBtn.addActionListener(this);
		outputBtn.setText("...");
		outputBtn.setName("outputBtn");
		
		artFC = new JFileChooser();
		artFC.setFileSelectionMode(JFileChooser.FILES_ONLY);
		artFC.setFileFilter(new FileNameExtensionFilter("文本文件(.txt)", "txt"));
		
		refFC = new JFileChooser();
		refFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		outputFC = new JFileChooser();
		outputFC.setFileSelectionMode(JFileChooser.FILES_ONLY);
		outputFC.setFileFilter(new FileNameExtensionFilter("网页文件(.html)", "html"));

		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
			layout.createParallelGroup().addGroup(
				layout.createSequentialGroup().addGroup(
					layout.createParallelGroup()
						.addComponent(artLbl)
						.addComponent(refLbl)
						.addComponent(outputLbl)
				).addGroup(
					layout.createParallelGroup()
						.addComponent(refTF)
						.addComponent(artTF)
						.addComponent(outputTF)
				).addGroup(
					layout.createParallelGroup()
						.addComponent(artBtn)
						.addComponent(refBtn)
						.addComponent(outputBtn)
				)
			).addComponent(okBtn, GroupLayout.Alignment.CENTER)
		);
		
		layout.setVerticalGroup(
			layout.createSequentialGroup().addGroup(
				layout.createParallelGroup()
					.addComponent(artLbl)
					.addComponent(artTF)
					.addComponent(artBtn)
			).addGroup(
				layout.createParallelGroup()
					.addComponent(refTF)
					.addComponent(refLbl)
					.addComponent(refBtn)
			).addGroup(
				layout.createParallelGroup()
					.addComponent(outputTF)
					.addComponent(outputLbl)
					.addComponent(outputBtn)
			).addGap(20).addComponent(okBtn)
		);
		
		this.setContentPane(p);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	

	public static void main(String[] args) 
	{
		SwingEntrance f = new SwingEntrance();
		f.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource().equals(artBtn))
		{
			int res = artFC.showOpenDialog(this);
			if(res == JFileChooser.APPROVE_OPTION)
				artTF.setText(art = artFC.getSelectedFile().getAbsolutePath());
		}
		if(e.getSource().equals(refBtn))
		{
			int res = refFC.showOpenDialog(this);
			if(res == JFileChooser.APPROVE_OPTION)
				refTF.setText(ref = refFC.getSelectedFile().getAbsolutePath());
		}
		if(e.getSource().equals(outputBtn))
		{
			int res = outputFC.showSaveDialog(this);
			if(res == JFileChooser.APPROVE_OPTION)
				outputTF.setText(output = outputFC.getSelectedFile().getAbsolutePath() +
						(outputFC.getSelectedFile().getAbsolutePath().toLowerCase().endsWith(".html")
								? "" :".html"));
		}
		if(e.getSource().equals(okBtn))
		{
			try{
				
				Cloaker cloaker = new Cloaker();
				
				/*
				 * 实例化过滤器，并设置过滤器的相似度阈值。
				 */
				CloakerFilter cf = new CloakerFilter();
				cf.setThreshold(.4f);
				
				/*
				 * 向Cloaker类中添加过滤器cf。
				 */
				cloaker.addFilter(cf);

				if(output == null || output.isEmpty())
					output = "./result.html";

				if(art == null || ref == null || output == null)
					throw new Exception("请填写必需的信息");
					
				/*
				 * 开始使用指定的art（目标文章），ref（文献库）
				 * ，debug（是否为开发模式），output（输出文件
				 * 路径）参数对目标文章进行相似度分析。
				 */
				File o = cloaker.analysis(art, ref, false, output);

				if(JOptionPane.showOptionDialog(this, "分析完成\n是否现在打开生成结果？", "Cloaker",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null)
						== JOptionPane.OK_OPTION)
				{
				    final Runtime runtime = Runtime.getRuntime();  
				    final String cmd = "rundll32 url.dll FileProtocolHandler file://" + o.getAbsolutePath();
				    
				    runtime.exec(cmd);  
				}
			}catch(Exception ex)
			{
				JOptionPane.showMessageDialog(this, ex.toString(), "Cloaker", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
