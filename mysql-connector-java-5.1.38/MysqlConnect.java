//Import statements
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
import java.util.Date;
import java.util.Calendar;
import java.sql.Timestamp;
import java.util.ResourceBundle;

/**
   Created by Gretchen. Based on Robert Erickson's code. This class connects to SQL Database, and reads in data passed
   in as statistics from the game Sorry.
*/

public class MysqlConnect{

      /**
      
         This constructor takes in game variables, and syncs them with the database.
      
      */


      public MysqlConnect(String theName, boolean theValue, String theWinner, int theMoves)
      {
         
         //Variables
         String name = theName;
         boolean value = theValue;
         String winner = theWinner;
         int moves = theMoves;
         
         //Set connection to null
   		Connection conn = null;
         Statement stmt = null;
         
         //Time & date stamp
         java.util.Date thedate= new java.util.Date();
   	   Timestamp date = new Timestamp(thedate.getTime());
         
            
     
   		//SQL Information
   		String url = "jdbc:mysql://webdb.uvm.edu/";
   		String dbName = "GGIRDZIS_CS205";
   		String driver = "com.mysql.jdbc.Driver";
   		String userName = "ggirdzis_admin";
   		String password = "NB1FSgATnsmK";
   
   
      
         //Connection for database
   		try {
   			Class.forName(driver).newInstance();
   			conn = DriverManager.getConnection(url + dbName, userName, password);
            
                     
            stmt = conn.createStatement();
         
            //Insert statement
            String sql = "INSERT INTO tblPlayer " +
                      "VALUES ('"+name+"'," +value+", '"+winner+"', "+moves+", '"+date+"')";
            
            //Update
            stmt.executeUpdate(sql);
             
         //Exception           
   		} catch (Exception e) {
   			System.err.println("Error MysqlConnect.myConnect: " + e.getMessage());
   			e.printStackTrace();
   		}
   		
       }  
      
}	