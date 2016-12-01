package g53sqm.chat.server;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Event;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.*;

public class ChatClient extends Applet
{
	private Socket socket = null;
	private DataInputStream console;
	private DataOutputStream streamOut = null;
	private Connection client;
	private Server server;

	private String username = "Boon" ;
	ArrayList<String> userList = new ArrayList<String>();
	boolean isConnected = false;
	BufferedReader reader;

	private TextArea  display = new TextArea();
	private TextField input   = new TextField();
	private TextArea name = new TextArea();

	private Button send = new Button("Send");
	private Button connect = new Button("Connect");
    private Button quit = new Button("Disconnect");

	private String serverName = "localhost";
	private int serverPort = 9000;

	public void init()
	{
		input.setEditable(false);

		Panel keys = new Panel();
		keys.setLayout(new GridLayout(1,2));
		keys.add(quit); keys.add(connect);

		Panel south = new Panel();
		south.setLayout(new BorderLayout());
		south.add("West", keys); south.add("Center", input);  south.add("East", send);

		Panel users = new Panel();
		users.setLayout(new BorderLayout());
		users.add("Center", name);

		Label title = new Label("Simple Chat Client Applet", Label.CENTER);
		title.setFont(new Font("Helvetica", Font.BOLD, 14));

		name.setText("User List\n");

		setLayout(new BorderLayout());

		add("North", title);
		add("Center", display);
		add("South",  south);
		add("East", users);

		quit.disable();
		send.disable();

		getHost();
		getPort();


	}

	public boolean action(Event e, Object o) {

		if(e.target == quit) {
			if(isConnected = true) {
			input.setText(".bye");
			send();
			quit.disable();
			send.disable();
			connect.enable();
			userRemove(username);
			name.setText("User List\n" +userList.toString());
			isConnected = false;

			}

		}

		else if(e.target == connect) {
			connect(serverName, serverPort);
		}

		else if (e.target == send) {
			send();
			input.requestFocus();
		}

		return true;

	}

	public void connect(String serverName, int serverPort) {
		println("Establishing connection. Please wait...");

		if(isConnected == false) {

			try {


				socket = new Socket(serverName, serverPort);
				println("Connected: " + socket);
				println(username + " has connected.");
				open();
				send.enable();
				connect.disable();
				quit.enable();
				isConnected = true;

				userAdd(username);
				name.setText("User List\n" +userList.toString());
				name.setEditable(false);


			}
			catch (UnknownHostException e)
				{
				println("Host unknown: " + e.getMessage());
				}

			catch (IOException e)
			{
				println("Unexpected exception: " + e.getMessage());
				isConnected =false;

			}
		}
		else {
			display.append("You are already connected.\n");
		}

		if(isConnected == true){
			input.setEditable(true);
			display.setEditable(false);
		}
	}

	private void send() {
		try {
			streamOut.writeUTF(input.getText());
			streamOut.flush();
			println(username + ": " + input.getText());
			input.setText("");
		} catch (IOException e)
		{
			println("Sending error: " + e.getMessage());
			close();
		}


	}

	public void handle(String msg)
	{
		if (msg.equals(".bye"))
		{
			println("Goodbye. Press RETURN to exit...");
			close();
		}
		else println(msg);
	}

	public void open()
	{
		try
		{
			streamOut = new DataOutputStream(socket.getOutputStream());
			client = new Connection(socket, server);
		}
		catch(IOException e)
		{
			println("Error opening output stream: " + e);
		}

	}

	public void close()
	{
		try
		{
			if(streamOut != null)
				streamOut.close();

			if(socket != null)
				socket.close();

		} catch(IOException e)
		{
			println("Error closing...");
		}
	}

	public void userAdd(String data)
	{
		userList.add(data);
	}

	public void userRemove(String data)
	{
		userList.remove(data);
	}

	private void println(String msg)
	{
		display.append(msg+ "\n");
	}

	public void getHost()
	{
		serverName = getParameter("host");

	}

	public int getPort()
	{
		return serverPort;
	}



}