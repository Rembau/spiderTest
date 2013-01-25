package sql_spider;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;

/**
 * Description:根据url获取内容
 * @author Administrator
 * 
 *
 */
@SuppressWarnings("unused")
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
		try {
			url=new URL(urlStr);
			/*System.out.println(url.getAuthority());
			System.out.println(url.getHost());
			System.out.println(url.getFile());
			System.out.println(url.getPath());
			System.out.println(url.getQuery());
			System.out.println(url.getRef());*/
			
		} catch (MalformedURLException e) {
			System.out.println(urlStr);
			e.printStackTrace();
			return "error";
		}
		GZIPInputStream gi=null;
		try {
			input=url.openStream();
			//gi=new GZIPInputStream(input);
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
		c.setHost("www.ahedu.gov.cn");
		String str=c.getContent("http://www.ahedu.gov.cn/vod/last");
		//http://www.baidu.com/s?tn=baiduhome_pg&bs=java%BD%E2%C2%EB&f=8&rsv_bp=1&rsv_spt=1&wd=Content-Encoding%3A+gzip&n=2&inputT=584
		System.out.println(str);
		System.out.println("===================================================");
		//System.out.println(c.getContent("http://view.news.qq.com/"));
		
		long j=System.currentTimeMillis();
		System.out.println("\n"+(j-i));
	}
}
