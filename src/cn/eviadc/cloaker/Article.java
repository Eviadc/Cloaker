package cn.eviadc.cloaker;

import java.util.ArrayList;

import cn.eviadc.cloaker.analyzer.PaperAnalyzer;
import cn.eviadc.cloaker.analyzer.ContrastResult;
import cn.eviadc.cloaker.analyzer.EntryContrast;

/**
 * Article是一个存储文章数据和相关方法的类。
 * <p>
 * 私有数据：<br>
 * ArrayList&lt;Entry&gt;	entry			分句列表。<br>
 * String 					rawContent 		未经处理过的文章原始数据。<br>
 * String 					title 			文章标题。
 * 
 * @author Nathan Wang
 *
 */
public class Article 
{
	/**
	 * 分句列表。
	 */
	private ArrayList<Entry> entry;
	
	/**
	 * 未经处理过的文章原始数据。
	 */
	private String rawContent;
	
	/**
	 * 文章标题。
	 */
	private String title;
	
	/**
	 * 设置文章标题。
	 * <p>
	 * 用于将文件名设置为待检测文章名。
	 * 
	 * @param t 文章标题字符串。
	 */
	public void setTitle(String t)
	{
		title = t;
	}
	
	/**
	 * 获取文章标题。
	 * <p>
	 * 用于生成检测报告时显示文章标题。
	 * 
	 * @return 文章标题字符串。
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * 设置未经处理的文章内容。
	 * <p>
	 * 在从文件中读取文章内容后将内容存储到文章中时使用。
	 * 
	 * @param s 文章内容。
	 */
	public void setRawContent(String s)
	{
		rawContent = s;
	}
	
	/**
	 * 获取未经处理的文章内容。
	 * <p>
	 * 在对文章进行分句时用于获取文章原文。
	 * 
	 * @return 未经处理的文章内容。
	 */
	public String getRawContent()
	{
		return rawContent;
	}

	/**
	 * 将文章内容清空。
	 * <p>
	 * 对文章进行分句之后用于清空文章原文。
	 */
	public void releaseRawContent()
	{
		rawContent = null;
	}
	
	/**
	 * 清空文章分句列表的内容。
	 */
	public void releaseEntryRawContent()
	{
		for(int i=0;i<this.entry.size();i++)
			entry.get(i).releaseRawContent();
	}
	
	/**
	 * 设置文章分句列表的内容。
	 * <p>
	 * 用于保存分句后的文章。
	 * 
	 * @param e 要设置的文章分句列表内容。
	 */
	public void setEntryList(ArrayList<Entry> e)
	{
		entry = e;
	}
	
	/**
	 * 获取文章的分句列表。
	 * <p>
	 * 在某分句要与文章所有分句进行比对时用于获取文章的分句。
	 * 
	 * @return 文章分句列表。
	 */
	public ArrayList<Entry> getEntryList()
	{
		return entry;
	}

	/**
	 * 获取文章分句列表的长度。
	 * <p>
	 * 用于获取文章分句时的计数。
	 * 
	 * @return 文章分句列表的长度。
	 */
	public int size()
	{
		return entry.size();
	}
	
	/**
	 * 获取文章分句列表中下标为i的分句。
	 * <p>
	 * 在某分句要与文章所有分句进行比对时用于获取文章的某一分句。
	 * 
	 * @param i 要获取分句的下标；
	 * @return 目标分句。
	 */
	public Entry get(int i)
	{
		return entry.get(i);
	}
	
	/**
	 * 去除分句列表中下标为i的分句。
	 * 
	 * @param i 分句下标；
	 * @return 被删除的分句对象。
	 */
	public Entry remove(int i)
	{
		return entry.remove(i);
	}
	
	/**
	 * 将下标为i的分句替换为分句s。
	 * 
	 * @param i 目标分句下标；
	 * @param s 新的分句对象；
	 * @return 原先在目标分句位置的分句
	 */
	public Entry replace(int i, Entry s)
	{
		return entry.set(i, s);
	}
	
	/**
	 * 此方法可以接受处理对象为文章（Article）或者
	 * 处理对象为分句（Entry）的模块。
	 * <p>
	 * 此方法会首先检查模块需要的参数类型名，决定
	 * 适合的参数（文章或者文章中所有的分句）来调用
	 * 子模块的Analyze方法来实现模块的主要功能。
	 * 
	 * @param ana 对此文章进行操作的模块对象。
	 * @throws Exception 模块对象的Analyze方法可能会抛出的异常。
	 */
	public void analyze(PaperAnalyzer ana) throws Exception
	{
		String name = ana.targetClassName();
		if(name == Article.class.getName())
			ana.Analyze(this);
		else if(name == Entry.class.getName())
			for(int i=0;i<this.entry.size();i++)
				ana.Analyze(entry.get(i));
	}
	
	/**
	 * 分析分句entry和这篇文章中所有分句的相似度。
	 * 
	 * @param entry 要与这篇文章中所有分句对比相似度的分句。
	 * @return 相似度分析结果。
	 */
	public ContrastResult similarity(Entry entry)
	{
		return EntryContrast.getSimilarity(this, entry);
	}
	
	/**
	 * 打印出分句列表的内容。
	 */
	public void print()
	{
		for(int i=0;i<entry.size();i++)
			entry.get(i).print();
	}
}
