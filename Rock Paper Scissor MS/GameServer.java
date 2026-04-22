import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    private int port = 3000;

 // Single room for now
    private Room mainRoom = new Room();

    private void start(int port) {
        this.port = port;
        System.out.println("Listening on port " + this.port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client connected: " + client);

                ClientHandler handler = new ClientHandler(client, mainRoom);
                handler.start();
            }

        } catch (IOException e) {
            System.out.println("Exception from start()");
            e.printStackTrace();
        } finally {
            System.out.println("closing server socket");
        }
    }

    public static void main(String[] args) {
        System.out.println("Server Starting");
        GameServer server = new GameServer();
        int port = 3000;

        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) { }

        server.start(port);
        System.out.println("Server Stopped");
    }
}

/* ================= ROOM CLASS ================= */
class Room {
    private List<ClientHandler> clients = new ArrayList<>();

    public synchronized void addClient(ClientHandler client) {
        clients.add(client);
        broadcast("A new player joined the room. Total: " + clients.size());
    }

    public synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
        broadcast("A player left the room. Total: " + clients.size());
    }

    public synchronized void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;
    private Room room;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket, Room room) {
        this.socket = socket;
        this.room = room;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            room.addClient(this);
            out.println("Welcome to the room!");

            String fromClient;

            while ((fromClient = in.readLine()) != null) {

                if ("/kill server".equalsIgnoreCase(fromClient)) {
                    System.out.println("Client requested shutdown");
                    break;
                }

                System.out.println("From client: " + fromClient);

                // Broadcast to everyone in room
                room.broadcast("Player says: " + fromClient);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            room.removeClient(this);
            try {
                socket.close();
            } catch (IOException e) { }
        }
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }
}