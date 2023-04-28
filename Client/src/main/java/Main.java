import client.Interaction;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static console.Console.*;

/**
 * Это главный класс с методом main
*/
public class Main {

    /**
     * Метод main - стартовая точка проекта
     */

    public static final String serverName = "localhost";
    public static final int port = 4782;
    public static void main(String[] args) {

        File file = getFile(new Scanner(System.in)); //NAME_FILE
        Socket client = Interaction.Connection(serverName, port);
        try {
            byte[] bytes = new byte[16 * 1024];
            InputStream in = new FileInputStream(file);
            OutputStream out = client.getOutputStream();
            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }
            out.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}   