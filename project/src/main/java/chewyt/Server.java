package chewyt;

import java.io.*;
import java.net.*;

public class Server {

    private static final int port = 12345;
    private ServerSocket server;
    
    public Server(ServerSocket server){
        this.server = server;
    }
    
    public void startServer(){
        try {
            System.out.println("[SERVER] Server ready. Listening for client...");
            while(!server.isClosed()){
                Socket socket = server.accept();
                System.out.println("A new client has connected.");
                ClientHandler clienthandler = new ClientHandler(socket);

                Thread thread = new Thread(clienthandler);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }

    public void closeServerSocket(){

        try {
            if(server!=null){
                server.close();
            }
            
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        catch (IOException i) {
            i.printStackTrace();
        }
    } 
    
    public static void main(String[] args) throws IOException {
        
        //Setup server 
        ServerSocket serverSocket = new ServerSocket(port);
        Server server = new Server(serverSocket);
        server.startServer();

        /* //Listening for client
        System.out.println("[SERVER] Server ready. Listening for client connection...");
        Socket client = server.accept();
        System.out.println("[SERVER] Client joined the server!");

        //Server needs to send message to client
        PrintWriter out = new PrintWriter(client.getOutputStream(),true);
        //Server needs to read data from client socket
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));  */


    }


}
