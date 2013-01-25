package datasql;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.JOptionPane;

public class Conn {

	//private int GetConNum=5;//�������Ӵ���
	private static Properties props;
	private static String url;
	private static String user;
	private static String password;
	private Connection con;
	private Statement stat;
	private PreparedStatement preparedstat = null;
	private static int connid=0;
	private int id=0;
	static {
		props=new Properties();
        InputStream in = null;
		try {
			in = Conn.class.getResource("data.properties").openStream();
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Sorry��δ���������ļ�",
					"����", JOptionPane.INFORMATION_MESSAGE);
			System.exit(1);
		}
		try {
			props.load(in);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		url=props.getProperty("MysqlUrl");
		user=props.getProperty("MysqlUser");
		password=props.getProperty("MysqlPassword");
	}
	public Conn(){
		con=Getcon();
	}
	//public int getnum=0;//���Ի�ȡ���Ӵ������
	public synchronized Connection Getcon(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Sorry��δ�������ݿ�����⣡",
					"����", JOptionPane.INFORMATION_MESSAGE);
			System.exit(1);
		}catch (SQLException e) {
			e.printStackTrace();
			//if(getnum++>GetConNum)return null;con=Getcon();
		} 
		id=connid++;
		System.out.println("���ݿ��ʼ��"+id);
		return con;
	}
	public ResultSet findForResultSet(String sql) {
		try {
			if (con == null|con.isClosed()) con=Getcon();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}
		ResultSet rs = null;
		try {
			stat = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = stat.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("���ݿ��ѯʧ��");
		}
		return rs;
	}
	public int findforUpdate(String sql){
		try {
			if (con == null|con.isClosed()) con=Getcon();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return -1;
		}
		try {
			stat = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			return stat.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	public PreparedStatement GetPreparedStat(String sql){
		try {
			preparedstat=con.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return preparedstat;
		}
		return preparedstat;
	}
	public void colseCon(){
		if(con!=null){
			//System.out.println("null");
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
