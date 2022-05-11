package dao;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import bean.AccountBean;
import bean.AccountCreatedBean;

public class UserDAO {
	DataSource ds;
	
	public UserDAO() throws ClassNotFoundException{
		try{
			ds = (DataSource) (new InitialContext()).lookup("jdbc/Db2-EECS4413");
		} catch(NamingException e) {
				e.printStackTrace();
		}
	}
	
	
	public AccountBean retrieveUser(String Username) throws SQLException, NoSuchAlgorithmException {
		String query = "select * from USER where USERNAME='" + Username + "'";
		AccountBean acc = null;
		
		Connection con = (Connection) this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		
		ResultSet r = p.executeQuery();
		
		while(r.next()){
			String userID = r.getString("Username");
			String hashPass = r.getString("HASHEDPASSWORD");
			
			acc = new AccountBean(userID, hashPass);
		}
		r.close();
		p.close();
		con.close();
		return acc;
	}
	
	public AccountCreatedBean retrieveUserInfo(String Username) throws SQLException, NoSuchAlgorithmException {
		String query = "SELECT A.USERNAME, A.HASHEDPASSWORD, B.FNAME, B.LNAME, B.ADDRESS,B.ADDRESS2, B.CITY, B.PROVINCE, B.POSTALCODE, B.COUNTRY, B.PHONENUMBER FROM USER A, USERINFO B WHERE A.USERNAME = B.USERNAME AND A.USERNAME='" + Username + "'";
		AccountCreatedBean acc = new AccountCreatedBean();
		
		Connection con = (Connection) this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		
		ResultSet r = p.executeQuery();
		
		while(r.next()){
			String userID = r.getString("USERNAME");
			String hashPass = r.getString("HASHEDPASSWORD");
			String fName = r.getString("FNAME");
			String lName = r.getString("LNAME");
			String address = r.getString("ADDRESS");
			String address2 = r.getString("ADDRESS2");
			String city = r.getString("CITY");
			String province = r.getString("PROVINCE");
			String postal = r.getString("POSTALCODE");
			String country = r.getString("COUNTRY");
			String phone = r.getString("PHONENUMBER");
			
			acc.setUsername(userID);
			acc.setHashOfPass(hashPass);
			acc.setFirstName(fName);
			acc.setLastName(lName);
			acc.setAddress(address);
			acc.setAddreess2(address2);
			acc.setCity(city);
			acc.setProvince(province);
			acc.setPostalCode(postal);
			acc.setCountry(country);
			acc.setPhoneNumber(phone);
		}
		r.close();
		p.close();
		con.close();
		return acc;
	}


	public int insertUserInfo(String userID, String firstName, String lastName, String addressOne, String addressTwo,
		String citySanitized, String provinceSanitized, String postal, String countrySanitized, String phone) throws SQLException {
		String query = "INSERT INTO USERINFO (USERNAME, FNAME, LNAME,ADDRESS,ADDRESS2, CITY,PROVINCE,POSTALCODE,COUNTRY, PHONENUMBER) VALUES (?,?,?,?,?,?,?,?,?,?)";
		
		Connection con = (Connection) this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		
		p.setString(1, userID);
		p.setString(2, firstName);
		p.setString(3, lastName);
		p.setString(4, addressOne);
		p.setString(5, addressTwo);
		p.setString(6, citySanitized);
		p.setString(7, provinceSanitized);
		p.setString(8, postal);
		p.setString(9, countrySanitized);
		p.setString(10, phone);
		
		int i = p.executeUpdate();
		
		con.close();
		
		return i;
	}
	
	public int insertUserInfoWithoutPhone(String userID, String firstName, String lastName, String addressOne, String addressTwo,
			String citySanitized, String provinceSanitized, String postal, String countrySanitized) throws SQLException {
			String query = "INSERT INTO USERINFO (USERNAME, FNAME, LNAME,ADDRESS,ADDRESS2, CITY,PROVINCE,POSTALCODE,COUNTRY) VALUES (?,?,?,?,?,?,?,?,?)";
			
			Connection con = (Connection) this.ds.getConnection();
			PreparedStatement p = con.prepareStatement(query);
			
			p.setString(1, userID);
			p.setString(2, firstName);
			p.setString(3, lastName);
			p.setString(4, addressOne);
			p.setString(5, addressTwo);
			p.setString(6, citySanitized);
			p.setString(7, provinceSanitized);
			p.setString(8, postal);
			p.setString(9, countrySanitized);
			
			int i = p.executeUpdate();
			
			con.close();
			
			return i;
		}
	
	public int insertUserInfoWithoutAddressTwo(String userID, String firstName, String lastName, String addressOne,
			String citySanitized, String provinceSanitized, String postal, String countrySanitized, String phone) throws SQLException {
			String query = "INSERT INTO USERINFO (USERNAME, FNAME, LNAME,ADDRESS, CITY,PROVINCE,POSTALCODE,COUNTRY, PHONENUMBER) VALUES (?,?,?,?,?,?,?,?,?)";
			
			Connection con = (Connection) this.ds.getConnection();
			PreparedStatement p = con.prepareStatement(query);
			
			p.setString(1, userID);
			p.setString(2, firstName);
			p.setString(3, lastName);
			p.setString(4, addressOne);
			p.setString(5, citySanitized);
			p.setString(6, provinceSanitized);
			p.setString(7, postal);
			p.setString(8, countrySanitized);
			p.setString(9, phone);
			
			int i = p.executeUpdate();
			
			con.close();
			
			return i;
		}
	
	public int insertUserInfoWithoutAddressNPhone(String userID, String firstName, String lastName, String addressOne,
			String citySanitized, String provinceSanitized, String postal, String countrySanitized) throws SQLException {
			String query = "INSERT INTO USERINFO (USERNAME, FNAME, LNAME,ADDRESS, CITY,PROVINCE,POSTALCODE,COUNTRY) VALUES (?,?,?,?,?,?,?,?)";
			
			Connection con = (Connection) this.ds.getConnection();
			PreparedStatement p = con.prepareStatement(query);
			
			p.setString(1, userID);
			p.setString(2, firstName);
			p.setString(3, lastName);
			p.setString(4, addressOne);
			p.setString(5, citySanitized);
			p.setString(6, provinceSanitized);
			p.setString(7, postal);
			p.setString(8, countrySanitized);
			
			int i = p.executeUpdate();
			
			con.close();
			
			return i;
		}


	public int insertUser(String userID, String hash) throws SQLException {
		String query = "INSERT INTO USER (USERNAME, HASHEDPASSWORD) VALUES  (?,?)";
		
		Connection con = (Connection) this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		
		p.setString(1, userID);
		p.setString(2, hash);
		
		int i = p.executeUpdate();
		
		con.close();
		
		return i;
	}


	public int deleteUser(String userID) throws SQLException {
		String query = "DELETE FROM USER WHERE USERNAME=?";
		
		Connection con = (Connection) this.ds.getConnection();
		PreparedStatement p = con.prepareStatement(query);
		
		p.setString(1, userID);
		
		int i = p.executeUpdate();
		
		con.close();
		
		return i;
	}
	
	
	public int insertPO(String lname, String fname,String status, String address) throws SQLException {
		String query = "INSERT INTO PO (lname, fname, status, address)  VALUES  (?,?,?,?)";
		
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
	
	
}
