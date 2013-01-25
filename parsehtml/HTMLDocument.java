package parsehtml;

import java.io.StringReader;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


public class HTMLDocument {

    private Document doc;
    private String body;

   // ����ָ���ģ�����HTML�﷨������ַ����й���һ��HTML�ĵ�
    
    public static HTMLDocument createHTMLDocument(String str) {
        DOMParser parser = new DOMParser();
        try {
            parser.parse(new InputSource(new StringReader(str)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HTMLDocument(str, parser.getDocument());
    }
       
    // url ��ҳ��URL��ַ
     // content �����ĵ���HTML�ַ���
     // doc ��HTML�ַ�����������Dom�ĵ��� 
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
     * ����HTML�ı��ı���
     * @return HTML�ı��ı���
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
     * �����ı������ĵ�DOM��
     * @return HTML������DOM��
     */
    public Document getDoc() {
        return doc;
    }

    /**
     * ��õ���ҳ���е���������
     * @return ��õ���ҳ���е���������
     */
    public String getBody() {
        return body;
    }
}
