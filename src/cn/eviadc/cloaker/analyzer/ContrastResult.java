package cn.eviadc.cloaker.analyzer;

import java.util.ArrayList;

/**
 * 此类用于实例化EntryContrast类方法的返回对象。
 * <p>
 * 公有数据：
 * float					maxSimilarity 	相似度阈值。<br>
 * ArrayList&lt;Float&gt;	similarity 		相似度列表。<br>
 * 
 * @author Nathan Wang
 *
 */
public class ContrastResult 
{
	public float maxSimilarity;
	public ArrayList<Float> similarity;
	
	/**
	 * 构造函数，设置最大相似度和相似度列表。
	 * 
	 * @param sim 最大相似度。
	 * @param list 相似度列表。
	 */
	public ContrastResult(float sim, ArrayList<Float> list)
	{
		maxSimilarity = sim;
		similarity = list;
	}
}
