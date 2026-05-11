import java.util.ArrayList;
import java.util.List;

public class Room {

    private List<ClientHandler> clients =
            new ArrayList<>();

    public synchronized void addClient(ClientHandler client) {

        clients.add(client);

        broadcast(client.getClientName()
                + " joined the room.");

        // Start game when 2 players join
        if (clients.size() == 2) {

            ClientHandler p1 = clients.get(0);
            ClientHandler p2 = clients.get(1);

            broadcast("Starting match...");

            GameSession game =
                    new GameSession(p1, p2);

            game.start();

            // reset room for next players
            clients.clear();
        }
    }

    public synchronized void removeClient(
            ClientHandler client) {

        clients.remove(client);

        broadcast(client.getClientName()
                + " left the room.");
    }

    public synchronized void broadcast(String msg) {

        for (ClientHandler client : clients) {
            client.sendMessage(msg);
        }
    }
}