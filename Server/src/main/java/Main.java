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
    public static int port = 4782;
    public static void main(String[] args) {

        ServerSocket serverSocket = Interaction.connection(port);
        System.out.println(String.format("Порт для подключения клиентов %d", serverSocket.getLocalPort()));
        Interaction.workWithClient(serverSocket);
    }
}   