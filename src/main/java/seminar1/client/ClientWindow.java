package seminar1.client;

import org.jetbrains.annotations.NotNull;
import seminar1.server.ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientWindow extends JFrame {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private final ServerWindow serverWindow;

    private final JButton btnSend = new JButton("Send");
    private final JButton btnLogin = new JButton("Login");
    private final JTextField ipAddress = new JTextField();
    private final JTextField port = new JTextField();
    private final JTextField clientName = new JTextField();
    private final JTextField password = new JTextField();
    private final JTextArea messageDisplayField = new JTextArea();
    private final JTextField messageField = new JTextField();
    private boolean isLogin = false;
    private static int count = 0;



    public ClientWindow(@NotNull ServerWindow serverWindow){
        this.serverWindow = serverWindow;

        createClientWindow();

        btnLogin.addActionListener(_ -> logIn());

        btnSend.addActionListener(_ -> sendMessage());

        messageField.addActionListener(_ -> sendMessage());

        count++;
        serverWindow.getClients().add(this);
    }

    public JTextArea getMessageDisplayField(){
        return messageDisplayField;
    }

    private void createClientWindow(){
        setBounds(100, 100, WIDTH, HEIGHT);
        add(createServerInfoField(), BorderLayout.NORTH);
        add(createMessageDisplayField(), BorderLayout.CENTER);
        add(createMessageField(), BorderLayout.SOUTH);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void logIn() {
        if (serverWindow.getIsServerWorking()){
            isLogin = true;
            try {
                messageDisplayField.setText(getMessageHistory(setConnection(serverWindow)) + "\n");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }else{
            messageDisplayField.setText("Server is DOWN.\n");
            isLogin = false;
        }
    }

    private void sendMessage() {

        if (setConnection(serverWindow)){
            if (serverWindow.getIsServerWorking() && !clientName.getText().isEmpty() && isLogin){
                if (!messageField.getText().isEmpty()){
                    try {
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String formattedTime = now.format(formatter);
                        serverWindow.updateChat(formattedTime + " " + clientName.getText() + ": " + messageField.getText() + "\n");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    messageField.setText("");
                    try {
                        serverWindow.saveMessageHistory();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }else if(!serverWindow.getIsServerWorking() && !clientName.getText().isEmpty()){
                messageDisplayField.append("Server is DOWN.\n");
                isLogin = false;
            }else if (serverWindow.getIsServerWorking() && !clientName.getText().isEmpty() && !isLogin){
                messageDisplayField.append("You are not login.\n");
            }
        }
        else{
            messageDisplayField.append("Server is DOWN.\n");
            isLogin = false;
        }
    }

    private String getMessageHistory(boolean connection) throws IOException {
        if (connection) {
            if (!serverWindow.getMessageHistory().isEmpty()){
                return serverWindow.getMessageHistory();
            }
        }
        return "Connection is successful\n";
    }

    private boolean setConnection(@NotNull ServerWindow serverWindow){
        String connection = this.ipAddress.getText() +
                ":" +
                this.port.getText() +
                ":" +
                this.clientName.getText() +
                ":" +
                this.password.getText();
        return serverWindow.isConnected(connection);
    }

    @NotNull
    private Component createServerInfoField(){
        JPanel serverInfoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        ipAddress.setText("127.0.1.1");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        serverInfoPanel.add(ipAddress, gbc);
        port.setText("8080");
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        serverInfoPanel.add(port, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        clientName.setText(getClientNameForChat());
        serverInfoPanel.add(clientName, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        password.setText(getClientPasswordForChat());
        serverInfoPanel.add(password, gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        serverInfoPanel.add(btnLogin, gbc);
        return serverInfoPanel;
    }

    @NotNull
    private Component createMessageField() {
        JPanel messagePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.85;
        messagePanel.add(messageField, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.15;
        messagePanel.add(btnSend, gbc);
        return messagePanel;
    }

    @NotNull
    private Component createMessageDisplayField() {
        JScrollPane messageScroll = new JScrollPane(messageDisplayField);
        JPanel messages = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        messageDisplayField.setEditable(false);
        messages.add(messageScroll, gbc);
        return messages;
    }

    @NotNull
    private String getClientNameForChat(){
        if (count == 0)
            return "Michael";
        return "Vlad";
    }

    @NotNull
    private String getClientPasswordForChat(){
        if (count == 0)
            return "12345678";
        return "87654321";
    }

}
