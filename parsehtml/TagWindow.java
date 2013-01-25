package parsehtml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * һ��HTML��Ǵ��ڴ��ı���ȡϵͳ�еĳ���
 * ������������ȡ�˱�Ǳ�����һЩ�������������糬�ı��ܶȵȵȡ�

 */
public class TagWindow {

    private Node node = null;
    private String text = "";
    private String anchorText = "";
    private int numInfoNodes = 0;

    /**
     * ��һ����׼��w3cNode����������һ��Tag����
     * @param node w3cNode����
     */
    public TagWindow(Node node) {
        this.node = node;
        text = getInnerText(node, false);
        anchorText = getAnchorText();

        if (node.getNodeType() == Node.ELEMENT_NODE) {
            numInfoNodes = getNumInfoNode((Element) node);
        }
    }

    /**
     * ƽ������������ƽ�����ǵ��㷨�е�һЩֵ��ĿǰΪ��Ч�ʣ�����ʹ�÷ǳ��ֲڵĽ��⺯��
     * @param x ����
     * @return ƽ����Ĳ���
     */
    private static double fn(double x) {
        if (x > 0.8f) {
            return 0.8f;
        }
        return x;
    }

    /**
     * ͨ��������HTMLȫ�Ĳ���������������ڸ�HTML�ĵ��е�Ȩ�ء�
     * @param totalT HTML�ĵ�����������
     * @param totalA HTML�ĵ����ܳ�����������
     * @param totalNumInfoNodes HTML�ĵ���<a href="#">infoNode</a>������
     * @return �ñ�ǩ������������HTML�ĵ����е�Ȩ��
     */
    public double weight(int totalT, int totalA, int totalNumInfoNodes) {
        double weight = 0;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element e = (Element) node;
            weight += Utility.isTableNodes(e) ? .1 : 0;
            weight += Utility.isLargeNode(e) ? .1 : 0;
            weight += 0.2 * fn(numInfoNodes / (double) (totalNumInfoNodes));
            weight -= Utility.containsInput(e) ? .5 : 0;
        }

        if (Utility.containsNoise(text)) {
            weight -= 0.5;
        }

        weight += 1.0 - anchorDensity();
        weight += share(totalA, totalT);
        return weight;
    }

    /**
     * ���壺ê�ı��������ı��ı�ֵ
     * @note �ر�ģ����text����anchorTextΪ�գ���ô���anchorDensityΪ0
     * @return
     */
    private double anchorDensity() {
        int anchorLen = anchorText.length();
        int textLen = text.length();
        if (anchorLen == 0 || textLen == 0) {
            return 0;
        }
        return anchorLen / (double) textLen;
    }

    /**
     * ͨ��������HTML��������ñ���ڸ�HTML�ĵ�����վ�ķݶ������
     * ���㷽���ǣ�alpha*������ռ�ı���-beta��������ռ��������ʽ�������������
     * @param totalA ��HTML�ĵ������ĳ������ı���������
     * @param totalT ��HTML�ĵ��������ı�������
     * @return ��������ռ�ı�����
     */
    private double share(int totalA, int totalT) {
        if (totalA == 0) {
            return 1.6 * fn((double) text.length() / totalT);
        }
        return 1.6 * fn((double) text.length() / totalT) - .8 * anchorText.length() / totalA;
    }

    /**
     *  ��ȡ�����ǩ�������ı�
     * @param viewMode �Ƿ������������ʾ�ķ�ʽ�����Ż������������Ŀո�ͻ��У���
     * @return �����ǩ��������ʵ���ı���
     */
    public String getInnerText(boolean viewMode) {
        if (viewMode) {
            return getInnerText(node, viewMode);
        } else {
            return text;
        }
    }

    /**
     * �����ǩ������infoNode�ĸ�����
     * @return �����ǩ������infoNode�ĸ�����
     */
    public int getNumInfoNodes() {
        return numInfoNodes;
    }

    /**
     * �����ǩ��������ê�ı��ַ�
     * @return �����ǩ��������ê�ı��ַ�
     */
    private String getAnchorText() {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            return getAnchorText((Element) node);
        }
        return "";
    }

    /**
     * ��ȡ��������w3cNode����
     * @return
     */
    public Node getNode() {
        return node;
    }

    /**
     * ��ȡָ����ǩ������������Ч����
     * @param node ��ָ���ı�ǩ
     * @param viewMode viewMode �Ƿ������������ʾ�ķ�ʽ�����Ż������������Ŀո�ͻ��У���
     * @return ��ȡָ����ǩ������������Ч����
     */
    public static String getInnerText(Node node, boolean viewMode) {
        if (node.getNodeType() == Node.TEXT_NODE) {
            return Utility.filter(((Text) node).getData());
        }
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            if (Utility.isInvalidElement(element)) {
                return "";
            }
            StringBuilder nodeText = new StringBuilder();
            //replace the line break with space,
            //beacause inappropriate line break may cause the paragraph corrupt.
            if (viewMode && element.getTagName().equals("BR")) {
                nodeText.append(" ");
            }
            //let the appearance tags stay
            if (viewMode && Utility.isHeading(element)) {
                nodeText.append("<" + element.getTagName() + ">");
            }
            NodeList list = element.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                String t = getInnerText(list.item(i), viewMode);
                //whether we need to add extra space?
                if (viewMode && Utility.needSpace(element)) {
                    t += " ";
                }
                nodeText.append(t);
            }
            if (viewMode && Utility.isHeading(element)) {
                nodeText.append("</" + element.getTagName() + ">");
            }
            //break the line, if the element is a REAL BIG tag, such as DIV,TABLE
            if (viewMode &&
                    Utility.needWarp(element) &&
                    nodeText.toString().trim().length() != 0) {
                nodeText.append("\r\n");
            }
            return nodeText.toString().replaceAll("[\r\n]+", "\r\n");
        }

        return "";
    }

    /**
     * �����ǩ��������ê�ı��ַ�
     * @param e ��ָ����w3cHTMLԪ��
     * @return �����ǩ��������ê�ı��ַ�
     */
    public static String getAnchorText(Element e) {
        StringBuilder anchorLen = new StringBuilder();
        // get anchor text length
        NodeList anchors = e.getElementsByTagName("A");
        for (int i = 0; i < anchors.getLength(); i++) {
            anchorLen.append(getInnerText(anchors.item(i), false));
        }
        return anchorLen.toString();
    }

    /**
     * ��ǰ����ڵ��°������ٸ�InfoNode?
     * @param e ��������w3cԪ��
     * @return ��ǰ����ڵ��°������ٸ�InfoNode?
     */
    public static int getNumInfoNode(Element e) {
        int num = Utility.isInfoNode(e) ? 1 : 0;
        NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                num += getNumInfoNode((Element) children.item(i));
            }
        }
        return num;
    }
}