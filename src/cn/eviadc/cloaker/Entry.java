package cn.eviadc.cloaker;

import java.util.ArrayList;

import cn.eviadc.cloaker.analyzer.PaperAnalyzer;

/**
 * Entry类存储由词构成的列表，代表一个分句。
 * <p>
 * 私有数据：<br>
 * ArrayList&lt;String&gt;	content 	组成分句的词列表；<br>
 * ArrayList&lt;String&gt;	ending 		用于存储句尾的列表；<br>
 * String 					rawContent	未经处理过的句子。
 * 
 * @author Nathan Wang
 */
public class Entry
{
	private ArrayList<String> content;
	private ArrayList<String> ending;
	private String rawContent;

	/**
	 * Entry类的构造函数，创建content数组。
	 */
	public Entry()
	{
		content = new ArrayList<String>();
	}
	
	/**
	 * Entry类的构造函数，创建content数组并设置未经处理过
	 * 的句子内容。
	 * 
	 * @param s 用于初始化的未经处理的句子内容。
	 */
	public Entry(String s)
	{
		content = new ArrayList<String>();
		//if(!s.isEmpty())
			rawContent = s;
	}
	
	/**
	 * Entry类的构造函数，创建content数组并设置s为未经处理
	 * 过的句子内容和ed为句尾的列表。
	 * 
	 * @param s 未经过分词处理的分句原文。
	 * @param ed 用于保存此分句结尾字符的数组。
	 */
	public Entry(String s, ArrayList<String> ed)
	{
		content = new ArrayList<String>();
		rawContent = s;
		ending = ed;
	}
	
	/**
	 * 获取ending数组。
	 * <p>
	 * 在检测完成后输出原文时用来还原句尾。
	 * 
	 * @return ending数组。
	 */
	public ArrayList<String> getEnding()
	{
		return ending;
	}
	
	/**
	 * 设置rawContent的值。
	 * <p>
	 * 在将文章原文分句时将分好的句子存入rawContent时使用。
	 * 
	 * @param s rawContent的值。
	 */
	public void setRawContent(String s)
	{
		rawContent = s;
	}
	
	/**
	 * 获取rawContent的值。
	 * <p>
	 * 在将分句进行分词时用于获取分句原文。
	 * 
	 * @return 未经处理的句子内容。
	 */
	public String getRawContent()
	{
		return rawContent;
	}
	
	/**
	 * 清空rawContent。
	 */
	public void releaseRawContent()
	{
		rawContent = null;
	}
	
	/**
	 * 添加新的词语到分词列表中。
	 * <p>
	 * 在进行分词时将分出的词语添加进分词列表中。
	 * 
	 * @param s 需要添加的分词。
	 */
	public void addContent(String s)
	{
		content.add(s);
	}
	
	/**
	 * 设置分词列表的内容。
	 * <p>
	 * 在进行分词时将分好的分词列表存储到content中使使用。
	 * 
	 * @param s 需要设置的分词列表。
	 */
	public void setContent(ArrayList<String> s)
	{
		content = s;
	}
	
	/**
	 * 获取分词列表。
	 * <p>
	 * 在对比词语相似度时用于从分词列表中获取词语。
	 * 
	 * @return 分词列表。
	 */
	public ArrayList<String> getContent()
	{
		return content;
	}
	
	/**
	 * 获取分词列表的长度。
	 * <p>
	 * 在获取分词列表中的词语时用于计数循环。
	 * 
	 * @return 分词列表的长度。
	 */
	public int size()
	{
		return content.size();
	}
	
	/**
	 * 获取分词列表中下标为i的词语。
	 * <p>
	 * 在对比词语相似度时用于从分词列表中获取词语。
	 * 
	 * @param i 词语的下标。
	 * @return 目标词语。
	 */
	public String get(int i)
	{
		return content.get(i);
	}
	
	/**
	 * 去除分词列表中下标为i的词语。
	 * 
	 * @param i 要删除词语的下标。
	 * @return 被删除的词语。
	 */
	public String remove(int i)
	{
		return content.remove(i);
	}
	
	/**
	 * 替换下标为i的词语为s。
	 * <p>
	 * 在同义词替换时用于将词语替换为其标准词。
	 * 
	 * @param i 被替换词语的下标。
	 * @param s 新的词语。
	 * @return 原先在该位置的词语。
	 */
	public String replace(int i, String s)
	{
		return content.set(i, s);
	}
	
	/**
	 * 调用此方法，会以此对象为参数调用传入的模块对象的
	 * Analyze方法（子模块的主要功能方法）。
	 * <p>
	 * 使用不同模块对文章进行处理时用于执行被传入的模块。
	 * 
	 * @param ana 要对此分句对象进行操作的模块对象。
	 * @throws Exception 模块对象Analyze方法可能会抛出的异常。
	 */
	public void analyze(PaperAnalyzer ana) throws Exception
	{
		ana.Analyze(this);
	}
	
	/**
	 * 打印出分词列表。
	 * <p>
	 * 用于调试时打印出额外信息。
	 */
	public void print()
	{
		for(int i=0;i<content.size();i++)
			System.out.print(content.get(i) + (i == content.size() - 1 ? "/\\\n" : " | "));
	}
}
