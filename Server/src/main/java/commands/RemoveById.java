package commands;

import lombok.extern.java.Log;
import service.CollectionClass;
import service.command.Command;
import service.command.OneArgument;

import java.io.ObjectOutputStream;
import java.util.UUID;
/**
 * Класс команды: remove_by_id id<p>
 * Реализует класс Command, чтобы можно было вызывать выполнение команды<p>
 * Реализует маркировочный интерфейс OneArgument, чтобы можно было проверить какие аргументы принимает команда (один аргумент - id)<p>
 * Аннотация @Log создает поле логгера
 * */
@Log
public class RemoveById implements Command, OneArgument {
    private CollectionClass collectionClass;
    private UUID id;
    public RemoveById(CollectionClass collectionClass){
        this.collectionClass = collectionClass;
    }
    /**
     * Пустой конструктор нужен для создания пустых объектов в списках команд
     * */
    public RemoveById(){}
    @Override
    public void setParametr(String uuidString) {
        try {
            this.id = UUID.fromString(uuidString);
        } catch (IllegalArgumentException e){
            log.warning("Неверный тип id");
        }
    }

    @Override
    public void clearFields() {
        id = null;
    }

    @Override
    public String description() {
        return "remove_by_id: удалить элемент из коллекции по его id";
    }

    @Override
    public String name() {
        return "remove_by_id";
    }

    @Override
    public void execute() {
        if (id == null){
            log.warning("Недостаточно параметров, чтобы выполнить команду");
        } else {
            collectionClass.removeById(id);
        }
    }
    @Override
    public void setCollection(CollectionClass collectionClass) {
        this.collectionClass = collectionClass;
    }

}
