package dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DBConnection {

	private static volatile Connection connection;
    
    public static void getConnection() throws SQLException {

        if(connection == null){
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatapplication?useSSL=false", "root", "password");
        }

    }

    public static void closeConnection() throws SQLException {
        if(connection != null){
            connection.close();
            connection = null;
        }
    }

    public static void addUserInDB(String user) throws SQLException {
        getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users VALUES (null, ?, ?)");
        preparedStatement.setString(1, user);
        preparedStatement.setDate(2,new java.sql.Date(System.currentTimeMillis()));
        int rows_affected = preparedStatement.executeUpdate();

        if(rows_affected > 0){
            System.out.println("succesfully inserted the msg in DB");
        }else{
            System.out.println("unable to insert the msg in DB");
        }
        closeConnection();
    }

    public static void chatBackUp(String user, String msg_id, String message) throws SQLException {

        getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO chat_backup VALUES (?, ?, ?)");
        preparedStatement.setString(1, msg_id);
        preparedStatement.setString(2, user);
        preparedStatement.setString(3, message);
        int rows_affected = preparedStatement.executeUpdate();

        if(rows_affected > 0){
            System.out.println("Succesfully inserted the msg in DB");
        }else{
            System.out.println("unable to insert the msg in DB");
        }
        closeConnection();
    }

    public static Map<String, String> get100Message() throws SQLException {
    	Map<String, String> map = new HashMap<>();
    	getConnection();
    	 PreparedStatement preparedStatement = connection.prepareStatement("SELECT msg_id, message FROM chat_backup LIMIT 100");
    	 ResultSet rs = preparedStatement.executeQuery();
    	 while(rs.next()) {
    		 map.put(rs.getString("msg_id"), rs.getString("message"));
    	 }
         closeConnection();
         return map;
    	 
    }

	
    
}
