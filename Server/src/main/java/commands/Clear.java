package commands;

import service.CollectionClass;
import service.command.Command;
import service.command.NoArgument;
/**
 * Класс очистки коллекции: clear<p>
 * Реализует класс Command, чтобы можно было вызывать выполнение команды<p>
 * Реализует маркировочный интерфейс NoArgument, чтобы можно было проверить какие аргументы принимает команда (без аргументов)
 * */
public class Clear implements Command, NoArgument {

    private CollectionClass collectionClass;

    public Clear(CollectionClass collectionClass){
        this.collectionClass = collectionClass;
    }
    /**
     * Пустой конструктор нужен для создания пустых объектов в списках команд
     * */
    public Clear(){}

    @Override
    public String description() {
        return "clear : очистить коллекцию";
    }

    @Override
    public String name() {
        return "clear";
    }
    @Override
    public void execute() {
        collectionClass.clear();
    }
    @Override
    public void setCollection(CollectionClass collectionClass) {
        this.collectionClass = collectionClass;
    }
}
