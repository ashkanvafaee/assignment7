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

import java.util.*;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

public class ChatBox {
	private TextField input = new TextField();
	private TextArea output = new TextArea();
	private static int count = 0;
	private String name;
	private HashSet<String> usernames = new HashSet<>();
	private Label nameLabel = new Label();

	public HashSet<String> getUsernames() {
		return usernames;
	}

	private ClientObserver ID;

	private int chatWidth = 300;

	public TextField getInput() {
		return input;
	}

	public TextArea getOutput() {
		return output;
	}

	public Label getNameLabel() {
		return nameLabel;
	}
	
	public static int getCount(){
		return count;
	}

	public ChatBox(/* ClientObserver ID */) {

		this.ID = ID;

		// Input Text
		input.setMaxHeight(30);
		input.setMinHeight(30);
		input.setMaxWidth(chatWidth);
		input.setMinWidth(chatWidth);
		input.setLayoutY(new ChatClient().getWorldHeight() - input.getMaxHeight() - 5);
		input.setLayoutX(count * chatWidth + 25);
		input.setStyle("-fx-border-color: red;");


		// Send Message Handler
		input.setOnKeyPressed(e -> {

			if (e.getCode() == KeyCode.ENTER && !input.getText().equals("")) {
				try {
					Packet p = new Packet();
					p.setClientGroup(new HashSet<String>(usernames));
					p.setMessage(ChatClient.getName() + ": " + input.getText() + "\n");
					input.setText("");
					input.requestFocus();

					ChatClient.getToServer().writeObject(p);
					ChatClient.getToServer().flush();
					input.setText("");
					input.requestFocus();
					Sound.playSendSound();
				} catch (Exception e2) {

				}
			}

		});

		// Output Text
		output.setMaxHeight(200);
		output.setMinHeight(200);
		output.setMinWidth(chatWidth);
		output.setMaxWidth(chatWidth);
		output.setLayoutY(new ChatClient().getWorldHeight() - output.getMaxHeight() - input.getMaxHeight() - 5);
		output.setEditable(false);
		output.setLayoutX(count * chatWidth + 25);
		output.setWrapText(true);
		output.setStyle("-fx-border-color: red; -fx-control-inner-background: Aqua;");

		// name label
		nameLabel.setStyle("-fx-background-color: Black; -fx-border-color: red; -fx-text-fill: Yellow; ; -fx-font: 16 Impact;-fx-alignment: center ;");
		nameLabel.setMaxWidth(chatWidth);
		nameLabel.setMinWidth(chatWidth);
		nameLabel.setLayoutY(new ChatClient().getWorldHeight() - input.getMaxHeight() - output.getMaxHeight() - 20);
		nameLabel.setLayoutX(count * chatWidth + 25);

		count++;

	}

}
