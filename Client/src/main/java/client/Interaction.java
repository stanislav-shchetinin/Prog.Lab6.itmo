package client;

import commands.Exit;
import commands.Save;
import exceptions.ReadValueException;
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

    private static Command setCommand() throws ReadValueException{
        try {
            return inputCommand();
        } catch (IllegalArgumentException e) {
            log.warning("Неверный параметр для команды");
            return null;
        }

    }

    public static void commandsToServer(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        try {
            while (true){
                Command command = setCommand();
                if (command instanceof Exit){
                    out.writeObject(new Save(new File("Server/files/file")));
                    out.flush();
                    String answer = (String) in.readObject();
                    command.execute();
                } else if (command != null){
                    out.writeObject(command);
                    out.flush();

                    String answer = (String) in.readObject();
                    System.out.println(answer);
                }

            }
        } catch (ReadValueException e){
            log.warning(e.getMessage());
        }

    }

}
