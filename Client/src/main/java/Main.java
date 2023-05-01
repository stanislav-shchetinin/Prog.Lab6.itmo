import base.Vehicle;
import client.Interaction;
import lombok.extern.java.Log;
import service.command.Command;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

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
    public static final int port = 4782;
    public static void main(String[] args) {

        Socket client = Interaction.Connection(serverName, port);
        File file = getFile(new Scanner(System.in)); //NAME_FILE
        Interaction.workWithServer(client, file);

    }
}   