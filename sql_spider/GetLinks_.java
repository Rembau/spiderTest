package sql_spider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:根据页面的全部内容获取URL
 * @author Administrator
 *
 */
public class GetLinks_ {
	private DispatchQueue dq=new DispatchQueue();
	//private Pattern p=Pattern.compile("<[a]\\s+?[^>]*?>[^<]+?</[a]\\s*?>");//获取title <\\s*?(title)\\s*?>[\\s\\S]*?</\\s*?(title)\\s*?>
	//private Pattern p=Pattern.compile(
			//"\\bhref=['\"]?([^'\">\\s#]*)(?:.html|.htm|.jsp|.asp|/)?(?:\\?(?:\\S+=\\S+&)+)?([\\s>'\"])?|" +
			//"\\b(http://[^'\">\\s]*)(?:.html|.htm|.jsp|.asp|/)?(?:\\?(?:\\S+=\\S+&)+)?([\\s>'\"])?",Pattern.CASE_INSENSITIVE);
	//private Pattern p=Pattern.compile("\\bvalue=['\"]?(http://[^'\"\\s]*)['\"\\s]+>",Pattern.CASE_INSENSITIVE);
	//private Pattern p=Pattern.compile("\\b(http://[^'\">\\s]*)([\\s>'\"])?",Pattern.CASE_INSENSITIVE);
	private Pattern p=Pattern.compile("<a\\s+href=['\"]?([^\\s>'\"]*)['\"\\s]+[^>]*>|\\bvalue=['\"]?(http://[^'\"\\s]*)['\"\\s]+>",Pattern.CASE_INSENSITIVE);
	private Matcher m;
	public GetLinks_(){
	}
	public void setD(DispatchQueue dq){
		this.dq=dq;
	}
	@SuppressWarnings("unused")
	public void getLink(String url,String content){
		m=p.matcher(content);
		int n=1;
		String hostStr="";
		try {
			URL u=new URL(url);
			hostStr=u.getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		String link="";
		while(m.find()){
			String str_1=m.group();
			
			//System.out.println(n+":"+str_1);
			link=m.group(1);
			if(str_1.startsWith("value")){
				link=m.group(2);
			}
			if(link.startsWith("/")){
				link="http://"+hostStr+""+link;
			}
			else if(link.startsWith("../")){
				link=url.substring(0,url.substring(0,url.lastIndexOf("/")).lastIndexOf("/"))+link.replace("..","");
			}
			//System.out.println(link);
			n++;
			dq.add(link);
		}
		//System.out.println("执行!==============");
		//dq.addExecute();
	}
	public static void main(String[] args) {
		GetContent_http c=new GetContent_http();
		GetLinks_ gl=new GetLinks_();
		String ip="http://www.hsu.edu.cn/";
		GetContent_http.setHost("www.hsu.edu.cn");
		gl.getLink(ip,c.getContent(ip));
		//System.out.println(c.getContent("http://www.hsu.edu.cn/"));
	}
}

