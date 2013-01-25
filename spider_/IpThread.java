package spider_;
import java.util.ArrayList;
public class IpThread extends Thread {
	String s="192.168";
	String ip="";
	int b;
	int c;
	int m=0,n=0;
    PortScan portscan;
    static ArrayList<Thread> th=new ArrayList<Thread>();
	IpThread( int i,int a){
	    this.b=i;
		this.c=a;
		this.portscan=new PortScan();
		}
	public void run(){
		for(m=(256/c+1)*(b-1);m<(256/c+1)*b;m++)
		//for(m=30;m<31;m++)
		{
			if(m>255)
			break;
			//for(n=(256/c+1)*(b-1);n<(256/c+1)*b;n++)
			for(n=0;n<255;n++)
			{	//if(n>255)  break;
			        ip=s+"."+m+"."+n;
	            this.portscan.Scan(ip);
			}
	     }
		
	}
	
	public static void main(String args[]){
		//new IpThread(1,10);
	}
		
     
}

