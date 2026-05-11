import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {

    private int port = 3000;
    private Room room = new Room();

    private void start(int port) {

        this.port = port;

        System.out.println("Server started on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {

                Socket clientSocket = serverSocket.accept();

                System.out.println("Client connected: "
                        + clientSocket.getInetAddress());

                ClientHandler client =
                        new ClientHandler(clientSocket, room);

                client.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        int port = 3000;

        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
        }

        GameServer server = new GameServer();
        server.start(port);
    }
}