package spider_;
import java.net.*;
import java.io.*;
//import java.net.InetSocketAddress;
import java.util.Date;
public class PortScan {
	 StringBuffer info=new StringBuffer();
	 int port=80;
	 boolean bool=false;
	//Window window;
	UrlThread thread;
    
	public PortScan(){
		//Scan();
	}
	public  boolean  Scan(String ip){
		bool=false;
		Socket  socket=new Socket();
		try {				
			    InetSocketAddress address=new InetSocketAddress(ip,port);
				socket.connect(address,1000);
				//System.out.print(socket.getInetAddress());
				if(socket.isConnected()){
					//System.out.print(socket.isConnected());
				bool=true;				
				}
				//new UrlThread("http://"+ip);
				socket.shutdownInput();
				socket.shutdownOutput();
				socket.close();
			}
		catch (IOException e) 
		    {
			//System.out.print(socket.isConnected());
		    System.out.print(e);
		    }
		finally{
		      try {
					socket.close();
				if(!socket.isClosed()){
				System.out.print("没有关掉了11111111111111111111111111111111111111111\n");
				}
			      } 
		          catch (IOException e) 
		          {
				  e.printStackTrace();
			      }		
		        }
		info.append(ip+":");
		
       if(bool){
			info.append("有WEB服务器 666666666666--------");
			Window.setText(info.toString(),1);
				}
		else{
			info.append("无WEB服务器  ");
		}

       
       info.append(new Date()+"\n");
       		 System.out.println(info);
       		 info.delete(0,info.length());
       		 return bool;
	}

	public static void main(String args[]){
//		PortScan p=new PortScan();
		//for(int i=1;i<=40;i++) p.Scan("192.168.2."+i);
		      new PortScan().Scan("192.167.2.232");
	}

}
