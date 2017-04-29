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

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Packet implements Serializable {
	
	private String message;
	
	private HashSet<String> clientGroup = new HashSet<>();
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HashSet<String> getClientGroup() {
		return clientGroup;
	}

	public void setClientGroup(HashSet<String> clientGroup) {
		this.clientGroup = clientGroup;
	}


	
	
	
	
}
