public class GameSession extends Thread {

    private ClientHandler p1;
    private ClientHandler p2;
    private Room room;

    private int s1 = 0;
    private int s2 = 0;

    public GameSession(ClientHandler p1,
                       ClientHandler p2,
                       Room room) {

        this.p1 = p1;
        this.p2 = p2;
        this.room = room;
    }

    @Override
    public void run() {

        try {

            p1.sendMessage("MATCH START");
            p2.sendMessage("MATCH START");

            while (s1 < 2 && s2 < 2) {

                p1.sendMessage("R.P.S SHOOT:");
                p2.sendMessage("R.P.S SHOOT:");

                String m1 = safe(p1);
                String m2 = safe(p2);

                if (m1 == null || m2 == null) return;

                m1 = m1.toUpperCase().trim();
                m2 = m2.toUpperCase().trim();

                int r = winner(m1, m2);

                if (r == 0) {
                    broadcast("TIE");
                }
                else if (r == 1) {
                    s1++;
                    broadcast("P1 WINS ROUND");
                }
                else {
                    s2++;
                    broadcast("P2 WINS ROUND");
                }

                broadcast(score());
            }

            ClientHandler winner =
                    (s1 > s2) ? p1 : p2;

            broadcast("WINNER: " + winner.getName());

            room.returnWinner(winner);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String safe(ClientHandler c) {
        return c.readMessage();
    }

    private void broadcast(String msg) {
        p1.sendMessage(msg);
        p2.sendMessage(msg);
    }

    private String score() {
        return "Score P1:" + s1 + " P2:" + s2;
    }

    private int winner(String a, String b) {

        if (a.equals(b)) return 0;

        if ((a.equals("ROCK") && b.equals("SCISSORS")) ||
            (a.equals("PAPER") && b.equals("ROCK")) ||
            (a.equals("SCISSORS") && b.equals("PAPER"))) {
            return 1;
        }

        return 2;
    }
}