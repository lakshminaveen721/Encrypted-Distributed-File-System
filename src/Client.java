import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String SERVER = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER, PORT);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to the server.");
            System.out.print("Enter command (UPLOAD, DOWNLOAD, UPDATE, DELETE): ");
            String command = scanner.nextLine().toUpperCase();

            System.out.print("Enter file name: ");
            String filename = scanner.nextLine();

            out.writeUTF(command);
            out.writeUTF(filename);

            switch (command) {
                case "UPLOAD":
                case "UPDATE":
                    System.out.print("Enter path to the local file: ");
                    String path = scanner.nextLine();
                    byte[] content = readFileBytes(path);
                    out.writeInt(content.length);
                    out.write(content);
                    System.out.println("Server: " + in.readUTF());
                    break;

                case "DOWNLOAD":
                    int length = in.readInt();
                    if (length > 0) {
                        byte[] receivedData = new byte[length];
                        in.readFully(receivedData);
                        System.out.print("Enter destination file path to save: ");
                        String destPath = scanner.nextLine();
                        writeFileBytes(destPath, receivedData);
                        System.out.println("File downloaded and saved.");
                    } else {
                        System.out.println("Server: " + in.readUTF());
                    }
                    break;

                case "DELETE":
                    System.out.println("Server: " + in.readUTF());
                    break;

                default:
                    System.out.println("Invalid command");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] readFileBytes(String filePath) throws IOException {
        File file = new File(filePath);
        return java.nio.file.Files.readAllBytes(file.toPath());
    }

    private static void writeFileBytes(String filePath, byte[] data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(data);
        }
    }
}