package commands;

import service.CollectionClass;
import service.command.Command;
import service.command.NoArgument;

import java.io.ObjectOutputStream;

/**
 * Класс команды: print_ascending<p>
 * Реализует класс Command, чтобы можно было вызывать выполнение команды<p>
 * Реализует маркировочный интерфейс NoArgument, чтобы можно было проверить какие аргументы принимает команда (без аргументов)
 * */
public class PrintAscending implements Command, NoArgument {
    private CollectionClass collectionClass;

    public PrintAscending(CollectionClass collectionClass){
        this.collectionClass = collectionClass;
    }
    /**
     * Пустой конструктор нужен для создания пустых объектов в списках команд
     * */
    public PrintAscending(){}

    @Override
    public String description() {
        return "print_ascending : вывести элементы коллекции в порядке возрастания";
    }


    @Override
    public String name() {
        return "print_ascending";
    }
    @Override
    public void execute(ObjectOutputStream out) {
        collectionClass.printAscending(out);
    }
    @Override
    public void setCollection(CollectionClass collectionClass) {
        this.collectionClass = collectionClass;
    }
}
