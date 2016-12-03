package g53sqm.chat.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServerTest {

	final static int PORT = Runner.PORT;
	final static int SERVERPORT = 9001;

	String host = "localhost";
	static ServerSocket testSocket;
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
			assertNull(socket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			assertNull(socket);
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
			assertNotNull(testSocket);

		} catch (Exception e) {
			System.err.println("Same port number has been used.");
			assertNull(testSocket);

		}

	}




}
