package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import bean.AddressBean;
import bean.BookBean;
import bean.OrderBean;
import bean.OrderItemBean;

public class OrderDAO {
	
	private DataSource ds;

	public OrderDAO() throws ClassNotFoundException {
		try {
			ds = (DataSource) (new InitialContext()).lookup("jdbc/Db2-EECS4413");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public AddressBean getAddressById (int addressId) throws SQLException {
		String addressQuery = "SELECT * FROM PO, ADDRESS WHERE ADDRESS.ID=? AND PO.ID=?";
		Connection con = this.ds.getConnection();
		PreparedStatement stmt = con.prepareStatement(addressQuery);
		stmt.setInt(1, addressId);
		stmt.setInt(2, addressId);
		
		ResultSet result = stmt.executeQuery();
		AddressBean addressBean = null;

		while (result.next()) {
			int id = result.getInt("ID");
			String fName = result.getString("FNAME");
			String lName = result.getString("LNAME");
			String street = result.getString("STREET");
			String province = result.getString("PROVINCE");
			String country = result.getString("COUNTRY");
			String zip = result.getString("ZIP");
			
			addressBean = new AddressBean(id, fName, lName, street, province, country, zip);
		}
		result.close();
		stmt.close();
		con.close();
		
		return addressBean;
	}
	
	public AddressBean retirveAddressByBid(String bid) throws SQLException {
		String query = "SELECT * FROM BOOK, PO, POITEM, ADDRESS WHERE BOOK.BID=? "
				+ "AND POITEM.BID=? AND PO.ID=POITEM.ID AND  ADDRESS.ID=PO.ID";
		AddressBean address = null;
		Connection con = (Connection) this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setString(1,bid);
		p.setString(2,bid);
		
		ResultSet r = p.executeQuery();
		
		while(r.next()){
			int id = r.getInt("ID");
			String firstName = r.getString("FNAME");
			String lastName = r.getString("LNAME");
			String street = r.getString("STREET");
			String province = r.getString("PROVINCE");
			String country = r.getString("COUNTRY");
			String zip = r.getString("ZIP");
			
			address = new AddressBean(id, firstName, lastName, street, province, country, zip);
		}
		r.close();
		p.close();
		con.close();
		return address;		
	}
	
	public String getCommentByBid(String bid) throws SQLException {
		String query = "SELECT COMMENT FROM PO, POItem, BILLINGADDRESS WHERE PO.ID=POItem.ID AND POItem.BID=? AND BILLINGADDRESS.ID=PO.ID";
		
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setString(1, bid);
		
		ResultSet r = p.executeQuery();
		String comment = r.getString("COMMENT");
		
		return comment;
	}
	
	public List<OrderItemBean> purchasedBookOrders(String bid) throws SQLException {
		String query = "SELECT * FROM BOOK, PO, POITEM, BILLINGADDRESS WHERE BOOK.BID=? "
				+ "AND POITEM.BID=? AND PO.ID=POITEM.ID AND  BILLINGADDRESS.ID=PO.ID";		
		List<OrderItemBean> bookList = new ArrayList<OrderItemBean>();
		
		Connection con = (Connection) this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setString(1,bid);
		p.setString(2, bid);
		
		ResultSet r = p.executeQuery();
		
		while(r.next()){
			int id = r.getInt("ID");
			String lName = r.getString("LNAME");
			String fName = r.getString("FNAME");
			String status = r.getString("STATUS");
			int quantity = r.getInt("QUANTITY");
			
			String street = r.getString("STREET");
			String province = r.getString("PROVINCE");
			String country = r.getString("COUNTRY");
			String zip = r.getString("ZIP");
			AddressBean billing = new AddressBean(id, fName, lName, street, province, country, zip);
			AddressBean shipping = retirveAddressByBid(bid);
			String comment = r.getString("COMMENT");
			
			OrderBean order = new OrderBean(id, lName, fName, status, quantity, shipping, billing, comment);
			
			String bookID = r.getString("BID");
			String title = r.getString("TITLE");
			int price = r.getInt("PRICE");
			String bookCategory = r.getString("CATEGORY");
			BookBean book = new BookBean(bookID, title, price,bookCategory);			
			
			OrderItemBean item = new OrderItemBean(order, book);			
			bookList.add(item);
		}
		r.close();
		p.close();
		con.close();
		return bookList;
	}

	public List<OrderBean> getOrderByBookId(String bid) throws SQLException {
		String query = "SELECT * FROM PO, POItem WHERE PO.ID=POItem.ID AND POItem.BID=?";
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setString(1, bid);
		ResultSet results = p.executeQuery();

		List<OrderBean> orderLists = new ArrayList<OrderBean>();
		while (results.next()) {
			int orderId = results.getInt("ID");
			String lastName = results.getString("LNAME");
			String firstName = results.getString("FNAME");
			String status = results.getString("STATUS");
			int quantity = results.getInt("QUANTITY");
			int addressId = results.getInt("ADDRESS");
			AddressBean shipping = getAddressById(addressId);
			AddressBean billing = shipping;
			String comment = "";
			
			OrderBean ob = new OrderBean(orderId, lastName, firstName, status, quantity, shipping, billing, comment);
			orderLists.add(ob);
		}

		results.close();
		p.close();
		con.close();
		
		return orderLists;
	}
	
	public int insertAddress(String street, String province, String country, String zip, String phone) throws SQLException {
		String query = "INSERT INTO Address (street, province, country, zip, phone) VALUES (?,?,?,?,?)";
		
		Connection con = (Connection) this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		
		p.setString(1, street);
		p.setString(2, province);
		p.setString(3, country);
		p.setString(4, zip);
		p.setString(5, phone);
		
		int i = p.executeUpdate();
		
		con.close();
		
		return i;
	}
	
	public String retrieveAddressNumber(String street, String province, String country, String zip) throws SQLException {
		String query = "select ADDRESS.ID FROM ADDRESS where street=? AND province=? AND country=? AND zip=?";
		
		Connection con = this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		p.setString(1, street);
		p.setString(2, province);
		p.setString(3, country);
		p.setString(4, zip);
		ResultSet results = p.executeQuery();

		String answer = "";
		while (results.next()) {
			String id = results.getString("ID");
			answer = id;
		}

		results.close();
		p.close();
		con.close();
		
		return answer;
	}
	
	public int insertPO(String lname, String fname, String status, String address) throws SQLException {
		String query = "INSERT INTO PO (lname, fname, status, address)  VALUES (?,?,?,?)";
		
		Connection con = (Connection) this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		
		p.setString(1, lname);
		p.setString(2, fname);
		p.setString(3, status);
		p.setString(4, address);
		
		int i = p.executeUpdate();
		
		con.close();
		
		return i;
	}

	public int insertPOitem(String id, String bookID, int price, int quantity) throws SQLException {
		String query = "INSERT INTO POItem (id, bid, price, quantity)  VALUES (?,?,?,?)";
		
		Connection con = (Connection) this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		
		p.setString(1, id);
		p.setString(2, bookID);
		p.setInt(3, price);
		p.setInt(4, quantity);
		
		int i = p.executeUpdate();
		
		con.close();
		
		return i;
		
	}

	public int insertBillingAddress(String street, String province, String country, String zip, String phone,String comment) throws SQLException {
		String query = "INSERT INTO BILLINGADDRESS (street, province, country, zip, phone, comment) VALUES (?, ?, ?, ? ,?, ?)";
		
		Connection con = (Connection) this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		
		p.setString(1, street);
		p.setString(2, province);
		p.setString(3, country);
		p.setString(4, zip);
		p.setString(5, phone);
		p.setString(6, comment);
		
		int i = p.executeUpdate();
		
		con.close();
		
		return i;
	}
	
}
