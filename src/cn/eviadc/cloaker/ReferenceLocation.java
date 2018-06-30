package cn.eviadc.cloaker;

/**
 * 用于保存待检测文章命中语句和被引用文章被引用语句
 * 在各自文章中的位置，在CloakerFilter中被用于保存
 * 命中结果。
 * 
 * @author Nathan Wang
 *
 */
public class ReferenceLocation 
{
	/**
	 * 待检测文章中被命中语句的位置。
	 */
	public int ref;
	
	/**
	 * 被引用文章中被引用语句的位置。
	 */
	public int loc;
	
	/**
	 * 保存命中结果。
	 * 
	 * @param r 待检测文章中被命中语句的位置。
	 * @param l 被引用文章中被引用语句的位置。
	 */
	public ReferenceLocation(int r, int l)
	{
		ref = r;
		loc = l;
	}

}
