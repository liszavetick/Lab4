import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int serverPort = 12345;
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                double x, y;
                while (true) {
                    System.out.print("Enter x value (or 'exit' to exit): ");
                    String xInput = userInput.readLine();

                    if (xInput.equalsIgnoreCase("exit")) {
                        System.out.println("Program finished. Thank you)");
                        return;
                    }

                    try {
                        x = Double.parseDouble(xInput);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid format. Please enter a number for x");
                    }
                }
                while (true) {
                    System.out.print("Enter y value: ");
                    String yInput = userInput.readLine();
                    try {
                        y = Double.parseDouble(yInput);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid format. Please enter a number for y.");
                    }
                }
                String data = x + " " + y;
                byte[] sendData = data.getBytes();
                InetAddress serverHost = InetAddress.getByName(serverAddress);
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverHost, serverPort);
                socket.send(sendPacket);

                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                String serverResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Server responded: " + serverResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
