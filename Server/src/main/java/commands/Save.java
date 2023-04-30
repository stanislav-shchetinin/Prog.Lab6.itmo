package commands;

import base.Vehicle;
import lombok.extern.java.Log;
import service.CollectionClass;
import service.InitGlobalCollections;
import service.Parse;
import service.command.Command;
import service.command.NoArgument;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.PriorityQueue;

import static service.Validate.writeCheckFile;
/**
 * Класс команды: remove_first<p>
 * Реализует класс Command, чтобы можно было вызывать выполнение команды<p>
 * Реализует маркировочный интерфейс NoArgument, чтобы можно было проверить какие аргументы принимает команда (без аргументов)<p>
 * Аннотация @Log создает поле логгера
 * */
@Log
public class Save implements Command, NoArgument {

    private CollectionClass collectionClass;
    private final String HEAD = getHead(Vehicle.class);
    private File file = null;
    public Save(CollectionClass collectionClass, File file){
        this.collectionClass = collectionClass;
        this.file = file;
    }
    /**
     * Пустой конструктор нужен для создания пустых объектов в списках команд
     * */
    public Save(){}

    /**
     * Метод для генерации заголовка csv файла по полям класса clazz
     * primitiveTypes - список типов данных на которых нужно остановиться спускаться в рекурсию и вернуть в строку
     * */
    private String getHead(Class clazz){
        HashSet<Class> primitiveTypes = InitGlobalCollections.primitiveTypes();
        String ans = "";
        for (Field field : clazz.getDeclaredFields()){
            ans += String.format("%s", getStringFields(field, primitiveTypes));
        }
        return String.format("%s\n", ans.substring(0, ans.length() - 1)); //убрал последнюю запятую
    }
    /**
     * Метод спуска в рекурсию по полям для метода getHead
     * */
    private String getStringFields(Field field, HashSet<Class> primitiveTypes){
        String ans = "";

        if (primitiveTypes.contains(field.getType())){
            return String.format("%s,", field.getName());
        }
        for (Field simpleField : field.getType().getDeclaredFields()){
            ans += String.format("%s", getStringFields(simpleField, primitiveTypes));
        }

        return ans;
    }

    @Override
    public String description() {
        return "save : сохранить коллекцию в файл";
    }

    @Override
    public String name() {
        return "save";
    }

    /**
     * В методе реализована сериализация данных и запись их в файл
     * */
    @Override
    public void execute() {
        try (FileOutputStream fos = new FileOutputStream(writeCheckFile(file));
             BufferedOutputStream bos = new BufferedOutputStream(fos)) { //Создание потоков для записи в файл и сериализации
            byte[] bufferHead = HEAD.getBytes(); //Превращение заголовка в список байтов
            bos.write(bufferHead, 0, bufferHead.length); //массив байтов, смещение от начала, количество байт для записи
            String queueString = Parse.queueToString(new PriorityQueue<>(collectionClass.getCollection())); //перевод в строку очереди
            byte[] buffer = queueString.getBytes();
            bos.write(buffer, 0, buffer.length);
        } catch (IOException e) {
            log.warning(String.format("Проблемы с файлом %s", file.toString()));
        }
    }
    @Override
    public void setCollection(CollectionClass collectionClass) {
        this.collectionClass = collectionClass;
    }
}
