package test_getUrls;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;

/**
 * 
 * Description:获取网页代码,setURL(url)设置url,可以重复设置,调用getContent(),获取内容
 *
 */
public class GetContent_http {
	static int port=80;
	static String host;
	String content,head;
	InputStream in_=null;
	DataInputStream  inR;
	BufferedInputStream inB;
	GZIPInputStream inG;
	DataOutputStream out=null;
	Socket socket=null;
	String charset="gbk",charset_="";
	private ByteBuffer bb;
	private byte b_[];
	public GetContent_http(){
		bb=ByteBuffer.allocate(1024*1000);
		b_=new byte[1024*1000];
	}
	public static boolean setHost(String host){
		Socket socket=null;
		try{
			GetContent_http.host=host;
			socket=new Socket();
			InetSocketAddress address=new InetSocketAddress(host,port);
			socket.connect(address, 20000);
			socket.close();
		} catch (UnknownHostException e) {
			System.out.println("无法识别主机!");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("连接失败!");
			return false;
		} finally{
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	public boolean connection(){
		try {
			socket=new Socket();
			InetSocketAddress address=new InetSocketAddress(host,port);
			socket.connect(address, 20000);
			out=new DataOutputStream(socket.getOutputStream());
			in_=socket.getInputStream();
			inR = new DataInputStream(in_);
			
		} catch (UnknownHostException e) {
			System.out.println("无法识别主机!");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("连接失败!");
			return false;
		}
		return true;
	}
	@SuppressWarnings("deprecation")
	public String getContent(String url){
		try {
		    if(!connection()){
		    	return "error socket 连接错误！";
		    }
			try {
				String http=requestStr(url);
				System.out.println(http);
				out.write(http.getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			StringBuilder headb=new StringBuilder();
			String str="";
			int mark_=0,count,markLine=1,markCode=0;
			boolean flag_gzip=false;
			while((str=inR.readLine())!=null){
				//System.out.println(str);
				if(markLine==1){
					if(str.indexOf("30")!=-1){
						markCode=30;
						markLine++;
						continue;
					}
					if(str.indexOf("20")==-1){
						return "error"+str.substring(9,9+3);
					}
					markLine++;
				}
				if(markCode==30 && str.indexOf("Location:")!=-1){
					return str.substring(10).trim();
				}
				//获取字符编码
				if(mark_==0 && str.indexOf("charset=")!=-1){
					charset_=str.substring(str.indexOf("charset=")+8);
					mark_++;
				}
				//流是否经过gzip压缩
				if(str.indexOf("gzip")!=-1){
					flag_gzip=true;
				}
				//head部分取完
				if(str.length()==0){
					break;
				}
				headb.append(str+"\n");
			}
			if(flag_gzip){
				getContent_gzip();
			}
			else {
				getContent_();
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
			head = new String(headb);
			//System.out.println(head);
			
			count=bb.position();
			bb.flip();
			bb.get(b_, 0, count);
			content=new String(b_,0,count,charset);
			
			in_.close();
			in_=null;
			inR.close();
			inR=null;
			socket.close();
			socket=null;
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public String requestStr(String url){
		int i=url.lastIndexOf("/");
		if(i!=url.length()-1 && url.substring(i).indexOf(".")==-1){
			url=url+"/";
		}
		//String req="GET http://"+host+"/nothing.html HTTP/1.0\r\n\r\n";
		/*if((url.indexOf("?"))==-1){
			return "GET "+url+" HTTP/1.0\r\n\r\n";
		}
		else {
			
		}*/
		return "GET "+url+" HTTP/1.0\r\n\r\n";
		//return "GET /a/20111020/000025.htm HTTP/1.1\r\nUser-Agent: Java/1.6.0_14\r\nHost: view.news.qq.com\r\nAccept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\r\n";
	}
	public String getContent_() throws Exception{
		bb.clear();
		inB = new BufferedInputStream(in_);
		byte b[]=new byte[1024*2];
		int n=0,mark_=0;
		String charset_=null,str=null;
		while((n=inB.read(b))!=-1){
			bb.put(b, 0, n);
			if(mark_ ==0 && (str=new String(b,0,n)).indexOf("charset=")!=-1){
				charset_=str.substring(str.indexOf("charset=")+8);
				charset_=charset_.replaceAll("\"", "");
				mark_=1;
			}
		}
		inB.close();
		inB=null;
		return charset_;
	}
	public String getContent_gzip() throws Exception{
		bb.clear();
		inG=new GZIPInputStream(in_);
		byte b[]=new byte[1024*2];
		int n=0,mark_=0;
		String charset_=null,str=null;
		while((n=inG.read(b))!=-1){
			bb.put(b, 0, n);
			if(mark_ ==0 && (str=new String(b,0,n)).indexOf("charset=")!=-1){
				charset_=str.substring(str.indexOf("charset=")+8);
				charset_=charset_.replaceAll("\"", "");
				mark_=1;
			}
		}
		inG.close();
		inG=null;
		return charset_;
	}
	 public static String filter(String text) {
	        text = text.replaceAll("[^\u4e00-\u9fa5|a-z|A-Z|0-9|０-９,.，。:；：><?…》《\"”“!\\-?|\\s|\\@]", "");
	        text = text.replaceAll("[【】]", " ");
	        text = text.replaceAll("[\r\n]+", "\r\n");
	        text = text.replaceAll("\n+", "\n");
	        text = text.replaceAll("\\|", "");
	        text = text.replaceAll("\\s+", " ");
	        text = text.trim();
	        return text;
	    }
	@SuppressWarnings("static-access")
	public static void main(String args[]){
		long i=System.currentTimeMillis();
		GetContent_http st=new GetContent_http();
		String content=null;
		if(st.setHost("www.hsu.edu.cn")){
	//http://www.baidu.com/s?tn=baiduhome_pg&bs=java%BD%E2%C2%EB&f=8&rsv_bp=1&rsv_spt=1&wd=Content-Encoding%3A+gzip&n=2&inputT=584
	// /s?tn=baiduhome_pg&bs=java%BD%E2%C2%EB&f=8&rsv_bp=1&rsv_spt=1&wd=Content-Encoding%3A+gzip&n=2&inputT=584
			//下面的链接可以 上面的不可以
			content=st.getContent("http://www.hsu.edu.cn/");//http://finance.qq.com/money/futures
			while(content.startsWith("http:")){
				content=st.getContent(content);
			}
		}
		System.out.println(filter(content));
		long j=System.currentTimeMillis();
		System.out.println("\n"+(j-i));
	}
}
