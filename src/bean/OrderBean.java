package bean;

public class OrderBean {
	
	private int orderId;
	private String lastName;
	private String firstName;
	private String status;
	private int quantity;
	private AddressBean shipping;
	private AddressBean billing;
	private String comment;
	
	public OrderBean (int orderId, String lastName, String firstName, String status, int quantity, 
			AddressBean shipping, AddressBean billing, String comment) {
		this.orderId = orderId;
		this.lastName = lastName;
		this.firstName = firstName;
		this.status = status;
		this.quantity = quantity;
		this.shipping = shipping;
		this.billing = billing;
		this.comment = comment;
	}
	
	public AddressBean getShipping() {
		return shipping;
	}

	public void setShipping(AddressBean shipping) {
		this.shipping = shipping;
	}

	public AddressBean getBilling() {
		return billing;
	}

	public void setBilling(AddressBean billing) {
		this.billing = billing;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	public int getOrderId() {
		return orderId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
