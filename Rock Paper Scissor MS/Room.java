import java.util.*;

public class Room {

    private final List<ClientHandler> waiting =
            new ArrayList<>();

    public synchronized void addClient(ClientHandler client) {

        waiting.add(client);

        broadcast(client.getName() + " joined.");

        if (waiting.size() >= 2) {

            ClientHandler p1 = waiting.remove(0);
            ClientHandler p2 = waiting.remove(0);

            GameSession session =
                    new GameSession(p1, p2);

            session.start();
        }
    }

    public synchronized void broadcast(String msg) {

        for (ClientHandler c : waiting) {
            c.sendMessage(msg);
        }
    }
}