package ctrl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.AccountBean;
import bean.AccountCreatedBean;
import bean.BookBean;
import bean.BookInfoBean;
import bean.BookReviewBean;
import bean.cartItemBean;
import model.BOOKSTORE;

/**
 * Servlet implementation class Bookstore
 */
@WebServlet({"/Bookstore", "/Bookstore/*"})
public class Bookstore extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String mainPage = "/MainPage.jspx";
	private String productInfoPage = "/ProductPage.jspx";
	private String cartPage = "/Cart.jspx";
	private String paymentPage = "/PaymentPage.jspx";
	private String checkoutPage = "/Checkout.jspx";
	private String confirmOrderPage = "/ConfirmOrder.jspx";
	private String successPage = "/PaymentSuccessful.jspx";
	private int po = 1;
	private ServletContext context;
	private BOOKSTORE model;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Bookstore() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		context = getServletContext();
		
		try {
			model = BOOKSTORE.getInstance();
			context.setAttribute("model", model);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	
	@SuppressWarnings("null")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/plain");
		
		String bid = request.getParameter("bid");
		
		
		String cart = request.getPathInfo();
		
		//get instance of model
		BOOKSTORE model = (BOOKSTORE) context.getAttribute("model");
		HttpSession session = request.getSession();
		
		String logoutButton = "	<FORM action=\"\" method=\"POST\" class=\"logout\">\r\n"
				+ "     	<BUTTON action=\"submit\" name=\"logout\" value=\"true\">Log Out</BUTTON>\r\n"
				+ "	</FORM>";
		
		if(session.getAttribute("isLoggedIn") != null && session.getAttribute("isLoggedIn").equals(true)) {
			session.setAttribute("logout", logoutButton);
		}

		
		if (bid != null && model.validateBID(bid)){ //when user wants to access productInfoPage of a specific product
			
			System.out.println("IM HERE");
			//retrieve book info 
			BookInfoBean book = null;
			try {
				book = model.retrieveBookInfo(bid);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(request.getParameter("addReview") != null) {
				if(session.getAttribute("isLoggedIn") != null && session.getAttribute("isLoggedIn").equals(true)) {
					//ur logged in u can add review
					String firstname = (String) session.getAttribute("firstName");
					String lastname = (String) session.getAttribute("lastName");
					String bookID = book.getBid();
					String rating = request.getParameter("rating");
					String review = request.getParameter("bookComment");
					
					int i = model.insertReview(bookID,firstname,lastname,rating, review);
					if(i == 0) {
						request.setAttribute("test", "Sorry,  " + firstname + " "+ lastname + " feedbacks can only be alphanumberic characters");
					}else {
						request.setAttribute("test", "Review Submitted By: " + firstname + " "+ lastname);
					}
					
				}else {
					//ur not logged in u cant add review
					request.setAttribute("test", "SORRY YOU MUST BE LOGGED IN TO WRITE REVIEWS");
				}
			}
			
			
			//retrieve reviews
			int rating = 0;
			int numOfBooks = 0;
			List<BookReviewBean> bookReviews = new ArrayList<BookReviewBean>();
			try {
				bookReviews = model.retrieveBookReviewsByBID(bid);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			for(BookReviewBean e: bookReviews) {
				rating += e.getRating();
				numOfBooks++;
			}
			
			int avgRating = rating/numOfBooks;
			
			
			bid = request.getParameter("bookBID");
			if(request.getParameter("addToCart") != null){//when user presses add to cart button
				bid = request.getParameter("bookBID");
				
				BookBean bookItem = null;
				try {
					bookItem = model.retrieveBookByBid(bid);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				addToCart(bookItem, session); //adds book to cart
			}
			
			//put book info and reviews in request scope for visitors to see
			request.setAttribute("avgRating", avgRating);
			request.setAttribute("info", book);
			request.setAttribute("reviews", bookReviews);
			
			request.getRequestDispatcher(productInfoPage).forward(request, response);
			
		}else if(cart != null && request.getParameter("addToCart") == null && cart.equals("/Cart")) {//when user are on cart page or want to access it
			bid = request.getParameter("bookBID");
			
			if(request.getParameter("removeFromCart") != null) {
				removeFromCart(bid, session);//removes book from cart
			}
			
			if(request.getParameter("incrementQuantity") != null) {
				incrementQuantityOfBook(bid, session);
			}
			
			if(request.getParameter("decrementQuantity") != null) {
				decrementQuantityOfBook(bid, session);
			}
			
			request.getRequestDispatcher(cartPage).forward(request, response);
		}else if (request.getParameter("cartButton") != null && request.getParameter("cartButton").equals("true")){//when you click checkout in cart
			@SuppressWarnings("unchecked")
			List<cartItemBean> books = (List<cartItemBean>) session.getAttribute("items");
			if(books == null || books.isEmpty()) {
				request.getRequestDispatcher(cartPage).forward(request, response);//dont let them proceed
			}else {//send them to login page
				session.setAttribute("isNotLoggedIn", true);
				session.setAttribute("isPaymentRegister", false);
				session.setAttribute("isPaymentLogin", false);
			
				
				if(session.getAttribute("isLoggedIn") != null && session.getAttribute("isLoggedIn").equals(true)) {
					request.getRequestDispatcher(checkoutPage).forward(request, response);
				}else {
					request.getRequestDispatcher(paymentPage).forward(request, response);
				}
			}
			
		}else if(request.getParameter("payment-login") != null || request.getParameter("payment-register") != null){//when user clicks a button on login/register page
			session.setAttribute("isNotLoggedIn", false);
			session.setAttribute("isPaymentLogin", false);
			session.setAttribute("isPaymentRegister", false);
			
			if(request.getParameter("payment-login") != null && request.getParameter("payment-login").equals("true")) {
				session.setAttribute("isPaymentLogin", true);
				request.getRequestDispatcher(paymentPage).forward(request, response);
			}
			
			if(request.getParameter("payment-register") != null && request.getParameter("payment-register").equals("true")) {
				session.setAttribute("isPaymentRegister", true);
				request.getRequestDispatcher(paymentPage).forward(request, response);
			}
			
			
			String userID = (String) request.getParameter("paymentUsername");
			String password = (String) request.getParameter("loginPassword");
			
			if(userID != null && password != null) {//when trying to login
				String hash = null;
				AccountBean acc = new AccountBean();
				try {
					hash = acc.hashPassword(password);
					acc = new AccountBean(userID, hash);
					AccountBean db = model.retrieveAccountForValidation(userID);
					if (db != null && db.getHashOfPass() != null && db.getUsername() != null && db.getHashOfPass().equals(acc.getHashOfPass())) {
						session.setAttribute("isPaymentLogin", false);
						request.setAttribute("test", "SUCCESS");
						session.setAttribute("isLoggedIn", true);
						
						AccountCreatedBean accInfo = model.retrieveUserInfo(userID);
//						request.setAttribute("test", accInfo.getUsername());
						//set shipping info on default
						session.setAttribute("firstName", accInfo.getFirstName());
						session.setAttribute("lastName", accInfo.getLastName());
						session.setAttribute("address1", accInfo.getAddress());
						session.setAttribute("address2", accInfo.getAddreess2());
						session.setAttribute("city", accInfo.getCity());
						session.setAttribute("province", accInfo.getProvince());
						session.setAttribute("postal", accInfo.getPostalCode());
						session.setAttribute("number", accInfo.getPhoneNumber());
						session.setAttribute("country", accInfo.getCountry());
						
						//set billing info on default
						session.setAttribute("fName", accInfo.getFirstName());
						session.setAttribute("lName", accInfo.getLastName());
						session.setAttribute("address", accInfo.getAddress());
						session.setAttribute("city1", accInfo.getCity());
						session.setAttribute("province1", accInfo.getProvince());
						session.setAttribute("postalCode1", accInfo.getPostalCode());
						session.setAttribute("phone1", accInfo.getPhoneNumber());
						session.setAttribute("country1", accInfo.getCountry());
						
						
						
						System.out.println("sending to checkoutpage");
						request.getRequestDispatcher(checkoutPage).forward(request, response);
					}else {
						session.setAttribute("isPaymentLogin", true);
						request.setAttribute("test", "FAILURE");
						request.getRequestDispatcher(paymentPage).forward(request, response);
					}
					//request.setAttribute("test", acc.getHashOfPass());
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				request.setAttribute("test", "FAILURE #2");
				request.getRequestDispatcher(paymentPage).forward(request, response);
			}


		}else if(request.getParameter("paymentButton") != null) {//confirm user info
			
			request.getRequestDispatcher(confirmOrderPage).forward(request, response);
		}else if(request.getParameter("createAccountButton") != null && request.getParameter("createAccountButton").equals("true")){//user created account then sent page to login or register page
				
				String userID = request.getParameter("registerUsername");
				String userPassword = request.getParameter("registerPassword");
				String firstName = request.getParameter("registerFirstName");
				String lastName = request.getParameter("registerLastName");
				String address = request.getParameter("registerStreet");
				String address2 = request.getParameter("registerAddress2");
				String city = request.getParameter("registerCity");
				String province = request.getParameter("registerProvince");
				String postal = request.getParameter("registerPostalCode");
				String country = request.getParameter("registerCountry");
				String phone = request.getParameter("registerPhone");
				
				AccountBean acc = new AccountBean();
				String hashedPass = null;
				String resultOne = null;
				String resultTwo = null;
				try {
					hashedPass = acc.hashPassword(userPassword);
					acc = new AccountBean(userID, hashedPass);
					
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Boolean correct = true;
				
				if(address2 == null) {
					address2 = "";
				}
				if(phone == null) {
					phone = "";
				}
				
				if(userPassword != null && userPassword.length() < 4) {
					correct = false;
				}
			
				
				if(acc != null && correct) {
					try {
						resultOne = model.insertUser(acc.getUsername(), acc.getHashOfPass());
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(resultOne != null && resultOne.equals("1")) {
						resultTwo = model.insertUserInfo(userID, firstName, lastName, address, address2,city, province, postal, country, phone);
						if(resultTwo != null && !resultTwo.equals("1")) {
							//remove inserted user
							try {
								model.deleteUser(acc.getUsername());
							} catch (URISyntaxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							request.setAttribute("test", "FAILED TO CREATE ACCOUNT!!");
						}else {
							request.setAttribute("test", "ACCOUNT CREATED!!");
						}
					}else {
						request.setAttribute("test", "FAILED TO CREATE ACCOUNT!!");
					}
				}else {
					request.setAttribute("test", "FAILED TO CREATE ACCOUNT!!");
				}

				//request.setAttribute("test", resultOne);
				
//				if(resultOne != null && resultTwo != null) {
//					request.setAttribute("test", "clicked create account" + resultOne + resultTwo);
//				}
				
				session.setAttribute("isPaymentRegister", false);
				session.setAttribute("isPaymentRegister", false);
				session.setAttribute("isNotLoggedIn", true);
			
			request.getRequestDispatcher(paymentPage).forward(request, response);
		}else if(request.getParameter("proceedToPayment") != null && request.getParameter("proceedToPayment").equals("true")){//after users confirm their info
			session.setAttribute("isPaymentRegister", false);
			session.setAttribute("isPaymentRegister", false);
			session.setAttribute("isNotLoggedIn", false);
			
			//set all session info here after user confirms
			
			//all shipping info
			String shippingFirstName= request.getParameter("shippingFirstName");
			String shippingLastName = request.getParameter("shippingLastName");
			String shippingAddress = request.getParameter("shippingAddress");
			String shippingAddress2 = request.getParameter("shippingAddress2");
			String shippingCity = request.getParameter("shippingCity");
			String shippingProvince = request.getParameter("shippingProvince");
			String shippingPostalCode = request.getParameter("shippingPostalCode");
			String shippingCountry = request.getParameter("shippingCountry");
			String shippingPhone = request.getParameter("shippingPhone");
		
			//all billing info provided
			String billingFirstname = request.getParameter("billingFirstname");
			String billingLastname = request.getParameter("billingLastname");
			String billingAddress = request.getParameter("billingAddress");
			String billingAddress2 = request.getParameter("billingAddress2");
			String billingCity = request.getParameter("billingCity");
			String billingProvince = request.getParameter("billingProvince");
			String billingPostalCode = request.getParameter("billingPostalCode");
			String billingCountry = request.getParameter("billingCountry");
			String billingPhone = request.getParameter("billingPhone");
			String sameAs = request.getParameter("hideBilling");
			
			
			String comment = request.getParameter("shippingComment");
			
			//first validate the request info to make sure its valid then update session scope
			Boolean same = false;
			if (sameAs != null) {
				same = true;// edit so it gets from user their option 
			}
			
			if (model.validateReview(comment)) {
				session.setAttribute("comment", comment);
			}
			
			
			if(model.validateFName(shippingFirstName)) {
				session.setAttribute("firstName", shippingFirstName);
				if (same) {
					session.setAttribute("fName", shippingFirstName);
				}
			}

			if(model.validateLName(shippingLastName)) {
				session.setAttribute("lastName", shippingLastName);
				if (same) {
					session.setAttribute("lName", shippingLastName);
				}
			}
			
			if(model.validateAddress(billingAddress)) {
				session.setAttribute("address1", shippingAddress);
				if (same) {
					session.setAttribute("address", shippingAddress);
				}
			}
			
			if(model.validateAddress(billingAddress)) {
				session.setAttribute("address2", shippingAddress2);
			}
			
			if (model.validateCity(shippingCity)){
				session.setAttribute("city", shippingCity);
				if (same) {
					session.setAttribute("city1", shippingCity);
				}
			}
			
			if(model.validateProvince(shippingProvince)) {
				session.setAttribute("province", shippingProvince);
				if(same) {
					session.setAttribute("province1", shippingProvince);
				}
			}
			
			if(model.validatePostal(shippingPostalCode)) {
				session.setAttribute("postal", shippingPostalCode);
				if(same) {
					session.setAttribute("postalCode1", shippingPostalCode);
				}
			}
			
			if(model.validateNumber(shippingPhone)) {
				session.setAttribute("number", shippingPhone);
				if(same) {
					session.setAttribute("phone1", shippingPhone);
				}
			}

			if (model.validateCountry(shippingCountry)) {
				session.setAttribute("country", shippingCountry);
				if(same) {
					session.setAttribute("country1", shippingCountry);
				}
			}
			
	
			
 
			//need to first validate all these
			if(!same) {//set same session info for billing as for shipping
				if(model.validateFName(billingFirstname)) {
					session.setAttribute("fName", billingFirstname);
				}
				
				if(model.validateLName(billingLastname)) {
					session.setAttribute("lName", billingLastname);				
				}

				if(model.validateAddress(billingAddress)) {
				session.setAttribute("address", billingAddress);					
				}
				
				if(model.validateCity(billingCity)) {
					session.setAttribute("city1", billingCity);					
				}
				
				if(model.validateProvince(billingProvince)) {
					session.setAttribute("province1", billingProvince);				
				}
				
				if(model.validatePostal(billingPostalCode)) {
					session.setAttribute("postalCode1", billingPostalCode);
				}

				if(model.validateNumber(billingPhone)) {
					session.setAttribute("phone1", billingPhone);				
				}
				
				if(model.validateCountry(billingCountry)) {
					session.setAttribute("country1", billingCountry);				
				}
			}
			
			//validate the new info and if 
			//work here
			
			request.getRequestDispatcher(paymentPage).forward(request, response);
		}else if(request.getParameter("CHECKOUT") != null && request.getParameter("CHECKOUT").equals("true") && session.getAttribute("isLoggedIn") != null && session.getAttribute("isLoggedIn").equals(true) && session.getAttribute("items") != null){
			//person wants to purchase should process order and add to db if successful order and clear cart if successful
			
			String first = (String) session.getAttribute("firstName");
			String last = (String) session.getAttribute("lastName");
			String address1 = (String) session.getAttribute("address1");
			String address2 = (String) session.getAttribute("address2");
			String city = (String) session.getAttribute("city");
			String province = (String) session.getAttribute("province");
			String postal = (String) session.getAttribute("postal");
			String number = (String) session.getAttribute("number");
			String country = (String) session.getAttribute("country");
			
			String billingStreet = (String) session.getAttribute("address");
			String billingProvince = (String) session.getAttribute("province1");
			String billingCountry = (String) session.getAttribute("country1");
			String billingZip = (String) session.getAttribute("postalCode1");
			String billingPhone = (String) session.getAttribute("phone1");
			String comment = (String) session.getAttribute("comment");
			
			
			String status ="";
			String msg = "";
			int mod = (po % 3);
			if(mod == 1) {
				status = "ORDERED";
				msg = "Order Successfully Completed";
			}else if(mod == 2) {
				status = "PROCESSED";
				msg = "Order Successfully Completed";
			}else {
				status = "DENIED";
				msg = "Credit Card Authorization Failed";
			}
			
			session.setAttribute("status", status);
			
			if(first == null || last == null || address1 == null || province == null || postal == null) {
				session.setAttribute("msg", "failed to place order");
				request.getRequestDispatcher(confirmOrderPage).forward(request, response);
			}else{
				int i = 0;
				try {
					i = model.insertAddress(address1, province, country, postal, number);
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (i == 1) {
					try {
						model.insertBillingAddress(billingStreet, billingProvince, billingCountry, billingZip, billingPhone, comment);
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				String id = null;
				try {
					id = model.retrieveAddressID(address1, province, country, postal, number);
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					model.insertPO(last, first, status, id);
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				session.getAttribute("items");
				
				@SuppressWarnings("unchecked")
				List<cartItemBean> books = (List<cartItemBean>) session.getAttribute("items");
				
				for(cartItemBean item: books) {
						String bookID = item.getBid();
						int price = item.getPrice();
						int quantity = item.getQuantity();
						try {
							model.insertPOitem(id, bookID, price, quantity);
						} catch (URISyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				
				po++;
				if(po > 9) {
					po = 1;
				}
				
				if(status != null && !status.equals("DENIED")) {
					books.clear();
				}

				session.setAttribute("msg", msg);
				session.setAttribute("items", books);
				updateCartPrices(books,session);
				request.getRequestDispatcher(successPage).forward(request, response);
			}
			
		}else{ //when the user wants to see main page or is on main page
			
			if(request.getParameter("logout") != null && request.getParameter("logout").equals("true")) {
				session.invalidate();
			}
			
			if(request.getParameter("addToCart") != null){//when user presses add to cart button
				bid = request.getParameter("bookBID");
				
				BookBean book = null;
				try {
					book = model.retrieveBookByBid(bid);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				addToCart(book, session); //adds book to cart
			}
			
			String category = request.getParameter("category");
			
			Map<String, BookBean> bookInfo = new HashMap<>();
			ArrayList<BookBean> books = new ArrayList<BookBean>();
			
			//access model and get all the books in record
			//test fix later
			if(category != null) {
				request.setAttribute("category", category);
				//access model and get book with specific category
				try {
					bookInfo = model.retriveBooksByCategory(category);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else {
				request.setAttribute("category", "View All");
				//access model and get all the books in record
				try {
					bookInfo = model.retriveAllBooks();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			
			for (Map.Entry<String, BookBean> entry :bookInfo.entrySet()) {
				books.add(entry.getValue());
				System.out.println(entry.getValue().getBid());
				System.out.println(entry.getValue().getTitle());
				System.out.println(entry.getValue().getCategory());
				System.out.println(entry.getValue().getPrice());
			}
			
			request.setAttribute("books", books);
			request.getRequestDispatcher(mainPage).forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	
	private void addToCart(BookBean book, HttpSession session) {
		if(session.getAttribute("items") == null && book != null) {//if items list is not made make one and add book to cart
			List<cartItemBean> books = new ArrayList<>();
			
			cartItemBean item = new cartItemBean(book.getBid(), book.getTitle(), book.getPrice(), book.getCategory(), 1);
			books.add(item);
			
			updateCartPrices(books, session);
			
			session.setAttribute("items", books);
		}else if(book != null){ //if items list is already made just add book to cart
			@SuppressWarnings("unchecked")
			List<cartItemBean> books = (List<cartItemBean>) session.getAttribute("items");
			boolean found =false;
			
			for(cartItemBean item: books) {
				if(item.getBid().equals(book.getBid())) {
					item.incrementQuantity();
					found =true;
				}
			}
			
			if(!found) {
				cartItemBean item = new cartItemBean(book.getBid(), book.getTitle(), book.getPrice(), book.getCategory(), 1);
				books.add(item);
			}
			
			updateCartPrices(books, session);
			session.setAttribute("items", books);
		}
	}
	
	private void removeFromCart(String bid, HttpSession session) {
		if(session.getAttribute("items") != null) {
			@SuppressWarnings("unchecked")
			List<cartItemBean> books = (List<cartItemBean>) session.getAttribute("items");
			cartItemBean book = null;
			boolean found = false;
			
			for(cartItemBean item: books) {
				if(item.getBid().equals(bid)) {
					found =true;
					book = item;
				}
			}
			
			if (found) {
				books.remove(book);
			}
			
			updateCartPrices(books, session);
			session.setAttribute("items", books);
		}
	}
	
	private void updateCartPrices(List<cartItemBean> books, HttpSession session) {
		double subtotal = model.getSubtotal(books);
		double shipping = 0;
		double tax = model.getTax(subtotal, shipping);
		double total = model.getTotal(subtotal, shipping, tax);
		
		session.setAttribute("subtotalPrice", subtotal);
		session.setAttribute("shippingPrice", shipping);
		session.setAttribute("taxPrice", tax);
		session.setAttribute("totalPrice", total);
	}
	
	private void incrementQuantityOfBook(String bid, HttpSession session) {
		if(session.getAttribute("items") != null) {
			@SuppressWarnings("unchecked")
			List<cartItemBean> books = (List<cartItemBean>) session.getAttribute("items");
			
			for(cartItemBean item: books) {
				if(item.getBid().equals(bid)) {
					item.incrementQuantity();
				}
			}
			updateCartPrices(books, session);
			session.setAttribute("items", books);
		}
	}
	
	private void decrementQuantityOfBook(String bid, HttpSession session) {
		if(session.getAttribute("items") != null) {
			@SuppressWarnings("unchecked")
			List<cartItemBean> books = (List<cartItemBean>) session.getAttribute("items");
			int quantity = 1;
			
			for(cartItemBean item: books) {
				if(item.getBid().equals(bid)) {
					item.decrementQuantity();
					quantity = item.getQuantity();
				}
			}
			
			updateCartPrices(books, session);
			session.setAttribute("items", books);
			
			if(quantity <= 0) {
				removeFromCart(bid, session);
			}	
		}
	}
}
