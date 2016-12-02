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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.*;

public class ChatClient extends Applet implements KeyListener
{
	private int serverPort = 9000;

	private Socket socket = null;

	private String username = "" ;
	ArrayList<String> userList = new ArrayList<String>();
	boolean isConnected = false;

	BufferedReader in;
	PrintWriter out = null;

	private TextArea  display = new TextArea();
	private TextField input   = new TextField();
	private TextArea name = new TextArea();

	private Button send = new Button("Send");
	private Button connect = new Button("Connect");
    private Button quit = new Button("Disconnect");

	private String serverName = "localhost";

	private String msgToServer;
	private String msgFromServer;

	public void init()
	{
		input.setEditable(false);

		Panel keys = new Panel();
		keys.setLayout(new GridLayout(1,2));
		keys.add(quit);
		keys.add(connect);

		Panel south = new Panel();
		south.setLayout(new BorderLayout());
		south.add("West", keys);
		south.add("Center", input);
		south.add("East", send);

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

		quit.setEnabled(false);
		send.setEnabled(false);

		getHost();
		getPort();

		input.addKeyListener(this);

	}

	public boolean action(Event e, Object o) {

		if(e.target == quit) {
			if(isConnected = true) {
			input.setText(".bye");
			send();
			quit.setEnabled(false);
			send.setEnabled(false);
			connect.setEnabled(true);
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

	@SuppressWarnings("deprecation")
	public void connect(String serverName, int serverPort) {
		println("Establishing connection. Please wait...");

		if(isConnected == false) {

			try {

				socket = new Socket(serverName, serverPort);

				println("Connected: " + socket);
				println(username + " has connected.");
				open();

				msgFromServer=in.readLine();
				System.out.println(msgFromServer);

				println(msgFromServer);

				send.enable();
				connect.disable();
				quit.enable();
				isConnected = true;

				//System.out.println(client.isRunning());

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

			println(username + ": " + input.getText());

			msgToServer = input.getText();

			if (msgToServer.length() >= 6){
				switch (msgToServer.substring(0, 6)){

				case "./LIST":
					msgToServer = "LIST";
					toConsole(msgToServer);

					input.setText("");
					break;

				case "./STAT":
					msgToServer = "STAT";
					toConsole(msgToServer);

					input.setText("");
					break;

				case "./IDEN":
					username = msgToServer.substring(7);
					msgToServer = "IDEN " + username;
					toConsole(msgToServer);

					input.setText("");
					break;

				case "./HAIL":
					msgToServer = "HAIL";
					toConsole(msgToServer);

					input.setText("");
					break;

				case "./MESG":
					msgToServer = "MESG";
					toConsole(msgToServer);

					input.setText("");
					break;

				case "./QUIT":
					msgToServer = "QUIT";
					toConsole(msgToServer);

					input.setText("");
					break;

				default:
					input.setText("");
					break;
				}
			}
			else{}


/*			if(msgToServer.equals("./LIST") || msgToServer.equals("./STAT") || msgToServer.equals("./IDEN") || msgToServer.equals("HAIL")
					|| msgToServer.equals("./MESG") || msgToServer.equals("./QUIT") )
				{
				out.println(msgToServer);

				msgFromServer = in.readLine();
				println(msgFromServer);
				System.out.println(msgFromServer);
				}

			input.setText("");*/


	}

	private void toConsole(String message) {
		try {
		out.println(message);

		message = in.readLine();
		println(message);
		System.out.println(message);
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
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			//client = new Connection(socket, server);
			// client.run();
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

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

		System.out.println(e.getKeyChar());


		switch(e.getKeyCode()) {

		case KeyEvent.VK_ENTER:
			if(!input.getText().isEmpty()){
				System.out.println("Enter pressed");
				send();
				input.requestFocus();
			}
			break;


		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}



}