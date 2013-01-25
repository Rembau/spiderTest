package test_getUrls;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:����ҳ���ȫ�����ݻ�ȡURL
 * @author Administrator
 *
 */
public class GetLinks {
	private DispatchQueue dq=new DispatchQueue();
	//private Pattern p=Pattern.compile("<[a]\\s+?[^>]*?>[^<]+?</[a]\\s*?>");//��ȡtitle <\\s*?(title)\\s*?>[\\s\\S]*?</\\s*?(title)\\s*?>
	//private Pattern p=Pattern.compile("http://[^>]*[>]");
	private Pattern p=Pattern.compile("\\bhref=['\"]?[^'\">\\s]*([\\s>'\"])?");
	private Matcher m;
	public GetLinks(){
	}
	public void setD(DispatchQueue dq){
		this.dq=dq;
	}
	public void getLink(String url,String content){
		m=p.matcher(content);
		String strurl=null;
		int start,end;
		int n=0;
		while(m.find()){
			try{
				strurl=m.group();  //�������磺<a href=""></a>
				if((start=strurl.indexOf("href")+5)==4){  //<a>����Ҫ��href=
					continue;
				}
				strurl=strurl.substring(start);   //��ȡ����"http://host/index.html" >word</a>
				//System.out.println(strurl);
				if(strurl.startsWith("\"")){           //"http://host/index.html" >word</a>
					strurl=strurl.replaceFirst("\"", "");  //http://host/index.html" >word</a>
					end=strurl.indexOf("\"");
				}
				else if(strurl.startsWith("'")){     //'http://host/index.html' >word</a>
					strurl=strurl.replaceFirst("'", "");//http://host/index.html" >word</a>
					end=strurl.indexOf("'");
				}
				else if(strurl.indexOf("html")!=-1){// http://host/index.html >word</a>
					end=strurl.indexOf("html")+4;//http://host/index.html" >word</a>
				}
				else {  //"http://host/index.html" name="">word</a>
					end=strurl.indexOf(" ");       
				}
				if(end!=-1){
					strurl=strurl.substring(0, end);
				}
				if(strurl.indexOf(">")!=-1) {
					end=strurl.indexOf(">");   // "http://host/index.html">word</a>
				}
				//System.out.println(strurl);<a href='/s/1/t/3/p/2/c/15/list.htm' id="p2c15">ѧУ�ſ�</a>
				//System.out.println(end);
				strurl=strurl.substring(0, end);
				//System.out.println(strurl);
				
				if(!isPage(url,strurl)){			//�ж��Ƿ���ҳ��
					continue;
				}
				
				strurl=manage(strurl,url);          //url����			
				dq.addW(strurl);
				content=null;
				System.out.println(n+" "+m.group());
				n++;
				//System.out.println(strurl);
			}catch(Exception e){
				//e.printStackTrace();
				//System.out.println("����һ����������"+m.group());
			}
		}
		//System.out.println(links);
	}
	public String manage(String strurl,String url) throws Exception{
		strurl=strurl.replaceAll("\"","");
		strurl=strurl.replaceAll("'","");
		
		//�ж�ҳ���������·��
		if(strurl.startsWith("/")){
			URL u=new URL(url);
			strurl="http://"+u.getHost()+""+strurl;
		}
		else if(strurl.startsWith("../")){
			strurl=url.substring(0,url.substring(0,url.lastIndexOf("/")).lastIndexOf("/"))+strurl.replace("..","");
		}
		else if(!strurl.startsWith("http")){
			strurl=url.substring(0, url.lastIndexOf("/")+1)+strurl;
		}
		int n;
		
		//��λ�ñ�־��������ַ�ɾ��
		if((n=strurl.indexOf("#"))!=-1){
			strurl=strurl.substring(0,n);
		}
		return strurl;
	}
	public boolean isPage(String url,String strurl){
		//�ж�# js
		if(strurl.equals("") || strurl.startsWith("javascript:")){
			return false;
		}
		//�ж��Ƿ���ҳ��
		if(strurl.indexOf(".htm")!=-1 || strurl.indexOf(".HTML")!=-1 || strurl.indexOf(".jsp")!=-1 || strurl.indexOf(".asp")!=-1 ||
				strurl.indexOf(".php")!=-1 || strurl.indexOf(".JSP")!=-1 || strurl.indexOf(".ASP")!=-1 ||
				strurl.indexOf(".PHP")!=-1 || strurl.indexOf(".shtml")!=-1){
			return true;
		}
		//�ж��Ƿ����ļ�
		if(strurl.endsWith(".zip") || strurl.endsWith(".doc") ||strurl.endsWith(".rar") || strurl.endsWith(".pdf") || 
				strurl.endsWith(".xls")){
			return false;
		}
		return true;
	}
	public static void main(String[] args) {
		GetContent_http c=new GetContent_http();
		GetLinks gl=new GetLinks();
		String ip="http://www.ku6.com/";
		GetContent_http.setHost("www.ku6.com");
		gl.getLink(ip,c.getContent(ip));
		//System.out.println(c.getContent("http://www.hsu.edu.cn/"));
	}
}
