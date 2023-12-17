import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

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
                        if (inputLine.equalsIgnoreCase("exit")) {
                            System.out.println("Client requested exit.");
                            break;
                        }
                        try {
                            int N = Integer.parseInt(inputLine);
                            if (N > 0) {
                                ArrayList<Integer> numbers = new ArrayList<>();
                                for (int i = 1; i <= N; i++) {
                                    numbers.add(i);
                                }
                                Random random = new Random();
                                for (int i = 0; i < N; i++) {
                                    int randomIndex = random.nextInt(numbers.size());
                                    int randomNumber = numbers.get(randomIndex);
                                    numbers.remove(randomIndex);
                                    out.println(randomNumber);
                                }
                                out.println("completed");
                            } else {
                                out.println("Please, enter an integer positive number N.");
                            }
                        } catch (NumberFormatException e) {
                            out.println("Incorrect input format. Please enter an integer positive number N.");
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error input/output: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error creating server socket: " + e.getMessage());
        }
    }
}
