package test;

import java.sql.Connection; 
import java.sql.DriverManager; 

public class Conn { 
        private static Connection scConn; 
        private static Connection stConn; 
        private static Connection indConn; 

        static{ 
                try { 
                        Class.forName("com.mysql.jdbc.Driver").newInstance(); 
                        scConn = DriverManager.getConnection("jdbc:mysql://192.168.7.251:3306/lj_scanning", "lb", "123"); 
                        stConn = DriverManager.getConnection("jdbc:mysql://192.168.7.251:3306/lj_store", "lb", "123"); 
                        indConn = DriverManager.getConnection("jdbc:mysql://192.168.7.251:3306/lj_index", "lb", "123"); 
                } catch (Exception e) { 
                        e.printStackTrace(); 
                } 
                
        } 
        
        public static Connection getScanningCon(){ 
                return scConn; 
        } 
        
        public static Connection getStoreCon(){ 
                return stConn; 
        } 
        
        public static Connection getIndexCon(){ 
                return indConn; 
        } 

} 