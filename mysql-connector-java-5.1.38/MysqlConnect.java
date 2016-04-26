import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.Date;
import java.util.Calendar;
import java.sql.Timestamp;

import java.util.ResourceBundle;



public class MysqlConnect{


      public MysqlConnect(String theName, boolean theValue, String theWinner, int theMoves)
      {
         //Variables
         String name = theName;
         boolean value = theValue;
         String winner = theWinner;
         int moves = theMoves;
         
         
         
      
   	   //public static void main(String[] args) {
   		// System.out.println("MySQL Connect Example.");
   		Connection conn = null;
         Statement stmt = null;
         
         java.util.Date thedate= new java.util.Date();
   	   Timestamp date = new Timestamp(thedate.getTime());
         
            
     
   		//ResourceBundle properties = ResourceBundle.getBundle("MysqlConnect");
   		String url = "jdbc:mysql://webdb.uvm.edu/";
   		String dbName = "GGIRDZIS_CS205";
   		String driver = "com.mysql.jdbc.Driver";
   		String userName = "ggirdzis_admin";
   		String password = "NB1FSgATnsmK";
   
   
      
   		try {
   			Class.forName(driver).newInstance();
   			conn = DriverManager.getConnection(url + dbName, userName, password);
            
            
            
                      
            stmt = conn.createStatement();
         
            String sql = "INSERT INTO tblPlayer " +
                      "VALUES ('"+name+"'," +value+", '"+winner+"', "+moves+", '"+date+"')";
            stmt.executeUpdate(sql);
             
            //stmt.setString(1, "Name");
            //stmt.setDate(2, timestamp);
            //stmt.setBoolean(2, true);
            //stmt.setString(3, "Player");
           // stmt.setInt(4, 10);
                      
            stmt.executeUpdate(sql);          
   		} catch (Exception e) {
   			System.err.println("Error MysqlConnect.myConnect: " + e.getMessage());
   			e.printStackTrace();
   		}
   		
       }  
      
	//}
}

            

 
   
   
   
   



