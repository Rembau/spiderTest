package url;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import parsehtml.HTMLDocument;

import search.FindUrl;
import search.SelectedByUrl;

import datasql.Conn;
import datasql.ConnsForThread;

public class UrlThread implements Runnable{

	private static int thid=0;
	private long idd=0;
	private Thread th=null;
	private String first_url=null;
	private ConnsForThread conns=null;
	//private FindUrl findurl=null;
	//private SelectedByUrl select=null;
	private ThreadList list=null;
	private boolean wait=true;
	public UrlThread(ConnsForThread conns,ThreadList list){
		this.conns=conns;
		this.list=list;
		idd=thid++;;
		System.out.println("线程"+idd+"初始化");
		//findurl=new FindUrl();
		//select=new SelectedByUrl();
	}
	
	/**
	 * @param url 有效搜索url
	 */
	public synchronized void Set_first_url(String url){
		//System.out.println("线程标号："+this.Getid()+"搜索url:"+url);
		this.first_url=url;
		wait=false;
		if(!wait)this.notify();
	}
	public void run() {
		// TODO 自动生成方法存根
		while(th!=null){
			synchronized(this){
				while(wait)
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
			}
			String text=SelectedByUrl.getWebBody(first_url);
			TreeMap<String,String[]> map=FindUrl.Find(text);
			HTMLDocument doc = HTMLDocument.createHTMLDocument(text);
			text=doc.getBody();
			Conn c=conns.GetConn("更新链接文本,放入url");
			try {
				
				SaveUrlToSql(map,c);
				SaveText(first_url,text,c);
			} finally{
				//System.out.println(".......................................当前线程标号："+this.Getid());
				conns.PutConn(c,"更新链接文本,放入url完成");
				wait=true;
				list.put(this,"获取线程搜索url完成");
			}
		}
		
	}
	private void SaveText(String url,String text,Conn c){
		String sql="update url set content='"+text+"',isNew=2 where urlname='"+url+"'";
		System.out.println(this.Getid()+"是标号，"+sql+c.findforUpdate(sql)+"sql:"+sql);
		
		//c.findforUpdate(sql);
	}
	
	private void SaveUrlToSql(TreeMap<String,String[]> map,Conn c){
		String sql="insert into url (urlname,upurl,keyword) values (?,?,?);";
		System.out.println(this.Getid()+"是标号，"+sql);
		PreparedStatement pre=c.GetPreparedStat(sql);
		
		if(pre==null){
			pre=c.GetPreparedStat(sql);
			if(pre==null){
				System.out.println("保存连接失败！");
				return ;
			}
		}
		//System.out.println("当前链接数目："+map.size());
		String url = null;
		for(String[] arr:map.values()){
			
			if(!arr[0].contains(first_url)&&!arr[0].startsWith("htt")){
				url=arr[0];
				arr[0]=this.Geturl(first_url, url);
				//System.out.println("获取链接:----------------------------"+arr[0]+"  "+first_url+"  "+url);
			}
			try {
				pre.setString(1, arr[0]);
				pre.setString(2, first_url);
				pre.setString(3, arr[1]);
				pre.executeUpdate();
			} catch(MySQLIntegrityConstraintViolationException e){
				
			}catch(MysqlDataTruncation e)
			{
				System.out.println("worng: "+arr[0]+"  "+first_url+"  "+url);
			//e.printStackTrace();	
			}catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
			//System.out.println("...."+arr[0]+"------"+arr[1]+"....");
			//System.out.println("---------------------------------------------------------------");
		}
		
	}
	/**
	 * @param url 给出url
	 * @return  返回根目录
	 */
	public String Getrooturl(String url){
		Pattern p=Pattern.compile(
				"(http:[\\/][\\/][^/\\s\\\\]+)"
		);
		Matcher m=p.matcher(url);
		if(m.find())
		return m.group();
		return null;
	}
	/**
	 * @param upurl 当前目录
	 * @param url 当前链接
	 * @return 当前链接指定目录
	 */
	public String Geturl(String upurl,String url){
		if(url.startsWith("/")||url.startsWith("\\"))return Getrooturl(upurl)+"/"+url;
		String[] t =url.split("./|.\\\\");
		if(t.length==1)return upurl+"/"+t[0];
		int len=t.length-1;
		char[] u=upurl.toCharArray();
		for(int i=u.length-2;i>=0;i--){
			if(u[i]=='/'||u[i]=='\\')len--;
			if(len==0){
				len=i;
				break;
			}
		}
		return new String(u,0,len)+t[t.length-1];
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
	public long  Getid(){
		return idd;
	}

}
