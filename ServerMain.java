package assignment7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

			Thread t = new Thread(new ClientHandler(clientSocket));
			t.start();
			this.addObserver(writer);
			System.out.println("Got a connection");
		}

	}
	
	


	class ClientHandler implements Runnable {
		private ObjectInputStream inputFromClient;
		private ObjectOutputStream outputFromClient;

		public ClientHandler(Socket clientSocket) {
			Socket sock = clientSocket;

			try {
				inputFromClient = new ObjectInputStream(clientSocket.getInputStream());
				outputFromClient = new ObjectOutputStream(clientSocket.getOutputStream());

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
							
							// Check if username and password valid
							if(((UserInfo) object).getName() == null){
								
								for(UserInfo ul : UserInfo.getUsers()){
									if(ul.getUsername().equals(((UserInfo) object).getUsername())){
										if(ul.getPassword().equals(((UserInfo) object).getPassword())){
											setChanged();
											ul.setFlag(true);
											notifyObservers(ul);
										}
										else{
											setChanged();
											ul.setFlag(false);
											notifyObservers(ul);
										}
										
										
									}
								}
								
								setChanged();
								Boolean b = false;
								notifyObservers(b);
								
								
								
							}
							else{
								// Associates the outputstream to its respective client
								((UserInfo)object).setToClient(outputFromClient);
								UserInfo.getUsers().add((UserInfo)object);
								
							}
							
						}

						// STRING
						if (object instanceof String) {
							System.out.println("server read " + (String) object);
							setChanged();
							notifyObservers(object);
							// Observer.update(obj); // to notify a single observer

						}
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
