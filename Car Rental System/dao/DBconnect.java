package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnect {
    String url;
    String username;
    String password;
   public DBconnect(String url,String username,String password) {
	   this.url=url;
       this.username=username;
       this.password=password;
    } 
        
    public Connection getConn() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        Connection con=null;
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        con=DriverManager.getConnection(url,username,password);
        return con;
    }
}
