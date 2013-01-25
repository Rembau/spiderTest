package spider_;

//import java.sql.Connection;

public class UrlThread extends Thread { 
	//Connection con;

	String ip,url;

	String head = "";
    ConData condata=new ConData();
	UrlThread(String url) {
		this.url = url;
		url = url + "/";
		ip = url.substring(7, url.indexOf("/", 7));
		System.out.println("IP:"+ip);
	/*	StringBean sb = new StringBean();
		sb.setLinks(false);
		sb.setReplaceNonBreakingSpaces(true);
		sb.setCollapse(true);
		sb.setURL(this.url);
		String content = sb.getStrings();
		content = content.replaceAll("\'|\"|\n| ", "");
		System.out.println(content);
		condata.updateTable(content,ip,this.url);*/
	/*	try {
			URL u = new URL(this.url);
			u.openStream();
			int m;
			content = content.trim();
			String str=new String(content.getBytes());
			m = str.indexOf("??");
			if(m!=-1)
			m=str.indexOf("??",m+2);
			if(m!=-1)
			m=str.indexOf("??",m+2);
			if (m >= 0)
				content = new String(content.getBytes("ISO-8859-1"), "gb2312");
			System.out.println(content);
             //condata.updateTable(content,ip,this.url);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
*/
	}
	
	public static void main(String args[])
	{
		new UrlThread("http://www.hsu.edu.cn/");
	}
	

}
