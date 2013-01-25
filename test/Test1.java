package test;

import spider_.PortScan;
public class Test1 extends Thread {
	String s="192.168";
	String ip="";
	int b;
	int c;
	int m=0,n=0;
    PortScan portscan;
	Test1(){
		this.portscan=new PortScan();
		run1();
		}
	public void run1(){

		for(m=2;m<3;m++)
		{
			if(m>255)
			break;
			//for(n=(256/c+1)*(b-1);n<(256/c+1)*b;n++)
			for(n=2;n<100;n++)
			{	if(n>255)
					break;
			        ip=s+"."+m+"."+n;
	            //if(this.portscan.Scan(ip).equals("ok"))
			        if(new PortScan().Scan(ip))
	            {
	            	System.out.println("一次循环结束");
	            }
			       
		}
	 }
	  return;
	}
	
	public static void main(String args[]){
		new Test1();
	}
		
     
}

