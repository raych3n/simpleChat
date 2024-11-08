package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;
import java.util.ArrayList;
import ocsf.server.*;
import edu.seg2105.client.common.ChatIF;
import edu.seg2105.edu.server.*;
/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  ChatIF serverUI;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) throws IOException
  {
    super(port);
    this.serverUI = serverUI;
    listen();
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  String tmpMsg = (String)msg;
	  System.out.println("Message received: " + tmpMsg + " from " + client.getInfo("id"));
	  if (tmpMsg.length() >= 6 && tmpMsg.substring(0,6).equals("#login")) {
		  if (client.getInfo("id") != null) {
			  try {
				  String[] out = {"Login ID should not be entered now.", "error"};
				  client.sendToClient(out);
				  client.close();
			} catch (IOException e) {}
		  }
		  if (tmpMsg.length() >= 8) {
			  client.setInfo("id", tmpMsg.substring(7));
			  String[] out = {client.getInfo("id") + " has logged on.", "start"};
			  System.out.println(out[0]);
			  try {
				client.sendToClient(out);
			} catch (IOException e) {}
		  }
	  }
	  else {
		    String[] item = {tmpMsg, "c" + client.getInfo("id")};
		    this.sendToAllClients(item);
	  }
    
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  /**
   * This method prints out the new client that connects.
   * @param client connection to client
   */
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("A new client has connected to the server.");
  }
  
  /**
   * This method prints out the client that disconnects.
   * @param client connection to client
   */
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println("Client " + client.getInfo("id") + " disconnected");
  }
  
  /**
   * This method terminates the server.
   */
  public void quit()
  {
    try
    {
      close();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  
}
//End of EchoServer class
