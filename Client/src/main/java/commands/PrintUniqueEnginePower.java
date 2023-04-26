package commands;

import service.CollectionClass;
import service.command.Command;
import service.command.NoArgument;
/**
 * Класс команды: print_unique_engine_power<p>
 * Реализует класс Command, чтобы можно было вызывать выполнение команды<p>
 * Реализует маркировочный интерфейс NoArgument, чтобы можно было проверить какие аргументы принимает команда (без аргументов)
 * */
public class PrintUniqueEnginePower implements Command, NoArgument {
    private CollectionClass collectionClass;

    public PrintUniqueEnginePower(CollectionClass collectionClass){
        this.collectionClass = collectionClass;
    }
    /**
     * Пустой конструктор нужен для создания пустых объектов в списках команд
     * */
    public PrintUniqueEnginePower(){}
    @Override
    public String description() {
        return "print_unique_engine_power : вывести уникальные значения поля enginePower всех элементов в коллекции";
    }

    @Override
    public String name() {
        return "print_unique_engine_power";
    }
    @Override
    public void execute() {
        collectionClass.printUniqueEnginePower();
    }
    @Override
    public void setCollection(CollectionClass collectionClass) {
        this.collectionClass = collectionClass;
    }
}
