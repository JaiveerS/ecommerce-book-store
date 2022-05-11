package bean;

public class BookReviewBean {
	private String bid;
	private String firstname;
	private String lastname;
	private int rating;
	private String review;
	
	
	public BookReviewBean(String bid, String firstname, String lastname, int rating, String review) {
		super();
		this.bid = bid;
		this.firstname = firstname;
		this.lastname = lastname;
		this.rating = rating;
		this.review = review;
	}


	public String getBid() {
		return bid;
	}


	public void setBid(String bid) {
		this.bid = bid;
	}


	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return lastname;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public int getRating() {
		return rating;
	}


	public void setRating(int rating) {
		this.rating = rating;
	}


	public String getReview() {
		return review;
	}


	public void setReview(String review) {
		this.review = review;
	}
	
	
	
	
}
