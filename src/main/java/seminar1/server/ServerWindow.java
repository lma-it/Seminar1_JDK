package seminar1.server;

import org.jetbrains.annotations.NotNull;
import seminar1.client.ClientWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ServerWindow extends JFrame {
    private static final int POS_X = 500;
    private static final int POS_Y = 550;
    private static final int WIDTH = 408;
    private static final int HEIGHT = 380;
    private static final String SERVER = "Server is";
    private static final String IP_ADDRESS = "127.0.1.1";
    private static final String PORT = "8080";

    private final JTextArea log = new JTextArea();
    private boolean isServerWorking;
    private static String logs;
    private final StringBuilder messageLog = new StringBuilder();
    private final List<ClientWindow> clients = new ArrayList<>();
    private static int count = 0;



    public ServerWindow() {

        JButton btnStart = new JButton("Start");
        btnStart.addActionListener(_ -> btnStart());

        JButton btnStop = new JButton("Stop");
        btnStop.addActionListener(_ -> btnStop());

        createServerWindow(btnStart, btnStop);

        setVisible(true);

    }

    public List<ClientWindow> getClients(){
        return clients;
    }

    private void createServerWindow(JButton @NotNull ... buttons){

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    saveMessageHistory();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        isServerWorking = false;

        JScrollPane serverPanel = new JScrollPane(log);
        JPanel serverButtonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat Server");
        setAlwaysOnTop(true);
        log.setEditable(false);
        add(serverPanel);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        serverButtonsPanel.add(buttons[0], gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        serverButtonsPanel.add(buttons[1], gbc);
        add(serverButtonsPanel, BorderLayout.SOUTH);
    }

    private void btnStart() {
        isServerWorking = true;
        logs = SERVER + " started: " + isServerWorking + "\n";
        log.append(logs);
    }

    private void btnStop() {
        isServerWorking = false;
        logs = SERVER + " stopped: " + isServerWorking + "\n";
        log.append(logs);
    }

    public boolean getIsServerWorking(){
        return isServerWorking;
    }

    public void updateChat(String message) throws IOException {
        for (ClientWindow client : getClients()){
            client.getMessageDisplayField().append(message);
        }
        messageLog.append(message);
    }

    public void saveMessageHistory() throws IOException {
        String path = String.format("src/main/java/seminar1/chats/historyOfChat%s", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(String.valueOf(messageLog));
        writer.close();
    }

    public String getMessageHistory() throws IOException {
        StringBuilder sb = new StringBuilder();
        String path = String.format("src/main/java/seminar1/chats/historyOfChat%s", ".txt");

        if(new File(path).exists()){
            try{
                BufferedReader reader = new BufferedReader(new FileReader(path));
                String line;
                while((line = reader.readLine()) != null){
                    sb.append(line).append("\n");
                    if(!messageLog.toString().contains(line))
                        messageLog.append(line).append("\n");
                }
            }catch (RuntimeException e){
                System.out.printf(e.getMessage());
            }
        }
        return sb.toString();
    }

    public boolean isConnected(@NotNull String connection){
        int ip = 0;
        int port = 1;
        int name = 2;
        int password = 3;

        String[] connectAttributes = connection.split(":");
        if (connectAttributes.length != 0){
            if (connectAttributes[ip] != null && connectAttributes[port] != null){
                if (connectAttributes[ip].equals(IP_ADDRESS) && connectAttributes[port].equals(PORT)) {
                    if (count < clients.size()){
                        log.append(connectAttributes[name] + " is connected.\n");
                        count++;
                    }
                    return true;
                }
            }
        }
        return false;
    }

}
