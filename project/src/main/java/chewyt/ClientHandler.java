package chewyt;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

//Server class construct clienthanlder object the moments a client enter via server.accept
//clienthanlder will work for functions to take in input and out streams for server from all clients

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clienthandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private String username;

    public ClientHandler(Socket socket){

        try {
            this.socket = socket;
            this.bw = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));
            this.br = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            this.username = br.readLine();
            //Adding client to arraylist, so that it can be part of group chat
            clienthandlers.add(this);
            //Informing the chat that somebody joined in the session
            broadcastMessage("[SERVER] "+ username +" has entered the chat!");
        } catch (IOException e) {
            closeEverything(socket,br,bw); 
        }

    }

    //Running run() to use separate threads to perform for blocking operations like receiving messages
    @Override
    public void run() {
        String messageFromClient ="";
        while(socket.isConnected()){
            try {

                messageFromClient = br.readLine(); //blocking operation
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket,br,bw);
                break;
            }
        }
        
    }

    //sub method to support thread operation from run()

    public void broadcastMessage(String message){

        for(ClientHandler aclient : clienthandlers){
            
            try {
                if (!aclient.username.equals(username)) {
                    aclient.bw.write(message);
                    aclient.bw.newLine(); //explicity write "Enter" for receiver to accept message due to br.readline()
                    aclient.bw.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, br, bw);
            }

        }

    }
    //part of closeEverything method
    public void removeClientHandler(){
        clienthandlers.remove(this);
        broadcastMessage("[SERVER] "+username+ " has left the chat!");

    }

    public void closeEverything(Socket socket, BufferedReader br, BufferedWriter bw){
        removeClientHandler();
        try {
            if(br!=null){
                br.close();
            }
            if (bw!=null){
                bw.close();
            }
            if(socket!=null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
