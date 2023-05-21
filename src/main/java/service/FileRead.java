package service;

import base.Coordinates;
import base.Vehicle;
import exceptions.ReadValueException;
import lombok.extern.java.Log;

import java.lang.reflect.Field;
import java.util.Scanner;

import static service.Validate.thisType;

/**
 * Класс для чтения из файла<p>
 * Аннотация @Log создает поле логгера
 * */
@Log
public class FileRead {
    /**
     * Метод для считывания из файла Vehicle
     * */
    public static void fromFileVehicle(CollectionClass collectionClass, Scanner in) {
        while (in.hasNext()){
            Vehicle vehicle = new Vehicle();
            boolean isCorrectVehicle = true;
            for (Field field : vehicle.getClass().getDeclaredFields()){
                field.setAccessible(true);
                if (in.hasNext()){
                    try {
                        String value = in.nextLine();
                        if (field.getType() == Coordinates.class && in.hasNext()){
                            value += " " + in.nextLine();
                            value.replaceAll(",", "."); //Если в Double запятые, а не точки
                        }
                        field.set(vehicle, thisType(value, field, collectionClass));
                    } catch (IllegalArgumentException e) {
                        isCorrectVehicle = false;
                        log.warning(String.format("Неверный тип %s", field.getName()));
                        break;
                    } catch (ReadValueException e) {
                        isCorrectVehicle = false;
                        log.warning(e.getMessage());
                    } catch (IllegalAccessException e) {
                        log.warning("Нет доступа к полю");
                    }
                }
            }
            if (isCorrectVehicle){
                collectionClass.add(vehicle);
            } else {
                break;
            }
        }
    }

}
