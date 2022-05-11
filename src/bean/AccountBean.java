package bean;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AccountBean {
	private String username;
	private String hashOfPass;
	


	public AccountBean(String username, String hashPass) throws NoSuchAlgorithmException {
		super();
		this.username = username;
		this.hashOfPass = hashPass;
	}
	
	public AccountBean() {
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setHashOfPass(String password) {
		try {
			this.hashOfPass = hashPassword(password);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getHashOfPass() {
		return hashOfPass;
	}

	public String hashPassword(String pass) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-512");
		digest.reset();
		byte[] hash = digest.digest(pass.getBytes());
		
		BigInteger no = new BigInteger(1, hash);
		String hashtext = no.toString(16); 
		
		while (hashtext.length() < 32) { 
            hashtext = "0" + hashtext; 
        } 
		
		return hashtext;
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		AccountBean acc= new AccountBean();
		String hash = acc.hashPassword("test1");
		acc = new AccountBean("Jaiveer", hash);
		
		System.out.println(acc.getHashOfPass());
	}
}
