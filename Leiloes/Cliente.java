import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Cliente {
    private String serverAddress;
    private int portNumber;
    private Socket socket;
    private String username;
    private BufferedReader input;
    private PrintWriter output;
    private BufferedReader standardInput;

    public Cliente(String serverAddress, int portNumber) {
        this.serverAddress = serverAddress;
        this.portNumber = portNumber;
    }

    public static void main(String[] args) throws IOException {
        String serverAddress = args[0];
        int portNumber = Integer.parseInt(args[1]);
        Cliente chatClient = new Cliente(serverAddress, portNumber);
        chatClient.execute();
    }

    public void execute() throws IOException {
        Socket socket = new Socket(this.serverAddress, this.portNumber);
        System.out.println("Gestão de leilões iniciado!");
        this.input = new BufferedReader(
            new InputStreamReader(socket.getInputStream()));
        this.standardInput = new BufferedReader(
            new InputStreamReader(System.in));
        this.output = new PrintWriter(socket.getOutputStream(), true);
        new ServerListener().start();
        new ServerAnswer().start();
    }

    class ServerListener extends Thread {
        public void run() {
            try {
                String inputLine;
                while ((inputLine = input.readLine()) != null){
                    if(inputLine.equals("quit")){System.exit(0);} 
                    System.out.println(inputLine);
                    }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
    class ServerAnswer extends Thread {
        public void run() {
            try {
                String inputLine;
                while ((inputLine = standardInput.readLine()) != null){
                        output.println(inputLine);

                    }
               
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
