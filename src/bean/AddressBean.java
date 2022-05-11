package bean;

public class AddressBean {
	
	private int id;
	private String firstName;
	private String lastName;
	private String street;
	private String province;
	private String country;
	private String zip;
	
	public AddressBean (int id, String firstName, String lastName, String street, String province, String country, String zip) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.street = street;
		this.province = province;
		this.country = country;
		this.zip = zip;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
}