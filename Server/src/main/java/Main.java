import base.Coordinates;
import base.Vehicle;
import base.VehicleType;
import commands.ExecuteScript;
import commands.Save;
import console.Console;

import lombok.extern.java.Log;
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
    public static void main(String[] args) {

        String serverName = "localhost";
        int port = 4782;

        CollectionClass collectionClass = new CollectionClass(); //Менеджер коллекции

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            System.out.println("Can't setup server on this port number. ");
        }

        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;

        try {
            socket = serverSocket.accept();
        } catch (IOException ex) {
            System.out.println("Can't accept client connection. ");
        }

        try {
            in = socket.getInputStream();
        } catch (IOException ex) {
            System.out.println("Can't get socket input stream. ");
        }

        try {
            out = new FileOutputStream("Server/files/file");
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. ");
        }

        byte[] bytes = new byte[16*1024];

        try {

            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        File file = new File("Server/files/file");

        try {
            BufferedReader fin = new BufferedReader(new FileReader(file));
            String line;
            while ((line = fin.readLine()) != null) System.out.println(line);
            fin.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fromFileVehicle(collectionClass, new Scanner(parseFromCSVtoString(file))); //Считывание файла и запись его в collectionClass

    }
}   