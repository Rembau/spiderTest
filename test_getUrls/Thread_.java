package test_getUrls;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import conn.DBoperate;

/**
 * Description:获取页面的线程
 * @author Administrator
 *
 */
public class Thread_ extends Thread{
	GetContent_URL getC=new GetContent_URL();  //url方式获取页面
	GetContent_http getC_; //socket方式
	DBoperate dbOperate=new DBoperate();         //数据库操作
	GetLinks_ getL=new GetLinks_();				 //获取页面内容中链接
	DispatchQueue dq;
	DispatchHost dh;
	ShowError se;
	String url;
	String host;
	static volatile int urlNumCount=0;
	int mark;
	public Thread_(){
	}
	public void init(int mark,DispatchQueue dq,DispatchHost dh){
		this.mark=mark;
		this.dq=dq;
		this.dh=dh;
		getL.setD(dq);
	}
	public void init(int mark,DispatchQueue dq,DispatchHost dh,ShowError se){
		this.mark=mark;
		this.dq=dq;
		this.dh=dh;
		this.se=se;
		getL.setD(dq);
	}
	public void setHost(String host){
		try {
			URL url=new URL(host);
			this.host=url.getHost();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getC.setHost(this.host);
		if(GetContent_http.setHost(this.host)){
			System.out.println("连接成功！");
		}
	}
	public void run(){
			go();
	}
	public void go(){
		while(true){
			if((url=dq.getW())==null){    //是否成功获取一个完好的链接
				continue;
			}
			dq.addN(mark, url);				//当前链接 加到当前链接队列中
			if(judgeExternal(url)){			//是否是外部链接
				dq.addEX(url);				
				continue;
			}
			getC_=new GetContent_http();
			String content=getC_.getContent(url);	//
			while(content.trim().startsWith("http://")){	//判断页面是否跳转
				if(se!=null){
					se.addE(url+" "+content);		//在错误显示界面显示
				}
				if(dq.isExist(content)){
					break;
				}
				content = getC_.getContent(content);//根据返回的新的url获取内容
			}
			if(content.trim().startsWith("http:")){
				continue;
			}
			if(content.startsWith("error")){		//获取页面是否错误返回错误代码
				System.out.println(url+" "+content+"===============================");
				dq.addER(url);
				if(se!=null){
					se.addE(url+" "+content);
				}
				continue;
			}
			getL.getLink(url,content);				//获取页面链接，与链接队列管理类交互
			dq.addA(url);
			System.out.println(url+" ===获取成功===！");
			dbOperate.setPStmt(content, url); //数据库
		    urlNumCount++;
			System.out.println(urlNumCount+" "+new Date());
			getC_=null;
		}
	}
	/**
	 * Description:判断是否是外部链接
	 * @param url 判断的链接
	 * @return  boolean
	 */
	public boolean judgeExternal(String url){
		try {
			String host_=new URL(url).getHost();
			URL u_1=new URL("http://"+host_);
			URL u_2=new URL("http://"+host);
			if(u_1.sameFile(u_2)){
				return false;
			}
			System.out.println(url+"是外部链接！======================"+host_);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	public static void main(String[] args) {

	}

}
