package model;


import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.xml.bind.JAXBException;

import org.json.JSONArray;
import org.json.JSONObject;

import bean.AccountBean;
import bean.AccountCreatedBean;
import bean.BookBean;
import bean.BookInfoBean;
import bean.BookReviewBean;
import bean.OrderItemBean;
import bean.cartItemBean;
import dao.BookDAO;
import dao.OrderDAO;
import dao.UserDAO;


public class BOOKSTORE {
	private BookDAO book;
	private UserDAO user;
	private OrderDAO order;
	private static BOOKSTORE instance;
	
	
	//ensures only one instance of SIS model
	public static BOOKSTORE getInstance() throws ClassNotFoundException {
		if(instance == null) {
			instance = new BOOKSTORE();
		}
		return instance;
	}
	
	private BOOKSTORE() throws ClassNotFoundException {		
		book = new BookDAO();
		user = new UserDAO();
		
		order= new OrderDAO();
		
	}
	
	public Map<String, BookBean> retriveAllBooks() throws Exception{
		return book.retrieveAllBooks();
	}

	public Map<String, BookBean> retriveBooksByCategory(String category) throws SQLException {
		if (validBookCategory(category)) {
			try {
				return book.retrieveBooksByCategory(category);
			} catch (SQLException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			return new HashMap<String,BookBean>();
		}
		return null;
	}
	
	public BookBean retrieveBookByBid(String bid) throws SQLException {
		try {
			return book.retrieveBook(bid);
		} catch (SQLException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// Used for the rest service as JSON output
	public String exportBookJson(String bid) throws SQLException, JAXBException, URISyntaxException {
		BookInfoBean bean = book.retrieveBookInfoByBid(bid);
		
		// Building JSON String from the StudentBean map
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = new JSONObject(); 
		
		jsonObj.put("bid", bid).put("title", bean.getTitle()).put("price", bean.getPrice()).put("category", bean.getCategory())
				.put("description", bean.getDescription());			
		JSONObject wrapperObject = new JSONObject();
		wrapperObject.put("productInfo", jsonObj);		
		jsonArray.put(wrapperObject); 
		
		return jsonArray.toString();
	}

	public BookInfoBean retrieveBookInfo(String bid) throws SQLException, URISyntaxException {
		return book.retrieveBookInfoByBid(bid);
	}
	
	public AccountBean retrieveAccountForValidation(String Username) throws NoSuchAlgorithmException, SQLException, URISyntaxException {
		if(Username == null) {
			return null;
		}else {
			return user.retrieveUser(Username);
		}
	}
	
	public AccountCreatedBean retrieveUserInfo(String Username) throws NoSuchAlgorithmException, SQLException, URISyntaxException {
		if(Username == null) {
			return null;
		}else {
			return user.retrieveUserInfo(Username);
		}
	}
	
	
	public double getSubtotal(List<cartItemBean> books) {
		double subtotal =0;
		for(cartItemBean item: books) {
			subtotal += item.getPrice() * item.getQuantity();
		}
		return subtotal;
	}
	
	public double getTax(double subtotal, double shipping) {
		return (subtotal + shipping) * 0.13;
	}
	
	public double getTotal(double subtotal, double shipping, double tax) {

		return subtotal + shipping + tax;
	}	
	
	public Boolean validBookCategory(String category) {
		return category.matches("[A-Z][a-z]*");
	}
	
	public String insertUser(String username, String hash) throws URISyntaxException {
		String userID = username;
		
		if(validateUsername(userID)) {
			int result;
			try {
				result = user.insertUser(userID, hash);
				return String.valueOf(result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				return "0 : (entry with same userID already exists!)" + e;
			}
		}else {
			return "0 : (Invalid Inputs)";
		}
	}
	

	public String deleteUser(String username) throws URISyntaxException {
		String userID = username;
		
		if(validateUsername(userID)) {
			int result;
			try {
				result = user.deleteUser(userID);
				return String.valueOf(result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				return "not deleted" + e;
			}
		}else {
			return "0 : (Invalid Inputs)";
		}
		
	}
	
	//Used to insert a student into the DB
	public String insertUserInfo(String username, String fname, String lname, String address,String address2, String city, String province, String postalCode, String country, String phoneNumber){
		String userID = username;
		String firstName = fname;
		String lastName = lname;
		String addressOne = address;
		String addressTwo = address2;
		String citySanitized = city;
		String provinceSanitized = province;
		String postal = postalCode;
		String countrySanitized = country;
		String phone = phoneNumber;
	
		//System.out.println("sanitized"); //for debugging
		
		if(validateUsername(userID) && validateFName(firstName) && validateLName(lastName) && validateAddress(addressOne) && 
				validateCity(citySanitized) && validateProvince(provinceSanitized) && validatePostal(postal) && validateCountry(countrySanitized)) {
			
			boolean aTwo = false;
			boolean numb = false;
			if (addressTwo != null && validateAddress(addressTwo) && addressTwo.length() != 0) {
				aTwo = true;
			}
			
			if (phone != null && validateNumber(phone) && phone.length() != 0) {
				numb = true;
			}
			
			int test = 0;
			
			int result;
			try {
				if (aTwo && numb) {
					test = 1;
					result = user.insertUserInfo(userID, firstName, lastName, addressOne, addressTwo, citySanitized, provinceSanitized, postal, countrySanitized, phone);
				}else if(aTwo) {
					test = 2;
					result = user.insertUserInfoWithoutPhone(userID, firstName, lastName, addressOne, addressTwo, citySanitized, provinceSanitized, postal, countrySanitized);
				}else if(numb) {
					test = 3;
					result = user.insertUserInfoWithoutAddressTwo(userID, firstName, lastName, addressOne, citySanitized, provinceSanitized, postal, countrySanitized, phone);
				}else {
					test = 4;
					result = user.insertUserInfoWithoutAddressNPhone(userID, firstName, lastName, addressOne, citySanitized, provinceSanitized, postal, countrySanitized);
				}
				return String.valueOf(result);
			} catch (SQLException e) {
				return "0 : (entry with same userID already exists!)" + e + test;
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}else {
			return "0 : (Invalid Inputs)";
		}
		return phone;
	}
	
	public int insertAddress(String street, String province, String country, String zip, String phone) throws URISyntaxException {
		try {
			return order.insertAddress(street, province, country, zip, phone);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	///FIX THIS
	public int insertBillingAddress(String street, String province, String country, String zip, String phone, String comment) throws URISyntaxException {
		try {
			return order.insertBillingAddress(street, province, country, zip, phone, comment);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	public String retrieveAddressID(String street, String province, String country, String zip, String phone) throws URISyntaxException {
		try {
			return order.retrieveAddressNumber(street, province, country, zip);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "0";
		}
	}

	public String exportAllPurchase(String bid) throws SQLException, JAXBException, URISyntaxException {
		List<OrderItemBean> itemBean = order.purchasedBookOrders(bid);
		
		// Building JSON String for all purchased book by bid		
		JSONArray outputArray = new JSONArray();		
		JSONObject outputWrapper = new JSONObject();
		
		
		for (OrderItemBean item: itemBean) {
			JSONObject jsonOrder = new JSONObject();
			JSONObject shipping = new JSONObject(); 
			JSONObject billing = new JSONObject(); 
			
			// Building Shipping Address
			String name = item.getOrder().getFirstName() + " " + item.getOrder().getLastName();
			String country = item.getOrder().getShipping().getCountry();
			String street = item.getOrder().getShipping().getStreet();
			String province = item.getOrder().getShipping().getProvince();
			String zip = item.getOrder().getShipping().getZip();
	
			shipping.put("name", name).put("street", street).put("state", province).put("zip", zip);
			jsonOrder.put("shipToCountry = " + country, shipping);
			
			// Building Billing Address
			String b_country = item.getOrder().getBilling().getCountry();
			String b_street = item.getOrder().getBilling().getStreet();
			String b_province = item.getOrder().getBilling().getProvince();
			String b_zip = item.getOrder().getBilling().getZip();
			billing.put("name", name).put("street", b_street).put("state", b_province).put("zip", b_zip);
			jsonOrder.put("billToCountry = " + b_country, billing);
			
			String comment = item.getOrder().getComment();
			jsonOrder.put("comment", comment);
			
			// Building items
			JSONObject orderItem = new JSONObject();
			JSONObject items = new JSONObject();
			String title = item.getBook().getTitle();
			int quantity = item.getOrder().getQuantity();
			int price = item.getBook().getPrice();
			
			orderItem.put("productName", title).put("quantity", quantity).put("price", price).put("status", item.getOrder().getStatus());			
			items.put("partNumber = " + bid, orderItem);	
			jsonOrder.put("items", items);			
			
			outputArray.put(jsonOrder);
		}			
		outputWrapper.put("purchaseOrders", outputArray);		
		return outputWrapper.toString();
	}
	
	
	public boolean validateUsername(String sid) {
		return sid.matches("^[a-zA-Z0-9]+$");
	}
	
	public boolean validateFName(String givenName) {
		return givenName.matches("[a-zA-Z]+");
	}
	
	public boolean validateLName(String lastName) {
		return lastName.matches("[a-zA-Z]+");
	}
	
	public boolean validateAddress(String address) {
		return address.matches("^[a-zA-Z0-9\\s]+$");
	}
	
	public boolean validateCity(String city) {
		return city.matches("[a-zA-Z]+");
	}
	
	public boolean validateProvince(String province) {
		return province.matches("[a-zA-Z]+");
	}
	
	public boolean validatePostal(String postal) {
		return postal.matches("^[a-zA-Z0-9\\s]+$");
	}
	
	public boolean validateCountry(String country) {
		return country.matches("[a-zA-Z]+");
	}
	
	public boolean validateNumber(String number) {
		return number.matches("^\\d+$");
	}
	
	public boolean validateBID(String bid) {
		return bid.matches("^[a-zA-Z0-9]+$");
	}
	
	//sanitizes string by removing all non-alphanumeric characters
//	private static String sanitize(String s) {
//		return s.replaceAll("[\\W+]", "");
//	}

	public String insertPO(String last, String first, String status, String id) throws URISyntaxException {
		String result = "1";
		try {
			order.insertPO(last, first, status, id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0";
		}
		return result;
	}

	public String insertPOitem(String id, String bookID, int p, int quantity) throws URISyntaxException {
		String result = "1";
		try {
			order.insertPOitem(id, bookID, p, quantity);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "0";
		}
		return result;
		
	}
	
	public List<BookReviewBean> retrieveBookReviewsByBID(String bid) throws SQLException {
		try {
			return book.retrieveReviewsByBID(bid);
		} catch (SQLException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int insertReview(String bookID, String firstname, String lastname, String rating, String review) {
		int r;
		try {
			r = Integer.parseInt(rating);
		}catch(Exception e) {
			r = -1;
		}
		if(validateReview(review) && validateRating(r)) {
			try {
				return book.insertReview(bookID, firstname, lastname, r, review);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			return 0;
		}
		return r;
	}
	
	public boolean validateReview(String review) {
		return review.matches("^[a-zA-Z0-9\\s]+$");
	}
	
	public boolean validateRating(int r) {
		return (( r <= 5) && (r >= 0));
	}
	
//	public static void main(String[] args) throws NoSuchAlgorithmException {
////		System.out.println(validateNumber("2"));
//	}
}
