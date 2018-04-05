/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author Ashen One
 */
public class PrivateMessageThread implements Runnable{
        private Socket socket;
	private String userName;
	private final LinkedList<String> messageToSend;
	private boolean hasMessage = false;
	private PrivateMessageGUI gui;
        
	public PrivateMessageThread(Socket socket, String userName, PrivateMessageGUI gui) {
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

                
                //try{
//                    InputStream input = socket.getInputStream();
//                    Scanner serverIn = new Scanner(input);
//                    String clients = null;
//                    ArrayList <String> users = new ArrayList();
//                    
//                    while((clients = serverIn.nextLine()) == null);
//                    int numOfClients = Integer.parseInt(clients);
//                    System.out.println("numOfClients: " + numOfClients);
//                    for(int i = 0; i < numOfClients; i ++){
//                            String user;
//                            while((user = serverIn.nextLine()) == null);
//                            users.add(user);
//                            System.out.println("users sent " + users.get(i));
//                            //System.out.println("users sent " + users.get(1));
//                        
//                    }
//                    System.out.println("after the looperino");
//                }catch(IOException iex){
//                    iex.printStackTrace();
//                }
		try {
			PrintWriter serverOut = new PrintWriter(socket.getOutputStream(), false);
			InputStream serverInStream = socket.getInputStream();
			Scanner serverIn = new Scanner(serverInStream);
			
			while (!socket.isClosed()) {
				if (serverInStream.available() > 0) {
					if(serverIn.hasNextLine()) {
                                            String readInput = serverIn.nextLine();
                                            gui.getPMChat().append(readInput+"\n");
                                            System.out.println("sending a message");    
					}
				}
				if(hasMessage) {
                                        gui.repaint();
                                        gui.validate();
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
	}finally{
                    try {
                        System.out.println("closing timeeee");
                        socket.close();
                        
                    } catch (IOException ex) {
                        Logger.getLogger(MessageThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
        }

   }	
}
