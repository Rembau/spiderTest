package test_bot;

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
	int port=80;
	String host;
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
	public boolean setHost(String host){
		this.host=host;
		if(socket!=null){
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return connection();
	}
	@SuppressWarnings(value = { "all" })
	public boolean connection(){
		try {
			socket=new Socket();
			InetSocketAddress address=new InetSocketAddress(host,port);
			socket.connect(address, 1000);
			out=new DataOutputStream(socket.getOutputStream());
			in_=socket.getInputStream();
			inR = new DataInputStream(in_);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("连接失败!");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	@SuppressWarnings("deprecation")
	public String getContent(String url){
		try {
		    if(!connection()){
		    	System.out.println(url+"连接超时！");
		    	return "error";
		    }
			try {
				String http=requestStr(url);
				out.write(http.getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			StringBuilder headb=new StringBuilder();
			String str="";
			int mark_=0,count,markLine=1;
			boolean flag_gzip=false;

			while((str=inR.readLine())!=null){
				if(markLine==1){
					System.out.println(str);
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
			System.out.println(charset);
			
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
		//String req="GET http://"+host+"/nothing.html HTTP/1.0\r\n\r\n";
		/*if((url.indexOf("?"))==-1){
			return "GET "+url+" HTTP/1.0\r\n\r\n";
		}
		else {
			
		}*/
		return "GET "+url+" HTTP/1.0\r\n\r\n";
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
		return charset_;
	}
	public static void main(String args[]){
		long i=System.currentTimeMillis();
		GetContent_http st=new GetContent_http();
		String content="";
		if(st.setHost("www.baidu.com")){
			content=st.getContent("http://www.hsu.edu.cn/s/1/t/3/4b/94/info19348.htm");//http://www.tudou.com/
			System.out.println("==========================");
			content=st.getContent("http://www.hsu.edu.cn/s/1/t/3/4b/94/info19348.htm");
		}
		System.out.println(content);
		long j=System.currentTimeMillis();
		System.out.println("\n"+(j-i));
	}
}
