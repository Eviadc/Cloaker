package cn.eviadc.cloaker.analyzer;

import cn.eviadc.cloaker.Article;
import cn.eviadc.cloaker.Entry;
import cn.eviadc.utils.LevenshteinDistance;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * 此类用于分析相似度。
 * 
 * @author Nathan Wang
 *
 */
public class EntryContrast
{
	/**
	 * 此方法用于计算一个分句和一篇文章中所有分句的相似度。
	 * <p>
	 * 在cn.eviadc.cloaker.analyzer.filter.CloakerFilter中
	 * 待检测文章中的每一条分句与文献库对比时会使用。
	 * 
	 * @param art 要对比的文章。
	 * @param entry 要对比的分句。
	 * @return ContrastResult对象，包括相似度列表和相似度最大值。
	 */
	public static ContrastResult getSimilarity(Article art, Entry entry)
	{
		/*
		 * 保存所有分句的相似度。
		 */
		ArrayList<Float> simList = new ArrayList<Float>();
		
		/*
		 * 变量max用于保存相似度最大值。
		 */
		float max = .0f;
		
		/*
		 * 将entry与文章中的每个分句计算相似度。
		 */
		for(int i=0;i<art.size();i++)
		{
			/*
			 * 获得entry与文章中当前分句的相似度。
			 */
			float sim = getSimilarity(art.get(i), entry);
			
			/*
			 * 获得所有相似度中的最大值并将相似度加入相似度列表。
			 */
			if(sim > max) max = sim;
			simList.add(sim);
		}
		
		/*
		 * 返回相似度列表和最大相似度。
		 */
		return new ContrastResult(max, simList);
	}
	
	/**
	 * 用于计算两个分句的综合相似度。
	 * <p>
	 * 在上方的getSimilarity方法中比较文章中每一条分句
	 * 与待检测分句时会使用。
	 * 
	 * @param a 一个分句。
	 * @param b 领一个分句。
	 * @return 相似度。
	 */
	public static float getSimilarity(Entry a, Entry b)
	{
		return synthesisGenSimilarity(getWordSimilarity(a, b), getLDSimilarity(a, b));
	}

	/**
	 * 计算分句a和分句b的词语相似度。
	 * <p>
	 * 私有方法，在synthesisGenSimilarity方法中会用来
	 * 计算词语相似度。
	 * 
	 * @param a 一个分句。
	 * @param b 另一个分句。
	 * @return 两个分句的词语相似度。
	 */
	private static float getWordSimilarity(Entry a, Entry b)
	{
		HashSet<String> set = new HashSet<String>();
		
		/*
		 * 将分句a中的所有分词（此时分句中的
		 * 所有分词都已经被替换为其标准词）加入到set集合中。
		 */
		for(int i=0;i<a.size();i++)
			set.add(a.get(i));
		
		/*
		 * 将词语相似度初始化为零。
		 */
		float sim = .0f;
		
		/*
		 * 对于分句b中的每个分词，在set（分句a的分词）中寻找
		 * 和其相同的词语，得出相同词语的个数。
		 */
		for(int i=0;i<b.size();i++)
			if(set.contains(b.get(i)))
				sim++;
		
		/*
		 * 将相同词语数量/词语总数作为词语相似度。
		 */
		sim = sim * 2 / (float)(a.size() + b.size());
//		if(sim > .2f)
//		{
//			System.out.println("a = " + a.getContent() + ", b = " + b.getContent());
//			System.out.print(" => WORDsim = " + sim);
//		}
		return sim;
	}
	
	/**
	 * 计算文本编辑距离相似度。
	 * <p>
	 * 私有方法，在synthesisGenSimilarity方法中会用来
	 * 计算文本编辑距离相似度。
	 * 文本编辑距离相似度生成方式 S<sub>LD</sub> = 1 - ld / avg(A, B);
	 * 
	 * @param a
	 * @param b
	 * @return 文本编辑距离相似度。
	 */
	private static float getLDSimilarity(Entry a, Entry b)
	{
		/*
		 * 计算两个分句的文本编辑距离。
		 */
		int ld = LevenshteinDistance.getLD(a.getContent(), b.getContent());
		
		//return 1 - ld / Math.max(a.size(), b.size());
		
		/*
		 * 文本编辑距离生成
		 */
		float res = 1 - ld * 2 / (float)(a.size() + b.size()); 

		
		/*
		 * 若相似度小于零则返回零。
		 */
		return res < 0 ? 0 : res;
	}
	
	/**
	 * 返回综合相似度（由词语相似度和文本编辑距离相似度加权得到）。
	 * <p>
	 * 在getSimilarity方法中会调用getWordSimilarity和getLDSimilarity的结果
	 * 传入此方法用来加权两个算法得到综合相似度。
	 * 
	 * @param word 词语相似度。
	 * @param ld 文本编辑距离。
	 * @return 综合相似度。
	 */
	private static float synthesisGenSimilarity(float word, float ld)
	{
		/*
		 * 词语相似度在综合相似度中的权重。
		 */
		float wordWeight = .6f;
		
		/*
		 * 文本编辑距离相似度在综合相似度中的权重。
		 */
		float ldWeight = .4f;
		
//		if(ld > .2f)
//			System.out.println(" => WORDsim = " + word + " => fullSim = " + (word * wordWeight + ld * ldWeight));
		
		/*
		 * 返回综合相似度。
		 */
		return word * wordWeight + ld * ldWeight;
	}
	
	/**
	 * 计算相似度列表list的平均值。
	 * 
	 * @param list 需要计算平均值的相似度列表。
	 * @return 相似度列表平均值。
	 */
	public static float synthesisSimilarity(ArrayList<Float> list)
	{
		float sum = .0f;
		for(int i=0;i<list.size();i++)
			sum += list.get(i);
		return sum / list.size();
	}
}
