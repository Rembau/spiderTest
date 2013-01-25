package test_bot;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;

public class Test_1 {
	public static void main(String args[]){
		long i=System.currentTimeMillis();
		URL u=null;
		try {
			u=new URL("http://www.ip138.com/");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataInputStream in=null;
		try {
			in=new DataInputStream(u.openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] b=new byte[1024]; 
		ByteBuffer bb=ByteBuffer.allocate(1024*100);
		int n=0;
		int m=0;
		try {
			while((n=in.read(b))!=-1){
				bb.put(b, 0, n);
				m=m+n;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(bb.array().length);
		//if(new String(bb.array(),0,m,)){}
		try {
			System.out.println(new String(bb.array(),0,m,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long j=System.currentTimeMillis();
		System.out.println("\n"+(j-i));
	}
}
