
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientGUI {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private JTextField ipAddressField, portField, mField, nField;
    private JButton connectButton, calculateButton;
    private JTextArea resultArea;
    private JPanel panel1;

    public ClientGUI() {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel connectionPanel = new JPanel();
        ipAddressField = new JTextField("127.0.0.1", 15);
        portField = new JTextField("12345", 5);
        connectButton = new JButton("Connect");
        connectionPanel.add(connectButton);
        connectionPanel.add(new JLabel("IP Address:"));
        connectionPanel.add(ipAddressField);
        connectionPanel.add(new JLabel("Port:"));
        connectionPanel.add(portField);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        mField = new JTextField(5);
        nField = new JTextField(5);
        calculateButton = new JButton("Calculate");
        inputPanel.add(calculateButton);
        inputPanel.add(new JLabel("m:"));
        inputPanel.add(mField);
        inputPanel.add(new JLabel("n:"));
        inputPanel.add(nField);

        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        frame.add(connectionPanel, BorderLayout.NORTH);
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculate();
            }
        });
    }

    private void connectToServer() {
        try {
            String serverAddress = ipAddressField.getText();
            int serverPort = Integer.parseInt(portField.getText());

            socket = new Socket(serverAddress, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            resultArea.setText("Connected to the server.");
        } catch (IOException e) {
            resultArea.setText("Connection failed: " + e.getMessage());
        }
    }

    private void calculate() {
        try {
            int m = Integer.parseInt(mField.getText());
            int n = Integer.parseInt(nField.getText());

            if (out != null) {
                out.println(m);
                out.println(n);
                String response = in.readLine();
                resultArea.append("\nResult: " + response);
            } else {
                resultArea.append("\nNot connected to the server. Please connect first.");
            }
        } catch (NumberFormatException e) {
            resultArea.append("\nInvalid input. Please enter valid numbers.");
        } catch (IOException e) {
            resultArea.append("\nError: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientGUI();
            }
        });
    }
}
