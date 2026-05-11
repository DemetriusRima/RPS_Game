import java.net.*;

public class GameServer {

    private int port = 3000;
    private Room room = new Room();

    public void start() {

        System.out.println("Server running on port " + port);

        try (ServerSocket serverSocket =
                     new ServerSocket(port)) {

            while (true) {

                Socket socket = serverSocket.accept();

                System.out.println("Client connected");

                ClientHandler handler =
                        new ClientHandler(socket, room);

                handler.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new GameServer().start();
    }
}