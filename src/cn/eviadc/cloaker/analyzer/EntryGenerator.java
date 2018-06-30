package cn.eviadc.cloaker.analyzer;

import java.util.ArrayList;
import java.util.Stack;

import cn.eviadc.cloaker.Article;
import cn.eviadc.cloaker.Entry;

/**
 * 分句模块，用于处理文章原内容，使用停止符和包围符对文章
 * 进行分句处理。
 * <p>
 * 私有数据：<br>
 * int 						divLength 			每个分句的最小字符数量。<br>
 * String[] 				hardStopper 		用于判断分局结尾的停止符数组。<br>
 * String[] 				softStopper 		软分句停止符<br>
 * String[] 				wrapper 			用于判断分局的包围符数组。<br>
 * ArrayList&lt;String&gt; 	hardStopperList 	包含停止符字符串的数组列表。<br>
 * ArrayList&lt;String&gt; 	softStopperList 	包含软分句停止符字符串的数组列表<br>
 * ArrayList&lt;String&gt; 	wrapperStarterList 	包含包围符开始字符串的数组列表。<br>
 * ArrayList&lt;String&gt; 	wrapperEnderList 	包含包围符结束字符串的数组列表。<br>
 * boolean 					ENABLESOFTSTOPPER	启用软分句
 * 
 * @author Nathan Wang
 *
 */
public class EntryGenerator implements PaperAnalyzer
{
	private static int divLength = 10;
	private static String hardStopper[] = {"。", "!", "?", ".", "!", "?", "\n"};
	private static String softStopper[] = {"，", "；", "：", ",", ":", ";"};
	private static String wrapper[] = {"()", "\"\""};
	private static ArrayList<String> hardStopperList;
	private static ArrayList<String> softStopperList;
	private static ArrayList<String> wrapperStarterList;
	private static ArrayList<String> wrapperEnderList;
	
	private static boolean ENABLESOFTSTOPPER = false;
	
	/**
	 * 构造函数，初始化停止符列表。
	 */
	public EntryGenerator()
	{
		hardStopperList = new ArrayList<String>();
		softStopperList = new ArrayList<String>();
		wrapperStarterList = new ArrayList<String>();
		wrapperEnderList = new ArrayList<String>();
		
		/*
		 * 将数组中的停止符和包围符写入字符串列表中。
		 */
		for(int i=0;i<hardStopper.length;i++)
			hardStopperList.add(hardStopper[i]);
		for(int i=0;i<softStopper.length;i++)
			softStopperList.add(softStopper[i]);
		for(int i=0;i<wrapper.length;i++)
		{
			wrapperStarterList.add(wrapper[i].substring(0, 1));
			wrapperEnderList.add(wrapper[i].substring(1));
		}
	}
	
	/**
	 * 调用此方法来对文章分句。
	 * <p>
	 * 此方法将对文章对象参数a中的rawContent内容进行分句，
	 * 并将分局结果保存为一个分句（Entry）对象列表保存到
	 * 文章对象a中。
	 * 
	 * @param a 需要分句的文章对象。
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
		
		Article art = (Article) a;
		
		/*
		 * 读取文章的原始内容rawContent，并创建一个分句列表。
		 */
		String raw = art.getRawContent();
		ArrayList<Entry> ar = new ArrayList<Entry>();
		
		/*
		 * 整型变量currentS用于保存上一分句的结束位置的下一
		 * 下标位置，即当前分句的开始字符位置。
		 */
		int currentS = 0;
		
		/*
		 * 用于软分句中等待匹配的括号类标点的栈
		 */
		Stack<String> pending = new Stack<String>();
		
		/*
		 * 开始分句。
		 */
		for(int i=0;i<raw.length();i++)
		{
			/*
			 * 布尔变量flag用于标识当前处理的字符是否为停止符。
			 */
			boolean flag = false;
			
			/*
			 * 字符串变量c用于获取和保存当前循环要处理的字符。
			 */
			String c = raw.substring(i, i+1);
			
			/*
			 * 判断当前字符是否为停止符。
			 */
			if(hardStopperList.contains(c))
				flag = true;
			
			/*
			 * 软分句功能实现
			 */
			if(ENABLESOFTSTOPPER) 
			{
				if(softStopperList.contains(c))
					if(i - currentS >= divLength)
						flag = true;
				
				if(wrapperStarterList.contains(c))
					if(raw.indexOf(wrapperEnderList.get(wrapperStarterList.indexOf(c)), i) - i >= divLength)
					{
						pending.add(c);
						flag = true;
					}
				
				if(!pending.isEmpty())
					if(wrapperEnderList.contains(c))
					{
						String cc = wrapperStarterList.get(wrapperEnderList.indexOf(c));
						if(pending.contains(cc))
							pending.remove(cc);
					}
			}
			
			/*
			 * 判断当前字符是否为原文的结尾，若是结尾
			 * 则当作停止符处理。
			 */
			if(i == raw.length() - 1)
				flag = true;
			
			/*
			 * 若当前字符为停止符且分句的长度不为零则
			 * 进行分句处理。
			 */
			if(flag && currentS < i)
			{
				/*
				 * 去除分句两侧的空字符，若去除后分句长度为零则
				 * 继续处理下一个字符。
				 */
				if(raw.substring(currentS, i).trim().isEmpty())
					continue;
				
				/*
				 * 创建句尾保存数组，用于保存句尾。
				 */
				ArrayList<String> ed = new ArrayList<String>();
				ed.add(c);
				
				/*
				 * i表示的字符（当前处理的字符）为停止符，此循环
				 * 依次判断此停止符后面的字符是否为停止符，并将
				 * 此分句结尾处所有连续的停止符保存到ed数组中。
				 * 此数组将用于还原文章的格式。
				 */
				for(int j=i+1; j < raw.length() && hardStopperList.contains(raw.substring(j, j+1)); j++)
					ed.add(raw.substring(j, j+1));
				
				/*
				 * 将识别出的分句原文和句尾存储到一个新的分句对象中
				 * 并将此对象添加到分句列表中。
				 */
				ar.add(new Entry(raw.substring(currentS, i).trim(), ed));
				
				/*
				 * 更新下一分句的开始位置。
				 */
				currentS = i + 1;
			}
		}
		
		/*
		 * 所有分句处理完成后将分句列表存储到文章对象中。
		 */
		art.setEntryList(ar);
	}

	/**
	 * 返回此分句模块Analyze方法所需要的参数类型名。
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
