package chewyt;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedWriter bw;
    private BufferedReader br;
    private String username;

    public Client(Socket socket, String username){

       try {
           this.username=username;
           this.socket=socket;
           this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

       } catch (Exception e) {
           closeEverything(socket, br, bw);
       } 

    }

    public void sendMessage(){
        System.out.println("Sending..");
        try {
            bw.write(username);
            bw.newLine();
            bw.flush();
            
            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String messageToSend = scanner.nextLine();
                bw.write(username+": "+messageToSend);
                bw.newLine();
                bw.flush();
            }
            scanner.close();
        } catch (IOException e) {
            closeEverything(socket, br, bw);
        } 
    }

    public void receiveMessage(){    // Need to use new thread by passing runnable object
        System.out.println("Receviing..");
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                String messagefromGroupChat;
                while(socket.isConnected()){
                    try {
                        messagefromGroupChat = br.readLine();
                        System.out.println(messagefromGroupChat);
            
                    } catch (IOException e) {
                        closeEverything(socket, br, bw);
                    }
                }                
            }
        }).start();   
    }

    public void closeEverything(Socket socket, BufferedReader br, BufferedWriter bw){
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

    public static void main(String[] args) throws UnknownHostException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        //scanner.close();
        Socket socket  = new Socket("localhost",12345);
        Client client = new Client(socket, username);
        client.receiveMessage(); // blocking operation use thread within method
            client.sendMessage(); //yet another blocking operation which uses main thread of the client program
        
    }
}
