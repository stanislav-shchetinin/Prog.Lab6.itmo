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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    public static ExecuteScript getInstance() {
        if (Instance == null){
            Instance = new ExecuteScript();
        }
        return Instance;
    }
    private ExecuteScript(File fileSave){
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
    public void setCollection(CollectionClass collectionClass) {
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

}
