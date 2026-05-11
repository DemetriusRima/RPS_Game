public class GameSession extends Thread {

    private ClientHandler p1;
    private ClientHandler p2;

    private int s1 = 0;
    private int s2 = 0;
    private int ties = 0;

    public GameSession(ClientHandler p1, ClientHandler p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public void run() {

        try {

            p1.sendMessage("MATCH STARTED");
            p2.sendMessage("MATCH STARTED");

            while (s1 < 2 && s2 < 2) {

                p1.sendMessage("ROCK/PAPER/SCISSORS:");
                p2.sendMessage("ROCK/PAPER/SCISSORS:");

                String m1 = p1.readMessage();
                String m2 = p2.readMessage();

                if (m1 == null || m2 == null) {
                    broadcast("A player disconnected.");
                    break;
                }

                m1 = m1.toUpperCase().trim();
                m2 = m2.toUpperCase().trim();

                int result = winner(m1, m2);

                if (result == 0) {
                    ties++;
                    broadcast("TIE: " + m1);
                }
                else if (result == 1) {
                    s1++;
                    broadcast("P1 wins round");
                }
                else {
                    s2++;
                    broadcast("P2 wins round");
                }

                broadcast(score());
            }

            if (s1 > s2)
                broadcast("PLAYER 1 WINS");
            else
                broadcast("PLAYER 2 WINS");

            p1.close();
            p2.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void broadcast(String msg) {
        p1.sendMessage(msg);
        p2.sendMessage(msg);
    }

    private String score() {
        return "Score -> P1: " + s1 +
               " P2: " + s2 +
               " Ties: " + ties;
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