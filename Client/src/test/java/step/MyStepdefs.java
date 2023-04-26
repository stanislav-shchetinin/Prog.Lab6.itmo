package step;

import commands.AddElement;
import commands.ExecuteScript;
import commands.Info;
import io.cucumber.java.PendingException;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.checkerframework.checker.units.qual.C;
import org.junit.rules.ErrorCollector;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import service.CollectionClass;
import service.command.Command;

import java.io.*;
import java.util.Scanner;

import static console.Console.getFile;
import static console.Console.inputVehicle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static service.FileRead.fromFileVehicle;
import static service.Parse.parseFromCSVtoString;
@Log
public class MyStepdefs {
    private File file;
    private CollectionClass collectionClass = new CollectionClass();

    @Дано("запуск метода получения файла с переменными окружения из {string}")
    public void startGetFile(String arg) {
        try {
            File fileWithTests = new File(arg);
            Scanner scanner = new Scanner(fileWithTests);
            while (scanner.hasNext()){
                this.file = getFile(scanner);
            }
        } catch (Throwable e){
            throw new PendingException("Ошибка при попытке получить файл из переменной окружения");
        }

    }
    @Тогда("получение коллекции из файла")
    public void setCollectionFromFile(){
        try {
            fromFileVehicle(collectionClass, new Scanner(parseFromCSVtoString(file)));
        } catch (Throwable e){
            throw new PendingException("Ошибка при попытке преобразовать файл в коллекцию");
        }

    }

    @Пусть("тестируется случай {string}")
    public void testCase(String nameFile){

        String nameInputFile = "files/test_files/test_cases/" + nameFile + "/test";
        String nameOutputFile = "files/test_files/test_cases/" + nameFile + "/tets_my";
        String nameRightFile = "files/test_files/test_cases/" + nameFile + "/test_right";

        try {

            FileInputStream FileInputStream = new FileInputStream(nameInputFile);
            PrintStream printStream = new PrintStream(nameOutputFile);
            System.setIn(FileInputStream);
            System.setOut(printStream);

            CollectionClass oldCollectionClass = new CollectionClass(collectionClass);
            Command command = ExecuteScript.getInstance(oldCollectionClass, file);
            command.setParametr(nameInputFile);
            command.execute();
            command.clearFields();

            File fileMy = new File(nameOutputFile);
            File fileRight = new File(nameRightFile);

            if (!FileUtils.readFileToString(fileMy, "utf-8").equals(FileUtils.readFileToString(fileRight, "utf-8"))){
                throw new PendingException(String.format("Неверный вывод %s", nameFile));
            }

        } catch (IOException e) {
            log.warning("Проблемы с файлом ввода/вывода при тестировании");
        }

    }

}