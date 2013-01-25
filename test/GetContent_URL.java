package test;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;

/**
 * Description:根据url获取内容
 * @author Administrator
 * 
 *
 */
public class GetContent_URL {
	private String urlStr;
	private URL url;
	private String charset="gb2312";
	private BufferedInputStream in=null;
	private InputStream input=null;
	private ByteBuffer bb;
	private byte b_[];
	private String strContent;
	private String host;
	int count;
	public GetContent_URL(){
		bb=ByteBuffer.allocate(1024*1000);
		b_=new byte[1024*1000];
	}
	/**
	 * 
	 * @param host:主机的IP地址
	 */
	public void setHost(String host){
		this.host=host;
	}
	/**
	 * Description:获取网页的内容
	 * @return
	 */
	public String getContent(String strUrl){
		this.urlStr=strUrl;
		System.out.println(urlStr+"  开始获取！");
		try {
			url=new URL(urlStr);
			/*System.out.println(url.getAuthority());
			System.out.println(url.getHost());
			System.out.println(url.getFile());
			System.out.println(url.getPath());
			System.out.println(url.getQuery());
			System.out.println(url.getRef());*/
			
			String strHost=null;
			strHost=url.getHost();
			URL url1=new URL("http://"+strHost);
			URL url2=new URL("http://"+host);
			if(!url1.sameFile(url2)){
				System.out.println(strHost+"是外部链接！"+host);
				return "external";
			}
		} catch (MalformedURLException e) {
			System.out.println(urlStr);
			e.printStackTrace();
			return "error";
		}
		try {
			input=url.openStream();
			in=new BufferedInputStream(input);
		} catch (FileNotFoundException e_1){
			System.out.println(urlStr+"=========页面找不到=========");
			return "error";
		} catch (IOException e) {
			System.out.println(urlStr);
			e.printStackTrace();
			return "error";
		}
		String str=null;
		String charset_="";
		int mark=0,n=0,count=0;
		byte b[]=new byte[1024*2];
		bb.clear();
		try {
			while((n=in.read(b))!=-1){
				bb.put(b, 0, n);
				if(mark ==0 && (str=new String(b,0,n)).indexOf("charset=")!=-1){
					if(str.indexOf("<htm")==-1 && str.indexOf("<HTML")==-1){
						return "garbled";
					}
					charset_=str.substring(str.indexOf("charset=")+8);
					charset_=charset_.replaceAll("\"", "");
					mark=1;
				}
				count+=n;
			}
			if(charset_.startsWith("gb") || charset_.startsWith("GB")){
				charset="gbk";
			}
			else if(charset_.startsWith("UTF") || charset_.startsWith("utf")){
				charset="UTF-8";
			}
			else if(charset_.startsWith("iso") || charset_.startsWith("ISO")){
				charset="iso-8859-1";
			}
			in.close();
			in=null;
			input.close();
			input=null;
			
			count=bb.position();
			bb.flip();
			bb.get(b_, 0, count);
			
			strContent=new String(b_,0,count,charset);
			System.out.println(charset+" "+count+" "+strContent.length());
		    return strContent;
		} catch (IOException e) {
			System.out.println(urlStr);
			e.printStackTrace();
			return "error";
		}
	}
	public static void main(String args[]){
		long i=System.currentTimeMillis();
		GetContent_URL c=new GetContent_URL();
		c.setHost("192.168.0.53");
		String str=c.getContent("http://192.168.0.53");
		System.out.println(str);
		System.out.println("===================================================");
		//System.out.println(c.getContent("http://view.news.qq.com/"));
		
		long j=System.currentTimeMillis();
		System.out.println("\n"+(j-i));
	}
}

