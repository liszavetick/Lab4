import java.io.*;
import java.net.*;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class Server {
    public static void main(String[] args) {
        int serverPort = 12345;
        DatagramSocket socket = null;

        try {
            socket = new DatagramSocket(serverPort);
            System.out.println("Server running and waiting for requests...");

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);
                String clientMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("Received message from client: " + clientMessage);
                String[] parts = clientMessage.split(" ");
                if (parts.length != 2) {
                    System.out.println("Invalid message format. Two values were expected.");
                    continue;
                }

                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                double result = calculateFunction(x, y);
                byte[] sendData = Double.toString(result).getBytes();
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                socket.send(sendPacket);
                saveToLogFile(x, y, result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    private static double calculateFunction(double x, double y) {
        return 5 * atan(x) - 0.25 * cos((x + 3 * abs(x - y) + pow(x, 2)) /
                (pow(abs(x + pow(y, 2)), 3) + pow(x, 3)));
    }

    private static void saveToLogFile(double x, double y, double result) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("log.txt", true))) {
            writer.println("x = " + x + ", y = " + y + ", result = " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}