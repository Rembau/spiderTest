package bin;

import java.sql.ResultSet;
import java.sql.SQLException;

import sql_spider.conn.DBoperate;

import bin.HtmlParse;


public class Test_1 {

	public static void main(String[] args) {
		ResultSet rs=DBoperate.select("select * from spider_all_qqnews where all_id='1'");
		try {
			if(rs.next()){
				System.out.println(rs.getString("all_content"));
				HtmlParse ht=new HtmlParse(rs.getString("all_content"));
				System.out.println(ht.getBody());
				System.out.println(ht.getTitle());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			 DBoperate.close();
		}
	}

}
