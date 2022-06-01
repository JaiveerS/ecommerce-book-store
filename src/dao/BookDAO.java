package dao;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import bean.AddressBean;
import bean.BookBean;
import bean.BookInfoBean;
import bean.BookReviewBean;
import bean.OrderBean;
import bean.OrderItemBean;

public class BookDAO {
	
	private static Connection getConnection() throws URISyntaxException, SQLException {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

	    String username = dbUri.getUserInfo().split(":")[0];
	    String password = dbUri.getUserInfo().split(":")[1];
	    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

	    return DriverManager.getConnection(dbUrl, username, password);
	}
	
	//Returns all books in db
	public Map<String, BookBean> retrieveAllBooks() throws SQLException, URISyntaxException {
		String query = "select * from BOOK order by CATEGORY";
		
		Map<String, BookBean> rv= new HashMap<String, BookBean>();
		System.out.println("Connecting to DB");
		Connection con = getConnection();
		System.out.println("Connected to DB");
		PreparedStatement p = con.prepareStatement(query);
		
		ResultSet r = p.executeQuery();
		while(r.next()){
			String bid = r.getString("BID");
			String title = r.getString("TITLE");
			int price = r.getInt("PRICE");
			String category = r.getString("CATEGORY");
			
			BookBean s = new BookBean(bid, title, price,category);
			
			rv.put(bid, s);
			System.out.println(bid);
		}
		r.close();
		p.close();
		con.close();
		return rv;
		}

	public Map<String, BookBean> retrieveBooksByCategory(String category) throws SQLException, URISyntaxException {
		String query = "select * from BOOK where category='" + category + "'";
		
		Map<String, BookBean> rv= new HashMap<String, BookBean>();
		Connection con = (Connection) getConnection();
		PreparedStatement p = con.prepareStatement(query);
		
		ResultSet r = p.executeQuery();
		
		while(r.next()){
			String bid = r.getString("BID");
			String title = r.getString("TITLE");
			int price = r.getInt("PRICE");
			String bookCategory = r.getString("CATEGORY");
			
			BookBean s = new BookBean(bid, title, price,bookCategory);
			
			rv.put(bid, s);
		}
		r.close();
		p.close();
		con.close();
		return rv;
	}
	
	// Retrieving a BookBean for bid (a single instance will return)
	public BookBean retrieveBook(String bid) throws SQLException, URISyntaxException {
		String query = "select * from BOOK where bid='" + bid + "'";		
		BookBean book = null;
		
		Connection con = (Connection) getConnection();
		PreparedStatement p = con.prepareStatement(query);
		
		ResultSet r = p.executeQuery();
		
		while(r.next()){
			String bookID = r.getString("BID");
			String title = r.getString("TITLE");
			int price = r.getInt("PRICE");
			String bookCategory = r.getString("CATEGORY");
			
			book = new BookBean(bookID, title, price,bookCategory);
		}
		r.close();
		p.close();
		con.close();
		return book;
	}
	
	public BookInfoBean retrieveBookInfoByBid(String bid) throws SQLException, URISyntaxException{
		String query = "select B.BID, B.TITLE,B.PRICE,B.CATEGORY,C.DESCRIPTION from BOOK B, BOOKEXTRA C where B.BID = '" + bid + "' and C.BID = '" + bid + "'";
		BookInfoBean bookInfo = null;
		
		Connection con = (Connection) getConnection();
		PreparedStatement p = con.prepareStatement(query);
		
		ResultSet r = p.executeQuery();
		
		while(r.next()){
			String bookID = r.getString("BID");
			String title = r.getString("TITLE");
			int price = r.getInt("PRICE");
			String bookCategory = r.getString("CATEGORY");
			String description = r.getString("DESCRIPTION");
			
			bookInfo = new BookInfoBean(bookID, title, price, bookCategory, description);
		}
		r.close();
		p.close();
		con.close();
		return bookInfo;
	}
	
// 	public List<OrderItemBean> purchasedBookOrders(String bid) throws SQLException {
// 		String query = "SELECT * FROM BOOK, PO, POITEM, ADDRESS WHERE BOOK.BID=? AND POITEM.BID=? AND PO.ID=POITEM.ID AND ADDRESS.ID=PO.ID";		
// 		List<OrderItemBean> bookList = new ArrayList<OrderItemBean>();
		
// 		Connection con = (Connection) this.ds.getConnection();
// 		PreparedStatement p = con.prepareStatement(query);
// 		p.setString(1,bid);
// 		p.setString(2, bid);
		
// 		ResultSet r = p.executeQuery();
		
// 		while(r.next()){
// 			int id = r.getInt("ID");
// 			String lName = r.getString("LNAME");
// 			String fName = r.getString("FNAME");
// 			String status = r.getString("STATUS");
// 			int quantity = r.getInt("QUANTITY");
			
// 			String street = r.getString("STREET");
// 			String province = r.getString("PROVINCE");
// 			String country = r.getString("COUNTRY");
// 			String zip = r.getString("ZIP");
// 			AddressBean address = new AddressBean(id, fName, lName, street, province, country, zip);
// 			OrderBean order = new OrderBean(id, lName, fName, status, quantity, address);
			
// 			String bookID = r.getString("BID");
// 			String title = r.getString("TITLE");
// 			int price = r.getInt("PRICE");
// 			String bookCategory = r.getString("CATEGORY");
// 			BookBean book = new BookBean(bookID, title, price,bookCategory);			
			
// 			OrderItemBean item = new OrderItemBean(order, book);			
// 			bookList.add(item);
// 		}
// 		r.close();
// 		p.close();
// 		con.close();
// 		return bookList;
// 	}
	
	
	public List<BookReviewBean> retrieveReviewsByBID(String bid) throws SQLException, URISyntaxException {
		String query = "select * from BOOKREVIEWS WHERE BID=?";
		
		List<BookReviewBean> rv = new ArrayList<BookReviewBean>();
		Connection con = (Connection) getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setString(1, bid);
		
		
		ResultSet r = p.executeQuery();
		
		while(r.next()){
			String bookID = r.getString("BID");
			String review = r.getString("REVIEW");
			int rating = r.getInt("RATING");
			String firstname = r.getString("firstname");
			String lastname = r.getString("lastname");
			
			BookReviewBean s = new BookReviewBean(bookID, firstname, lastname, rating, review);
			
			rv.add(s);
			System.out.println(bid);
		}
		r.close();
		p.close();
		con.close();
		return rv;
		}
	
	public int insertReview(String bid, String firstname, String lastname, int rating, String review) throws SQLException, URISyntaxException {
		String query = "insert into BOOKREVIEWS (bid, review,rating, firstname, lastname) VALUES (?, ?, ?,?, ?)";
		
		Connection con = (Connection) getConnection();
		PreparedStatement p = con.prepareStatement(query);
		
		p.setString(1, bid);
		p.setString(2, review);
		p.setInt(3, rating);
		p.setString(4, firstname);
		p.setString(5, lastname);
		
		int i = p.executeUpdate();
		
		con.close();
		
		return i;
	}
}
