package dao;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class TestBooktDAO {
	final static String url = "";
	
	private static Connection getConnection() throws URISyntaxException, SQLException {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    URI dbUri = new URI(url);

	    String username = dbUri.getUserInfo().split(":")[0];
	    String password = dbUri.getUserInfo().split(":")[1];
	    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

	    return DriverManager.getConnection(dbUrl, username, password);
	}

	
	
	public static void main(String[] args) throws SQLException 
	{
		try{
//			DataSource ds= (DataSource) (new InitialContext()).lookup("java:comp/env/jdbc/postgres");
//			Connection con = (Connection) this.ds.getConnection();
//			Connection con = (Connection) DriverManager.getConnection(url, user, password);
			System.out.println("CONNECTING");
			Connection con = getConnection();
			System.out.println("CONNECTED");
			Statement stmt= con.createStatement();
			ResultSet r= stmt.executeQuery("SELECT * FROM BOOK");
			while(r.next()){
				String bid = r.getString("BID");
				String title = r.getString("TITLE");
				int price = r.getInt("PRICE");
				String category = r.getString("CATEGORY");
				
				System.out.println("\t"+ bid+ ",\t"+ title+ "\t "+ price +"\t "+ category +"\t ");
			}
			con.close();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
