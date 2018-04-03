package server;

import server.ServerGUI;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientRunnerino  implements Runnable{
	private Socket socket;
	private PrintWriter clientOut;
	private ServerGUI server;
	private String username;
	
	public ClientRunnerino(ServerGUI server, Socket socket,String username) {
		this.server = server;
		this.socket = socket;
		this.username = username;
            try {
                this.clientOut = new PrintWriter(socket.getOutputStream(), false);
            } catch (IOException ex) {
                Logger.getLogger(ClientRunnerino.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                Thread t = new Thread(server);
                t.start();
	}
	
	public String getUsername() {
		
		return username;
	}
	
	public PrintWriter getWriter() {
		return clientOut;
	}
	
        public Socket getSocket(){
            return socket;
        }
        
	@Override
	public void run() {
		try {
			
			
			Scanner in = new Scanner (socket.getInputStream());
                        
                        
			while(!socket.isClosed()) {
				if(in.hasNextLine()) {
					String input = in.nextLine();
                                        String message = input.substring(input.indexOf(":"));
                                        String actualInput = message.substring(5);
                                        String toDisplay = input.substring(0, input.indexOf(":")).concat(": " + actualInput);
                                        String messageType = message.substring(2, 5);                       // /w/ (whisper), /a/ (all), or /g/ (group)
					System.out.println("INPUT: "+input);
                                        server.getServerText().append(toDisplay + "\n");
                                        //this is where we put if statements to check if private/group/global

                                        if(messageType.equals("/a/")){
                                           for (ClientRunnerino thatClient : server.getClients()) {            //iterates through all clients
                                                PrintWriter thatClientOut = thatClient.getWriter();
                                                
                                                if(thatClientOut != null) {
                                                    thatClientOut.write(toDisplay + "\r\n");
                                                    thatClientOut.flush();
                                                    
                                                }
                                            } 
                                        }else if(messageType.equals("/g/")){
                                            
                                            
                                        }else if(messageType.equals("/w/")){
                                            for (ClientRunnerino thatClient : server.getClients()) {            //iterates through all clients
                                                if(thatClient.getUsername().equals("" /*recipient*/)){
                                                    PrintWriter thatClientOut = thatClient.getWriter();
                                                
                                                    if(thatClientOut != null) {
                                                        thatClientOut.write(toDisplay + "\r\n");
                                                        thatClientOut.flush();
                                                    
                                                    }
                                                }
                                                
                                            }
                                        }
						
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	

}
