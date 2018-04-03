package server;

import server.ServerGUI;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientRunnerino  implements Runnable{
	private Socket socket;
	private PrintWriter clientOut;
	private ServerGUI server;
	private String username;
	
	public ClientRunnerino(ServerGUI server, Socket socket,String username) {
		this.server = server;
		this.socket = socket;
		this.username = username;
                
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
			
			this.clientOut = new PrintWriter(socket.getOutputStream(), false);
			Scanner in = new Scanner (socket.getInputStream());
			while(!socket.isClosed()) {
				if(in.hasNextLine()) {
					String input = in.nextLine();
					System.out.println("INPUT: "+input);
                                        server.getServerText().append(input + "\n");
                                        //this is where we put if statements to check if private/group/global
//                                        for (ClientRunnerino thatClient : server.getClients()) {
//						PrintWriter thatClientOut = thatClient.getWriter();                 //can use getUsername and getWriter to send to specific users
//                                                        
//                                        }
                                            //input.s
						for (ClientRunnerino thatClient : server.getClients()) {            //iterates through all clients
							PrintWriter thatClientOut = thatClient.getWriter();
							if(thatClientOut != null) {
								thatClientOut.write(input + "\r\n");
								thatClientOut.flush();
							}
						}
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	

}
