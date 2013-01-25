package test_getUrls;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:根据页面的全部内容获取URL
 * @author Administrator
 *
 */
public class GetLinks {
	private DispatchQueue dq=new DispatchQueue();
	//private Pattern p=Pattern.compile("<[a]\\s+?[^>]*?>[^<]+?</[a]\\s*?>");//获取title <\\s*?(title)\\s*?>[\\s\\S]*?</\\s*?(title)\\s*?>
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
				strurl=m.group();  //返回形如：<a href=""></a>
				if((start=strurl.indexOf("href")+5)==4){  //<a>里面要有href=
					continue;
				}
				strurl=strurl.substring(start);   //获取形如"http://host/index.html" >word</a>
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
				//System.out.println(strurl);<a href='/s/1/t/3/p/2/c/15/list.htm' id="p2c15">学校概况</a>
				//System.out.println(end);
				strurl=strurl.substring(0, end);
				//System.out.println(strurl);
				
				if(!isPage(url,strurl)){			//判断是否是页面
					continue;
				}
				
				strurl=manage(strurl,url);          //url处理			
				dq.addW(strurl);
				content=null;
				System.out.println(n+" "+m.group());
				n++;
				//System.out.println(strurl);
			}catch(Exception e){
				//e.printStackTrace();
				//System.out.println("处理一个错误链接"+m.group());
			}
		}
		//System.out.println(links);
	}
	public String manage(String strurl,String url) throws Exception{
		strurl=strurl.replaceAll("\"","");
		strurl=strurl.replaceAll("'","");
		
		//判断页面相对主机路径
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
		
		//把位置标志符后面的字符删掉
		if((n=strurl.indexOf("#"))!=-1){
			strurl=strurl.substring(0,n);
		}
		return strurl;
	}
	public boolean isPage(String url,String strurl){
		//判断# js
		if(strurl.equals("") || strurl.startsWith("javascript:")){
			return false;
		}
		//判断是否是页面
		if(strurl.indexOf(".htm")!=-1 || strurl.indexOf(".HTML")!=-1 || strurl.indexOf(".jsp")!=-1 || strurl.indexOf(".asp")!=-1 ||
				strurl.indexOf(".php")!=-1 || strurl.indexOf(".JSP")!=-1 || strurl.indexOf(".ASP")!=-1 ||
				strurl.indexOf(".PHP")!=-1 || strurl.indexOf(".shtml")!=-1){
			return true;
		}
		//判断是否是文件
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
