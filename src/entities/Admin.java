package entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Admin
 *
 */
@Entity

public class Admin implements Serializable {

	
	private static final long serialVersionUID = 1L;
	@Id
	private String username;
	private String password;
	private int accessLevel;
	private boolean status;

	public Admin() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
   
}
