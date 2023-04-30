package commands;

import base.Vehicle;
import lombok.extern.java.Log;
import service.CollectionClass;
import service.Pair;
import service.command.Command;
import service.command.ElementArgument;
import service.command.OneArgument;

import java.io.ObjectOutputStream;
import java.util.UUID;

/**
 * Класс команды: update_bu_id id {element}<p>
 * Реализует класс Command, чтобы можно было вызывать выполнение команды<p>
 * Реализует маркировочный интерфейс ElementArgument (т.к. один из аргументов - объект класса Vehicle) и OneArgument (id)<p>
 * Аннотация @Log создает поле логгера
 * */
@Log
public class UpdateId implements Command, ElementArgument, OneArgument {

    private Pair<Vehicle, UUID> pair = new Pair<>();
    private CollectionClass collectionClass;

    public UpdateId(CollectionClass collectionClass){
        this.collectionClass = collectionClass;
    }
    /**
     * Пустой конструктор нужен для создания пустых объектов в списках команд
     * */
    public UpdateId(){}
    @Override
    public void setElement(Vehicle vehicle) {
        if (vehicle != null)
            pair.setL(vehicle);
    }

    @Override
    public void setParametr(String uuidString){
        try {
            this.pair.setR(UUID.fromString(uuidString));
        } catch (IllegalArgumentException e){
            log.warning("Неверный тип id");
        }
    }

    @Override
    public void clearFields() {
        pair = new Pair<>();
    }

    @Override
    public String description() {
        return "update_id id:обновить элемент с заданным id";
    }

    @Override
    public String name() {
        return "update_id";
    }

    @Override
    public void execute() {
        if (pair.getR() == null || pair.getL() == null){
            log.warning("Недостаточно параметров, чтобы выполнить комманду");
        } else {
            collectionClass.updateById(pair);
        }
    }
    @Override
    public void setCollection(CollectionClass collectionClass) {
        this.collectionClass = collectionClass;
    }
}
