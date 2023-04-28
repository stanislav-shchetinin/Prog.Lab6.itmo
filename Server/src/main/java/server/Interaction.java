package server;

import lombok.extern.java.Log;

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
    public static File fileFromClient(String pathFile, Socket server){
        try {
            InputStream in = server.getInputStream();
            OutputStream out = new FileOutputStream(pathFile);
            byte[] bytes = new byte[16*1024];
            try {

                int count;
                while ((count = in.read(bytes)) > 0) {
                    out.write(bytes, 0, count);
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            File file = new File(pathFile);
            return file;
        } catch (IOException e){
            log.warning("Ошибка при передаче файла");
            return null;
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

}
