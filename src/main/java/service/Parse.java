package service;

import base.Coordinates;
import base.Vehicle;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Класс, в котором находятся статические методы для перевода типов из одного вида в другой<p>
 * Аннотация @Log создает поле логгера
 * */
@Log
public class Parse {

    /**
     * Перевод csv файла в строку
     * */
    public static String parseFromCSVtoString(File file) {

        String res = "";

        try (Scanner in = new Scanner(file)){
            if (in.hasNext())
                in.nextLine(); //Заголовок
            while (in.hasNext()) {

                Reader reader = new StringReader(in.next());
                CSVReader csvReader = new CSVReader(reader);

                String[] record;
                while ((record = csvReader.readNext()) != null) {
                    String dop = String.join("\n", record);
                    res += dop;
                    res += "\n";
                }
            }
        } catch (IOException e){
            log.warning(String.format("Файл с именем %s не найден", e.getMessage()));
        } catch (CsvValidationException e){
            log.warning(e.getMessage());
        }

        return res;

    }

    private static String doubleWithQuotMark (Double d){
        return "\"" + d + "\""; //Не через String.format, чтобы double записывался через точку, а не запятую
    }
    private static String floatWithQuotMark (Float f){
        return "\"" + f + "\""; //Не через String.format, чтобы double записывался через точку, а не запятую
    }

    /**
     * Перевод очереди в строку
     * */
    public static String queueToString(PriorityQueue<Vehicle> priorityQueue){
        String ans = "";
        while (!priorityQueue.isEmpty()){
            Vehicle vehicle = (Vehicle) priorityQueue.poll();
            for (Field field : vehicle.getClass().getDeclaredFields()){
                field.setAccessible(true);
                try{
                    if (field.getType() == Double.class || field.getType() == double.class){
                        ans += doubleWithQuotMark((Double) field.get(vehicle));
                    } else if (field.getType() == Coordinates.class){
                        Field[] fieldsCoordinates = field.getType().getDeclaredFields();
                        fieldsCoordinates[0].setAccessible(true); //x
                        fieldsCoordinates[1].setAccessible(true); //y
                        ans += floatWithQuotMark((Float)fieldsCoordinates[0].get(vehicle.getCoordinates())) + "," +
                                floatWithQuotMark((Float) fieldsCoordinates[1].get(vehicle.getCoordinates()));
                    } else {
                        ans += field.get(vehicle).toString();
                    }
                    ans += ",";
                } catch (IllegalAccessException e){
                    log.warning("Нет доступа к полю класса");
                }
            }
            ans = ans.substring(0, ans.length() - 1); //отбрасывание последней запятой
            ans += "\n";
        }
        return ans;
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
