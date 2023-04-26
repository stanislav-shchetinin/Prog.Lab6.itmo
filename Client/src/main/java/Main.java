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
import java.net.Socket;
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

            File file = getFile(new Scanner(System.in)); //NAME_FILE
            /*try {
                Socket clientSocket = new Socket ("localhost",50001);
                InputStream is = clientSocket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String receivedData = br.readLine();
                System.out.println("Received Data: "+receivedData);
            }
            catch (Exception e) {
                e.printStackTrace();
            }*/
            Console.inputCommands(); //Ввод команд из консоли

    }
}   