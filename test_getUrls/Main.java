package test_getUrls;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import conn.Conn;
import conn.DBoperate;

/**
 * Description:���̣߳�����ץȡҳ����߳̿�ʼ�ͽ�������Ϊ�̷߳�����Դ
 * @author Administrator
 *
 */
public class Main {
	DispatchQueue dq=new DispatchQueue();   //���Ӷ��й���
	DispatchHost dh=new DispatchHost();		//��������
	ShowError se;							//��ʾ�������ӽ���

	String host;							//��ʼҳ�������		
	String url;								//��ʼҳ��
	int threadNum;							//�߳�����
	ArrayList<Thread_> threadList=new ArrayList<Thread_>();   //�߳�����
	public Main(int n){
		this.threadNum=n;
		url="http://view.news.qq.com/";//http://view.news.qq.com/
	}
	/**
	 * Description:��ʼ������
	 * @param se	��ʾ�������
	 * @param url	��ʼurl
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
	 * Description:ִ������
	 */
	public void handle(){
		Thread_ t=null;
		for(int i=1;i<=this.threadNum;i++){
			t=new Thread_();
			if(se==null){
				t.init(i,dq,dh);   //��ʼ�������Բ�������ʾ�������
			}else{
				t.init(i,dq,dh,se);
			}
			t.setHost(url);
			threadList.add(t);
			t.start();
		}
		/**
		 * �жϳ����Ƿ�Ӧ�ý����Ķ�ʱ��
		 */
		Timer time=new Timer();
		time.schedule(new Task(dq,threadList), 2000,10000);
	}
	public static void main(String[] args) {
		new Main(20);
	}
}
/**
 * Description����ʱ��
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
	 * Description:����ȴ�����Ϊ�ղ�����2Tʱ����������
	 */
	public void run() {
		if(dq.isEmptyW()){
			mark++;
		}
		else{
			mark=0;
		}
		
		if(mark==2){
			System.out.println("���������");
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