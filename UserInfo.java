package assignment7;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class UserInfo implements Serializable {

	private static ArrayList<UserInfo> users = new ArrayList<>();
	private ArrayList<UserInfo>	sendUsers;
	private ArrayList<UserInfo> friends = new ArrayList<>();

	private boolean getUser = false;
	private boolean loginFound = false;
	private String name = null;
	private Socket clientSocket;
	private String username;
	private String password;
	// Client Identifier
	private ClientObserver writer;

	// Copy Constructor
	public UserInfo(UserInfo ui) {
		this.setUsers(ui.getUsers());
		this.setLoginFound(ui.getLoginFound());
		this.name = ui.name;
		this.clientSocket = ui.clientSocket;
		this.username = ui.username;
		this.password = ui.password;
		this.writer = ui.writer;
	}

	public UserInfo() {

	}

	public ClientObserver getWriter() {
		return writer;
	}

	public void setWriter(ClientObserver writer) {
		this.writer = writer;
	}

	public boolean getUserFlag() {
		return getUser;
	}

	public void setGetUserFlag(boolean getUser) {
		this.getUser = getUser;
	}

	public boolean getLoginFound() {
		return loginFound;
	}

	public void setLoginFound(boolean flag) {
		this.loginFound = flag;
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
	
	public ArrayList<UserInfo> getFriendList() {
		return friends;
	}
	
	public ArrayList<UserInfo> getSendUsers() {
		sendUsers = new ArrayList<>(users);
		return sendUsers;
	}
	
	public void setSendUsers(ArrayList<UserInfo> newUserList) {
		sendUsers = new ArrayList<>(newUserList);
		users = newUserList;
	}
}
