package spider_;
import java.sql.Statement;
public class ConData {
      
       public void  updateTable(String content,String ip,String url){
    	   System.out.println(content+"**");
    	   Statement stmt=(new Conn()).stmt;
    try{
/*ResultSet rs = stmt.executeQuery("select * from spidercontent where url='"+ url + "' or ip='" + ip + "'");
rs.last();
System.out.println(rs.getRow());
if (rs.getRow() > 0) {
System.out.println("执行了update语句");
stmt.executeUpdate("update spidercontent set content='"+content+"' where url='"+url+"' or ip='"+ip +"'");
	}
   	else{*/
		System.out.println("执行了insert语句");
		    stmt.executeUpdate("insert into spidercontent(ip,url,content) values('"+ip+ "','"+url+ "','"+content+"')");
		   // }
	              }
    	   catch (Exception e) {
            System.out.print("sql异常");
			  }
       }
}
