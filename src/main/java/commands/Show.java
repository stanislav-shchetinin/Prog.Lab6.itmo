package commands;

import lombok.extern.java.Log;
import service.CollectionClass;
import service.command.Command;
import service.command.NoArgument;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Класс команды: show<p>
 * Реализует класс Command, чтобы можно было вызывать выполнение команды<p>
 * Реализует маркировочный интерфейс NoArgument, чтобы можно было проверить какие аргументы принимает команда (без аргументов)<p>
 * */
@Log
public class Show implements Command, NoArgument {
    private CollectionClass collectionClass;

    public Show(CollectionClass collectionClass){
        this.collectionClass = collectionClass;
    }
    /**
     * Пустой конструктор нужен для создания пустых объектов в списках команд
     * */
    public Show(){}

    @Override
    public String description() {
        return "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }

    @Override
    public String name() {
        return "show";
    }

    /**
     * Из менеджера коллекции берется коллекция, переводится в строку и удаляется последний символ (лишняя запятая)
     * */
    @Override
    public void execute(ObjectOutputStream out) {
        try {
            out.writeObject(collectionClass.getCollection().toString().substring(1, collectionClass.getCollection().toString().length() - 1));
            out.flush();
        } catch (IOException e){
            log.warning(e.getMessage());
        }
    }
    @Override
    public void setCollection(CollectionClass collectionClass) {
        this.collectionClass = collectionClass;
    }
}
