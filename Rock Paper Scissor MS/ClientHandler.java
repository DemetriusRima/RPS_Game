import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {

    private Socket socket;
    private Room room;

    private BufferedReader in;
    private PrintWriter out;

    private String name = "Player";

    public ClientHandler(Socket socket, Room room) {

        this.socket = socket;
        this.room = room;
    }

    @Override
    public void run() {

        try {

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(
                    socket.getOutputStream(),
                    true);

            out.println("Enter your name:");

            String n = in.readLine();

            if (n != null && !n.trim().isEmpty()) {
                name = n.trim();
            }

            out.println("Welcome " + name);

            room.addClient(this);

        } catch (Exception e) {
            System.out.println(name + " error");
        }
    }

    public String getClientName() {
        return name;
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public String readMessage() {
        try {
            return in.readLine();
        } catch (Exception e) {
            return null;
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (Exception ignored) {}
    }
}