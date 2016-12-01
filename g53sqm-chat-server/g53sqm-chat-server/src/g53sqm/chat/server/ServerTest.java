package g53sqm.chat.server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

public class ServerTest {

	final static int PORT = Runner.PORT;



	@Test
	public void testConnection() {

		ServerSocket testSocket = null;

		try {
			testSocket = new ServerSocket(PORT);

			assertTrue(testSocket.accept().equals(Runner.server));

		} catch (Exception e)
		{
			e.printStackTrace();
		}



	}

}
