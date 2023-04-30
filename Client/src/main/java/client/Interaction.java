package client;

import lombok.extern.java.Log;
import service.command.Command;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static console.Console.inputCommand;

@Log
public class Interaction {

    public static Socket Connection(String serverName, int port){
        Scanner in = new Scanner(System.in);
        while (true){
            try {
                System.out.println(String.format("Подключение к %s на порт %d", serverName, port));
                Socket client = new Socket(serverName, port);
                System.out.println(String.format("Подключение к %s", client.getRemoteSocketAddress()));
                return client;
            } catch (IOException e) {
                log.warning("Ошибка подключения к серверу");
                System.out.println("Введите другой порт");
                port = in.nextInt();
            }
        }
    }

    public static void fileToServer(ObjectOutputStream out, File file) throws IOException {
        out.writeObject(file);
        out.flush();
    }

    public static void commandsToServer(ObjectOutputStream out) throws IOException {
        while (true){
            Command command = inputCommand();
            out.writeObject(command);
            out.flush();
            command.clearFields();
        }
    }

}