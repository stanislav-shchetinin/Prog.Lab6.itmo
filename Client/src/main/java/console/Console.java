package console;

import base.Vehicle;
import exceptions.ReadValueException;

import lombok.extern.java.Log;
import service.CollectionClass;
import service.NoInputTypes;
import service.command.Command;
import service.InitGlobalCollections;
import service.command.ElementArgument;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

import static service.InitGlobalCollections.setNoInputTypes;
import static service.Parse.formatInput;
import static service.Validate.*;
/**
 * Класс, в котором реализовано взаимодействие с пользователем (ввод данных из консоли)
 * <p>
 * Аннотация @Log создает поле логгера
 * */
@Log
public class Console {
    /**
     * Метод получения файла из консоли (из имени переменной окружения)
     * */
    public static File getFile(Scanner in){

        String nameFile = "";
        Map<String, String> mapEnv = System.getenv(); //получение всех переменных окружения

        while (true){
            try {
                System.out.print("\nВведите имя переменной среды:\n");
                String nameVar = null;
                if (in.hasNext())
                    nameVar = in.nextLine().trim();
                if (mapEnv.containsKey(nameVar)){
                    nameFile = mapEnv.get(nameVar);
                    File file = readCheckFile(new File(nameFile));
                    log.info("Файл успешно получен");
                    return file;
                } else {
                    System.out.print("Не существует такой переменной окружения");
                }
            } catch (FileNotFoundException e){
                log.warning(e.getMessage());
            } catch (NullPointerException e){
                log.warning("Не введены значения");
                System.exit(1); //1 - означает ошибку no line из-за которой произошло завершение
            }

        }
    }

    /**
     * Метод для ввода команд из консоли
     * <p>
     * <b>file</b> нужен для получения команды Save в mapCommand
     * */
    public static Command inputCommand() throws ReadValueException{
        HashMap<String, Command> mapCommand = InitGlobalCollections.mapCommand();
        Scanner in = new Scanner(System.in);
        try {
            String[] arrayString = in.nextLine().trim().split(" "); //разрез строки по пробелу и удаление крайних пробелов
            if (arrayString.length == 0){
                return null;
            }
            String nameCommand = arrayString[0];
            Command command = mapCommand.get(nameCommand);
            if (command == null){
                log.warning("Не существует команды с указанным названием");
                return null;
            }
            if (arrayString.length != 1){
                command.setParametr(arrayString[1]);
            }
            if (command instanceof ElementArgument) {
                Vehicle vehicle = inputVehicle();
                command.setElement(vehicle);
            }

            /*
            * команда отправляется на сервер
            * на сервере нужно всем командам установить коллекцию
            * */
            return command;
        } catch (NoSuchElementException e){
            throw new ReadValueException("Не введены значения");
        }

    }

    /**
     * Вспомогательный метод для ввода Vehicle: выдает сообщение пользователю с просьбой о вводе данных и устанавливает значения в поле
     * */
    private static void consoleVehicle (Scanner in, Vehicle vehicle, Field field) throws ReadValueException, IllegalAccessException {
        System.out.println(String.format("\n%sВведите %s: ", formatInput(field.getType()), field.getName()));
        String value = in.nextLine();
        field.set(vehicle, thisType(value, field));
    }
     /**
      * Ввод Vehicle из консоли
      * */
    public static Vehicle inputVehicle() {
        Scanner in = new Scanner(System.in);
        Vehicle vehicle = new Vehicle();
        HashSet<String> setNoInputTypes = setNoInputTypes(NoInputTypes.values());
        for (Field field : vehicle.getClass().getDeclaredFields()){
            if (setNoInputTypes.contains(field.getType().getSimpleName())){ //Если ID или DATA
                continue;
            }
            field.setAccessible(true);
            boolean isCorrectValue = false;
            while (!isCorrectValue){
                try {
                    isCorrectValue = true;
                    consoleVehicle(in, vehicle, field);
                } catch (IllegalArgumentException e) {
                    isCorrectValue = false;
                    log.warning(String.format("Неверный тип %s", field.getName()));
                } catch (IllegalAccessException e){
                    log.warning("Запись в поле запрещена");
                } catch (ReadValueException e){
                    isCorrectValue = false;
                    log.warning(e.getMessage());
                }
            }
        }
        return vehicle;
    }

}
