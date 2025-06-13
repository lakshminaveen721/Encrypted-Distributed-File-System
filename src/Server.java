import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 5000;
    private static final int MAX_CLIENTS = 10;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(MAX_CLIENTS);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(new ClientHandler(clientSocket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream())
            ) {
                String command = in.readUTF();
                String filename = in.readUTF();

                switch (command.toUpperCase()) {
                    case "UPLOAD":
                        int length = in.readInt();
                        byte[] fileBytes = new byte[length];
                        in.readFully(fileBytes);
                        FileHandler.saveEncryptedFile(filename, fileBytes);
                        out.writeUTF("Upload successful");
                        break;

                    case "DOWNLOAD":
                        if (FileHandler.fileExists(filename)) {
                            byte[] data = FileHandler.readDecryptedFile(filename);
                            out.writeInt(data.length);
                            out.write(data);
                        } else {
                            out.writeInt(0);
                            out.writeUTF("File not found");
                        }
                        break;

                    case "UPDATE":
                        int updateLen = in.readInt();
                        byte[] updateData = new byte[updateLen];
                        in.readFully(updateData);
                        FileHandler.updateFile(filename, updateData);
                        out.writeUTF("Update successful");
                        break;

                    case "DELETE":
                        FileHandler.deleteFile(filename);
                        out.writeUTF("Delete successful");
                        break;

                    default:
                        out.writeUTF("Unknown command");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}