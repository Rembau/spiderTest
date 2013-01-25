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
                        System.out.println("������!"); 
                } 
        } 
        
        public void putFtpAdds(String url){ 
                try{ 
                        Statement statement = scCon.createStatement(); 
                        String sql = "insert into ftpadds(ftpadds_urls) values('"+url+"')"; 
                        statement.executeUpdate(sql); 
                }catch(Exception e){ 
                        System.out.println("������!"); 
                } 
        } 
        //��Ҫ�� 
        public void putWebContent(String url,int depth,String title,String keywords,String description,String content){ 
                try{ 
                        Statement statement = stCon.createStatement(); 
                        String sql = "insert into web(web_urls,web_depth,web_title,web_keywords,web_description,web_text) values('"+url+"','"+depth+"','"+title+"','"+keywords+"','"+description+"','"+content+"')"; 
                        statement.executeUpdate(sql); 
                }catch(Exception e){ 
                        System.out.println("web����������!"+url); 
                        e.printStackTrace(); 
                } 
        } 
        //��Ҫ�� 
        public void putFtpContent(String url,String content){ 
                try{ 
                        Statement statement = stCon.createStatement(); 
                        String sql = "insert into ftp(ftp_urls,ftp_text) values('"+url+"','"+content+"')"; 
                        statement.executeUpdate(sql); 
                }catch(Exception e){ 
                        System.out.println("ftp����������!"); 
                } 
        } 
        
        //���մ�������id��,ȡ��id��Ӧ��web_url���� 
        public String getWebUrls(int num){ 
                try{ 
                        Statement statement = scCon.createStatement(); 
                        String sql = "select webadds_urls from webadds where webadds_id="+num; 
                        ResultSet rs = statement.executeQuery(sql); 
                        rs.next(); 
                        ip = rs.getString("webadds_urls");   
                }catch(Exception e){ 
                        System.out.println("��ȡweb_url��ַ����!"); 
                } 
                return ip; 
        } 
        
        
//        ���մ�������id��,ȡ��id��Ӧ��ftp_url���� 
        public String getFtpUrls(int num){ 
                try{ 
                        Statement statement = scCon.createStatement(); 
                        String sql = "select ftpadds_urls from ftpadds where id="+num; 
                        ResultSet rs = statement.executeQuery(sql); 
                        rs.next(); 
                        ip = rs.getString("ftpadds_urls");   
                        //ip = new String(ip.getBytes("ISO-8859-1"),"GBK"); 
                }catch(Exception e){ 
                        System.out.println("��ȡftp_url��ַ����!"); 
                } 
                return ip; 
        } 
        
        //����webadds���м�¼���� 
        public int countWebID(){ 
                int num = 0; 
                try{ 
                        Statement statement = scCon.createStatement(); 
                        String sql = "select count(*) as c from webadds"; 
                        ResultSet rs = statement.executeQuery(sql); 
                        rs.next(); 
                    num = rs.getInt("c"); 
                }catch(Exception e){ 
                        System.out.println("sql�쳣!"); 
                } 
                return num; 
        } 
        
//        ����ftpadds���м�¼���� 
        public int countFtpID(){ 
                int num = 0; 
                try{ 
                        Statement statement = scCon.createStatement(); 
                        String sql = "select count(*) as c from ftpadds"; 
                        ResultSet rs = statement.executeQuery(sql); 
                        rs.next(); 
                    num = rs.getInt("c"); 
                }catch(Exception e){ 
                        System.out.println("sql�쳣!"); 
                } 
                return num; 
        } 
        

} 