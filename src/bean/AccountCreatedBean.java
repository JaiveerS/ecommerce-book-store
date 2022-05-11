package bean;

import java.security.NoSuchAlgorithmException;

public class AccountCreatedBean extends AccountBean{
	private String firstName;
	private String lastName;
	private String address;
	private String addreess2;
	private String city;
	private String province;
	private String postalCode;
	private String country;
	private String phoneNumber;
	
	
	public AccountCreatedBean(String username, String hashPass, String firstName, String lastName, String address,
			String addreess2, String city, String province, String postalCode, String country, String phoneNumber)
			throws NoSuchAlgorithmException {
		super(username, hashPass);
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.addreess2 = addreess2;
		this.city = city;
		this.province = province;
		this.postalCode = postalCode;
		this.country = country;
		this.phoneNumber = phoneNumber;
	}

	public AccountCreatedBean() {
		// TODO Auto-generated constructor stub
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		if(firstName != null) {
			this.firstName = firstName;
		}
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		if(lastName != null) {
			this.lastName = lastName;			
		}
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		if(address != null) {
			this.address = address;
		}
	}

	public String getAddreess2() {
		return addreess2;
	}

	public void setAddreess2(String addreess2) {
		if(addreess2 != null) {
			this.addreess2 = addreess2;
		}
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		if(city != null) {
			this.city = city;
		}
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		if(province != null) {
			this.province = province;			
		}
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		if(postalCode != null) {
			this.postalCode = postalCode;
		}
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		if(country != null) {
			this.country = country;			
		}
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		if(phoneNumber != null) {
			this.phoneNumber = phoneNumber;			
		}

	}
}
