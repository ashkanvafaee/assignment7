package assignment7;

import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Packet {

	private String name;
	private String text;
	
	// OutputStream of all Clients that message should be sent to
	ArrayList<ObjectOutputStream> clientRecipients = new ArrayList<ObjectOutputStream>();
	
	
	
	
}
