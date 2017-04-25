package assignment7;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;


public class chatBox {
	private TextField input = new TextField();
	private TextArea output = new TextArea();
	private static int count=0;
	
	private int chatWidth = 300;
	
	
	public TextField getInput(){
		return input;
	}
	
	public TextArea getOutput(){
		return output;
	}
	
	public chatBox(int y){
		
		// Input Text
		input.setMaxHeight(30);
		input.setMinHeight(30);
		input.setMaxWidth(chatWidth);
		input.setMinWidth(chatWidth);
		input.setLayoutY(new ChatClient().getWorldHeight()-input.getMaxHeight()-5);
		input.setLayoutX(count*chatWidth + 20);
		
		// Output Text
		output.setMaxHeight(200);
		output.setMinHeight(200);
		output.setMinWidth(chatWidth);
		output.setMaxWidth(chatWidth);
		output.setLayoutY(new ChatClient().getWorldHeight()-output.getMaxHeight()-input.getMaxHeight()-5);
		output.setEditable(false);
		output.setLayoutX(count*chatWidth + 20);
		
		
		count++;
		
		
	}
	

	
	
	

}
