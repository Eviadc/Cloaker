package cn.eviadc.utils;

import java.util.ArrayList;

/**
 * 此类用于计算两字符串的最小编辑距离。
 * 
 * @author Nathan Wang
 *
 */
public class LevenshteinDistance 
{
	/**
	 * 获取最小编辑距离。
	 * <p>
	 * 用的好像还是差不多的算法所以效率还是A*B
	 * 空间复杂度还是B。
	 * 
	 * @param a 一个字符串列表
	 * @param b 另一个字符串列表
	 * @return  最小编辑距离
	 */
	public static int getLD(ArrayList<String> a, ArrayList<String> b)
	{
		/*
		 * 两个字符串列表有任意一个为空的情况，
		 * 若有一个为空，
		 * 那么最小编辑距离为另一个字符串列表的长度。
		 */
		if(a.size() == 0 || b.size() == 0)
			return a.size() + b.size();
		
		/*
		 * 两个字符串列表都不为空的情况，
		 * 计算两字符串列表的最小编辑距离。
		 */
		int[] c = new int[b.size()];
		for(int i=0;i<b.size();i++)
			c[i] = i + 1;
		
//		System.out.print("[flat]\n   ");
//		for(int i=0;i<b.size();i++)
//			System.out.print(b.get(i) + "  ");
		
		for(int i=0;i<a.size();i++)
		{
			String tmp = a.get(i);
//			System.out.print("\n" + a.get(i));
			for(int j=b.size()-1;j>=0;j--)
			{
				int editD = j==0 ? i : c[j-1];
				if(!tmp.equals(b.get(j)))
					editD++;
				
				if(editD < ++c[j])
					c[j] = editD;
			}
			for(int j=0;j<b.size()-1;j++)
				if(c[j+1] > c[j] + 1)
					c[j+1] = c[j] + 1;

//			for(int j=0;j<b.size();j++)
//				System.out.print(String.format("%3d", c[j]));
		}
		
		return c[b.size() - 1];
	}
}
