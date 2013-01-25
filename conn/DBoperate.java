package conn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class DBoperate {
	private Conn conn_;
	private static Conn conn;
	public DBoperate(){
		conn_ = new Conn();
	}
	static {
		conn = new Conn();
	}
	public static void close(){
		conn.close();
	}
	public void close_(){
		conn_.close();
	}
	public static void insert(String sql){
		if(conn==null){
			conn=new Conn();
		}
		if(!conn.isConnNotNull()){
			conn.getConn();
		}
		try {
			conn.getOldStmt().executeUpdate(sql);
		} catch(MySQLIntegrityConstraintViolationException e1){
			
		} catch (SQLException e) {
			System.out.println(sql+"³ö´í");
			//e.printStackTrace();
		}
	}
	public static void update(String sql){
		if(conn==null){
			conn=new Conn();
		}
		if(!conn.isConnNotNull()){
			conn.getConn();
		}
		try {
			conn.getOldStmt().executeUpdate(sql);
		} catch(MySQLIntegrityConstraintViolationException e1){
			
		} catch (SQLException e) {
			System.out.println(sql+"³ö´í");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static ResultSet select(String sql){
		if(conn==null){
			conn=new Conn();
		}
		if(!conn.isConnNotNull()){
			conn.getConn();
		}
		try {
			return conn.getNewStmt().executeQuery(sql);
		} catch (SQLException e) {
			System.out.println(sql+"³ö´í");
			e.printStackTrace();
			return null;
		}
	}
	public void setPStmt(String content,String url){
		try {
			PreparedStatement pStmt=conn_.getConn().prepareStatement("insert spider_all(all_content,all_url) values(?,?)");
			pStmt.setString(1, content);
			pStmt.setString(2, url);
			pStmt.execute();
			pStmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {

	}
}
