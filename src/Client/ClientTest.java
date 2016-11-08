package Client;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by jebush2 on 11/8/2016.
 */
public class ClientTest {



    public static void main(String[] args) {
        ClientMaster client;
        try {
            client = new ClientMaster();
            (new Thread(client)).start();
            while (!client.isReady()) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    //thread interrupted
                }
            }
            System.out.println(1);
            client.requestGameId();
            System.out.println(2);
            client.setHandle("test");
            System.out.println(3);
            client.requestUsers();

            System.out.println(4);

            while(client.backlog());
            System.out.println(5);
            System.out.println(Arrays.toString(client.getUsers()));

            System.out.println();

        } catch (IOException e) {
            System.out.println("There was an error connecting to the server.");
        }

    }
}
