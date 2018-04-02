package client;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;


public class MessageThread implements Runnable {
	private Socket socket;
	private String userName;
	private final LinkedList<String> messageToSend;
	private boolean hasMessage = false;
	private ClientGUI gui;
        
	public MessageThread(Socket socket, String userName, ClientGUI gui) {
		this.socket = socket;
		this.userName = userName;
                this.gui = gui;
		messageToSend = new LinkedList<>();
	}
	
	public void addNextMessage(String message) {
		synchronized (messageToSend) {
			hasMessage = true;
			messageToSend.push(message);
		}
	}
	
	@Override
	public void run() {
		System.out.println("Welcome: " + userName);
		
		System.out.println("xD Beep Local Port: " + socket.getLocalPort());
		try{
                    PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false);
                    serverOut.println(userName);    //send username to server xd
                    serverOut.flush();
                    System.out.println("potato butthead");
                }catch(IOException iex){
                    iex.printStackTrace();
                }
                
		try {
			PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false);
			InputStream serverInStream = socket.getInputStream();
			Scanner serverIn = new Scanner(serverInStream);
			
			while (!socket.isClosed()) {
				if (serverInStream.available() > 0) {
					if(serverIn.hasNextLine()) {
                       gui.getChatFieldBox().append(serverIn.nextLine()+"\n");
                                                
					}
				}
				if(hasMessage) {
					String nextSend = "";
					synchronized(messageToSend) {
						nextSend = messageToSend.pop();
						hasMessage = !messageToSend.isEmpty();
					}
					serverOut.println("[" + userName + "]: "+nextSend);
					serverOut.flush();
				}
			}
		
	}
	catch(IOException ex) {
		ex.printStackTrace();
	}

   }	
}

