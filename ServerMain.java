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

			// Adding writer to list of users
			// UserInfo.

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
			String message = "";

			try {

				Object object;

				while (true) {
					object = inputFromClient.readObject();
					if (object != null) {

						// USERINFO
						if (object instanceof UserInfo) {

							// Get list of all users
							if (((UserInfo) object).getUserFlag()) {
								((UserInfo) object).setSendUsers(UserInfo.getUsers());
								System.out.println("GETTING LIST OF USERS");
								System.out.println(UserInfo.getUsers().size());
								System.out.println(UserInfo.getUsers().get(0).getName());
								setChanged();
								notifyObservers(object);
							}

							// Check if username and password valid
							else if (((UserInfo) object).getName() == null) {

								for (UserInfo ul : UserInfo.getUsers()) {
									if (ul.getUsername().equals(((UserInfo) object).getUsername())) {
										if (ul.getPassword().equals(((UserInfo) object).getPassword())) {
											setChanged();
											// ul.setFlag(true);
											UserInfo temp = new UserInfo(ul);
											temp.setLoginFound(true);
											notifyObservers(temp);

											break;
										} else {
											setChanged();
											UserInfo temp = new UserInfo(ul);
											temp.setLoginFound(false);
											notifyObservers(temp);
											break;
										}

									}
								}

							}


							// Add new client to the list
							else {
								// ((UserInfo)object).setWriter(writer);
								UserInfo.getUsers().add((UserInfo) object);
								System.out.println("ADDING CLIENT");
								System.out.println(UserInfo.getUsers().size());
								System.out.println(UserInfo.getUsers().get(0).getName());
							}

						}

						// STRING
						if (object instanceof String) {
							System.out.println("server read " + (String) object);
							setChanged();
							notifyObservers(object);
							// Observer.update(obj); // to notify a single
							// observer

						}
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
