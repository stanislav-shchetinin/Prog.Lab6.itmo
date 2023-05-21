package commands;

import base.Vehicle;
import service.CollectionClass;
import service.command.Command;
import service.command.ElementArgument;

/**
 * Класс команды добавления элемента: add_if_max {element}<p>
 * Реализует класс Command, чтобы можно было вызывать выполнение команды<p>
 * Реализует маркировочный интерфейс ElementArgument, чтобы можно было проверить какие аргументы принимает команда
 * */
public class AddIfMax implements Command, ElementArgument {
    private Vehicle vehicle;
    private CollectionClass collectionClass;

    public AddIfMax(CollectionClass collectionClass){
        this.collectionClass = collectionClass;
    }

    /**
     * Пустой конструктор нужен для создания пустых объектов в списках команд
     * */
    public AddIfMax(){}
    @Override
    public void setElement(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String description() {
        return "add_if_max {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции";
    }

    @Override
    public String name() {
        return "add_if_max";
    }

    @Override
    public void execute() {
        collectionClass.addIfMax(vehicle);
    }

    @Override
    public void setCollection(CollectionClass collectionClass) {
        this.collectionClass = collectionClass;
    }

}
