package cn.eviadc.cloaker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import cn.eviadc.cloaker.analyzer.EntryGenerator;
import cn.eviadc.cloaker.analyzer.FileReader;
import cn.eviadc.cloaker.analyzer.SynonymsReplacer;
import cn.eviadc.cloaker.analyzer.WordDivider_IK;
import cn.eviadc.cloaker.analyzer.filter.PaperFilter;

/**
 * Cloaker类相当于程序的控制器，控制了待检测文章的读取、文献库
 * 文章的读取、所有文章的预处理（包括文章分句、分句分词、同义词
 * 替换等）、使用添加的过滤器分析文章、生成HTML页面等过程。
 * <p>
 * 公有数据：<br>
 * Article							art		目标文章。<br>
 * ArrayList&lt;Article&gt; 	  	ref		对比文献库中的文章。<br>
 * ArrayList&lt;PaperFilter&gt; 	filter	要使用的过滤器。<br>
 * HashMap&lt;Integer, ReferenceLocation&gt;			rloc	记录分句过滤结果
 * 
 * @author Nathan Wang
 *
 */
public class Cloaker 
{
	/**
	 * 目标文章。
	 */
	public Article art = new Article();
	
	/**
	 * 文献库中的文章。
	 */
	public ArrayList<Article> ref = new ArrayList<Article>();
	
	/**
	 * 要使用的过滤器。
	 */
	public ArrayList<PaperFilter> filter = new ArrayList<PaperFilter>();
	
	/**
	 * 	记录分句过滤结果。
	 */
	public HashMap<Integer, ReferenceLocation> rloc = new HashMap<Integer, ReferenceLocation>();

	final static String formatSrc = "./assets/src.html";
	
	/**
	 * 添加一个过滤器f（PaperFilter对象）。
	 * <p>
	 * 用于将配置好参数的过滤器加入Cloaker中准备用其对文章进行分析。
	 * 
	 * @param f 过滤器对象。
	 */
	public void addFilter(PaperFilter f)
	{
		filter.add(f);
	}
	
	/**
	 * 此方法是实现主模块主要功能的方法，用来运行整个程序。
	 * <p>
	 * 用于进行文章预处理和让文章遍历过滤器并输出报告。
	 * 
	 * @param filename 	待检测的文章。
	 * @param refname 	文献库目录。
	 * @param debug		是否开启调试。
	 * @param htmlname 	输出目标文件位置。
	 * @return HTML输出到的文件对象。
	 * @throws Exception 可能的异常。
	 */
	public File analysis(String filename, String refname, boolean debug, String htmlname) throws Exception
	{
		/*
		 * 实例化分句模块、分词模块和同义词替换模块，准备对文章
		 * 进行处理。
		 */
		EntryGenerator entryGenerator = new EntryGenerator();
		WordDivider_IK wordDivider = new WordDivider_IK();
		SynonymsReplacer synonymsReplacer = new SynonymsReplacer();
		
		/*
		 * 调用文件读取模块并设置文件路径准备读取待检测文件内容。
		 */
		FileReader fr = new FileReader();
		fr.setFile(filename);
		
		/*
		 * 从文件加载待检测文章内容。
		 */
		art.analyze(fr);
		
		/*
		 * 对文章内容进行分句。
		 */
		art.analyze(entryGenerator);
		
		
		/*
		 * 清除文章原文（此时文章已经过分句存储到了entry）。
		 */
		art.releaseRawContent();
		
		/*
		 * 对文章进行分词。
		 */
		art.analyze(wordDivider);
		
//		art.releaseEntryRawContent();
		
		/*
		 * 对文章进行同义词替换。
		 */
		art.analyze(synonymsReplacer);
		
//		art.print();

		/*
		 * 加载文献库。
		 */
		File dir = new File(refname);
		File fref[] = dir.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith("txt"))
					return true;
				return false;
			}
		});
		int fsize = fref.length;
		
		/*
		 * 将文献库目录中的所有文件读入。
		 */
		for(int i=0;i<fsize;i++)
		{
			/*
			 * 一次循环处理一篇文献。
			 */
			
			/*
			 * 读取文件内容。
			 */
			System.out.print("[" + i + "]" + fref[i].getName() + " ---RD---");
			Article a = new Article();
			fr.setFile(fref[i]);
			a.analyze(fr);
			
			/*
			 * 对文章进行分句。
			 */
			System.out.print("EG---");
			a.analyze(entryGenerator);
			a.releaseRawContent();
			
			/*
			 * 对文章进行分词。
			 */
			System.out.print("WD---");
			a.analyze(wordDivider);
//			a.releaseEntryRawContent();
			
			/*
			 * 对文章进行同义词替换。
			 */
			System.out.println("SR---");
			a.analyze(synonymsReplacer);
			
			/*
			 * 把文章加入文献库文章列表。
			 */
			ref.add(a);
//			a.print();
			System.out.println("Done solving art#" + i + ": " 
					+ fref[i].getName() + ", EntryNum: " + a.size() + "\n");
		}
		
		/*
		 * 开始进行相似度分析。
		 */
		System.out.println("Generating Similarity...");
		
		/*
		 * 遍历过滤器。
		 */
		for(PaperFilter ff : filter)
		{
			ff.setDebug(debug);
			ff.Analyze(this);
		}
		System.out.println("Printing...");
		
		float result = rloc.size() / (float)art.size();
		
		/*
		 * 输出html到指定文件。
		 */
		File f = printHtml(htmlname, genHtml(result, this.formatHtml()));
		System.out.println("Done.");
		return f;
	}
	
	/**
	 * 从模板文件中读取HTML代码用于不同内容的显示。
	 * 
	 * @return 分好类别的HTML代码列表。
	 * @throws Exception 可能的异常。
	 */
	private HashMap<String, String> formatHtml() throws Exception
	{
		/*
		 * 读取HTML文件的模板。
		 */
		File f = new File(formatSrc);

	    BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"));
        
        String buffer = null;
        String raw = "";
        String reading = "";
        HashMap<String, String> map = new HashMap<String, String>();
        
        /*
         * 读入不同的HTML代码块，将用于不同内容的显示。
         */
        while ((buffer = reader.readLine()) != null)
        {
        	if(buffer.trim().startsWith("<!--") && buffer.trim().endsWith(" Start-->"))
        		reading = buffer.substring("<!--".length(), buffer.indexOf(" Start-->"));
        	else if(buffer.trim().startsWith("<!--") && buffer.trim().endsWith(" End-->"))
        	{
        		map.put(reading, raw);
        		reading = raw = "";
        	}
        	else if(!reading.isEmpty())
        		raw += buffer + "\n";
        }
        
        if(map.containsKey("Line Breaker"))
        	EndingTrans.lineBreaker = map.get("Line Breaker");
        
        reader.close();
        
        return map;
	}
	
	/**
	 * 用于将占位符替换为数据或内容。
	 * 
	 * @param raw 需要在其中填入数据的完整字符串。
	 * @param key 占位符名称。
	 * @param value 要填入的内容。
	 * @return 替换后的完整字符串。
	 */
	private String formatHtmlModel(String raw, String key, String value)
	{
		return raw.replace("<!--" + key + "-->", value);
	}
	
	/**
	 * 使用HTML模板生成HTML代码。
	 * 
	 * @param result 生成的HTML代码字符串。
	 * @param map 调用formatHtml生成的分好类的HTML代码列表。
	 * @return HTML代码字符串。
	 */
	private String genHtml(float result, HashMap<String, String> map)
	{		
		String html = map.get("HTML");
		html = this.formatHtmlModel(html, "Similarity", (result * 100) + "");
		{
			String target = map.get("Article");
			target = this.formatHtmlModel(target, "Article Title", art.getTitle());
			{
				String content = "";
				
				for(int i=0;i<art.size();i++)
				{
					Entry e = art.get(i);
					if(rloc.containsKey(i))
					{
						String hit = map.get("Target Hit");
						hit = this.formatHtmlModel(hit, "Entry Content", EndingTrans.trans(e, false));
						hit = this.formatHtmlModel(hit, "Opponent Content",
								EndingTrans.trans(ref.get(rloc.get(i).ref).get(rloc.get(i).loc), false));
						hit = this.formatHtmlModel(hit, "Self ID", "t_" + i);
						hit = this.formatHtmlModel(hit, "Opponent ID", "r_" + rloc.get(i).ref + "_" + rloc.get(i).loc);
						if(e.getEnding().contains("\n"))
							hit += EndingTrans.lineBreaker;
						content += hit;
					}
					else
						content += EndingTrans.trans(e, true);
				}
				target = this.formatHtmlModel(target, "Article Content", content);
			}
			html = this.formatHtmlModel(html, "Article Target", target);
		}
		{
			String sourceContent = "";
			for(int i=0;i<ref.size();i++)
			{
				String source = map.get("Article");
				source = this.formatHtmlModel(source, "Article Title", ref.get(i).getTitle());
				{
					String content = "";
					
					for(int j=0;j<ref.get(i).size();j++)
					{
						int index = -1;
						for(int k : rloc.keySet())
							if(rloc.get(k).ref == i && rloc.get(k).loc == j)
							{
								index = k;
								break;
							}
						
						Entry e = ref.get(i).get(j);
						if(index != -1)
						{
							String hit = map.get("Source Hit");
							hit = this.formatHtmlModel(hit, "Entry Content", EndingTrans.trans(e, false));
							hit = this.formatHtmlModel(hit, "Self ID", "r_" + i + "_" + j);
							hit = this.formatHtmlModel(hit, "Opponent ID", "t_" + index);
							if(e.getEnding().contains("\n"))
								hit += EndingTrans.lineBreaker;
							content += hit;
						}
						else
							content += EndingTrans.trans(e, true);
					}
					source = this.formatHtmlModel(source, "Article Content", content);
				}
				sourceContent += source;
			}
			html = this.formatHtmlModel(html, "Article Sources", sourceContent);
		}
		return html;
	}
	
	/**
	 * 输出html到指定的结果文件中。
	 * 
	 * @param filename 结果文件路径。
	 * @param html 需要输出的html代码。
	 * @throws Exception
	 */
	private static File printHtml(String filename, String html) throws Exception
	{
		File f = new File(filename);
		if(!f.exists())
			f.createNewFile();
		FileWriter fw = new FileWriter(f);
		fw.write(html);
		fw.flush();
		fw.close();
		
		FileCopy.copyTo(new File("./assets/style.css"), new File(f.getParent() + "/assets/style.css"));
		FileCopy.copyTo(new File("./assets/bg.png"), new File(f.getParent() + "/assets/bg.png"));
		
		return f;
	}
}

/**
 * 此类用于还原分句句尾。
 * 
 * @author Nathan Wang
 *
 */
class EndingTrans
{
	/**
	 * 用于保存换行符。
	 */
	public static String lineBreaker = "<br>";
	
	/**
	 * 返回分句的句尾加换行符（可选择）。
	 * 
	 * @param ed 句尾列表。
	 * @param lb 是否要将\n换为&lt;br&gt;。
	 * @return 句尾字符串。
	 */
	public static String trans(ArrayList<String> ed, boolean lb)
	{
		String res = "";
		for(String s : ed)
		{
			if(lb && s.equals("\n"))
				s = lineBreaker;
			
			res += s;
		}
		return res;
	}
	
	/**
	 * 返回添加好句尾的分句字符串。
	 * 
	 * @param e 需要添加句尾的分句对象。
	 * @param lb 是否要将\n换为&lt;br&gt;。
	 * @return 添加好句尾的分句字符串。
	 */
	public static String trans(Entry e, boolean lb)
	{
		return e.getRawContent() + EndingTrans.trans(e.getEnding(), lb);
	}
}

/**
 * 用于复制文件的工具类。
 * 
 * @author Nathan Wang
 *
 */
class FileCopy
{
	/**
	 * 将source文件的内容复制到target文件中，
	 * 若target文件不存在将被创建。
	 * 
	 * @param source 被复制的文件。
	 * @param target 目标文件。
	 * @throws Exception 可能的异常。
	 */
	public static void copyTo(File source, File target) throws Exception 
	{
		int length = 2097152;
		
		/*
		 * 若目标文件不存在则创建此文件。
		 */
		if(!target.exists())
		{
			target.getParentFile().mkdir();
			target.createNewFile();
		}
		
		/*
		 * 复制文件。
		 */
		FileInputStream in = new FileInputStream(source);
		FileOutputStream out = new FileOutputStream(target);
		byte[] buffer = new byte[length];
		while (true) 
		{
			int ins = in.read(buffer);
			if (ins == -1) 
				break;
			else
				out.write(buffer, 0, ins);
		}
		in.close();
		out.flush();
		out.close();
	}
}
