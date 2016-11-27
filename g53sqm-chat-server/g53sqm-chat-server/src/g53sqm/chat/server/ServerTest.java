package g53sqm.chat.server;

import static org.junit.Assert.*;

import java.net.Socket;

import org.junit.Test;

public class ServerTest {

	final static int PORT = 9000;

	private Socket socket;
	private Server server;

	Connection test = new Connection(socket, server);

	@Test
	public void test() {



	}

}
