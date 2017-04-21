package g53sqm.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;


public class Server {

	private ServerSocket server;
	private ArrayList<Connection> list;

	public Server (int port) {
		try {
			server = new ServerSocket(port); //initialised a new server with a server socket using the specific port number.
			System.out.println("Server has been initialised on port " + port);
		}
		catch (IOException e) {
			System.err.println("error initialising server");
			e.printStackTrace();
		}
		list = new ArrayList<Connection>(); //create a list to record the connections.
		while(true) { //loop when the server is on
				Connection c = null;
				try {
					//the server will wait for a connection and accept it.
					//if there is no connection, the method is blocked and the program will wait until there's one.
					c = new Connection(server.accept(), this);
				}
				catch (IOException e) {
					System.err.println("error setting up new client conneciton");
					e.printStackTrace();
				}
				System.out.println("Connection finished");
				Thread t = new Thread(c);
				t.start();
				System.out.println("Connection about to run");
				list.add(c);
		}
	}

	//to get the user list from the server
	public ArrayList<String> getUserList() {
		ArrayList<String> userList = new ArrayList<String>();
		for( Connection clientThread: list){
			if(clientThread.getState() == Connection.STATE_REGISTERED) {
				userList.add(clientThread.getUserName());
			}
		}
		return userList;
	}

	//to check if specific user has connected to the server or not.
	public boolean doesUserExist(String newUser) {
		boolean result = false;
		for( Connection clientThread: list){
			if(clientThread.getState() == Connection.STATE_REGISTERED) {
				result = clientThread.getUserName().compareTo(newUser)==0;
			}
		}
		return result;
	}

	//send the message that all users can see
	public void broadcastMessage(String theMessage){
		System.out.println(theMessage);
		for( Connection clientThread: list){
			clientThread.messageForConnection(theMessage + System.lineSeparator());
		}
	}

	//send private message to the specific recipient
	public boolean sendPrivateMessage(String message, String user) {
		for( Connection clientThread: list) {
			if(clientThread.getState() == Connection.STATE_REGISTERED) {
				if(clientThread.getUserName().compareTo(user)==0) {
					clientThread.messageForConnection(message + System.lineSeparator());
					return true;
				}
			}
		}
		return false;
	}

	//remove any users that have disconnected.
	public void removeDeadUsers(){
		Iterator<Connection> it = list.iterator();
		while (it.hasNext()) {
			Connection c = it.next();
			if(!c.isRunning())
				it.remove();
		}
	}

	//get the number of users that have connected to the server.
	public int getNumberOfUsers() {
		return list.size();
	}

	//close the server
	protected void finalize() throws IOException{
		server.close();
	}

}
