package service;

import base.Coordinates;
import base.VehicleType;
import exceptions.ReadValueException;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Класс, в котором прописана вся валидация данных
 * */
public class Validate {

    public static UUID uuidFromString(String value, CollectionClass collectionClass) throws IllegalArgumentException, ReadValueException {
        UUID uuid = UUID.fromString(value);
        if (collectionClass.getUuidHashSet().contains(uuid)){
            throw new ReadValueException("Передаваемый id не уникален");
        }
        return uuid;
    }
    public static Coordinates coordinatesFromString (String value) throws IllegalArgumentException, ReadValueException {
        String[] str = value.split(" ");
        if (str.length < 2){
            throw new ReadValueException("Неверный формат");
        }
        try {
            Coordinates coordinates = new Coordinates(Float.parseFloat(str[0]), Float.parseFloat(str[1]));
            if (coordinates.getY() > -762){
                throw new ReadValueException("Координата Y не может быть больше -762");
            }
            return coordinates;
        } catch (NumberFormatException e){
            throw new ReadValueException("Неверный тип числа");
        }

    }

    private static Double doubleFromString (String value, Field field) throws IllegalArgumentException, ReadValueException {
        try {
            Double ans = Double.parseDouble(value);
            if (field.getName().equals("enginePower") || field.getName().equals("distanceTravelled")){
                if (ans <= 0){
                    throw new ReadValueException(String.format("Значение поля %s должно быть больше 0", field.getName()));
                }
            }
            return ans;
        } catch (NumberFormatException e){
            throw new ReadValueException("Неверный тип числа");
        }

    }

    private static Long longFromString (String value, Field field) throws IllegalArgumentException, ReadValueException {
        try {
            Long ans = Long.parseLong(value);
            if (field.getName().equals("capacity")){
                if (ans <= 0){
                    throw new ReadValueException(String.format("Значение поля %s должно быть больше 0", field.getName()));
                }
            }
            return ans;
        } catch (NumberFormatException e){
            throw new ReadValueException("Неверный тип числа");
        }

    }

    private static VehicleType vehicleTypeFromString (String value) throws IllegalArgumentException, ReadValueException {
        Integer num = -1;
        try {
            num = Integer.parseInt(value);
            return VehicleType.values()[num - 1];
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e){
            //Игнорирую исключение, т.к. если пришло не число - значит VehicleType
        }
        try {
            return VehicleType.valueOf(value);
        } catch (IllegalArgumentException e){
            throw new ReadValueException(String.format("Нет такого VehicleType %s", value));
        }
    }

    public static Object thisType (String value, Field field, CollectionClass collectionClass) throws IllegalArgumentException, ReadValueException {
        if (field.getType().equals(UUID.class)){
            return uuidFromString(value, collectionClass); //6f369f5d-7b26-4d0c-9c35-cb6c980f5f24
        } else if (field.getType().equals(String.class)){
            return value;
        } else if (field.getType().equals(Coordinates.class)){
            return coordinatesFromString(value); //12.5 -8877.9
        } else if (field.getType().equals(ZonedDateTime.class)){
            return ZonedDateTime.parse(value); //2022-05-26T08:15:30+07:00[Asia/Ho_Chi_Minh]
        } else if (field.getType().equals(Double.class) || field.getType().equals(double.class)){
            return doubleFromString(value, field);
        } else if (field.getType().equals(Long.class)){
            return longFromString(value, field);
        } else if (field.getType().equals(VehicleType.class)){
            return vehicleTypeFromString(value);
        }
        return null;
    }

    public static File checkFile(File file) throws FileNotFoundException {
        if (file.exists() && !file.isDirectory()){
            return file;
        } else {
            throw new FileNotFoundException("Не существует файла по указанному пути");
        }
    }

    public static File readCheckFile(File file)throws FileNotFoundException {
        file = checkFile(file);
        if (!file.canRead()){
            throw new FileNotFoundException("Отказано в доступе");
        }
        return file;
    }
    public static File writeCheckFile(File file) throws FileNotFoundException {
        file = checkFile(file);
        if (!file.canWrite()){
            throw new FileNotFoundException("Отказано в доступе");
        }
        return file;
    }

    /**
     * Метод, в котором в зависимости от класса предлагается формат ввода данных
     * */
    public static String formatInput(Class clazz){
        if (clazz.getSimpleName().equals("Coordinates")){
            return "(Пример: \"12.32 -800.004\") ";
        } else if (clazz.getSimpleName().equals("Double") ||
                clazz.getSimpleName().equals("double") ||
                clazz.getSimpleName().equals("Float") ||
                clazz.getSimpleName().equals("float")){
            return "(Пример: \"12.32\") ";
        } else if (clazz.getSimpleName().equals("Long")){
            return "(Пример: \"12\") ";
        } else if (clazz.getSimpleName().equals("VehicleType")){
            return "(Соответствие чисел типу: CAR=1; PLANE=2; SUBMARINE=3; BOAT=4; BICYCLE=5) ";
        } else {
            return "";
        }
    }

}
