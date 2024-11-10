package seminar1homework;

import seminar1homework.client.ClientWindow;
import seminar1homework.server.ServerWindow;

public class ChatMain {
    public static void main(String[] args)  {
        ServerWindow serverWindow = new ServerWindow();
        new ClientWindow(serverWindow);
        new ClientWindow(serverWindow);
    }
}
