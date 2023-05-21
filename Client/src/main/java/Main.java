import base.Vehicle;
import client.Interaction;
import lombok.extern.java.Log;
import service.command.Command;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static client.Interaction.inputPort;
import static console.Console.*;

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
        Scanner scanner = new Scanner(System.in);
        port = inputPort(scanner);
        Socket client = Interaction.connection(serverName, port);
        File file = getFile(scanner); //NAME_FILE
        Interaction.workWithServer(client, file, scanner);
    }
}   