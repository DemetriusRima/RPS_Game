import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {

    private Socket socket;
    private Room room;

    private BufferedReader in;
    private PrintWriter out;

    private String clientName = "Anonymous";

    public ClientHandler(Socket socket, Room room) {

        this.socket = socket;
        this.room = room;
    }

    @Override
    public void run() {

        try {

            // Input stream
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            // Output stream
            out = new PrintWriter(
                    socket.getOutputStream(),
                    true);
            // Ask for player name
            out.println("Enter your name:");

            String name = in.readLine();

            if (name != null
                    && !name.trim().isEmpty()) {

                clientName = name.trim();
            }

            out.println("Welcome " + clientName);
            out.println("Waiting for opponent...");

            // Add player to room
            room.addClient(this);

        } catch (IOException e) {

            System.out.println(
                    "Connection error with "
                            + clientName);

        } finally {

            closeConnection();
        }
    }

    // Send message to client
    public void sendMessage(String msg) {

        if (out != null) {
            out.println(msg);
        }
    }

    // Read message safely
    public String readMessage() {

        try {

            return in.readLine();

        } catch (IOException e) {

            System.out.println(
                    clientName
                            + " disconnected.");

            return null;
        }
    }

    // Get player name
    public String getClientName() {

        return clientName;
    }

    // Close socket safely
    public void closeConnection() {

        try {

            if (socket != null
                    && !socket.isClosed()) {

                socket.close();
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}