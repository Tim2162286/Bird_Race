import java.net.ServerSocket;
import java.net.Socket;

public class ServerMaster {
    private static final int PORTNUM = 29517;

    public static void main(String[] argv) {
        try {
            ServerSocket serverSocket = new ServerSocket(29517);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ServerPlayerThread clientThread = new ServerPlayerThread(clientSocket);
                (new Thread(clientThread)).start();
            }
        }
        catch (Throwable e) {
            System.out.println(e);
        }
    }
}