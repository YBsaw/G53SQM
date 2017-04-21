package g53sqm.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Connection implements Runnable {

	final static int STATE_UNREGISTERED = 0;
	final static int STATE_REGISTERED = 1;

	private volatile boolean running;
	private int messageCount;
	private int state;
	private Socket client;
	private Server serverReference;
	private BufferedReader in;
	private PrintWriter out;
	private String username;

	String line;

	Connection (Socket client, Server serverReference) {
		this.serverReference = serverReference;
		this.client = client;
		this.state = STATE_UNREGISTERED;
		messageCount = 0;
		System.out.println("Connection checkpoint initialized");
	}

	public void run(){
//		String line;
		System.out.println("Connection checkpoint in running");
		try {
			System.out.println("Connection checkpoint in try");
			in = new BufferedReader(new InputStreamReader(client.getInputStream())); //create a new buffer reader to read from the server
			out = new PrintWriter(client.getOutputStream(), true); //create a new print writer to write input to the server
		} catch (IOException e) {
			System.out.println("in or out failed");
			System.exit(-1);
		}
		System.out.println("Connection checkpoint finish try");
		running = true;
		System.out.println("Connection checkpoint after running");

		//welcome message to show users that they have connected to the server successfully.
		this.sendOverConnection("OK Welcome to the chat server, there are currently " + serverReference.getNumberOfUsers() + " user(s) online");

		//when the connection is running.
		while(running) {
			System.out.println("Connection in run loop");
			try {
				line = in.readLine(); //read the latest line in the server.

				if(line != null) { //check if there is any line to be read.
					validateMessage(line); //validate the message that have been typed by the user
				} else {
					in.close();
				}
			} catch (IOException e) {
				System.out.println("Read failed");
				System.exit(-1);
			}
		}
	}

	private void validateMessage(String message) {

		if(message.length() < 4){
			sendOverConnection ("BAD invalid command to server"); //if less than 4 characters, invalid commands.
		} else {
			switch(message.substring(0,4)){ //check the first 4 characters of the input.
				case "LIST":
					list();
					break;

				case "STAT":
					stat();
					break;

				case "IDEN":
					iden(message.substring(5));
					break;

				case "HAIL":
					hail(message.substring(5));
					break;

				case "MESG":
					mesg(message.substring(5));
					break;

				case "QUIT":
					quit();
					break;

				default:
					sendOverConnection("BAD command not recognised");
					break;
			}
		}

	}

	//to check the current state of the specific user.
	private void stat() {
		String status = "There are currently "+serverReference.getNumberOfUsers()+" user(s) on the server ";
		switch(state) {
			case STATE_REGISTERED:
				status += "You are logged im and have sent " + messageCount + " message(s)";
				break;

			case STATE_UNREGISTERED:
				status += "You have not logged in yet";
				break;
		}
		sendOverConnection("OK " + status);
	}

	//to show the list of users that are connected to the server.
	private void list() {
		switch(state) {
			case STATE_REGISTERED:
				ArrayList<String> userList = serverReference.getUserList();
				String userListString = new String();
				for(String s: userList) {
					userListString += s + ", ";
				}
				sendOverConnection("OK " + userListString);
				break;

			case STATE_UNREGISTERED:
				sendOverConnection("BAD You have not logged in yet");
				break;
		}

	}

	//to login to the server with the username.
	private void iden(String message) {
		switch(state) {
			case STATE_REGISTERED:
				sendOverConnection("BAD you are already registerd with username " + username);
				break;

			case STATE_UNREGISTERED:
				String username = message.split(" ")[0];
				if(serverReference.doesUserExist(username)) {
					sendOverConnection("BAD username is already taken");
				} else {
					this.username = username;
					state = STATE_REGISTERED;
					sendOverConnection("OK Welcome to the chat server " + username);
				}
				break;
		}
	}

	//to send a message that all users can see.
	private void hail(String message) {
		switch(state) {
			case STATE_REGISTERED:
				serverReference.broadcastMessage("Broadcast from " + username + ": " + message);
				messageCount++;
				break;

			case STATE_UNREGISTERED:
				sendOverConnection("BAD You have not logged in yet");
				break;
		}
	}

	//check if the connection is still running.
	public boolean isRunning(){
		return running;
	}

	//send a private message to specific user.
	private void mesg(String message) {

		switch(state) {
			case STATE_REGISTERED:

				if(message.contains(" ")) {
					int messageStart = message.indexOf(" ");
					String user = message.substring(0, messageStart);
					String pm = message.substring(messageStart+1);
					if(serverReference.sendPrivateMessage("PM from " + username + ":" + pm, user)){
						sendOverConnection("OK your message has been sent");
					} else {
						sendOverConnection("BAD the user does not exist");
					}
				}
				else{
					sendOverConnection("BAD Your message is badly formatted");
				}
				break;

			case STATE_UNREGISTERED:
				sendOverConnection("BAD You have not logged in yet");
				break;
		}
	}

	//disconnect from the server
	private void quit() {
		switch(state) {
			case STATE_REGISTERED:
				sendOverConnection("OK thank you for sending " + messageCount + " message(s) with the chat service, goodbye. ");
				break;
			case STATE_UNREGISTERED:
				sendOverConnection("OK goodbye");
				break;
		}
		running = false;
		try {
			client.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		serverReference.removeDeadUsers();
	}

	//send the input to the server.
	private synchronized void sendOverConnection (String message){
		out.println(message);
	}

	//send the message over the connection to the server.
	public void messageForConnection (String message){
		sendOverConnection(message);
	}

	//get the current state of the user, whether registered or not registered.
	public int getState() {
		return state;
	}

	//get the username of specific user.
	public String getUserName() {
		return username;
	}

}

