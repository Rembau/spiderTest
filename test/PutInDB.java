package test;

import java.sql.Connection; 
import java.sql.ResultSet; 
import java.sql.Statement; 

public class PutInDB { 
        private Connection scCon; 
        private Connection stCon; 
        //private Connection indCon; 
        private String ip = null; 
        
        public PutInDB(){ 
                scCon = Conn.getScanningCon(); 
                stCon = Conn.getStoreCon(); 
                //indCon = Conn.getIndexCon(); 
        } 
        
        public  void putWebAdds(String url){ 
                try{ 
                        Statement statement = scCon.createStatement(); 
                        String sql = "insert into webadds(webadds_urls) values('"+url+"')"; 
                        statement.executeUpdate(sql); 
                }catch(Exception e){ 
                        System.out.println("入库出错!"); 
                } 
        } 
        
        public void putFtpAdds(String url){ 
                try{ 
                        Statement statement = scCon.createStatement(); 
                        String sql = "insert into ftpadds(ftpadds_urls) values('"+url+"')"; 
                        statement.executeUpdate(sql); 
                }catch(Exception e){ 
                        System.out.println("入库出错!"); 
                } 
        } 
        //还要改 
        public void putWebContent(String url,int depth,String title,String keywords,String description,String content){ 
                try{ 
                        Statement statement = stCon.createStatement(); 
                        String sql = "insert into web(web_urls,web_depth,web_title,web_keywords,web_description,web_text) values('"+url+"','"+depth+"','"+title+"','"+keywords+"','"+description+"','"+content+"')"; 
                        statement.executeUpdate(sql); 
                }catch(Exception e){ 
                        System.out.println("web内容入库出错!"+url); 
                        e.printStackTrace(); 
                } 
        } 
        //还要改 
        public void putFtpContent(String url,String content){ 
                try{ 
                        Statement statement = stCon.createStatement(); 
                        String sql = "insert into ftp(ftp_urls,ftp_text) values('"+url+"','"+content+"')"; 
                        statement.executeUpdate(sql); 
                }catch(Exception e){ 
                        System.out.println("ftp内容入库出错!"); 
                } 
        } 
        
        //接收传进来的id号,取该id对应的web_url返回 
        public String getWebUrls(int num){ 
                try{ 
                        Statement statement = scCon.createStatement(); 
                        String sql = "select webadds_urls from webadds where webadds_id="+num; 
                        ResultSet rs = statement.executeQuery(sql); 
                        rs.next(); 
                        ip = rs.getString("webadds_urls");   
                }catch(Exception e){ 
                        System.out.println("获取web_url地址出错!"); 
                } 
                return ip; 
        } 
        
        
//        接收传进来的id号,取该id对应的ftp_url返回 
        public String getFtpUrls(int num){ 
                try{ 
                        Statement statement = scCon.createStatement(); 
                        String sql = "select ftpadds_urls from ftpadds where id="+num; 
                        ResultSet rs = statement.executeQuery(sql); 
                        rs.next(); 
                        ip = rs.getString("ftpadds_urls");   
                        //ip = new String(ip.getBytes("ISO-8859-1"),"GBK"); 
                }catch(Exception e){ 
                        System.out.println("获取ftp_url地址出错!"); 
                } 
                return ip; 
        } 
        
        //返回webadds表中记录条数 
        public int countWebID(){ 
                int num = 0; 
                try{ 
                        Statement statement = scCon.createStatement(); 
                        String sql = "select count(*) as c from webadds"; 
                        ResultSet rs = statement.executeQuery(sql); 
                        rs.next(); 
                    num = rs.getInt("c"); 
                }catch(Exception e){ 
                        System.out.println("sql异常!"); 
                } 
                return num; 
        } 
        
//        返回ftpadds表中记录条数 
        public int countFtpID(){ 
                int num = 0; 
                try{ 
                        Statement statement = scCon.createStatement(); 
                        String sql = "select count(*) as c from ftpadds"; 
                        ResultSet rs = statement.executeQuery(sql); 
                        rs.next(); 
                    num = rs.getInt("c"); 
                }catch(Exception e){ 
                        System.out.println("sql异常!"); 
                } 
                return num; 
        } 
        

} 