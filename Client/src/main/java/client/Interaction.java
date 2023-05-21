package client;

import commands.ExecuteScript;

import commands.Exit;
import exceptions.ReadValueException;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import service.command.Command;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static console.Console.inputCommand;

@Log
public class Interaction {

    @SneakyThrows //sleep выбрасывает InterruptedException (не обрабатывается, т.к. в данном случае выброшено не будет)
    public static Socket connection(String serverName, int port){
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

    public static int inputPort(Scanner scanner){
        System.out.println("Введите порт");
        return scanner.nextInt(); //Обработать произвольный ввод
    }

    public static void workWithServer(Socket client, File file, Scanner scanner){
        try (OutputStream outputStream = client.getOutputStream();
             InputStream inputStream = client.getInputStream();
             ObjectOutputStream out = new ObjectOutputStream(outputStream);
             ObjectInputStream in = new ObjectInputStream(inputStream)) {

            Interaction.fileToServer(out, file);
            Interaction.commandsToServer(out, in, scanner);


        } catch (IOException | ClassNotFoundException e) {
            log.warning(e.getMessage());
        }
    }

    public static void fileToServer(ObjectOutputStream out, File file) throws IOException {
        out.writeObject(file);
        out.flush();
    }
    public static void commandsToServer(ObjectOutputStream out, ObjectInputStream in, Scanner scanner)
            throws ClassNotFoundException, IOException {
        ExecuteScript.getInstance().setIn(in);
        ExecuteScript.getInstance().setOut(out);
        try {
            while (true){
                try {
                    Command command = inputCommand(scanner);
                    processingCommand(command, out, in);
                } catch (ReadValueException | IllegalArgumentException e){
                    log.warning(e.getMessage());
                }
            }
        } catch (NoSuchElementException e){
            log.warning("Не введены значения");
        }

    }

    private static void processingCommand(Command command, ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException{
        String NAME_SAVE_FILE_ON_SERVER = "Server/files/file";
        if (command instanceof Exit){
            command.execute();
        } else if (command instanceof ExecuteScript){
            List<Command> list = ((ExecuteScript) command).getCommandsFromScript();
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
