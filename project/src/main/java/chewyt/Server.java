package chewyt;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int port = 12345;
    private ServerSocket server;
    
    public Server(ServerSocket server){
        this.server = server;
    }
    
    public void startServer(){
        try {
            System.out.println("[SERVER] Server ready. Listening for client...");
            ExecutorService threadPool = Executors.newFixedThreadPool(2); // Third client join in--> Can Add to clienthandler but thread
            //but thread is not assigned to the ClientHandler via void run menthod to continuously do br.readline for third client,
            //thus unable to proceed to next step if broadcasting meesage

            //Symptoms --> Client 1 send message, Client 2 and 3 receives, Client 3 receive based on the new thread inside CLient program
            //         --> Client 2 send message, Client 1 and 3 receives, Client 3 receive based on the new thread inside CLient program
            //         --> Client 3 send message, Client 1 and 2 does not receive, as Client Handler is not working for CLient 3 to read in input and broadcast to other parties.
            
            while(!server.isClosed()){
                Socket socket = server.accept();
                System.out.println("A new client has connected.");
                ClientHandler clienthandler = new ClientHandler(socket);

                //Code for auto running and scheduling of threads from Threadpool by Executor Service
                threadPool.submit(clienthandler);

                //Code for normal thread operation without Threadpool management
                //Thread thread = new Thread(clienthandler);
                //thread.start();
                
                System.out.println("How many active threads after user join: "+ Thread.activeCount());
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
