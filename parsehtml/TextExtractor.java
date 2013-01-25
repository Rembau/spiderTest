/**
 * the package id used to optimize the dom tree
 */
package parsehtml;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**

 * <pre>
 *      ����ཫ���е������ı���һƪHTML�ĵ�����ȡ���������ҷֳ����ɶ��䡣
 *      @note ���� ��ָһƪHTML�ĵ��У�������Լ��е�һ�����֡�
 *      ÿһ����ȡ�����Ķ��佫����һ��Ȩ�ء����Ȩ���Ǹö����HTML�ĵ�����Ĺ��׳̶�
 *
 *      Usage:
 *      BufferedReader reader = new BufferedReader(new FileReader(...));
 *		DOMParser parser = new DOMParser();
 *		parser.parse(new InputSource(reader));
 *		Document doc = parser.getDocument();

 *		TextExtractor extractor = new TextExtractor(doc);
 *  	String text = extractor.extract();
 *  	System.out.println(text);
 *  </pre>
 */
public class TextExtractor {

    /**
     * ��HTML�ĵ����ж����������֣�
     */
    private int totalTextLen = 0;
    /**
     * ��HTML�ĵ����ж��ٳ��������֣�
     */
    private int totalAnchorTextLen = 0;
    /**
     * ��HTML�ĵ����ж���infoNodes?
     */
    private int totalNumInfoNodes = 0;
    /**
     * ����б�
     */
    private List<TagWindow> windowsList = new ArrayList<TagWindow>();
    /**
     * w3cHTML�ĵ�ģ��
     */
    private Document doc;

    /**
     * ���ýo����W3C�ęn����ģ�͘���һ��TextExtractor
     * @param doc ��������W3C�ĵ�����ģ��
     */
    public TextExtractor(Document doc) {
        super();
        this.doc = doc;

    }

    /**
     * ɾ���ĵ��е�һЩ��Ȼ�������������Ϣ�Ľڵ㣬����script,style,�ȵȣ����ǽ�Ӱ�����ǵ��ı���ȡ���ķ�����
     * @param e ����Ҫ�����w3c�ڵ�
     */
    private void cleanup(Element e) {
    	
        NodeList c = e.getChildNodes();
        //System.out.println("c.getLength()="+c.getLength());
        for (int i = 0; i < c.getLength(); i++) {
            if (c.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element t = (Element) c.item(i);
                if (Utility.isInvalidElement(t)) {
                    e.removeChild(c.item(i));
                } else {
                    cleanup(t);
                }
            }
        }
    }

    /**
     * ��ȡHTML�ı���Ϣ�����ҷֶΣ�Ϊÿһ���ı�����������Դ�֡�
     * @return ����ȡ����������Ϣ
     */
    public String extract() {
    	  String bodyText = "";
        long s = System.currentTimeMillis();
        Node body = doc.getElementsByTagName("BODY").item(0);
    //    System.out.print("boo=="+"ddddddddddddddddddddd");
        //cleanup, remove the invalid tags,
       // boolean boo= body.hasChildNodes();
     //   System.out.print("boo=="+boo);
     try{
    	 cleanup((Element) body);
     }  catch(Exception e){
    	 
    	 System.out.print("body=="+"bodyΪ��");
    	 return  bodyText = "";
     }

        totalTextLen = TagWindow.getInnerText(body, false).length();
        // get anchor text length
        totalAnchorTextLen = TagWindow.getAnchorText((Element) body).length();

        totalNumInfoNodes = TagWindow.getNumInfoNode((Element) body);

        extractWindows(body);

      
        if (windowsList.size() == 0) {
            bodyText = "";
        } else {
            //get the max score
            Collections.sort(windowsList, new Comparator<TagWindow>() {

                public int compare(TagWindow t1, TagWindow t2) {
                    if (t1.weight(totalTextLen, totalAnchorTextLen, totalNumInfoNodes) >
                            t2.weight(totalTextLen, totalAnchorTextLen, totalNumInfoNodes)) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            });
            TagWindow max = windowsList.get(windowsList.size() - 1);
            //print the method excution duration
            System.out.println("���������ʱ\t" + (System.currentTimeMillis() - s) + "\t");
            bodyText = max.getInnerText(true);
        }
        return bodyText;
    }

    /**
     * ����ÿ��Node���󣬰�ÿ��Node���洢��taglist���С�
     * @param node ����Ҫ������w3cNode����
     */
    private void extractWindows(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            return;
        }

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            if (Utility.isInvalidElement(element)) {
                return;
            }
            if (!node.getTextContent().trim().equals("")) //add the tags
            {
                windowsList.add(new TagWindow(node));
            }
            NodeList list = element.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                extractWindows(list.item(i));
            }
        }
    }
}
