import base.Coordinates;
import base.Vehicle;
import base.VehicleType;
import commands.ExecuteScript;
import commands.Save;
import console.Console;

import lombok.extern.java.Log;
import server.Interaction;
import service.CollectionClass;
import service.FileRead;
import service.command.Command;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Scanner;

import static console.Console.*;
import static service.FileRead.fromFileVehicle;
import static service.Parse.parseFromCSVtoString;

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

        CollectionClass collectionClass = new CollectionClass(); //Менеджер коллекции
        Socket server = Interaction.Connection(serverName, port);

        try (OutputStream outputStream = server.getOutputStream();
             InputStream inputStream = server.getInputStream();
             ObjectInputStream in = new ObjectInputStream(inputStream);
             ObjectOutputStream out = new ObjectOutputStream(outputStream)){

            File file = (File) in.readObject();

            fromFileVehicle(collectionClass, new Scanner(parseFromCSVtoString(file))); //Считывание файла и запись его в collectionClass
            Interaction.executeCommands(collectionClass, in, out);

        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            log.warning(e.getMessage());
        }

    }
}   