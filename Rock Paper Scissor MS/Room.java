import java.util.*;

public class Room {

    private Queue<ClientHandler> queue =
            new LinkedList<>();

    public synchronized void addClient(ClientHandler c) {

        queue.add(c);

        broadcast(c.getName() + " joined queue");

        if (queue.size() >= 2) {

            ClientHandler p1 = queue.poll();
            ClientHandler p2 = queue.poll();

            GameSession session =
                    new GameSession(p1, p2, this);

            session.start();
        }
    }

    public synchronized void returnWinner(
            ClientHandler winner) {

        queue.add(winner);

        broadcast(winner.getName()
                + " stays in queue");
        tryMatch();
    
    }
    
    private void tryMatch() {

        if (queue.size() < 2) return;

        ClientHandler p1 = queue.poll();
        ClientHandler p2 = queue.poll();

        broadcast("Match starting: "
                + p1.getName()
                + " vs "
                + p2.getName());

        GameSession session =
                new GameSession(p1, p2, this);

        session.start();
    }
    
    public synchronized void broadcast(String msg) {

        for (ClientHandler c : queue) {
            c.sendMessage(msg);
        }
    }
}