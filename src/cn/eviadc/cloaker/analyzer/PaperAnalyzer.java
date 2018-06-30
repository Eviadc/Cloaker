package cn.eviadc.cloaker.analyzer;

/**
 * 各个模块使用的公共接口。
 * 
 * @author Nathan Wang
 *
 */
public interface PaperAnalyzer
{
	/**
	 * 用于定义模块的主要功能方法。
	 * 
	 * @param a 模块主要功能所操作的对象。
	 * @throws Exception 自定义的“参数错误异常”。
	 */
	public void Analyze(Object a) throws Exception;
	
	/**
	 * 返回模块Analyze方法所需要的参数类型名。
	 * 
	 * @return 模块Analyze方法所需要的参数对象类型名。
	 */
	public String targetClassName();
}
