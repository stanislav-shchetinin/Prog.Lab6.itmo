package commands;

import service.CollectionClass;
import service.command.Command;
import service.command.NoArgument;

/**
 * Класс команды: remove_first<p>
 * Реализует класс Command, чтобы можно было вызывать выполнение команды<p>
 * Реализует маркировочный интерфейс NoArgument, чтобы можно было проверить какие аргументы принимает команда (без аргументов)
 * */
public class RemoveFirst implements Command, NoArgument {
    private CollectionClass collectionClass;

    public RemoveFirst(CollectionClass collectionClass){
        this.collectionClass = collectionClass;
    }
    /**
     * Пустой конструктор нужен для создания пустых объектов в списках команд
     * */
    public RemoveFirst(){}
    @Override
    public String description() {
        return "remove_first : удалить первый элемент из коллекции";
    }

    @Override
    public String name() {
        return "remove_first";
    }
    @Override
    public void execute() {
        collectionClass.removeFirst();
    }
    @Override
    public void setCollection(CollectionClass collectionClass) {
        this.collectionClass = collectionClass;
    }
}
