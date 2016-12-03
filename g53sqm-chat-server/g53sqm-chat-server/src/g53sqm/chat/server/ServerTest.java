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

		userConnection(PORT);

		assertNotNull(socket);

	}

	@Test
	public void testPortBeingUsed() {

		Socket acceptSocket = null;

		try {
			testSocket = new ServerSocket(PORT);

			//acceptSocket = testSocket.accept();
			userConnection(PORT);

		} catch (Exception e) {
			System.err.println("Same port number has been used.");

		}

		assertNull(acceptSocket);

	}




}
