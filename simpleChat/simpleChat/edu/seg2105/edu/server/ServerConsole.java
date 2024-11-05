package edu.seg2105.edu.server;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.common.*;
import edu.seg2105.edu.server.backend.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 */
public class ServerConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  EchoServer server;
  
  
  
  /**
   * Scanner to read from the console
   */
  Scanner fromConsole; 

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ServerConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ServerConsole(int port) 
  {
	  try 
	    {
	      server= new EchoServer(port, this);
	      
	      
	    } 
	   catch(IOException exception) 
	    {
	      System.out.println("Error: Can't setup connection!"
	                + " Terminating server.");
	      System.exit(1);
	    }
	    
	    // Create scanner object to read from console
	    fromConsole = new Scanner(System.in); 
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() 
  {
    try
    {

      String message;

      while (true) 
      {
        message = fromConsole.nextLine();
        if (message.charAt(0) == '#') {
        	if (message.equals("#quit")) {
        		System.out.println("Server quitting...");
        		server.quit();
        	}
        	else if (message.equals("#stop")) {server.stopListening();}
        	else if (message.equals("#close")) {server.close();}
        	else if (message.equals("#start")) {
        		if (!server.isListening()) {server.listen();}
        		else {System.out.println("Stop listening first.");}
        	}
        	else if (message.equals("#getport")) {System.out.println(server.getPort());}
        	if (message.length() > 9) {
        		if (message.substring(0, 8).equals("#setport")) {
            		int newPort = Integer.parseInt(message.substring(9));
            		if (!server.isListening()) {server.setPort(newPort);}
            		else {System.out.println("Please stop listening first.");}
            	}
        	}
        	
        }
        else {
        	display(message);
        }
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
	  String[] out = {message, "server"};
	  server.sendToAllClients(out);
	  System.out.println("SERVER MSG> " + message);
  }
  
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    ServerConsole sc = new ServerConsole(port);
    sc.accept();
  }

}
//End of ConsoleChat class
