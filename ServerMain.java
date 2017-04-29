 /* EE422C Project 7 submission by
 * Replace <...> with your actual data.
 * Kevin Chau
 * kc28535
 * 18238
 * Ashkan Vafaee
 * av28837
 * 18238
 * Slip days used: <1>
 * Git URL: https://github.com/ashkanvafaee/assignment7
 * Spring 2017
*/
package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

public class ServerMain extends Observable {

	public static void main(String[] args) {

		try {
			new ServerMain().init();

		} catch (Exception e) {
			

		}

	}

	private void init() throws Exception {
		ServerSocket serverSock = new ServerSocket(4242);

		while (true) {
			Socket clientSocket = serverSock.accept();
			ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());

			ClientHandler client = new ClientHandler(clientSocket);
			client.writer = writer;

			Thread t = new Thread(client);
			t.start();
			this.addObserver(writer);

			System.out.println("Got a connection");
		}

	}

	class ClientHandler implements Runnable {
		private ObjectInputStream inputFromClient;

		// Client Identifier
		private ClientObserver writer;

		public ClientHandler(Socket clientSocket) {
			Socket sock = clientSocket;

			try {
				inputFromClient = new ObjectInputStream(clientSocket.getInputStream());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {

			try {

				Object object;

				while (true) {
					object = inputFromClient.readObject();
					if (object != null) {

						// PACKET

						if (object instanceof Packet) {

							System.out.println("PACKET RECEIVED");

							setChanged();
							notifyObservers(object);

						}

						// USERINFO
						if (object instanceof UserInfo) {

							System.out.println("USER INFO RECEIVED:" + ((UserInfo) object).getUsername());

							// Get list of all users
							if (((UserInfo) object).getUserFlag()) {
								((UserInfo) object).setSendUsers(UserInfo.getUsers());
								System.out.println("GETTING LIST OF USERS");
								System.out.println(UserInfo.getUsers().size());
								System.out.println(UserInfo.getUsers().get(0).getName());
								setChanged();
								notifyObservers(new UserInfo((UserInfo) object));
							}

							// Updates user's friends list
							else if (((UserInfo) object).isUpdateFlag()) {
								System.out.println("IN UPDATE");
								System.out.println("SIZE =" + UserInfo.getUsers().size());

								for (int i = 0; i < UserInfo.getUsers().size(); i++) {
									if (UserInfo.getUsers().get(i).getUsername()
											.equals(((UserInfo) object).getUsername())) {
										System.out.println("UPDATE SUCCESSFUL");
										System.out.println(
												"FRIENDSLIST SIZE: " + ((UserInfo) object).getFriendList().size());
										UserInfo temp = new UserInfo((UserInfo.getUsers().get(i)));
										UserInfo.getUsers().remove(i);
										temp.getFriendList().add(((UserInfo) object).getFriendList().get(0));
										UserInfo.getUsers().add(temp);
										System.out.println("****" + UserInfo.getUsers().get(i).getUsername());
										break;

									}
								}

								for (UserInfo i : UserInfo.getUsers()) {
									System.out.println(i.getUsername() + i.getFriendList().size());
								}

							}

							// Check if username and password valid
							else if (((UserInfo) object).getName() == null) {

								boolean noUsersFlag = false;

								for (UserInfo ul : UserInfo.getUsers()) {
									if (ul.getUsername().equals(((UserInfo) object).getUsername())) {
										if (ul.getPassword().equals(((UserInfo) object).getPassword())) {
											setChanged();
											// ul.setFlag(true);
											UserInfo temp = new UserInfo(ul);
											System.out.println("SERVER SIDE FRIENDS LIST SIZE: "
													+ temp.getFriendList().size() + temp.getUsername());
											temp.setLoginFound(true);
											notifyObservers(temp);
											noUsersFlag = true;

											break;
										} else {
											setChanged();
											UserInfo temp = new UserInfo(ul);
											temp.setLoginFound(false);
											notifyObservers(temp);
											noUsersFlag = true;
											break;
										}

									}
								}

								if (!noUsersFlag) {
									UserInfo temp = new UserInfo();
									setChanged();
									notifyObservers(temp);
								}
							}

							// Add new client to the list
							else {
								
								System.out.println("TRYING TO ADD USER");
								
								boolean alreadyAdded = false;
								
								System.out.println(UserInfo.getUsers().size());
								
								for(UserInfo i: UserInfo.getUsers()){
									if(i.getUsername().equals(((UserInfo)object).getUsername())){
										
										alreadyAdded = true;
									}
								}
								
								if(!alreadyAdded){
									System.out.print("ADDING");
									UserInfo.getUsers().add((UserInfo) object);
								}
								
								// don't add if already exists
								else{
									System.out.println("NOT ADDED");
									((UserInfo)object).setGetUserFlag(true);
									((UserInfo)object).setSendUsers(UserInfo.getUsers());
									setChanged();
									notifyObservers(new UserInfo((UserInfo)object));
									
								}
						
							}

						}

						// STRING
						if (object instanceof String) {
							System.out.println("server read " + (String) object);
							setChanged();
							notifyObservers(object);


						}
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
