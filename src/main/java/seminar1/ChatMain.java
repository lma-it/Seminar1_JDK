package seminar1;

import seminar1.client.ClientWindow;
import seminar1.server.ServerWindow;

public class ChatMain {
    public static void main(String[] args)  {
        ServerWindow serverWindow = new ServerWindow();
        new ClientWindow(serverWindow);
        new ClientWindow(serverWindow);
    }
}
