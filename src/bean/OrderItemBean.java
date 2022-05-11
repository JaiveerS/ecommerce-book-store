package bean;

/**
 * 
 * @author ARIF
 * 
 * Schema for Purchase Order Item
 * 
 * CREATE TABLE POItem (
	id INT UNSIGNED NOT NULL,
	bid VARCHAR(20) NOT NULL,
	price INT UNSIGNED NOT NULL,
	PRIMARY KEY(id,bid),
	INDEX (id),
	FOREIGN KEY(id) REFERENCES PO(id) ON DELETE CASCADE,
	INDEX (bid),
	FOREIGN KEY(bid) REFERENCES Book(bid) ON DELETE CASCADE
);
 *
 */

public class OrderItemBean {
	
	private OrderBean order;
	private BookBean book;
	
	public OrderItemBean(OrderBean order, BookBean book) {
		this.order = order;
		this.book = book;
	}

	public OrderBean getOrder() {
		return order;
	}

	public void setOrder(OrderBean order) {
		this.order = order;
	}

	public BookBean getBook() {
		return book;
	}

	public void setBook(BookBean book) {
		this.book = book;
	}
}
