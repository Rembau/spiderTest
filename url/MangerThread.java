package url;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import parsehtml.HTMLDocument;
import search.SelectedByUrl;

import datasql.Conn;
import datasql.ConnsForThread;

public class MangerThread  implements Runnable{

	private Thread th=null;
	private JLabel jl=null;
	private boolean rest=false;
	private ConnsForThread conns=null;
	private static int wait_num=20;//�����̵߳ȴ���ȡurl����
	private int wait=0;
	private ThreadList list=null;
	/**
	 * @param jl ���ڶ�̬��ʾ��ǰ����url
	 * @param conns_num ���ݿ���������
	 * @param thread_num �����߳�����
	 */
	public MangerThread(JLabel jl,int conns_num,int thread_num){
		this.jl=jl;
		//System.out.println("aaa");
		conns=new ConnsForThread(conns_num);
		list=new ThreadList(thread_num,conns);
		
	}
	public void SetFirstUrl(String url,JTextArea area){
		//FindUrl findurl = new FindUrl();
		//SelectedByUrl select = new SelectedByUrl();
		String text=SelectedByUrl.getWebBody(url);
		//TreeMap<String,String[]> map=findurl.Find(text);
		HTMLDocument doc = HTMLDocument.createHTMLDocument(text);
		text=doc.getBody();
		area.setText(text.replace("//", "\n"));
		String sql="insert into url (urlname,upurl,keyword) values ('"+url+"','"+url+"','')";
		//System.out.println(sql);
		Conn c=conns.GetConn("�����׸�����");
		try {
			c.findforUpdate(sql);
		}catch(Exception e){
			
		}finally{
			conns.PutConn(c,"�����׸��������");
		}
	}
	public void waitforrest(){
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void run() {
		String sql="select urlname,upurl from url where isNew=0 limit 0,100";
		String sql2="update url set isNew=1 where urlname in ('";
		LinkedList<String[]> urllist=new LinkedList<String[]>();
		while(th!=null){
			if(rest){
				synchronized(this){
					waitforrest();
				}
			}
			if(urllist.isEmpty()){
				Conn c=conns.GetConn("��ȡurl");
				ResultSet rs=c.findForResultSet(sql);
				String sql3=sql2;
				try {
					while(rs.next()){
						String[] arr={rs.getString(1),rs.getString(2)};
						sql3=sql3+arr[0]+"','";
						urllist.add(arr);
					}
					//System.out.println(sql2);
					if(!urllist.isEmpty()){
						sql3=sql3.substring(0, sql3.length()-2)+")";
						System.out.println(sql3);
						c.findforUpdate(sql3);
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}finally{
					try {
						rs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}finally{
						conns.PutConn(c,"url��ȡ���,��ȡ��Ŀ��"+urllist.size());
						//System.out.println("aaaaa");
					}
				}
				
			}
			if(!urllist.isEmpty()){
				String[] arr=urllist.removeFirst();
				if(arr[0].contains(this.Getrooturl(arr[1]))){
					jl.setText("����������"+arr[0]);
					list.GetUrlThread("��ȡ�߳�����url").Set_first_url(arr[0]);
				}
			} else{
				if(++wait>wait_num){
					jl.setText("�������");
					th=null;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
				
			
			
			
			
		}
	}
	/**
	 * @param url ����url
	 * @return  ���ظ�Ŀ¼
	 */
	public String Getrooturl(String url){
		Pattern p=Pattern.compile(
				"(http:[\\/][\\/][^/\\s\\\\]+)"
		);
		Matcher m=p.matcher(url);
		if(m.find())
		return m.group();
		return "aaaa";
	}
	/**
	 * @param rest true��ʾ��ͣ
	 */
	public void rest(boolean rest){
		synchronized(this){
			this.rest=rest;
			if(!rest)notify();
		}
		
	}
	public boolean isRest(){
		return rest;
	}
	public boolean isRun(){
		return th!=null;
	}
	public void start(){
		if(th==null){
			th=new Thread(this);
			th.start();
		}
	}
	public void stop(){
		th=null;
	}
}
