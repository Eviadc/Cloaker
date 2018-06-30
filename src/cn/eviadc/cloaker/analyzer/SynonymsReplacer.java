package cn.eviadc.cloaker.analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cn.eviadc.cloaker.Entry;

/**
 * 同义词替换模块，用于
 * 
 * 私有数据：
 * String 							dic 		同义词字典文件名。<br>
 * String 							regex 		字典中同义词分隔符。<br>
 * ArrayList&lt;String&gt; 			standards 	标准词列表。<br>
 * HashMap&lt;String, Integer&gt; 	map			
 * 
 * @author Nathan Wang
 *
 */
public class SynonymsReplacer implements PaperAnalyzer
{
	private static String dic = "Synonyms.dic";
	private static String regex = " ";
	private ArrayList<String> standards;
	private HashMap<String, Integer> map;
	
	/**
	 * 构造函数，读入并处理字典文件内容。
	 */
	public SynonymsReplacer()
	{
		standards = new ArrayList<String>();
		map = new HashMap<String, Integer>();
		File file = new File(dic);
	    BufferedReader reader = null;
        try 
        {
        	/*
        	 * 读取字典文件，生成标准词数组和同义词与标准词的映射关系。
        	 */
			reader = new BufferedReader(new FileReader(file));
	        String buffer = null;
	        int index = 0;
	        while ((buffer = reader.readLine()) != null)
	        {
	        	/*
	        	 * 一次循环处理一行，即一组同义词。
	        	 */
	        	
	        	/*
	        	 * 判断字典中这行内容是否为空。
	        	 */
	        	if(buffer.trim().isEmpty())
	        		continue;
	        	
	        	/*
	        	 * 字符串数组s用于存储字典中的一组同义词。
	        	 */
	        	String[] s = buffer.split(regex);

	        	/*
	        	 * 确保同义词数组中同义词数量在2或2以上。
	        	 * 若字典中出现一行中只有一个词语的情况
	        	 * 则这个词语不需要替换，它本身即可作为
	        	 * 标准词。所以不将这个词加入标准词列表。
	        	 */
	        	if(s == null || s.length < 2)
	        		continue;
	        	
	        	/*
	        	 * 将这组同义词中的第一个词作为标准词加入
	        	 * 标准词列表。
	        	 */
	        	standards.add(s[0]);
	        	
	        	/*
	        	 * 将这组同义词中除了第一个标准词的其他词语
	        	 * 都映射到标准词数组中他们的标准词的下标。
	        	 */
	        	for(int i=1;i<s.length;i++)
	        		map.put(s[i], index);
	        	
	        	/*
	        	 * 标准词下标更新。
	        	 */
	        	index++;
	        }
	        reader.close();
		}
        
        /*
         * 异常处理。
         */
        catch (FileNotFoundException e) 
        {
			e.printStackTrace();
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
	}

	/**
	 * 此模块的主要功能方法，用于替换分句中的同义词为标准词。
	 * 
	 * @param a 要处理的目标分句（Entry）对象。
	 * @throws Exception 自定义的“参数错误异常”。
	 * @see cn.eviadc.cloaker.analyzer.PaperAnalyzer#Analyze(java.lang.Object)
	 */
	@Override
	public void Analyze(Object a) throws Exception 
	{
		/*
		 * 判断传入参数是否为Entry类的对象，若不是则抛出一个
		 * 自定义“错误参数异常”。
		 */
		if(a.getClass().getName() != Entry.class.getName())
			throw new WrongArgumentException(Entry.class.getName(), a.getClass().getName());
		
		Entry entry = (Entry) a;
		
		/*
		 * 对分句中的每一个被收录同义词的词语做标准词替换。
		 */
		for(int i=0;i<entry.size();i++)
			if(map.containsKey(entry.get(i)))
				entry.replace(i, standards.get(map.get(entry.get(i))));
	}

	/**
	 * 返回此同义词替换模块Analyze方法所需要的参数类型名。
	 * 
	 * @return Analyze方法所需要的参数类型名。
	 * @see cn.eviadc.cloaker.analyzer.PaperAnalyzer#targetClassName()
	 */
	@Override
	public String targetClassName() 
	{
		return Entry.class.getName();
	}
	
}
