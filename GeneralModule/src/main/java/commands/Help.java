package commands;

import lombok.extern.java.Log;
import service.CollectionClass;
import service.InitGlobalCollections;
import service.command.Command;
import service.command.NoArgument;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
/**
 * Класс команды вывода всех команд с описанием: help<p>
 * Реализует класс Command, чтобы можно было вызывать выполнение команды<p>
 * Реализует маркировочный интерфейс NoArgument, чтобы можно было проверить какие аргументы принимает команда (без аргументов)<p>
 * */
@Log
public class Help implements Command, NoArgument {

    @Override
    public String description() {
        return "help : вывести справку по доступным командам";
    }

    @Override
    public String name() {
        return "help";
    }

    /**
     * В методе перебирается список всех команд и выводится их описание
     * */
    @Override
    public void execute(ObjectOutputStream out) {
        try {
            String result = "";
            ArrayList<Command> helpCommand = InitGlobalCollections.helpCommand();
            for (Command command : helpCommand){
                result += command.description() + "\n";
            }
            out.writeObject(result);
            out.flush();
        } catch (IOException e){
            log.warning(e.getMessage());
        }
    }
    @Override
    public void setCollection(CollectionClass collectionClass) {}
}