package test;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
public class Test {
	Test(){}
	public void sockettest(){
		Socket  socket=new Socket();
		InetSocketAddress address=new InetSocketAddress("192.168.2.2",80);
		InetSocketAddress address1=new InetSocketAddress("192.168.2.1",80);
		try {
			socket.connect(address,200);
			System.out.println("23131");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket.connect(address1,200);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void ram(){
		ArrayList<ByteBuffer> ab=new ArrayList<ByteBuffer>();
		ByteBuffer bb=ByteBuffer.allocate(1024*100);
		for(int i=0;i<100000;i++){
			ab.add(bb);
			System.out.println(i);
		}
	}
	public void ipurl(){
		try {
			InetAddress adr=InetAddress.getByName("http://www.hsu.edu.cn/");
			System.out.println(adr);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String args[]){
		Test test=new Test();
		test.sockettest();
}
}
