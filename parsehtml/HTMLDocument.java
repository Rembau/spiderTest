package parsehtml;

import java.io.StringReader;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


public class HTMLDocument {

    private Document doc;
    private String body;

   // 利用指定的，符合HTML语法规则的字符串中构造一个HTML文档
    
    public static HTMLDocument createHTMLDocument(String str) {
        DOMParser parser = new DOMParser();
        try {
            parser.parse(new InputSource(new StringReader(str)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HTMLDocument(str, parser.getDocument());
    }
       
    // url 网页的URL地址
     // content 描述文档的HTML字符串
     // doc 由HTML字符串解析出的Dom文档。 
    protected HTMLDocument(String content, org.w3c.dom.Document doc) {
        this.doc = doc;
        parse();
    }

    private void parse() {
        Parse p = new Parse(doc);
        p.parse();
        body = p.getMainBody();
    }

/**
     * 返回HTML文本的标题
     * @return HTML文本的标题
     */
    @SuppressWarnings("finally")
	public String getTitle() {
    	String s=null;
    try{ 
    	 s= doc.getElementsByTagName("TITLE").item(0).getTextContent();
    	
    }catch(Exception e){
    	s= "NO Title";
    } finally{
    	return s;
        
    }  
      
        
    }

    /**
     * 返回文本构建的的DOM树
     * @return HTML构建的DOM树
     */
    public Document getDoc() {
        return doc;
    }

    /**
     * 获得的网页当中的主题正文
     * @return 获得的网页当中的主题正文
     */
    public String getBody() {
        return body;
    }
}
