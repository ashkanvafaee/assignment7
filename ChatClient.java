package assignment7;

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


	private ObjectInputStream fromServer;
	private ObjectOutputStream toServer;

	private int worldWidth = 1000;
	private int worldHeight = 800;
	
	public int getWorldWidth(){
		return worldWidth;
	}
	
	public int getWorldHeight(){
		return worldHeight;
	}
	
	public ObjectInputStream fromServer(){
		return fromServer;
	}
	
	public ObjectOutputStream toServer(){
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
		
		//grid.setPadding(new Insets(5, 5, 5, 5));
		
		Scene scene = new Scene(grid, worldWidth, worldHeight);
		grid.setStyle("-fx-background-color: white;");
		primaryStage.setScene(scene);
		primaryStage.show();
		

		
		
		chatBox cb = new chatBox(0);
		output = cb.getOutput();
		input = cb.getInput();
		
		grid.getChildren().add(cb.getInput());
		grid.getChildren().add(cb.getOutput());

		
		// Send message via ENTER handler
		cb.getInput().setOnKeyPressed(e-> {
			if(e.getCode() == KeyCode.ENTER && !input.getText().equals("")){
				try{
					String f = input.getText();
				toServer.writeObject(input.getText());
				toServer.flush();
				input.setText("");
				input.requestFocus();
				}
				catch(Exception e2){
					
				}
			}
			
		});
		

		// Friend Scroll bar
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setMaxHeight(worldHeight);
		scrollPane.setMinHeight(worldHeight);
		scrollPane.setMinWidth(200);
		scrollPane.setMaxWidth(200);
		GridPane scrollGrid = new GridPane();
		scrollPane.setContent(scrollGrid);
		scrollPane.setLayoutX(worldWidth - scrollPane.getMaxWidth());
		grid.getChildren().add(scrollPane);
		
		
		Button friend = new Button("Friend");
		friend.setMinWidth(200);
		friend.setMaxWidth(200);
		
		scrollGrid.add(friend, 0, 0);
		
		
		
	}
	
	
	private void setUpNetwork() throws Exception{
		Socket sock = new Socket("127.0.0.1", 4242);
		//Socket sock = new Socket("192.168.0.12", 4242);
		
		fromServer = new ObjectInputStream(sock.getInputStream());
		toServer =  new ObjectOutputStream(sock.getOutputStream());


		
		System.out.println("Networking Established");
		
		Thread t = new Thread(new IncomingReader());
		t.start();
		
	}
	
	class IncomingReader implements Runnable {
		public void run() {
			String message = "";
			try {
				Object object;

				while (true) {
					object = fromServer.readObject();
					if (object != null) {
						if (object instanceof String) {
							output.appendText((String) object + "\n");
						}

					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	

}
