package bean;

public class cartItemBean extends BookBean{
	private int quantity;
	
	public cartItemBean(String bid, String title, int price, String category, int quantity) {
		super(bid, title, price, category);
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public void incrementQuantity() {
		quantity++;
	}
	
	public void decrementQuantity() {
		quantity--;
	}
}
