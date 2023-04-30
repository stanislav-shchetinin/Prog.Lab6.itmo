package server;

import lombok.extern.java.Log;
import service.CollectionClass;
import service.command.Command;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

@Log
public class Interaction {
    public static Socket Connection(String serverName, int port){
        Scanner in = new Scanner(System.in);
        while (true){
            try {
                System.out.println("Подключение к серверу");
                ServerSocket serverSocket = serverSocket = new ServerSocket(port);
                Socket server = serverSocket.accept();
                return server;
            } catch (IOException ex) {
                log.warning("Ошибка при подключении сервера");
                System.out.println("Введите другой порт");
                port = in.nextInt();
            }
        }
    }

    public static void outputFile(File file){
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
    }

    public static void executeCommands(CollectionClass collectionClass, ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        while (true){
            try {
                Command command = (Command) in.readObject();
                command.setCollection(collectionClass);
                command.execute(out);
                command.clearFields();
            } catch (NullPointerException e){
                log.warning(e.getMessage());
            }
        }
    }

}
