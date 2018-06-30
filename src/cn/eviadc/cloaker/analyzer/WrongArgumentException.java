package cn.eviadc.cloaker.analyzer;

/**
 * 自定义异常类，用于PaperFliter
 * 
 * @author Nathan Wang
 *
 */
public class WrongArgumentException extends Exception
{
	private static final long serialVersionUID = -241154360982635393L;

	/**
	 * 当模块被传入一个类型错误的参数时抛出的自定义异常。
	 * 
	 * @param expected 希望的参数类型名。
	 * @param got 得到的参数类型名。
	 */
	public WrongArgumentException(String expected, String got)
	{
		super("Wrong Argument: expected " + expected + ", got " + got);
	}
}
