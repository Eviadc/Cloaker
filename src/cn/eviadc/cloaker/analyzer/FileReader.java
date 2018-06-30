package cn.eviadc.cloaker.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import cn.eviadc.cloaker.Article;

/**
 * 文件读取模块，读取设定的文件的内容并将其存储为传入文章
 * 对象的rawContent。
 * <p>
 * 读取指定文件的内容并将其存入传到Analyze方法的Article对象中
 * 作为文章尚未经过处理的内容。
 * <p>
 * 私有数据：<br>
 * File file 欲读取内容的目标文件对象。
 * 
 * @author Nathan Wang
 *
 */
public class FileReader implements PaperAnalyzer 
{
	private File file;

	/**
	 * 使用文件路径字符串设置欲读取的文件。
	 * <p>
	 * setFile方法的重载，通过创建文件对象并传入setFile方法
	 * 设置欲读取的文件。
	 * 
	 * @param filename 目标文件路径。
	 */
	public void setFile(String filename)
	{
		this.setFile(new File(filename));
	}
	
	/**
	 * 使用File对象设置欲读取的文件。
	 * 
	 * @param f 目标文件对象。
	 */
	public void setFile(File f)
	{
		file = f;
	}
	
	/**
	 * 文件读取模块的主要功能，调用此方法来读取文件内容
	 * 并存储到文章对象中。
	 * <p>
	 * 设置了目标文件后调用此方法，将读取目标文件内容
	 * 并将其存入参数a的rawContent中。
	 * 
	 * @param a 需要处理的目标文章（Article）对象。
	 * @throws Exception 自定义的“参数错误异常”。
	 * @see cn.eviadc.cloaker.analyzer.PaperAnalyzer#Analyze(java.lang.Object)
	 */
	@Override
	public void Analyze(Object a) throws Exception 
	{
		/*
		 * 判断传入参数是否为Article类的对象，若不是则抛出一个
		 * 自定义“错误参数异常”。
		 */
		if(a.getClass().getName() != Article.class.getName())
			throw new WrongArgumentException(Article.class.getName(), a.getClass().getName());
		
		Article art = (Article)a;
		
		/*
		 * 逐行读取文件内容。
		 */
	    BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
        String buffer = null, ans = "";
        while ((buffer = reader.readLine()) != null)
        	ans += buffer + "\n";
        
        /*
         * 将文件名设置为文章标题。
         */
        art.setTitle(file.getName());
        
        /*
         * 将文件内容存入文章的rawContent中并关闭文件。
         */
        art.setRawContent(ans);
        reader.close();
	}

	/**
	 * 返回此文件读取模块Analyze方法所需要的参数类型名。
	 * 
	 * @return Analyze方法所需要的参数类型名。
	 * @see cn.eviadc.cloaker.analyzer.PaperAnalyzer#targetClassName()
	 */
	@Override
	public String targetClassName() 
	{
		return Article.class.getName();
	}

}
