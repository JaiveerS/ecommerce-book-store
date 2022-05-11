package bean;

public class BookInfoBean extends BookBean{
	private String description;
	
	public BookInfoBean(String bid, String title, int price, String category, String description) {
		super(bid, title, price, category);
		this.setDescription(description);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
