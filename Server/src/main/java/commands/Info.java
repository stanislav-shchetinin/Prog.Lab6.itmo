package commands;

import service.CollectionClass;
import service.command.Command;
import service.command.NoArgument;
/**
 * Класс команды информации о коллекции: info<p>
 * Реализует класс Command, чтобы можно было вызывать выполнение команды<p>
 * Реализует маркировочный интерфейс NoArgument, чтобы можно было проверить какие аргументы принимает команда (без аргументов)
 * */
public class Info implements Command, NoArgument {

    private CollectionClass collectionClass;

    public Info(CollectionClass collectionClass){
        this.collectionClass = collectionClass;
    }
    /**
     * Пустой конструктор нужен для создания пустых объектов в списках команд
     * */
    public Info(){}
    @Override
    public String description() {
        return "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }


    @Override
    public String name() {
        return "info";
    }

    /**
     * В менеджере коллекции хранится вся информация, которая нужна
     * */
    @Override
    public void execute() {
        System.out.println(String.format("Тип: %s\nДата инициализации: %s\nКоличество элементов: %d",
                collectionClass.getCollection().getClass().getSimpleName(),
                collectionClass.getTime(),
                collectionClass.getCollection().size())
        );
    }
    @Override
    public void setCollection(CollectionClass collectionClass) {
        this.collectionClass = collectionClass;
    }
}
