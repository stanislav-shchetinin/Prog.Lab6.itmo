import client.Interaction;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static console.Console.*;

/**
 * Это главный класс с методом main
*/
public class Main {

    /**
     * Метод main - стартовая точка проекта
     */

    public static final String serverName = "localhost";
    public static final int port = 4782;
    public static void main(String[] args) {

        File file = getFile(new Scanner(System.in)); //NAME_FILE
        Socket client = Interaction.Connection(serverName, port);
        Interaction.fileToServer(file, client);

    }
}   