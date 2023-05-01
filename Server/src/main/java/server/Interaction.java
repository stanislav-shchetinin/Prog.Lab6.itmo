package server;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import service.CollectionClass;
import service.command.Command;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static service.FileRead.fromFileVehicle;
import static service.Parse.parseFromCSVtoString;

@Log
public class Interaction {
    @SneakyThrows
    public static ServerSocket Connection(int port){
        while (true){
            try {
                System.out.println("Подключение к серверу");
                return new ServerSocket(port);
            } catch (IOException ex) {
                log.warning("Ошибка при подключении сервера");
                System.out.println("Переподключение...");
                sleep();
            }
        }
    }

    private static void sleep(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.warning(e.getMessage());
        }
    }

    private static void mainTaskServer(Socket server, CollectionClass collectionClass){
        try (OutputStream outputStream = server.getOutputStream();
             InputStream inputStream = server.getInputStream();
             ObjectInputStream in = new ObjectInputStream(inputStream);
             ObjectOutputStream out = new ObjectOutputStream(outputStream)){

            File file = (File) in.readObject();

            fromFileVehicle(collectionClass, new Scanner(parseFromCSVtoString(file))); //Считывание файла и запись его в collectionClass
            Interaction.executeCommands(collectionClass, in, out);

        } catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            log.warning(e.getMessage());
        }
    }
    public static void workWithClient(ServerSocket serverSocket){
        Socket server = null;
        while (true){
            try {
                System.out.println("Создание клиента на сервере");
                server = serverSocket.accept();
                CollectionClass collectionClass = new CollectionClass(); //Менеджер коллекции
                mainTaskServer(server, collectionClass);

            } catch (IOException ex) {
                log.warning("Ошибка при создании клиента");
                System.out.println("Переподключение...");
                sleep();
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
