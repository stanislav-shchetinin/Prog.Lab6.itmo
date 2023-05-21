package service;

import base.VehicleType;
import commands.*;
import service.command.Command;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * Класс для инициализации вспомогательных коллекций
 * */
public class InitGlobalCollections {

    /**
     * Map для всех команд
     * */
    public static HashMap<String, Command> mapCommand(){
        List<Command> list = helpCommand();
        HashMap<String, Command> map = new HashMap<>();
        for (Command command : list){
            map.put(command.name(), command);
        }
        return map;
    }
    /**
     * Список всех команд
     * */
    public static ArrayList<Command> helpCommand(){
        return new ArrayList<>(List.of(
                new Help(), new Info(), new Show(), new AddElement(), new UpdateId(),
                new RemoveById(), new Clear(), ExecuteScript.getInstance(), new Exit(),
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
