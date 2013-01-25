package parsehtml;

import java.sql.ResultSet;
import java.sql.SQLException;

import sql_spider.conn.DBoperate;


public class Test_1 {

	public static void main(String[] args) {
		ResultSet rs=DBoperate.select("select * from spider_all_qqnews where all_id='1'");
		try {
			if(rs.next()){
				System.out.println(rs.getString("all_content"));
				HTMLDocument ht=HTMLDocument.createHTMLDocument(rs.getString("all_content"));
				System.out.println(ht.getBody());
				System.out.println(ht.getTitle());
			}
			DBoperate.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
