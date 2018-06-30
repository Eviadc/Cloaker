package cn.eviadc.cloaker.analyzer;

import java.util.ArrayList;

import cn.eviadc.cloaker.Entry;
import cn.eviadc.cloaker.LuceneEntrance;

/**
 * 分词模块，用于对分句的原句进行分词操作。
 * 
 * @author Nathan Wang
 *
 */
public class WordDivider_IK implements PaperAnalyzer
{
	/**
	 * 分词模块的主要功能方法，调用此方法进行分句的分词操作。
	 * 
	 * @param a 需要进行分词的分句对象。
	 * @throws Exception 若参数a的类型错误则抛出自定义的“参数错误异常”。
	 * @see cn.eviadc.cloaker.analyzer.PaperAnalyzer#Analyze(java.lang.Object)
	 */
	@Override
	public void Analyze(Object a) throws Exception 
	{
		/*
		 * 判断传入参数是否为Entry类的对象，若不是则抛出一个
		 * 自定义“错误参数异常”。
		 */
		if(a.getClass().getName() != Entry.class.getName())
			throw new WrongArgumentException(Entry.class.getName(), a.getClass().getName());
		
		Entry entry = (Entry) a;
		
		/*
		 * 使用分词工具对分句（Entry）的rawContent分词，并将结果
		 * 存入分句对象的content中。
		 */
		ArrayList<String> s = LuceneEntrance.analysis(entry.getRawContent());
		entry.setContent(s);
	}

	/**
	 * 返回此分词模块Analyze方法所需要的参数类型名。
	 * 
	 * @return Analyze方法所需要的参数类型名。
	 * @see cn.eviadc.cloaker.analyzer.PaperAnalyzer#targetClassName()
	 */
	@Override
	public String targetClassName() 
	{
		return Entry.class.getName();
	}
	
}
