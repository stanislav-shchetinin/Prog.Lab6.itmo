package commands;

import base.Coordinates;
import base.Vehicle;
import exceptions.ReadValueException;
import lombok.extern.java.Log;
import service.CollectionClass;
import service.NoInputTypes;
import service.command.Command;
import service.command.ElementArgument;
import service.command.OneArgument;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import static service.InitGlobalCollections.mapCommand;
import static service.InitGlobalCollections.setNoInputTypes;
import static service.Validate.readCheckFile;
import static service.Validate.thisType;

/**
 * Класс команды: execute_script имя_файла<p>
 * Реализует класс Command, чтобы можно было вызывать выполнение команды<p>
 * Реализует маркировочный интерфейс OneArgument, чтобы можно было проверить какие аргументы принимает команда (один аргумент - имя файла скрипта)<p>
 * Аннотация @Log создает поле логгера
 * */
@Log
public class ExecuteScript implements Command, OneArgument {

    private CollectionClass collectionClass;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private File file;
    private File fileSave;
    private HashSet<String> nameFiles = new HashSet<>();
    private static ExecuteScript Instance = null;
    private HashMap<String, Command> mapCommand;
    HashSet<String> setNoInputTypes = setNoInputTypes(NoInputTypes.values());
    /**
     * Один объект класса на весь проекта, чтобы не перезаписывался Set для проверки на цикл
     * */
    public static ExecuteScript getInstance(CollectionClass collectionClass, File fileSave) {
        if (Instance == null){
            Instance = new ExecuteScript(collectionClass, fileSave);
        }
        Instance.collectionClass = new CollectionClass(collectionClass);
        return Instance;
    }
    public static ExecuteScript getInstance() {
        if (Instance == null){
            Instance = new ExecuteScript();
        }
        return Instance;
    }
    private ExecuteScript(CollectionClass collectionClass, File fileSave){
        this.collectionClass = collectionClass;
        this.fileSave = fileSave;
    }
    /**
     * Пустой конструктор нужен для создания пустых объектов в списках команд
     * */
    public ExecuteScript(){}

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }
    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    @Override
    public void setParametr(String nameFile) {
        try {
            this.file = readCheckFile(new File(nameFile));
        } catch (FileNotFoundException e) {
            log.warning(e.getMessage());
        }
    }

    /**
     * Очистка поля после выполнения команды, чтобы ситуации, что в команду не были переданы аргументы, а началось выполнение с прошлыми, не было
     * */
    @Override
    public void clearFields() {
        file = null;
        nameFiles.clear();
    }

    @Override
    public String description() {
        return "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме";
    }

    @Override
    public String name() {
        return "execute_script";
    }

    public List<Command> methodForScript() {
        List<Command> list = new ArrayList<>();
        if (file == null){
            log.warning("Недостаточно параметров, чтобы выполнить команду");
            return list;
        }
        /**
         * Текущее имя файла встречалось ранее
         * */
        if (nameFiles.contains(file.getName())){
            log.warning("Вызов файлов зациклился");
            return list;
        }
        nameFiles.add(file.getName());
        mapCommand = mapCommand();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()){
                /**
                 * Разделение  line из файла на компоненты
                 * */
                String[] line = scanner.nextLine().trim().split(" ");
                if (line.length == 0){
                    continue;
                }
                /**
                 * Первое слово в line всегда имя команды
                 * */
                Command command = mapCommand.get(line[0]);
                if (command == null){
                    log.warning("Не существует команды с указанным названием");
                    continue;
                }
                commandSetParametr(command, line);

                try {
                    commandSetElement(command, line);

                    list.add(command);

                } catch (ReadValueException | IllegalAccessException e){
                    log.warning(e.getMessage());
                }
                //command.clearFields();
            }
        } catch (FileNotFoundException e) {
            log.warning("Файл не найден");
        }
        return list;
    }
    @Override
    public void setCollection(CollectionClass collectionClass) {
        this.collectionClass = collectionClass;
    }

    /**
     * Проверка команды на то, есть ли у нее одиночный аргумент, если да, то нужно его установить
     * */
    private void commandSetParametr(Command command, String[] line){
        if ((command instanceof OneArgument) && line.length >= 2){
            command.setParametr(line[1]);
        }
    }

    /**
     * Проверка команды на то, есть ли у нее element, как аргумент, если да, то нужно проверить есть ли у этой команды еще и одиночный элемент
     * */
    private void commandSetElement(Command command, String[] line) throws ReadValueException, IllegalAccessException {
        if (command instanceof ElementArgument){
            //+1 т.к. line[0] - имя команды, +1 т.к. Coordinates имеет два поля
            if (line.length == Vehicle.class.getDeclaredFields().length + 1 - setNoInputTypes(NoInputTypes.values()).size() + 1){
                command.setElement(vehicleFromArray(line));
            } else {
                command.setElement(vehicleFromArray(Arrays.copyOfRange(line, 1, line.length))); //у update_id еще один аргумент
            }
        }
    }
    /**
     * Метод для получения объекта Vehicle из line из файла
     * */
    private Vehicle vehicleFromArray (String[] line) throws ReadValueException, IllegalAccessException {
        if (line.length - 1 < Vehicle.class.getDeclaredFields().length - setNoInputTypes.size()){ //-1 т.к. line[0] - имя команды
            throw new ReadValueException("Недостаточно аргументов для записи Vehicle");
        }
        Vehicle vehicle = new Vehicle();
        int num = 1; //line[0] - название команды
        /**
         * Перебор всех полей класса Vehicle
         * */
        for (Field field : Vehicle.class.getDeclaredFields()){
            field.setAccessible(true);
            if (setNoInputTypes.contains(field.getType().getSimpleName())){ //Если ID или DATA
                continue;
            }
            line[num] = line[num].trim();
            String str = line[num];
            /**
             * Отдельный случай - класс координат, т.к. у него два поля => он принимает два значения из line
             * */
            if (field.getType().equals(Coordinates.class)){
                ++num;
                str += " " + line[num].trim();
            }
            /**
             * Установить в поле полученyю str (перед этим нужно пройти проверку на корректность)
             * */
            try {
                field.set(vehicle, thisType(str, field));
            } catch (ReadValueException | IllegalAccessException e) {
                throw e;
            }
            ++num;
        }
        return  vehicle;
    }

}
