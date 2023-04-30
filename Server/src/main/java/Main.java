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
        InputStream inputStream = server.getInputStream()){

            //File file = Interaction.fileFromClient("Server/files/file", server, inputStream);
            //Interaction.outputFile(file);

            Thread.sleep(1000);

            ObjectInputStream in = new ObjectInputStream(inputStream);
            Vehicle vehicle = (Vehicle) in.readObject();

            System.out.println(vehicle);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //fromFileVehicle(collectionClass, new Scanner(parseFromCSVtoString(file))); //Считывание файла и запись его в collectionClass

    }
}   