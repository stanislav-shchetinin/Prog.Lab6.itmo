package server;

import commands.Save;
import exceptions.ReadValueException;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import service.CollectionClass;
import service.command.Command;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Timer;

import static java.lang.Math.max;
import static service.FileRead.fromFileVehicle;
import static service.Parse.parseFromCSVtoString;

@Log
public class Interaction {
    @SneakyThrows
    public static ServerSocket connection(int port){
        while (true){
            try {
                System.out.println("Подключение к серверу");
                return new ServerSocket(port);
            } catch (IOException e) {
                log.warning("Ошибка при подключении сервера");
                System.out.println("Переподключение...");
                port = max((port + 1)%10000, 2000); //port увеличивается на 1 и находится между 2000 и 9999
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

    private static void mainTaskServer(Socket server, CollectionClass collectionClass) throws ReadValueException{
        try (OutputStream outputStream = server.getOutputStream();
             InputStream inputStream = server.getInputStream();
             ObjectInputStream in = new ObjectInputStream(inputStream);
             ObjectOutputStream out = new ObjectOutputStream(outputStream)){

            File file = (File) in.readObject();

            fromFileVehicle(collectionClass, new Scanner(parseFromCSVtoString(file))); //Считывание файла и запись его в collectionClass
            Interaction.executeCommands(collectionClass, in, out);

        } catch (IOException | IllegalArgumentException e) {
            log.warning(e.getMessage());
        } catch (ClassNotFoundException e){
            log.warning("Класс не найден");
        }
    }
    public static void workWithClient(ServerSocket serverSocket){

        CollectionClass collectionClass = new CollectionClass(); //Менеджер коллекции

        while (true){
            try (Socket server = serverSocket.accept()) {
                System.out.println("Создание клиента на сервере");

                mainTaskServer(server, collectionClass);
                specialCommand(collectionClass);

            } catch (IOException ex) {
                log.warning("Ошибка при создании клиента");
                System.out.println("Переподключение...");
                sleep();
            } catch (ReadValueException e){
                break;
            }
        }
    }

    public static void executeCommands(CollectionClass collectionClass, ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException, ReadValueException {
        while (true){
            Command command = (Command) in.readObject();
            command.setCollection(collectionClass);
            command.execute(out);
            command.clearFields();
        }
    }

    @SneakyThrows
    public static void specialCommand(CollectionClass collectionClass) throws IOException, ReadValueException {
        Scanner scanner = new Scanner(System.in);
        for (int numIteration = 0; numIteration < 10; ++numIteration){
            if (System.in.available() > 0){
                if (scanner.next().trim().equals("exit")){
                    throw new ReadValueException("exit");
                } else if (scanner.next().equals("save")){
                    System.out.println("Сохранение коллекции");
                    new Save(collectionClass, new File("Server/files/file")).execute();
                }
            }
            Thread.sleep(500);
        }
    }

}
