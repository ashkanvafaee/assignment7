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
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class ChatClient extends Application {

	private TextArea output;
	private static int yPos = 4;

	private static ObjectInputStream fromServer;
	private static ObjectOutputStream toServer;

	private static String name;
	private static UserInfo UI;
	
	private static String IPaddress = "";

	// maps usernames to names
	private HashMap<String, String> usernameToName = new HashMap<>();

	private static ArrayList<ChatBox> chatBoxes = new ArrayList<>();

	private ArrayList<UserInfo> allUsers = new ArrayList<>();

	private int worldWidth = 1600;
	private int worldHeight = 850;

	private ArrayList<Button> friendListButtons = new ArrayList<>();

	public static String getName() {
		return name;
	}

	public static ObjectOutputStream getToServer() {
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

		primaryStage.setTitle("Chat Client");

		AnchorPane grid = new AnchorPane();

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
		PasswordField password = new PasswordField();

		// Add friend button
		Button addFriendBtn = new Button("Add Friend");
		TextField addFriendTF = new TextField();

		// Login Screen
		Pane login = new Pane();
		login.setStyle("-fx-background-color: white;");
		Image loginImage = new Image("file:LoginImage.png");
		ImageView loginImgView = new ImageView(loginImage);
		login.getChildren().add(loginImgView);
		loginImgView.setX(50);
		Scene loginScene = new Scene(login, 350, 400);
		Stage loginStage = new Stage();
		loginStage.setScene(loginScene);

		// Pane to connect to server
		Stage serverStage = new Stage();
		Pane serverPane = new Pane();
		Scene serverScene = new Scene(serverPane, 300, 200);
		Button serverButton = new Button("Connect");
		serverButton.setLayoutX(105);
		serverButton.setLayoutY(110);
		TextField serverTF = new TextField();
		serverTF.setLayoutX(50);
		serverTF.setLayoutY(70);
		Text serverConnectPrompt = new Text("Connect to host server:");
		serverConnectPrompt.setLayoutX(65);
		serverConnectPrompt.setLayoutY(50);
		serverPane.getChildren().add(serverButton);
		serverPane.getChildren().add(serverTF);
		serverPane.getChildren().add(serverConnectPrompt);
		serverStage.setScene(serverScene);
		serverStage.setTitle("Connect");


		if(ChatClient.IPaddress.equals("")){
			serverStage.show();
		}
		else{
			//setUpNetwork(ChatClient.IPaddress);
			loginStage.show();
		}

		
		
		// Set up network handler
		serverButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				try {
					if (serverTF.getText().length()!= 0 && setUpNetwork(serverTF.getText())) {
						loginStage.show();
						ChatClient.IPaddress = serverTF.getText();
						serverStage.close();
					}
				} catch (Exception e) {
				}
			}
		});

		Button newUser = new Button("New User");
		Button oldUser = new Button("Sign in");
		newUser.setMaxWidth(100);
		newUser.setMinWidth(100);
		newUser.setLayoutX(40);
		newUser.setLayoutY(300);

		oldUser.setMaxWidth(100);
		oldUser.setMinWidth(100);
		oldUser.setLayoutX(195);
		oldUser.setLayoutY(300);

		login.getChildren().add(oldUser);
		login.getChildren().add(newUser);

		// Friend Scroll bar
		ScrollPane scrollPane = new ScrollPane();
		
		scrollPane.setMaxHeight(worldHeight);
		scrollPane.setMinHeight(worldHeight);
		
		scrollPane.setStyle("-fx-background: linear-gradient(from 25% 25% to 100% 100%, LightSkyBlue, blue);");		
		scrollPane.setMinWidth(200);
		scrollPane.setMaxWidth(200);
		scrollPane.setContent(scrollGrid);
		scrollPane.setLayoutX(worldWidth - scrollPane.getMaxWidth());
		grid.getChildren().add(scrollPane);
		grid.setRightAnchor(scrollPane, 0.0);

		Button addFriend = new Button("Add Friends");
		
		addFriend.setStyle("-fx-background-color: LimeGreen; -fx-border-color: black ; -fx-font: 16 Impact; ");
		addFriend.setOnMouseEntered(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				addFriend.setStyle("-fx-background-color: Orange; -fx-border-color: black ; -fx-font: 16 Impact;");

				
				
			}
			
		});
		
		addFriend.setOnMouseExited(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				addFriend.setStyle("-fx-background-color: LimeGreen; -fx-border-color: black ; -fx-font: 16 Impact;");

				
			}
			
			
		});
		
		
		
		
		
		
		addFriend.setMinWidth(198);
		addFriend.setMaxWidth(198);

		// Logout buttons
		Button logoutBtn = new Button("Logout");
		//logoutBtn.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, yellow, red);");

		
		logoutBtn.setStyle("-fx-background-color: Salmon; -fx-border-color: black; -fx-font: 16 Impact;");
		logoutBtn.setOnMouseEntered(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				logoutBtn.setStyle("-fx-background-color: Orange; -fx-border-color: black ; -fx-font: 16 Impact;");

				
				
			}
			
		});
		
		logoutBtn.setOnMouseExited(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				logoutBtn.setStyle("-fx-background-color: Salmon; -fx-border-color: black ; -fx-font: 16 Impact;");

				
			}
			
			
		});
		

		
		logoutBtn.setMinWidth(198);
		logoutBtn.setMaxWidth(198);
		Button logoutYes = new Button("Yes");
		Button logoutNo = new Button("No");
		

		// Group chat buttons
		Button groupChatBtn = new Button("Add users to group chat");
		
		
		
		groupChatBtn.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, yellow, pink); -fx-border-color: black ; -fx-font: 16 Impact;");
		groupChatBtn.setOnMouseEntered(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				groupChatBtn.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, pink, yellow); -fx-border-color: black ; -fx-font: 16 Impact;");

				
				
			}
			
		});
		
		groupChatBtn.setOnMouseExited(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				groupChatBtn.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, yellow, pink); -fx-border-color: black ; -fx-font: 16 Impact;");

				
			}
			
			
		});
		
		
		
		
		
		
		
		
		groupChatBtn.setMinWidth(198);
		groupChatBtn.setMaxWidth(198);
		Button addToGroupBtn = new Button("Add user");
		TextField groupChatTF = new TextField();

		// Logout Window
		Stage logoutStage = new Stage();
		Pane logoutPane = new Pane();
		logoutPane.getChildren().add(logoutYes);
		logoutPane.getChildren().add(logoutNo);
		logoutYes.setLayoutX(50);
		logoutYes.setLayoutY(100);
		logoutNo.setLayoutX(150);
		logoutNo.setLayoutY(100);
		Scene logoutScene = new Scene(logoutPane, 350, 200);
		logoutStage.setScene(logoutScene);

		//Text friendList = new Text("	      Friend's List");

		scrollGrid.add(logoutBtn, 0, 0);
		scrollGrid.add(addFriend, 0, 1);
		scrollGrid.add(groupChatBtn, 0, 2);
		//scrollGrid.add(friendList, 0, 3);

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

		// Group chat window
		Stage groupChatStage = new Stage();
		Pane groupChatPane = new Pane();
		groupChatTF.setLayoutX(50);
		groupChatTF.setLayoutY(100);
		groupChatPane.getChildren().add(groupChatTF);
		addToGroupBtn.setLayoutX(250);
		addToGroupBtn.setLayoutY(100);
		groupChatPane.getChildren().add(addToGroupBtn);
		Scene groupChatScene = new Scene(groupChatPane, 350, 200);
		groupChatStage.setScene(groupChatScene);

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
					Thread.sleep(500);

				} catch (Exception e) {

				}
				
				boolean alreadyAdded = false;
				for(UserInfo i: allUsers){
					if(i.getUsername().equals(userName.getText())){
						alreadyAdded = true;
					}	
				}

				if(!alreadyAdded){
					ChatClient.name = name.getText();
					ChatClient.UI = ui;

					loginStage.close();
					primaryStage.show();
					primaryStage.setTitle(ui.getName());
					Sound.playWelcomeSound();
				}
				else{
					Alert a = new Alert(AlertType.ERROR);
					a.setHeaderText("Invalid Username");
					a.setResizable(true);
					a.setContentText("Username already taken. Please try again.");
					a.showAndWait();
					
					
				}

			}

		});

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
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
				if (UI.getLoginFound()) {
					loginStage.close();
					primaryStage.show();
					primaryStage.setTitle(UI.getName());
					ChatClient.name = UI.getName();
					Sound.playWelcomeSound();

					for (UserInfo uInfo : UI.getFriendList()) {
						Button temp = new Button(uInfo.getName());
						temp.setMaxWidth(198);
						temp.setMinWidth(198);
						
						temp.setStyle("-fx-background-color: Black; -fx-border-color: Red; -fx-text-fill: Yellow; ; -fx-font: 16 Impact;");

						temp.setOnMouseEntered(new EventHandler<MouseEvent>(){

							@Override
							public void handle(MouseEvent arg0) {
								temp.setStyle("-fx-background-color: Orange; -fx-border-color: Red ; -fx-font: 16 Impact;");

							}
							
						});
						
						temp.setOnMouseExited(new EventHandler<MouseEvent>(){

							@Override
							public void handle(MouseEvent arg0) {
								temp.setStyle("-fx-background-color: Black; -fx-border-color: Red; -fx-text-fill: Yellow; ; -fx-font: 16 Impact;");

							}
							
						});
						
						
						usernameToName.put(uInfo.getUsername(), uInfo.getName());

						temp.setAccessibleHelp(uInfo.getUsername());

						temp.setOnAction(new EventHandler<ActionEvent>() {

							public void handle(ActionEvent event) {
								boolean alreadyMade = false;
								
								HashSet<String> check = new HashSet<>();
								check.add(temp.getAccessibleHelp());
								check.add(UI.getUsername());
								
								for(ChatBox i: chatBoxes){
									if(i.getUsernames().equals(check)){
										alreadyMade = true;
									}
								}
								
								
								
								
								
								if (ChatBox.getCount() <= 3 && !alreadyMade) {

									ChatBox friendChatBox = new ChatBox();
									friendChatBox.getNameLabel().setText(temp.getText());
									friendChatBox.getUsernames().add(temp.getAccessibleHelp());	//**
									friendChatBox.getUsernames().add(UI.getUsername());
									
									System.out.println(temp.getAccessibleHelp() + "***" + UI.getUsername());
									

									
									//temp.setAccessibleHelp(uInfo.getUsername());
									

									chatBoxes.add(friendChatBox);
									grid.getChildren().add(friendChatBox.getInput());
									grid.getChildren().add(friendChatBox.getOutput());
									grid.getChildren().add(friendChatBox.getNameLabel());
								}
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

				// Wait until validation received from server
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
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

				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
				}

				boolean flagAddYourself = false;
				boolean flagFound = false;
				boolean alreadyAdded = false;

				for (int i = 0; i < allUsers.size(); i++) {

					if (allUsers.get(i).getUsername().equals(addFriendTF.getText())) {
						
						
						for(Button b: friendListButtons){
							if(b.getAccessibleHelp().equals(allUsers.get(i).getUsername())){
								alreadyAdded=true;
							}
						}
						
						
						
						
						if (allUsers.get(i).getUsername().equals(UI.getUsername()) || alreadyAdded) {
							
							if(!alreadyAdded){
								Alert a = new Alert(AlertType.ERROR);
								a.setHeaderText("Error");
								a.setResizable(true);
								a.setContentText("Can't add yourself!");
								a.showAndWait();
								flagAddYourself = true;
								break;
							}
						
						} else {
							Button b = new Button(allUsers.get(i).getName());
							b.setMaxWidth(198);
							b.setMinWidth(198);
														
							b.setStyle("-fx-background-color: Black; -fx-border-color: red; -fx-text-fill: Yellow; ; -fx-font: 16 Impact;");

							b.setOnMouseEntered(new EventHandler<MouseEvent>(){

								@Override
								public void handle(MouseEvent arg0) {
									b.setStyle("-fx-background-color: Orange; -fx-border-color: red ; -fx-font: 16 Impact;");

								}
								
							});
							
							b.setOnMouseExited(new EventHandler<MouseEvent>(){

								@Override
								public void handle(MouseEvent arg0) {
									b.setStyle("-fx-background-color: Black; -fx-border-color: red; -fx-text-fill: Yellow; ; -fx-font: 16 Impact;");

								}
								
							});
							
							
							b.setAccessibleHelp(allUsers.get(i).getUsername());
							usernameToName.put(allUsers.get(i).getUsername(), allUsers.get(i).getName());

							b.setOnAction(new EventHandler<ActionEvent>() {

								@Override
								public void handle(ActionEvent event) {
									
									boolean alreadyMade = false;
									
									HashSet<String> check = new HashSet<>();
									check.add(b.getAccessibleHelp());
									check.add(UI.getUsername());
									
									for(ChatBox i: chatBoxes){
										if(i.getUsernames().equals(check)){
											alreadyMade = true;
										}
									}
									
									
									

									if (ChatBox.getCount() <= 3 && !alreadyMade) {
										ChatBox cb = new ChatBox();
										// Asscoiates usernames of both this
										// client
										// and the
										// Client associated with the button to
										// the
										// chatbox
										cb.getUsernames().add(b.getAccessibleHelp());
										cb.getUsernames().add(UI.getUsername());
										cb.getNameLabel().setText(usernameToName.get(b.getAccessibleHelp()));
										grid.getChildren().add(cb.getOutput());
										grid.getChildren().add(cb.getInput());
										grid.getChildren().add(cb.getNameLabel());
										chatBoxes.add(cb);
									}

								}

							});

							UI.getFriendList().add(0, allUsers.get(i));
							UI.setUpdateFlag(true);

							// allUsers.get(i).setUpdateFlag(true);

							flagFound = true;
							try {
								// Update user's friends list
								toServer.writeObject(new UserInfo(UI));
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

				if (!flagAddYourself && !flagFound && !alreadyAdded) {
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
				logoutStage.show();
			}
		});

		logoutYes.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				Sound.playGoodbyeSound();
				logoutStage.close();
				primaryStage.close();
				//loginStage.show();
				
				try {
					ChatClient.yPos = 4;
					ChatClient.name = null;
					ChatClient.UI = null;
					ChatClient.chatBoxes = new ArrayList<>();
					UserInfo.setUsers(new ArrayList<UserInfo>());
					ChatBox.setCount(0);
					//setUpNetwork(ChatClient.IPaddress);
								
					
					
					new ChatClient().start(new Stage());
				} catch (Exception e) {

				}
				
			}
		});

		logoutNo.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				logoutStage.close();
			}
		});

		// Groupchat Handler
		groupChatBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {

				groupChatStage.show();
			}
		});

		// Groupchat Add Users Handler
		addToGroupBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {

				String[] usernameArr = groupChatTF.getText().split(", |,");
				HashSet<String> listOfUsers = new HashSet<>();

				boolean invalidUserAdded = false;

				// Iterate through list of friends to check if valid
				for (int i = 0; i < usernameArr.length; i++) {
					if (usernameToName.containsKey(usernameArr[i])) {
						listOfUsers.add(usernameArr[i]);
					} else {
						invalidUserAdded = true;
					}
				}

				boolean createChatBoxFlag = true;
				listOfUsers.add(UI.getUsername());

				for (ChatBox cb : chatBoxes) {
					if (cb.getUsernames().equals(listOfUsers)) {
						createChatBoxFlag = false;
					}
				}

				// Creates the chatbox if there is more than 1 user and there
				// does not already exist a chatbox with the same set of users
				listOfUsers.remove(UI.getUsername());
				if (listOfUsers.size() > 0 && createChatBoxFlag && ChatBox.getCount()<=3) {
					ChatBox groupChat = new ChatBox();
					groupChat.getUsernames().add(UI.getUsername());
					for (String user : listOfUsers) {
						groupChat.getUsernames().add(user);
					}

					String nameOfGroupChat = UI.getName();
					for (String user : listOfUsers) {
						nameOfGroupChat += (", " + usernameToName.get(user));
					}

					groupChat.getNameLabel().setText(nameOfGroupChat);
					grid.getChildren().add(groupChat.getInput());
					grid.getChildren().add(groupChat.getOutput());
					grid.getChildren().add(groupChat.getNameLabel());
					chatBoxes.add(groupChat);
				} else if (!createChatBoxFlag) {
					Alert a = new Alert(AlertType.ERROR);
					a.setHeaderText("Invalid group");
					a.setResizable(true);
					a.setContentText("A group with these users already exists");
					a.showAndWait();
				}

				// If any user was not on friend's list output error
				if (invalidUserAdded || UI.getFriendList().size() == 0) {
					Alert a = new Alert(AlertType.ERROR);
					a.setHeaderText("Invalid User");
					a.setResizable(true);
					a.setContentText("One or more username(s) were incorrect");
					a.showAndWait();
				}
			}
		});
		
		
		
		
		
		
		/********************************WEB BROWSTER *************************/
		WebView myBrowser = new WebView();
		WebEngine myWebEngine = myBrowser.getEngine();
		myWebEngine.load("http://www.google.com");
		
		myBrowser.setLayoutX(0);
		myBrowser.setLayoutY(50);
		myBrowser.setMinWidth(1400);
		myBrowser.setMaxWidth(1400);		
		myBrowser.setMinHeight(550);
		myBrowser.setMaxHeight(550);
		
		grid.getChildren().add(myBrowser);
		
		TextField searchBar = new TextField();
		searchBar.setPromptText("Search...");
		searchBar.setMinWidth(1400);
		searchBar.setMaxWidth(1400);
		searchBar.setMaxHeight(30);
		searchBar.setMinHeight(30);
		searchBar.setLayoutX(0);
		searchBar.setLayoutY(0);
		
		searchBar.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER && !searchBar.getText().equals("")) {
				
				
				if (searchBar.getText().startsWith("www.")) {
					
					myWebEngine.load("https://" + searchBar.getText());
					

				} else if (!searchBar.getText().startsWith("https://www")) {
					myWebEngine.load("https://www." + searchBar.getText());
				}
				
				
			}
			
		});
	
		grid.getChildren().add(searchBar);
		
		
		
		
		
		
	}

	private boolean setUpNetwork(String serverIP) {
		Socket sock;
		try {
			sock = new Socket(serverIP, 4242);
			fromServer = new ObjectInputStream(sock.getInputStream());
			toServer = new ObjectOutputStream(sock.getOutputStream());
		} catch (Exception e) {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("Invalid Host");
			a.setResizable(true);
			a.setContentText("Could not find a host with that IP address");
			a.showAndWait();
			return false;
		}
		// Socket sock = new Socket("192.168.0.12", 4242);

		System.out.println("Networking Established");

		Thread t = new Thread(new IncomingReader());
		t.start();
		return true;
	}

	class IncomingReader implements Runnable {
		public void run() {
			try {
				Object object;

				while (true) {
					object = fromServer.readObject();
					if (object != null) {

						// PACKET
						if (object instanceof Packet) {

							if (((Packet) object).getClientGroup().contains(UI.getUsername())) {

								System.out.println(chatBoxes.size() + "***" );
								System.out.println(chatBoxes.get(0).getUsernames().size() + ">>>");
								
								
								for (int i = 0; i < chatBoxes.size(); i++) {

									if (chatBoxes.get(i).getUsernames().equals(((Packet) object).getClientGroup())) {
										chatBoxes.get(i).getOutput().appendText(((Packet) object).getMessage());
										
										if(!((Packet)object).getMessage().startsWith(ChatClient.name)){
											Sound.playReceiveSound();
										}
										
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

							// Username / Password Found
							if (((UserInfo) object).getLoginFound()) {
								UI = (UserInfo) object;
							}

							// Get list of all users
							else if (((UserInfo) object).getUserFlag()) {
								allUsers = ((UserInfo) object).getSendUsers();
							}

							// Username / Password Failed
							else {
								UI = (UserInfo) object;
							}
						}

					}

				}

			} catch (Exception ex) {
			}
		}
	}
}