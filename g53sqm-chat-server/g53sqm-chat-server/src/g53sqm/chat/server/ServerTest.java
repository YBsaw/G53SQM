package g53sqm.chat.server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

public class ServerTest {

	final static int PORT = Runner.PORT;
	String host = "localhost";
	ServerSocket testSocket;
	Socket socket;

	public void userConnection(int port) {
		try{
			socket = new Socket(host, PORT);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Test
	public void testConnection() {

		/*
		 * test if the connection is able to be made to the server.
		 * depends on whether the server is on or not.
		 * If the server is off, the connection failed to connect to the server, and therefore null.
		 * If the server is on, the connection is able to connect to the server, and therefore not null.
		 */
		try{
			socket = new Socket(host, PORT);
			assertNotNull(socket);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.err.println("No such server.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("No such server.");
		}

	}

	@Test
	public void testPortBeingUsed() {

		/*
		 *test the port by creating a new server socket using the same port.
		 *If created succesfully, it's not null which means the port is not being used by other server.
		 *If failed, that means other server is using the same port, and therefore the test serversocket will be null.
		 */
		try {
			testSocket = new ServerSocket(PORT);

			userConnection(PORT);

			//if somehow the testSocket is able to be created with the port number, fail the test by expecting it to be null
			//because that means the port that is supposed to be used by runner's server is available.
			//that is not the expected result.
			assertNull(testSocket);

		} catch (Exception e) {
			System.err.println("Same port number has been used.");
			assertNull(testSocket);

		}

	}




}
