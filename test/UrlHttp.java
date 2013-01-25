package test;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class UrlHttp {

	String ip="192.168.0.53";
	int port=80;
	UrlHttp(){
		try {
			URL url=new URL("http://"+ip+":"+port);
			try {
				InputStream in=url.openStream();
				int n=0;
				byte b[]=new byte[10000];
				while((n=in.read(b))!=-1){
					System.out.println(new String(b,0,n));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String args[]){
		long i=System.currentTimeMillis();
		new UrlHttp();
		long j=System.currentTimeMillis();
		System.out.println("\n"+(j-i));
	}
}
