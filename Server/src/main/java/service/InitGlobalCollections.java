package service;

import base.VehicleType;
import commands.*;
import service.command.Command;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Класс для инициализации вспомогательных коллекций
 * */
public class InitGlobalCollections {

    /**
     * Map для всех команд
     * */
    public static HashMap<String, Command> mapCommand(CollectionClass collectionClass, File file){
        HashMap<String, Command> map = new HashMap<>();
        map.put("help", new Help());
        map.put("info", new Info(collectionClass));
        map.put("show", new Show(collectionClass));
        map.put("add", new AddElement(collectionClass));
        map.put("update_id", new UpdateId(collectionClass));
        map.put("remove_by_id", new RemoveById(collectionClass));
        map.put("clear", new Clear(collectionClass));
        map.put("save", new Save(collectionClass, file));
        map.put("execute_script", ExecuteScript.getInstance(collectionClass, file));
        map.put("exit", new Exit());
        map.put("remove_first", new RemoveFirst(collectionClass));
        map.put("add_if_max", new AddIfMax(collectionClass));
        map.put("add_if_min", new AddIfMin(collectionClass));
        map.put("count_by_capacity", new CountByCapacity(collectionClass));
        map.put("print_ascending", new PrintAscending(collectionClass));
        map.put("print_unique_engine_power", new PrintUniqueEnginePower(collectionClass));
        return map;
    }
    /**
     * Список всех команд
     * */
    public static ArrayList<Command> helpCommand(){
        return new ArrayList<>(List.of(
                new Help(), new Info(), new Show(), new AddElement(), new UpdateId(),
                new RemoveById(), new Clear(), new Save(), new ExecuteScript(), new Exit(),
                new RemoveFirst(), new AddIfMax(), new AddIfMin(), new CountByCapacity(), new PrintAscending(),
                new PrintUniqueEnginePower()
        ));
    }
    /**
     * Set для поле Vehicle, который пользователь не должен вводить из консоли (или которые не получаются из execute_script)
     * */
    public static HashSet<String> setNoInputTypes (NoInputTypes[] args){
        HashSet<String> res = new HashSet<>();
        for (NoInputTypes x : args){
            res.add(x.getName());
        }
        return res;
    }
    /**
     * Set для примитивных типов данных - типы данных, на которых останавливаем рекурсию
     * */
    public static HashSet<Class> primitiveTypes(){
        return new HashSet<>(List.of(
                Double.class, Float.class, double.class, UUID.class, String.class, ZonedDateTime.class, Long.class, VehicleType.class,
                float.class
        ));
    }

}
