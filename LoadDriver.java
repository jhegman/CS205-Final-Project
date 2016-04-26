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
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
/*

      // Notice, do not import com.mysql.jdbc.*
      // or you will have problems!

      public class LoadDriver {
      public static void main(String[] args) {
      
          Statement stmt = null;
      
       /*  String x = "Gretchen";
         int y = 1;
         int z = 1;
         */
       /*
            
         Connection conn = null;

         try {
         
         
            
             Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
            
         
             conn =
               DriverManager.getConnection("jdbc:sqlserver://webdb.uvm.edu:3306","ggirdzis_admin","NB1FSgATnsmK");
                                  
                                   

       /*  // Do something with the Connection
         stmt = conn.createStatement();
         stmt.executeUpdate("INSERT INTO tblPerson(fldName, fldSetting, fldWin) VALUES ('" +x+ " ',' " +y+ " ',' " +z+" ')");



         } catch (SQLException ex) {
             // handle any errors
             System.out.println("SQLException: " + ex.getMessage());
             System.out.println("SQLState: " + ex.getSQLState());
             System.out.println("VendorError: " + ex.getErrorCode());
         }
     }
    
*/

public class NewClass {

    public static void main(String[] args) {

        Connection conn = null;
        String dbName = "GGIRDZIS_CS205";
        String serverip="ip0af50366.int.uvm.edu:55493";
        String serverport="3306";
        String url = "jdbc:sqlserver://"+serverip+"\\SQLEXPRESS:"+serverport+";databaseName="+dbName+"";
        Statement stmt = null;
        ResultSet result = null;
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String databaseUserName = "ggirdzis_admin";
        String databasePassword = "NB1FSgATnsmK";
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url, databaseUserName, databasePassword);
            stmt = conn.createStatement();
            result = null;
            String pa,us;
            result = stmt.executeQuery("select * from table1 ");

            while (result.next()) {
                us=result.getString("uname");
                pa = result.getString("pass");              
                System.out.println(us+"  "+pa);
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
   

 
   
   
   
   



