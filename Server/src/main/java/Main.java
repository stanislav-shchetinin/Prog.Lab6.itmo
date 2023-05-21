import commands.ExecuteScript;
import commands.Save;

import lombok.extern.java.Log;
import server.Interaction;

import java.net.ServerSocket;

/**
 * Это главный класс с методом main
*/
@Log
public class Main {

    /**
     * Метод main - стартовая точка проекта
     */
     public static final String serverName = "localhost";
    public static int port = 4782;
    public static void main(String[] args) {

        ServerSocket serverSocket = Interaction.connection(port);
        System.out.println(String.format("Порт для подключения клиентов %d", serverSocket.getLocalPort()));
        Interaction.workWithClient(serverSocket);
    }
}   