package cn.eviadc.cloaker;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 使用lucene_3.6 和 IKAnalyzer2012_u6.
 * 
 * @author Nathan
 *
 */
public class LuceneEntrance
{
	public static void main(String[] args) throws IOException 
	{	
		Analyzer anal=new IKAnalyzer(true);
		String s = "小明讲了个笑话真好笑啊哈哈哈哈哈哈啊哈";

        TokenStream tokenStream = anal.tokenStream("content", new StringReader(s));  
        CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);

        while (tokenStream.incrementToken()) 
        {  
			System.out.print(charTermAttribute.toString()+"|");
        }  
        
        tokenStream.close();
        anal.close();
		
	}
	
	/**
	 * 使用IKAnalyzer对字符串s进行分词。
	 * 
	 * @param s 待处理字符串。
	 * @return 分词列表。
	 * @throws IOException 可能会抛出的I/O异常。
	 */
	public static ArrayList<String> analysis(String s) throws IOException
	{
		//初始化IKAnalyzer
		Analyzer anal=new IKAnalyzer(true);
		ArrayList<String> al = new ArrayList<String>();

		//由StringReader流建立TokenStream并通过charTermAttribute遍历
        TokenStream tokenStream = anal.tokenStream("content", new StringReader(s));
        CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);

        try {
            while (tokenStream.incrementToken()) 
            	al.add(charTermAttribute.toString());

			tokenStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        anal.close();
        return al;
	}
}
