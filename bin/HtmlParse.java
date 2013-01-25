package bin;

import java.io.StringReader;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


public class HtmlParse {
	private Document doc;
	public HtmlParse(String str){
		/*DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
    	try {
			DocumentBuilder builder=factory.newDocumentBuilder();
			try {
				File f=new File("bin//11.html");
				doc = builder.parse(f);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}*/
		DOMParser parser = new DOMParser();
        try {
            parser.parse(new InputSource(new StringReader(str)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        doc = parser.getDocument();
	}
	public String getTitle(){
		String t="";
		try{
			t= doc.getElementsByTagName("TITLE").item(0).getTextContent();
		} catch(Exception e){
			System.out.println("√ª”–title±Í«©!");
			return t;
		}
		return t;
	}
	public String getBody(){
		String b;
		TextExtractor textExtractor=new TextExtractor(doc);
		b=textExtractor.extract();
		return b;
	}
	public static void main(String[] args) {
	}

}
