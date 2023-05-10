package client;

import commands.ExecuteScript;
import commands.Exit;
import commands.Save;
import exceptions.ReadValueException;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import service.command.Command;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import static console.Console.inputCommand;

@Log
public class Interaction {

    @SneakyThrows
    public static Socket Connection(String serverName, int port){
        while (true){
            try {
                System.out.println(String.format("Подключение к %s на порт %d", serverName, port));
                Socket client = new Socket(serverName, port);
                System.out.println(String.format("Подключение к %s", client.getRemoteSocketAddress()));
                return client;
            } catch (IOException e) {
                log.warning("Ошибка подключения к серверу");
                System.out.println("Переподключение...");
                Thread.sleep(3000);
            }
        }
    }

    public static void workWithServer(Socket client, File file){
        try (OutputStream outputStream = client.getOutputStream();
             InputStream inputStream = client.getInputStream();
             ObjectOutputStream out = new ObjectOutputStream(outputStream);
             ObjectInputStream in = new ObjectInputStream(inputStream)) {

            Interaction.fileToServer(out, file);
            Interaction.commandsToServer(out, in);


        } catch (IOException | ClassNotFoundException e) {
            log.warning(e.getMessage());
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
        ExecuteScript.getInstance().setIn(in);
        ExecuteScript.getInstance().setOut(out);
        try {
            while (true){
                Command command = setCommand();
                processingCommand(command, out, in);
            }
        } catch (ReadValueException e){
            log.warning(e.getMessage());
        }

    }

    private static void processingCommand(Command command, ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException{
        if (command instanceof Exit){
            out.writeObject(new Save(new File("Server/files/file")));
            out.flush();
            String answer = (String) in.readObject();
            command.execute();
        } else if (command instanceof ExecuteScript){
            List<Command> list = ((ExecuteScript) command).methodForScript();
            for (Command commandInES : list){
                processingCommand(commandInES, out, in);
            }
        } else if (command != null){
            out.writeObject(command);
            out.flush();
            String answer = (String) in.readObject();
            System.out.println(answer);
        }
    }

}
