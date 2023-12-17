import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int serverPort = 12345;

        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            boolean continueExecution = true;

            while (continueExecution) {
                boolean validInput = false;
                int N = 0;

                while (!validInput) {
                    System.out.print("Enter an integer positive number N (or type 'exit' to quit): ");
                    String inputLine = userInput.readLine();

                    if (inputLine.equalsIgnoreCase("exit")) {
                        System.out.println("Exiting the program.");
                        out.println("exit");
                        continueExecution = false;
                        break;
                    }

                    try {
                        N = Integer.parseInt(inputLine);
                        if (N > 0) {
                            validInput = true;
                        } else {
                            System.out.println("Please, enter a positive integer number N: ");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Incorrect input format. Please enter an integer positive number N: ");
                    }
                }

                if (continueExecution) {
                    out.println(N);

                    String response;
                    while ((response = in.readLine()) != null) {
                        if (response.equals("completed")) {
                            System.out.println("Server completed sending values.");
                            break;
                        } else {
                            System.out.println("Received from server: " + response);
                        }
                    }

                    System.out.print("Do you want to continue? (Type 'exit' to quit or any other input to continue): ");
                    String continueInput = userInput.readLine();
                    if (continueInput.equalsIgnoreCase("exit")) {
                        System.out.println("Exiting the program.");
                        out.println("exit");
                        continueExecution = false;
                    }
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + serverAddress);
        } catch (IOException e) {
            System.err.println("Error input/output: " + e.getMessage());
        }
    }
}
