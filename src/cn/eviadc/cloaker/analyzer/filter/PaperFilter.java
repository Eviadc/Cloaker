package cn.eviadc.cloaker.analyzer.filter;

import cn.eviadc.cloaker.Cloaker;
import cn.eviadc.cloaker.analyzer.PaperAnalyzer;
import cn.eviadc.cloaker.analyzer.WrongArgumentException;

/**
 * 过滤器父类，过滤器类需要继承这个类。
 * 
 * @author Nathan Wang
 *
 */
public class PaperFilter implements PaperAnalyzer
{
	protected float result;
	protected boolean debug;
	
	/**
	 * 返回过滤器可能生成的result。
	 * 
	 * @return 过滤器可能会生成的结果。
	 */
	public float getResult()
	{
		return result;
	}
	
	/**
	 * 过滤器的主要功能方法。
	 * 
	 * @param a 传入过滤器的Cloaker对象。
	 * @throws Exception 自定义的“参数错误异常”。 
	 * @see cn.eviadc.cloaker.analyzer.PaperAnalyzer#Analyze(java.lang.Object)
	 */
	@Override
	public void Analyze(Object a) throws Exception 
	{
		/*
		 * 判断传入参数是否为Cloaker类的对象，若不是则抛出一个
		 * 自定义“错误参数异常”。
		 */
		if(a.getClass().getName() != Cloaker.class.getName())
			throw new WrongArgumentException(Cloaker.class.getName(), a.getClass().getName());
	}

	/**
	 * 返回此过滤器Analyze方法所需要的参数类型名。
	 * 
	 * @return Analyze方法所需要的参数类型名。
	 * @see cn.eviadc.cloaker.analyzer.PaperAnalyzer#targetClassName()
	 */
	@Override
	public String targetClassName() 
	{
		return Cloaker.class.getName();
	}
	
	/**
	 * 此方法用于设置过滤器的参数。
	 * 
	 * @param paramName 参数名称。
	 * @param value 参数值。
	 */
	public void setParam(String paramName, Object value)
	{
		return ;
	}
	
	/**
	 * 设置调试模式。
	 * 
	 * @param debug 是否开启调试模式。
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
