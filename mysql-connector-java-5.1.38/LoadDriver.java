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



      // Notice, do not import com.mysql.jdbc.*
      // or you will have problems!

      public class LoadDriver {
      public static void main(String[] args) {
      
          Statement stmt = null;
      
       /*  String x = "Gretchen";
         int y = 1;
         int z = 1;
         */
       
            
         Connection conn = null;

         try {
         
         
            
             //Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); 
            
         
             conn =
               DriverManager.getConnection("jdbc:sql://webdb.uvm.edu:3306/GGIRDZIS_CS205","ggirdzis_admin","NB1FSgATnsmK");
                                  
                                   

        // Do something with the Connection
        // stmt = conn.createStatement();
        // stmt.executeUpdate("INSERT INTO tblPerson(fldName, fldSetting, fldWin) VALUES ('" +x+ " ',' " +y+ " ',' " +z+" ')");



         } catch (SQLException ex) {
             // handle any errors
             System.out.println("SQLException: " + ex.getMessage());
             System.out.println("SQLState: " + ex.getSQLState());
             System.out.println("VendorError: " + ex.getErrorCode());
}
}
}     
     
    



   

 
   
   
   
   



