
public class GameSession extends Thread {

    private ClientHandler player1;
    private ClientHandler player2;

    // Score tracking
    private int player1Score = 0;
    private int player2Score = 0;
    private int ties = 0;

    public GameSession(ClientHandler p1, ClientHandler p2) {
        this.player1 = p1;
        this.player2 = p2;
    }

    @Override
    public void run() {

        player1.sendMessage("=== MATCH STARTED ===");
        player2.sendMessage("=== MATCH STARTED ===");
        player1.sendMessage("You are Player 1");
        player2.sendMessage("You are Player 2");
        // Best of 3
        while (player1Score < 2 && player2Score < 2) {
            
            player1.sendMessage("Enter move: ROCK PAPER SCISSORS");
            player2.sendMessage("Enter move: ROCK PAPER SCISSORS");
            
            String move1 = player1.readMessage();
            String move2 = player2.readMessage();

            if (move1 == null || move2 == null) {
                sendBoth("A player disconnected.");
                break;
            }

            move1 = move1.toUpperCase().trim();
            move2 = move2.toUpperCase().trim();
            
            int result = determineWinner(move1, move2);
            
            // Tie
            if (result == 0) {
                ties++;
                
                sendBoth(
                        "Tie round! Both chose " + move1
                );
            }

            else if (result == 1) {
                player1Score++;
                
                sendBoth(
                        "Player 1 wins round! "
                                + move1 + " beats " + move2
                );
            }

            else {
                player2Score++;
                
                sendBoth(
                        "Player 2 wins round! "
                                + move2 + " beats " + move1
                );
            }
            
            // Show score after each round
            sendScoreboard();
        }
        // Final winner
        if (player1Score > player2Score) {
            sendBoth("=== PLAYER 1 WINS MATCH ===");
        } else {
            sendBoth("=== PLAYER 2 WINS MATCH ===");
        }
        sendBoth("Game Over.");
    }

    // Send score board
    private void sendScoreboard() {

        String score =
                "\n===== SCOREBOARD =====\n"
                + "Player 1: " + player1Score + "\n"
                + "Player 2: " + player2Score + "\n"
                + "Ties: " + ties + "\n"
                + "======================";

        sendBoth(score);
    }

    // Send message to both players
    private void sendBoth(String msg) {
        player1.sendMessage(msg);
        player2.sendMessage(msg);
    }

    // RPS logic
    private int determineWinner(String p1, String p2) {

        if (p1.equals(p2)) {
            return 0;
        }

        if (
            (p1.equals("ROCK") && p2.equals("SCISSORS")) ||
            (p1.equals("PAPER") && p2.equals("ROCK")) ||
            (p1.equals("SCISSORS") && p2.equals("PAPER"))
        ) {
            return 1;
        }

        return 2;
    }
}