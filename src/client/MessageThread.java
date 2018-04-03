package client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JLabel;


public class MessageThread implements Runnable {
	private Socket socket;
	private String userName;
	private final LinkedList<String> messageToSend;
	private boolean hasMessage = false;
	private ClientGUI gui;
        private final ArrayList <String> users;
        
	public MessageThread(Socket socket, String userName, ClientGUI gui) {
		this.socket = socket;
		this.userName = userName;
                this.gui = gui;
                users = new ArrayList();
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
                                            if(readInput.contains("~user~")){
                                                String actualUser = readInput.substring(6);
                                                if(!users.contains(actualUser)){
                                                    users.add(actualUser);
                                                    System.out.println("added user to list " + actualUser);
                                                    JButton item = new JButton();
                                                    item.setText(actualUser);
                                                    gui.getUserOnline().add(item);
                                                    gui.repaint();
                                                    gui.validate();
                                                }
                                                
                                            }else
                                                gui.getChatFieldBox().append(readInput+"\n");
                                                
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

