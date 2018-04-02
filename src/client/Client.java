/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.EventQueue;
/**
 *
 * @author gabri
 */
public class Client {
    
    public static void main(String[] args){
//    

        clientMainGUI client = new clientMainGUI();
        client.setVisible(true);
        
    }
    
    public Client(String ipAddress, int portnum, String username){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ClientGUI cGUI = new ClientGUI(username, ipAddress, portnum);
                cGUI.setVisible(true);
            }
        });     
    
    }
    
}
