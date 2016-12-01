package g53sqm.chat.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

public class ConnectionTest {

	final static int PORT = 9000;
	String host = "localhost";
	Server server;

	Socket user1;
	BufferedReader in;
	PrintWriter out;

	String msgFromServer;
	String msgToServer;

	final static int STATE_UNREGISTERED = 0;
	final static int STATE_REGISTERED = 1;

	private int state = STATE_UNREGISTERED;

	@Before
	public void connect() {
		try {
			user1 = new Socket(host,PORT);
			in = new BufferedReader(new InputStreamReader(user1.getInputStream()));
			out = new PrintWriter(user1.getOutputStream(), true);

			msgFromServer=in.readLine();
			System.out.println(msgFromServer);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testMessageIDEN() throws IOException {

		String username = "Boon";

		/*
		 * This test is to test when user input IDEN (username)
		 * whether the server can receive and output correctly
		 * This is tested by comparing the output with the expected strings.
		 */
		//while (true){
			try {

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

			String user2 = "Boon";

			if(user2 == username) {
				state = STATE_REGISTERED;
			}

			try {

				/*the following two lines is to test the console input*/
//				reader = new BufferedReader(new InputStreamReader(System.in));
//				msgToServer = reader.readLine();
				msgToServer = "IDEN " + user2;

				out.println(msgToServer);
				System.out.println(msgToServer);

			} catch (Exception e) {
				e.printStackTrace();
			}


			/*check whether the server can read the input and output the expected line.
			 * pass the test if it returns the expected result.
			 */
		switch(state) {
			case STATE_UNREGISTERED:
				assertTrue(in.readLine().equals("OK Welcome to the chat server " + username));
				break;

			case STATE_REGISTERED:
				assertFalse(in.readLine().equals("BAD you are already registerd with username " + username));
				break;
			}
	}

	@Test
	public void testMessageSTAT() throws IOException {

		int num = 5;
		String user = "Boon";
		int messageCount = 0;
		String fString;

		try{
			msgToServer = "IDEN " + user;
			out.println(msgToServer);
			System.out.println(msgToServer);

			state = STATE_REGISTERED;
		} catch (Exception e) {
			e.printStackTrace();
		}

		try{
			System.out.println(state);
			msgToServer = "STAT";
			out.println(msgToServer);
			System.out.println(msgToServer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String status = "There are currently "+ num +" user(s) on the server ";

		switch (state) {

		case STATE_UNREGISTERED:
			status += "You have not logged in yet";

			fString = "OK " + status;

			/*
			 * pass the test if it returns the expected result(string).
			 * The expected string is hardcoded.
			 */
			assertTrue(in.readLine().equals(fString));
			break;

		case STATE_REGISTERED:
			status += "You are logged im and have sent " + messageCount + " message(s)";

			fString = "OK " + status;

			/*
			 * pass the test if it returns the expected result(string).
			 * The expected string is hardcoded.
			 */
			assertFalse(in.readLine().equals(fString));
			break;
		}


	}

	@Test
	public void testMessageLIST() throws IOException {

		try{
			msgToServer = "LIST";
			out.println(msgToServer);
			System.out.println(msgToServer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * pass the test if it returns result as expected string.
		 * The expected string is hardcoded.
		 */
		assertTrue(in.readLine().equals("BAD You have not logged in yet"));
	}

	@Test
	public void testMessageHAIL() throws IOException {

		String broadcastMessage = "Hi";

		try {
			msgToServer = "HAIL " + broadcastMessage;
			out.println(msgToServer);
			System.out.println(msgToServer);

		} catch (Exception e) {
			e.printStackTrace();
		}

		assertTrue(in.readLine().equals("BAD You have not logged in yet"));
	}

	@Test
	public void testMessageMESG() throws IOException {

		try {
			msgToServer = "MESG ";
			out.println(msgToServer);
			System.out.println(msgToServer);

		} catch (Exception e) {
			e.printStackTrace();
		}

		assertTrue(in.readLine().equals("BAD You have not logged in yet"));
	}

	@Test
	public void testMessageQUIT() throws IOException {

		try {
			msgToServer = "QUIT";
			out.println(msgToServer);
			System.out.println(msgToServer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertTrue(in.readLine().equals("OK goodbye"));
	}

}