package sql_spider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import sql_spider.conn.DBoperate;

@SuppressWarnings("unused")
public class DispatchQueue {
	private DBoperate db;
	private boolean isEmpty=false;
	private volatile int markGetW=0;
	private PreparedStatement pstmtInsert,pstmtUpdate;
	private LinkedList<String> waitLinks;       //待操作队列 0
	private LinkedList<String> 	alreadyLinks=new LinkedList<String>();;	//已操作队列 1	
	private LinkedList<String> errorLinks=new LinkedList<String>();;		//错误队列   2
	private LinkedList<String> externalLinks;	//外部链接队列 3
	public DispatchQueue(){
		waitLinks=new LinkedList<String>();
		externalLinks=new LinkedList<String>();
	}
	public void setConn(DBoperate db){
		this.db=db;
		try {
			pstmtInsert=db.getConn().getConn().prepareStatement("insert spider_all(all_url,all_mark) value(?,?)");
			pstmtUpdate=db.getConn().getConn().prepareStatement("update spider_all set all_mark=? where all_url=?");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void addExecute(){
		try {
			pstmtInsert.executeBatch();
		} catch (SQLException e) {
			if(!e.getMessage().startsWith("Duplicate entry")){
				e.printStackTrace();
			}
		}
	}
	public void add(String url){
		try {
			pstmtInsert.setString(1, url);
			pstmtInsert.setInt(2, 0);
			pstmtInsert.execute();
		} catch(MySQLIntegrityConstraintViolationException e1){
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public boolean addO(String url,int mark){
		try {
			pstmtUpdate.setString(2, url);
			pstmtUpdate.setInt(1, mark);
			pstmtUpdate.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public synchronized String getW(){
		String url=null;
		try {
			while(waitLinks.isEmpty()){
				if(markGetW==0){
					markGetW++;
					ResultSet rs=DBoperate.select("select all_url from spider_all where all_mark='0'");
					if(!rs.next()){
						isEmpty=true;
						System.out.println("数据库没有待操作链接了");
					}
					else{
						rs.beforeFirst();
						while(rs.next()){System.out.println(rs.getString(1));
							waitLinks.add(rs.getString(1));
						}
						rs.getStatement().close();
						markGetW=0;
						notifyAll();
						return waitLinks.remove();
					}
				}
				else{
					wait();
				}
			}
		} catch(MySQLIntegrityConstraintViolationException e1){
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		url=waitLinks.remove();
		return url;
	}
	public boolean isEmptyW(){
		return isEmpty;
	}
	public static void main(String[] args) {

	}

}
