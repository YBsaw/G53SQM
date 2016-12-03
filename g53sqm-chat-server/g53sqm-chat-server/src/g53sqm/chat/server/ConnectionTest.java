package g53sqm.chat.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

public class ConnectionTest {

	final static int PORT = Runner.PORT;
	String host = "localhost";
	Server server;

	Socket socket;
	BufferedReader in;
	PrintWriter out;

	String msgFromServer;
	String msgToServer;

	final static int STATE_UNREGISTERED = 0;
	final static int STATE_REGISTERED = 1;

	private int state = STATE_UNREGISTERED;

	/*For each tests, create a new user to connect to the server*/

	@Before
	public void connect() {
		try {
			socket = new Socket(host,PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);

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

		String username = "user";

		/*
		 * This test is to test when user input IDEN (username)
		 * whether the server can receive and output correctly
		 * This is tested by comparing the output with the expected strings.
		 */
			try {
				msgToServer = "IDEN " + username;
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
				assertTrue(in.readLine().equals("BAD you are already registerd with username " + username));
				break;
			}
	}

	@Test
	public void testMessageSTAT() throws IOException {

		int num = 5;
		String username = "user1";
		int messageCount = 0;
		String fString;

		/*
		 * to test both state = STATE_REGISTERED and STATE_UNREGISTERED
		 * if testing STATE_REGISTERED, use idenUser(user) to register a user;
		 * if testing STATE_UNREGISTERED, comment out idenUser(user);
		 */
		idenUser(username);

		try{
			msgToServer = "STAT";
			out.println(msgToServer);
			System.out.println(msgToServer);

			msgFromServer =in.readLine();

		} catch (Exception e) {
			e.printStackTrace();
		}

		String status = "There are currently "+ num +" user(s) on the server ";

		switch (state) {

		case STATE_UNREGISTERED:
			status += "You have not logged in yet";
			break;

		case STATE_REGISTERED:
			status += "You are logged im and have sent " + messageCount + " message(s)";
			break;
		}

		fString = "OK " + status;

		assertTrue(msgFromServer.equals(fString));


	}

	@Test
	public void testMessageLIST() throws IOException {

		String username = "user2";

		/*
		 * to test both state = STATE_REGISTERED and STATE_UNREGISTERED
		 * if testing STATE_REGISTERED, use idenUser(user) to register a user;
		 * if testing STATE_UNREGISTERED, comment out idenUser(user);
		 */
		idenUser(username);

		try{
			msgToServer = "LIST";
			out.println(msgToServer);
			System.out.println(msgToServer);

			msgFromServer = in.readLine();
			System.out.println(msgFromServer);
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * pass the test if it returns result as expected string.
		 * The expected string is hardcoded.
		 */

		switch(state) {
		case STATE_REGISTERED:
			assertTrue(msgFromServer.equals("OK user3, user, user2, "));
			break;

		case STATE_UNREGISTERED:
			assertTrue(msgFromServer.equals("BAD You have not logged in yet"));
			break;
		}
	}

	@Test
	public void testMessageHAIL() throws IOException {

		String broadcastMessage = "Hi";
		String user = "user3";

		/*
		 * to test both state = STATE_REGISTERED and STATE_UNREGISTERED
		 * if testing STATE_REGISTERED, use idenUser(user) to register a user;
		 * if testing STATE_UNREGISTERED, comment out idenUser(user);
		 */
		idenUser(user);


		try {
			msgToServer = "HAIL " + broadcastMessage;
			out.println(msgToServer);
			System.out.println(msgToServer);
			msgFromServer = in.readLine();
			System.out.println(msgFromServer);

		} catch (Exception e) {
			e.printStackTrace();
		}

		switch(state) {
		case STATE_REGISTERED:
			assertTrue(msgFromServer.equals("Broadcast from " + user + ": " + broadcastMessage));
			break;

		case STATE_UNREGISTERED:
			assertTrue(msgFromServer.equals("BAD You have not logged in yet"));
			break;
		}
	}

	@Test
	public void testMessageMESG() throws IOException {

		String username1 = "user4";
		String username2 = "user2";
		String message = "Hi";

		/*
		 * to test both state = STATE_REGISTERED and STATE_UNREGISTERED
		 * if testing STATE_REGISTERED, use idenUser(user) to register a user;
		 * if testing STATE_UNREGISTERED, comment out idenUser(user);
		 */
		idenUser(username1);

		try {
			msgToServer = "MESG " + username2 + " " + message;
			out.println(msgToServer);
			System.out.println(msgToServer);

			msgFromServer = in.readLine();
			System.out.println(msgFromServer);

		} catch (Exception e) {
			e.printStackTrace();
		}

		switch(state) {
		case STATE_REGISTERED:
			assertTrue(msgFromServer.equals("OK your message has been sent"));
			break;

		case STATE_UNREGISTERED:
			assertTrue(msgFromServer.equals("BAD You have not logged in yet"));

		}
	}

	@Test
	public void testMessageQUIT() throws IOException {

		String username = "user5";
		int messageCount = 0;

		/*
		 * to test both state = STATE_REGISTERED and STATE_UNREGISTERED
		 * if testing STATE_REGISTERED, use idenUser(user) to register a user;
		 * if testing STATE_UNREGISTERED, comment out idenUser(user);
		 */
		idenUser(username);

		try {
			msgToServer = "QUIT";
			out.println(msgToServer);
			System.out.println(msgToServer);

			msgFromServer = in.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}

		switch(state) {
		case STATE_REGISTERED:
			if(username != "user4" || username != "user3" || username != "user2" || username != "user")
				assertTrue(msgFromServer.equals("OK thank you for sending " + messageCount + " message(s) with the chat service, goodbye. "));
			break;

		case STATE_UNREGISTERED:
			assertTrue(msgFromServer.equals("OK goodbye"));
			break;
		}

	}

	/*to log in as a user with a name*/

	public void idenUser(String user){

		try{
			msgToServer = "IDEN " + user;
			out.println(msgToServer);
			msgFromServer =in.readLine();

			state = STATE_REGISTERED;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}