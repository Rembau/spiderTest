package parsehtml;

import org.w3c.dom.Document;

public class Parse {
//解析网页中的文本和链接
 private TextExtractor textExtractor=null;
 private String mainBody=null;
 
 public  Parse(Document doc ){
	 textExtractor=new TextExtractor(doc);
 }
 public void parse(){
		mainBody=textExtractor.extract();
	}
	
public String getMainBody(){
	return  mainBody;
}
}
