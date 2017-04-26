package assignment7;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class UserInfo implements Serializable {
	
	private static ArrayList<UserInfo> users = new ArrayList<UserInfo>();
	
	private boolean flag = false;			// Used to determine if username/password found
	private boolean getUsersFlag = false;	// Used to determine if server should return a list of users
	private String name = null;
	private String username;
	private String password;
	private ObjectOutputStream toClient; 


	public boolean isGetUsersFlag() {
		return getUsersFlag;
	}
	public void setGetUsersFlag(boolean getUsersFlag) {
		this.getUsersFlag = getUsersFlag;
	}

	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public static ArrayList<UserInfo> getUsers() {
		return users;
	}
	public static void setUsers(ArrayList<UserInfo> users) {
		UserInfo.users = users;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public ObjectOutputStream getToClient() {
		return toClient;
	}
	public void setToClient(ObjectOutputStream toClient) {
		this.toClient = toClient;
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
	

	
	
}
