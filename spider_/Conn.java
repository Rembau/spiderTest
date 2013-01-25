package spider_;

import java.sql.*;

public class Conn {
	Connection con;
	Statement stmt;
	public Conn(){
		connMysql();		
	}
	public Statement connMysql(){
	try
	{
	Class.forName("com.mysql.jdbc.Driver");
	 //Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	}
	catch(ClassNotFoundException e)
	{
	 System.out.print("’“≤ªµΩ¿‡");
	} 
	try{con=DriverManager.getConnection("jdbc:mysql://localhost:3306/url","root","123");
	// con=DriverManager.getConnection("jdbc:odbc:sun","","");
	 stmt=con.createStatement();
	}
	catch(SQLException e){
		 System.out.println(e);
	}
	return stmt;
	}
	
	public static void main(String args[]){
		new Conn();
	}
}