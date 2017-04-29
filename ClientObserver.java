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

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

public class ClientObserver extends ObjectOutputStream implements Observer {
	
	
	
	public ClientObserver(OutputStream out) throws IOException {
		super(out);
	}

	@Override
	public void update(Observable o, Object arg) {
		try {
			this.writeObject(arg);// writer.println(arg);
			this.flush(); // writer.flush();
		
		} catch (Exception e) {

		}

	}

}