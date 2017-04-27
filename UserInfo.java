package assignment7;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class UserInfo implements Serializable {
	
	private static ArrayList<UserInfo> users = new ArrayList<UserInfo>();
	
	private boolean getUser = false;
	private boolean flag = false;
	private String name = null;
	private Socket clientSocket;
	private String username;
	private String password;
	// Client Identifier
	private ClientObserver writer;


	public ClientObserver getWriter() {
		return writer;
	}
	public void setWriter(ClientObserver writer) {
		this.writer = writer;
	}
	public boolean isGetUser() {
		return getUser;
	}
	public void setGetUser(boolean getUser) {
		this.getUser = getUser;
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
	public Socket getClientSocket() {
		return clientSocket;
	}
	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
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
