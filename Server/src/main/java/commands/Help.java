package commands;

import service.CollectionClass;
import service.InitGlobalCollections;
import service.command.Command;
import service.command.NoArgument;

import java.util.ArrayList;
/**
 * Класс команды вывода всех команд с описанием: help<p>
 * Реализует класс Command, чтобы можно было вызывать выполнение команды<p>
 * Реализует маркировочный интерфейс NoArgument, чтобы можно было проверить какие аргументы принимает команда (без аргументов)<p>
 * */
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
    public void execute() {
        ArrayList<Command> helpCommand = InitGlobalCollections.helpCommand();
        for (Command command : helpCommand){
            System.out.println(command.description());
        }
    }
    @Override
    public void setCollection(CollectionClass collectionClass) {}
}
