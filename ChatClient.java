package assignment7;

import java.awt.Event;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.event.HyperlinkEvent.EventType;

import javafx.collections.*;
import javafx.scene.input.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

public class ChatClient extends Application {

	private TextArea output;
	private TextField input;
	
    private static int yPos = 1;


	private boolean wait = false;
	private boolean accountFound = false;

	private static ObjectInputStream fromServer;
	private static ObjectOutputStream toServer;

	private static String name;
	private static UserInfo UI;
	
	// maps usernames to names
	private HashMap<String,String> usernameToName = new HashMap<>();
	
	private static ArrayList<ChatBox> chatBoxes = new ArrayList<>();
	
	private ArrayList<UserInfo> allUsers = new ArrayList<>();

	private int worldWidth = 1400;
	private int worldHeight = 800;

	private ArrayList<Button> friendListButtons = new ArrayList<>();

	
	public static String getName(){
		return name;
	}
	
	public static ObjectOutputStream getToServer(){
		return toServer;
	}
	
	public int getWorldWidth() {
		return worldWidth;
	}

	public int getWorldHeight() {
		return worldHeight;
	}

	public ObjectInputStream fromServer() {
		return fromServer;
	}

	public ObjectOutputStream toServer() {
		return toServer;
	}

	public static void main(String[] args) {
		try {
			launch();
		} catch (Exception e) {

		}

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		setUpNetwork();
		
		
		primaryStage.setTitle("Chat Client");

		Pane grid = new Pane();

		// grid.setPadding(new Insets(5, 5, 5, 5));

		Scene scene = new Scene(grid, worldWidth, worldHeight);
		grid.setStyle("-fx-background-color: white;");
		primaryStage.setScene(scene);
		// primaryStage.show();

		// Login Screen
		GridPane scrollGrid = new GridPane();
		Button submitNew = new Button("Submit");
		Button submitOld = new Button("Submit");
		Label nameLabel = new Label("Name:");
		Label userNameLabel = new Label("Username:");
		Label passwordLabel = new Label("Password:");
		TextField name = new TextField();
		TextField userName = new TextField();
		TextField password = new TextField();

		// Add friend button
		Button addFriendBtn = new Button("Add Friend");
		TextField addFriendTF = new TextField();

		// Logout button
		Button logoutBtn = new Button("Logout");

		Pane login = new Pane();
		login.setStyle("-fx-background-color: white;");
		Scene loginScene = new Scene(login, 350, 400);
		Stage loginStage = new Stage();
		loginStage.setScene(loginScene);
		loginStage.show();
		Button newUser = new Button("New User");
		Button oldUser = new Button("Sign in");
		newUser.setMaxWidth(100);
		newUser.setMinWidth(100);
		newUser.setLayoutX(40);
		newUser.setLayoutY(200);

		oldUser.setMaxWidth(100);
		oldUser.setMinWidth(100);
		oldUser.setLayoutX(195);
		oldUser.setLayoutY(200);

		login.getChildren().add(oldUser);
		login.getChildren().add(newUser);

		// newUser handler
		newUser.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent AE) {
				login.getChildren().remove(oldUser);
				login.getChildren().remove(newUser);

				submitNew.setLayoutX(125);
				submitNew.setLayoutY(300);

				nameLabel.setLayoutX(30);
				nameLabel.setLayoutY(50);

				userNameLabel.setLayoutX(30);
				userNameLabel.setLayoutY(100);

				passwordLabel.setLayoutX(30);
				passwordLabel.setLayoutY(150);

				name.setLayoutX(140);
				name.setLayoutY(50);

				userName.setLayoutX(140);
				userName.setLayoutY(100);

				password.setLayoutX(140);
				password.setLayoutY(150);

				login.getChildren().add(nameLabel);
				login.getChildren().add(userNameLabel);
				login.getChildren().add(passwordLabel);
				login.getChildren().add(name);
				login.getChildren().add(userName);
				login.getChildren().add(password);
				login.getChildren().add(submitNew);

			}

		});

		// oldUser handler
		oldUser.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent AE) {
				login.getChildren().remove(newUser);
				login.getChildren().remove(oldUser);

				submitOld.setLayoutX(125);
				submitOld.setLayoutY(250);

				userNameLabel.setLayoutX(30);
				userNameLabel.setLayoutY(100);

				passwordLabel.setLayoutX(30);
				passwordLabel.setLayoutY(150);

				userName.setLayoutX(140);
				userName.setLayoutY(100);

				password.setLayoutX(140);
				password.setLayoutY(150);

				login.getChildren().add(userNameLabel);
				login.getChildren().add(passwordLabel);
				login.getChildren().add(userName);
				login.getChildren().add(password);
				login.getChildren().add(submitOld);

			}

		});

		// New user submit
		submitNew.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {

				UserInfo ui = new UserInfo();

				ui.setName(name.getText());
				ui.setUsername(userName.getText());
				ui.setPassword(password.getText());

				try {
					toServer.writeObject(ui);
				} catch (IOException e) {

				}

				ChatClient.name = name.getText();
				ChatClient.UI = ui;

				loginStage.close();
				primaryStage.show();
				primaryStage.setTitle(ui.getName());

			}

		});

		
		EventHandler<MouseEvent> event = new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				System.out.println("asdfasdf");
				output.appendText("asdfasdf");
				
			}
			
		};
		
		
		
		
		
		// Old user submit
		submitOld.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				UserInfo ui = new UserInfo();
				ui.setUsername(userName.getText());
				ui.setPassword(password.getText());

				try {
					toServer.writeObject(ui);

				} catch (Exception e) {
				}

				// Wait until validation received from server
				while (!wait) {
				}
				wait = false;

				if (UI.getLoginFound()) {
					loginStage.close();
					primaryStage.show();
					primaryStage.setTitle(UI.getName());
					ChatClient.name = UI.getName();

					System.out.println("FRIENDLIST SIZE: " + UI.getFriendList().size());
					
					for (UserInfo uInfo : UI.getFriendList()) {
						Button temp = new Button(uInfo.getName());
						temp.setMaxWidth(198);
						temp.setMinWidth(198);
						
						
						usernameToName.put(uInfo.getUsername(), uInfo.getName());
						
						temp.setAccessibleHelp(uInfo.getUsername());
						


						
												
						temp.setOnAction(new EventHandler<ActionEvent>() {

							public void handle(ActionEvent event) {
								//temp.setText(uInfo.getName());
								ChatBox friendChatBox = new ChatBox();
								friendChatBox.getUsernames().add(temp.getAccessibleHelp());
								System.out.println("BUTTON USERNAME: " + temp.getAccessibleHelp());
								friendChatBox.getUsernames().add(UI.getUsername());
								
								System.out.println("asdfasdf");
								
								chatBoxes.add(friendChatBox);
								grid.getChildren().add(friendChatBox.getInput());
								grid.getChildren().add(friendChatBox.getOutput());
							}

						});
						friendListButtons.add(temp);
						scrollGrid.add(temp, 0, yPos);
						yPos++;
					}
				}

				else {

					Alert a = new Alert(AlertType.ERROR);
					a.setHeaderText("Invalid Input");
					a.setResizable(true);
					a.setContentText("Wrong username or password. Please try again.");
					a.showAndWait();
				}

			}

		});
		
		
		

		//ChatBox cb = new ChatBox();
		//output = cb.getOutput();
		//input = cb.getInput();

		//grid.getChildren().add(cb.getInput());
		//grid.getChildren().add(cb.getOutput());

		// Send message via ENTER handler
		/*cb.getInput().setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER && !input.getText().equals("")) {
				try {
					String f = input.getText();
					toServer.writeObject(this.name + ": " + input.getText());
					toServer.flush();
					input.setText("");
					input.requestFocus();
				} catch (Exception e2) {

				}
			}

		});*/

		// Friend Scroll bar
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setMaxHeight(worldHeight);
		scrollPane.setMinHeight(worldHeight);
		scrollPane.setMinWidth(200);
		scrollPane.setMaxWidth(200);
		scrollPane.setContent(scrollGrid);
		scrollPane.setLayoutX(worldWidth - scrollPane.getMaxWidth());
		grid.getChildren().add(scrollPane);

		Button addFriend = new Button("Add Friends");
		addFriend.setMinWidth(198);
		addFriend.setMaxWidth(198);

		scrollGrid.add(addFriend, 0, 0);
		
		// Add Friend Window
		Stage addFriendStage = new Stage();
		Pane addFriendPane = new Pane();
		addFriendBtn.setLayoutX(250);
		addFriendBtn.setLayoutY(100);
		addFriendPane.getChildren().add(addFriendTF);
		addFriendPane.getChildren().add(addFriendBtn);
		addFriendTF.setLayoutX(50);
		addFriendTF.setLayoutY(100);
		Scene addFriendScene = new Scene(addFriendPane, 350, 200);
		addFriendStage.setScene(addFriendScene);

		// Add Friend Handler
		addFriend.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				UserInfo ui = new UserInfo();
				ui.setUsername(UI.getUsername());
				ui.setGetUserFlag(true);

				try {
					toServer.writeObject(ui);
					toServer.flush();
				} catch (Exception e) {
				}

				System.out.println("WAITING NOW");
				
				// Wait until validation received from server
				while (!wait) {
				}
				wait = false;


				addFriendStage.show();
			}

		});

		// Add Friend Button Handler
		addFriendBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent arg0) {
				UserInfo temp = new UserInfo();
				temp.setGetUserFlag(true);

				try {
					toServer.writeObject(temp);
					toServer.flush();
				} catch (IOException e) {
				}

				while (!wait) {
				}
				wait = false;

				System.out.println(allUsers.size());

				System.out.println("GOT HERE");
				System.out.println("GOT 1");
				System.out.println(addFriendTF.getText());

				boolean flagAddYourself = false;
				boolean flagFound = false;
				for (int i = 0; i < allUsers.size(); i++) {

					if (allUsers.get(i).getUsername().equals(addFriendTF.getText())) {
						if (allUsers.get(i).getUsername().equals(ChatClient.name)) {
							Alert a = new Alert(AlertType.ERROR);
							a.setHeaderText("Error");
							a.setResizable(true);
							a.setContentText("Can't add yourself!");
							a.showAndWait();
							flagAddYourself = true;
							break;
						} else {							
							System.out.println("USER FOUND");
							Button b = new Button(addFriendTF.getText());
							b.setMaxWidth(198);
							b.setMinWidth(198);
							b.setAccessibleHelp(allUsers.get(i).getUsername());
							usernameToName.put(allUsers.get(i).getUsername(), addFriendTF.getText());
							
							b.setOnAction(new EventHandler<ActionEvent> (){

								@Override
								public void handle(ActionEvent event) {
									System.out.println("Button Pressed");
									
									
									ChatBox cb = new ChatBox();
									// Asscoiates usernames of both this client and the
									// Client associated with the button to the chatbox
									cb.getUsernames().add(b.getAccessibleHelp());
									cb.getUsernames().add(UI.getUsername());
									cb.getNameLabel().setText(usernameToName.get(b.getAccessibleHelp())+ ":");
									System.out.println("CHAT BOX ADDED");
									grid.getChildren().add(cb.getOutput());
									grid.getChildren().add(cb.getInput());
									grid.getChildren().add(cb.getNameLabel());
									chatBoxes.add(cb);
									
									
									
									
									
								}
								
								
							});
							
							System.out.println("BUTTON NAME SHOULD BE: " + addFriendTF.getText());
							System.out.println("BUTTON USERNAME SHOULD BE: "  );

							UI.getFriendList().add(0,allUsers.get(i));
							UI.setUpdateFlag(true);
							
							//allUsers.get(i).setUpdateFlag(true);
							
							flagFound = true;
							try {
								System.out.println("UPDATE REQUEST SENT" + UI.isUpdateFlag());
								toServer.writeObject(new UserInfo(UI));		// Update user's friends list
								toServer.flush();
							} catch (IOException e) {					
							}
							
							usernameToName.put(allUsers.get(i).getUsername(), allUsers.get(i).getName());
							friendListButtons.add(b);
							scrollGrid.add(b, 0, yPos++);
							friendListButtons.add(b);							
							
						}
					}
				}
				
				if(!flagAddYourself && !flagFound) {
					Alert a = new Alert(AlertType.ERROR);
					a.setHeaderText("Error");
					a.setResizable(true);
					a.setContentText("No such user exists");
					a.showAndWait();
				}
			}
		});

		// Logout Handler
		logoutBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
			}
		});

	}

	private void setUpNetwork() throws Exception {
		Socket sock = new Socket("127.0.0.1", 4242);
		// Socket sock = new Socket("192.168.0.12", 4242);

		fromServer = new ObjectInputStream(sock.getInputStream());
		toServer = new ObjectOutputStream(sock.getOutputStream());

		System.out.println("Networking Established");

		Thread t = new Thread(new IncomingReader());
		t.start();

	}

	class IncomingReader implements Runnable {
		public void run() {
			try {
				Object object;

				while (true) {
					object = fromServer.readObject();
					if (object != null) {
						
						
						// PACKET
						if(object instanceof Packet){
							
							
							if(((Packet)object).getClientGroup().contains(UI.getUsername())){
								System.out.println("MESSAGE RECEIVED");
								System.out.println("CHAT BOX SIZE: " + chatBoxes.size());
								System.out.println("CLIENT GROUP SIZE: " + ((Packet)object).getClientGroup().size() );

								for(int i=0; i<chatBoxes.size(); i++){
									
									if(chatBoxes.get(i).getUsernames().equals(((Packet)object).getClientGroup())){
										chatBoxes.get(i).getOutput().appendText(((Packet)object).getMessage());
										break;

									}

								}

							}

						}
						

						// STRING
						if (object instanceof String) {
							output.appendText((String) object + "\n");
						}

						// USERINFO
						if (object instanceof UserInfo) {
							
							System.out.println(((UserInfo)object).getUserFlag() + "***");
							

							// Username / Password Found
							if (((UserInfo) object).getLoginFound()) {

								UI = (UserInfo) object;
								// accountFound = true;
								wait = true;
								System.out.println("WAIT OVER");

							}

							// Get list of all users
							else if (((UserInfo) object).getUserFlag()) {

								allUsers = ((UserInfo) object).getSendUsers();
								System.out.println("CLIENT GET USER LIST");
								System.out.println(allUsers.size());
								System.out.println(allUsers.get(0).getName());
								wait = true;

							}

							// Username / Password Failed
							else {
								System.out.println("Password Failed");
								
								UI = (UserInfo) object;
								// accountFound = false;
								wait = true;

							}
						}

					}

				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

}
