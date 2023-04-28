package client;

import lombok.extern.java.Log;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

@Log
public class Interaction {

    public static Socket Connection(String serverName, int port){
        Scanner in = new Scanner(System.in);
        while (true){
            try {
                System.out.println("Подключение к " + serverName + " на порт " + port);
                Socket client = new Socket(serverName, port);
                System.out.println("Подключается к " + client.getRemoteSocketAddress());
                return client;
            } catch (IOException e) {
                log.warning("Ошибка подключения к серверу");
                System.out.println("Введите другой порт");
                port = in.nextInt();
            }
        }


    }

}
