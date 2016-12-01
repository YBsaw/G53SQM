package g53sqm.chat.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.Test;

public class ConnectionTest {

	final static int PORT = Runner.PORT;
	String host = "localhost";
	Server server;

	@Test
	public void testMessageIDEN() throws IOException {

		Connection connection = null;

		Socket user1 = null;
		BufferedReader in = null;
		BufferedReader reader = null;
		PrintWriter out = null;

		String msgFromServer;
		String msgToServer;
		String username = "Boon";

		// Try one user connection
		try{
			user1 = new Socket(host, PORT);
			connection = new Connection(user1, server);
			in = new BufferedReader(new InputStreamReader(user1.getInputStream()));
			out = new PrintWriter(user1.getOutputStream(), true);
		}
		catch(Exception e){
			e.printStackTrace();
		}

		//while (true){
			try {
				msgFromServer=in.readLine();
				System.out.println(msgFromServer);

				/*the following two lines is to test the console input*/
//				reader = new BufferedReader(new InputStreamReader(System.in));
//				msgToServer = reader.readLine();
				msgToServer = "IDEN " + username;
				out.println(msgToServer);
				System.out.println(msgToServer);

			} catch (Exception e) {
				e.printStackTrace();
			}
		//}

			/*check whether the server can read the input and output the expected line.
			 * pass the test if it returns the expected result.
			 */
			assertTrue(in.readLine().equals("OK Welcome to the chat server " + username));
	}

}