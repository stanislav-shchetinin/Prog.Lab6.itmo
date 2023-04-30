import base.Vehicle;
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

        Socket client = Interaction.Connection(serverName, port);

        File file = getFile(new Scanner(System.in)); //NAME_FILE

        try (OutputStream outputStream = client.getOutputStream();
        InputStream inputStream = client.getInputStream()) {

            //Interaction.fileToServer(file, client, outputStream);
            Thread.sleep(1000);
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            Vehicle vehicle = inputVehicle();
            out.writeObject(vehicle);
            out.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}   