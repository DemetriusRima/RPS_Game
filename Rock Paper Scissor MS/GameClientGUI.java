import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class GameClientGUI extends JFrame {

    private Socket socket;

    private BufferedReader in;
    private PrintWriter out;

    private JTextArea chatArea;
    private JTextField inputField;

    private JButton sendButton;

    private JButton rockButton;
    private JButton paperButton;
    private JButton scissorsButton;

    private String playerName;

    public GameClientGUI() {

        // Player name
        playerName =
                JOptionPane.showInputDialog(
                        this,
                        "Enter your name:");

        if (playerName == null
                || playerName.trim().isEmpty()) {

            playerName = "Anonymous";
        }

        // Window
        setTitle(
                "Rock Paper Scissors");

        setSize(600, 500);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // Chat area
        chatArea = new JTextArea();

        chatArea.setEditable(false);

        JScrollPane scroll =
                new JScrollPane(chatArea);

        add(scroll, BorderLayout.CENTER);

        // Input
        JPanel bottom =
                new JPanel(new BorderLayout());

        inputField = new JTextField();

        sendButton =
                new JButton("Send");

        bottom.add(
                inputField,
                BorderLayout.CENTER);

        bottom.add(
                sendButton,
                BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);

        // Game buttons
        JPanel top =
                new JPanel(new GridLayout(1, 3));

        rockButton =
                new JButton("ROCK");

        paperButton =
                new JButton("PAPER");

        scissorsButton =
                new JButton("SCISSORS");

        top.add(rockButton);
        top.add(paperButton);
        top.add(scissorsButton);

        add(top, BorderLayout.NORTH);


        connectToServer();


        sendButton.addActionListener(
                e -> sendChat());

        inputField.addActionListener(
                e -> sendChat());

        rockButton.addActionListener(
                e -> sendMove("ROCK"));

        paperButton.addActionListener(
                e -> sendMove("PAPER"));

        scissorsButton.addActionListener(
                e -> sendMove("SCISSORS"));

        setVisible(true);
    }

    private void connectToServer() {

        try {

            socket =
                    new Socket(
                            "localhost",
                            3000);

            in =
                    new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));

            out =
                    new PrintWriter(
                            socket.getOutputStream(),
                            true);

            // Send player name
            out.println(playerName);

            appendMessage(
                    "Connected as "
                            + playerName);

            // Listen thread
            new ServerListener().start();

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not connect to server.");
        }
    }

    private void sendChat() {

        String msg =
                inputField.getText().trim();

        if (!msg.isEmpty()) {

            out.println(msg);

            inputField.setText("");
        }
    }

    private void sendMove(String move) {

        out.println(move);

        appendMessage(
                "You played: "
                        + move);
    }

    private void appendMessage(String msg) {

        chatArea.append(msg + "\n");
    }

    class ServerListener extends Thread {

        @Override
        public void run() {

            try {

                String message;

                while ((message =
                        in.readLine()) != null) {

                    appendMessage(message);
                }

            } catch (IOException e) {

                appendMessage(
                        "Disconnected from server.");
            }
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new GameClientGUI());
    }
}