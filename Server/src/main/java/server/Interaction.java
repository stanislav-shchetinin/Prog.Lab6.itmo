package server;

import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

@Log
public class Interaction {
    public static Socket Connection(String serverName, int port){
        Scanner in = new Scanner(System.in);
        while (true){
            try {
                System.out.println("Под");
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
}
