package commands;

import lombok.extern.java.Log;
import service.CollectionClass;
import service.command.Command;
import service.command.OneArgument;
/**
 * Класс команды: count_by_capacity capacity<p>
 * Реализует класс Command, чтобы можно было вызывать выполнение команды<p>
 * Реализует маркировочный интерфейс OneArgument, чтобы можно было проверить какие аргументы принимает команда (один аргумент - capacity)<p>
 * Аннотация @Log создает поле логгера
 * */
@Log
public class CountByCapacity implements Command, OneArgument {
    private CollectionClass collectionClass;
    private Long capacity;
    public CountByCapacity(CollectionClass collectionClass){
        this.collectionClass = collectionClass;
    }
    /**
     * Пустой конструктор нужен для создания пустых объектов в списках команд
     * */
    public CountByCapacity(){}

    @Override
    public void setParametr(String longString) {
        try {
            this.capacity = Long.parseLong(longString);
        } catch (IllegalArgumentException e){
            log.warning("Неверный тип Long");
        }
    }

    @Override
    public String description() {
        return "count_by_capacity : очистить коллекцию";
    }

    @Override
    public String name() {
        return "count_by_capacity";
    }

    @Override
    public void execute() {
        if (capacity == null){
            log.warning("Недостаточно параметров, чтобы выполнить комманду");
        } else {
            collectionClass.countByCapacity(capacity);
        }
    }
    /**
     * Очистка поля после выполнения команды, чтобы ситуации, что в команду не были переданы аргументы, а началось выполнение с прошлыми, не было
     * */
    @Override
    public void clearFields() {
        capacity = null;
    }
    @Override
    public void setCollection(CollectionClass collectionClass) {
        this.collectionClass = collectionClass;
    }
}
