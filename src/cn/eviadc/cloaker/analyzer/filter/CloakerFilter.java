package cn.eviadc.cloaker.analyzer.filter;

import java.util.ArrayList;

import cn.eviadc.cloaker.Article;
import cn.eviadc.cloaker.Cloaker;
import cn.eviadc.cloaker.Entry;
import cn.eviadc.cloaker.ReferenceLocation;
import cn.eviadc.cloaker.analyzer.ContrastResult;
import cn.eviadc.cloaker.analyzer.EntryContrast;

/**
 * 过滤器（继承PaperFilter类），用于进行文章分析，计算相似度。
 * <p>
 * 私有数据：<br>
 * float 					threshold 	相似度阈值。<br>
 * ArrayList&lt;Integer&gt; source 		被引用文章列表。<br>
 * ArrayList&lt;Integer&gt; loc 		被引用文章中被引用分句下标。<br>
 * Cloaker 					c 			Cloaker控制器对象。<br>
 * Article 					art 		待检测文章对象。<br>
 * ArrayList&lt;Article&gt; ref 		文献库文章对象列表。<br>
 * boolean[] 				b 			记录分句是否有引用的布尔数组。
 * 
 * @author Nathan Wang
 *
 */
public class CloakerFilter extends PaperFilter
{
	private float threshold;
	private Cloaker c;
	private Article art;
	private ArrayList<Article> ref;
	
	/**
	 * 此方法用于设置参数（相似度阈值）。
	 * 
	 * @param paramName 参数名称。
	 * @param value 参数值。
	 * @see cn.eviadc.cloaker.analyzer.filter.PaperFilter#setParam(java.lang.String, java.lang.Object)
	 */
	@Override
	public void setParam(String paramName, Object value)
	{
		String name = paramName.trim().toLowerCase(); 
		if(name.equals("threshold"))
			threshold = (float)value;
	}
	
	/**
	 * 分析文章和文献库中文章的相似度，对传入的Cloaker对象进行操作。
	 * 传入的Cloaker对象中存储有使用各个模块处理过的目标文章和文献库
	 * 文章对象。
	 * 
	 * @param a Cloaker对象，过滤器会从Cloaker对象中获得待分析的文章和文献库。
	 * @throws Exception 若参数a的类型错误则抛出自定义的“参数错误异常”。
	 * @see cn.eviadc.cloaker.analyzer.filter.PaperFilter#Analyze(java.lang.Object)
	 */
	@Override
	public void Analyze(Object a) throws Exception 
	{
		/*
		 * 判断参数类型是否正确。
		 */
		super.Analyze(a);
		
		/*
		 * 获取Cloaker中的待检测文章和文献库。
		 */
		c = (Cloaker)a;
		art = c.art;
		ref = c.ref;
		
		/*
		 * count用于记录待检测文章中引用其他文献的分句数量。
		 */
		int count = 0;
		
		/*
		 * 对待检测文章中的每个分句都进行文献库所有
		 * 文章的相似度检测。
		 */
		for(int i=0;i<art.size();i++)
		{
			/*
			 * 一次循环处理一个分句。
			 */
			Entry entry = art.get(i);
			ContrastResult cr = null;
			
			/*
			 * 对文献库中每一篇文章进行查重检测。
			 */
			for(int j=0;j<ref.size();j++)
				
				/*
				 * 若该分句还未被过滤
				 * 获取一个分句与文献库中一篇文章的相似度检测
				 * 结果（相似度列表和最大相似度）。
				 */
				if((!c.rloc.containsKey(i)) && 
						(cr = EntryContrast.getSimilarity(c.ref.get(j), entry)).maxSimilarity >= threshold)
				{
					/*
					 * 命中后将命中结果存入rloc。
					 */
					c.rloc.put(i, new ReferenceLocation(j, cr.similarity.indexOf(cr.maxSimilarity)));
					
					/*
					 * 待检测文章引用其它文献的分句数量加一。
					 */
					count++;
					
					/*
					 * 开启调试模式用于在控制台打印出额外信息。
					 */
					if(debug)
					{
						System.out.print("\n[HIT]\tentry#" + i + " : ");
						entry.print();
						System.out.print(" hit artcle#" + j + " - #" + j + " : ");
						ref.get(c.rloc.get(i).ref).get(c.rloc.get(i).loc).print();
						System.out.println();
					}
					
					break;
				}
		}
		
		/*
		 * 以引用其它文献的分句数量所占整篇文章的比例作为相似度。
		 */
		result = count * 100 / (float)c.art.size();
		if(debug)
			System.out.println("[CLOAKER] result = " + result + "%");
	}
	
	/**
	 * 返回相似度阈值。
	 * 
	 * @return 相似度阈值。
	 */
	public float getThreshold() {
		return threshold;
	}

	/**
	 * 设置过滤器的相似度阈值。
	 * 
	 * @param threshold 要设置的相似度阈值。
	 */
	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}

	/**
	 * 返回文献库文章列表。
	 * 
	 * @return 文献库文章列表。
	 */
	public ArrayList<Article> getRef() {
		return ref;
	}
}