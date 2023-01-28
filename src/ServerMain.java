import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String[] args) {

        ServerSocket server = null;
        try {
            server = new ServerSocket(6666);
            while (true){
                Socket client = server.accept();
                Resolver clientResolver = new Resolver(client);
                new Thread(clientResolver).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(server != null){
                try {
                    server.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}