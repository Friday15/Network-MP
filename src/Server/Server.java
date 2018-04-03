/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.awt.EventQueue;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Paolo Delos Reyes, Richard Ingles, Emir Mendoza
 */
public class Server {
        
	public static void main(String[] args) {
        
         serverMainGUI smGUI = new serverMainGUI();  
         smGUI.setVisible(true);
          
	}
	

    //create a constructor for Server
    public Server(int portnum){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ServerGUI serverGUI = new ServerGUI(portnum);
                serverGUI.setVisible(true);
            }
        });
        
        
    }
    
}
