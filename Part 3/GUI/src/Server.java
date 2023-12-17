import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server running and waiting for clients...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())) ) {

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        int m = Integer.parseInt(inputLine);
                        int n = Integer.parseInt(in.readLine());
                        long result = factorial(m) + factorial(n);
                        out.println(result);
                    }
                } catch (IOException e) {
                    System.err.println("Error input/output: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error creating server socket: " + e.getMessage());
        }
    }

    private static long factorial(int n) {
        if (n == 0) {
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }
}
