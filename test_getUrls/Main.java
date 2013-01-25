package test_getUrls;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import conn.Conn;
import conn.DBoperate;

/**
 * Description:主线程，控制抓取页面的线程开始和结束，并为线程分配资源
 * @author Administrator
 *
 */
public class Main {
	DispatchQueue dq=new DispatchQueue();   //链接队列管理
	DispatchHost dh=new DispatchHost();		//主机管理
	ShowError se;							//显示错误链接界面

	String host;							//开始页面的主机		
	String url;								//开始页面
	int threadNum;							//线程总数
	ArrayList<Thread_> threadList=new ArrayList<Thread_>();   //线程链接
	public Main(int n){
		this.threadNum=n;
		url="http://view.news.qq.com/";//http://view.news.qq.com/
	}
	/**
	 * Description:初始化设置
	 * @param se	显示错误界面
	 * @param url	开始url
	 */
	public void init(ShowError se,String url){
		this.se=se;
		this.url=url;
		Conn con = new Conn();
		try {
			PreparedStatement pstmt= con.getConn().prepareStatement("insert spider_all(all_url) value('"+url+"')");
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dq.addW(url);
	}
	/**
	 * Description:执行命令
	 */
	public void handle(){
		Thread_ t=null;
		for(int i=1;i<=this.threadNum;i++){
			t=new Thread_();
			if(se==null){
				t.init(i,dq,dh);   //初始化，可以不设置显示错误界面
			}else{
				t.init(i,dq,dh,se);
			}
			t.setHost(url);
			threadList.add(t);
			t.start();
		}
		/**
		 * 判断程序是否应该结束的定时器
		 */
		Timer time=new Timer();
		time.schedule(new Task(dq,threadList), 2000,10000);
	}
	public static void main(String[] args) {
		new Main(20);
	}
}
/**
 * Description：定时器
 * @author Administrator
 *
 */
class Task extends TimerTask{
	long i=System.currentTimeMillis();
	DispatchQueue dq;
	ArrayList<Thread_> threadList;
	int mark=0;
	Task(DispatchQueue dq,ArrayList<Thread_> threadList){
		this.dq=dq;
		this.threadList=threadList;
	}
	/**
	 * Description:如果等待队列为空并保持2T时间则程序结束
	 */
	public void run() {
		if(dq.isEmptyW()){
			mark++;
		}
		else{
			mark=0;
		}
		
		if(mark==2){
			System.out.println("程序结束！");
			long j=System.currentTimeMillis();
			System.out.println("\n"+(j-i));
			DBoperate.close();
			for(Thread_ t:threadList){
				t.dbOperate.close_();
			}
			System.exit(0);
		}
	}
	
}