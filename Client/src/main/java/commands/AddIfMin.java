package commands;

import base.Vehicle;
import service.CollectionClass;
import service.command.Command;
import service.command.ElementArgument;

/**
 * Класс команды добавления элемента: add_if_min {element}<p>
 * Реализует класс Command, чтобы можно было вызывать выполнение команды<p>
 * Реализует маркировочный интерфейс ElementArgument, чтобы можно было проверить какие аргументы принимает команда
 * */
public class AddIfMin implements Command, ElementArgument {
    private Vehicle vehicle;
    private CollectionClass collectionClass;

    public AddIfMin(CollectionClass collectionClass){
        this.collectionClass = collectionClass;
    }
    /**
     * Пустой конструктор нужен для создания пустых объектов в списках команд
     * */
    public AddIfMin(){}
    @Override
    public void setElement(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String description() {
        return "add_if_min {element} : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции";
    }

    @Override
    public String name() {
        return "add_if_min";
    }
    @Override
    public void execute() {
        collectionClass.addIfMin(vehicle);
    }
    @Override
    public void setCollection(CollectionClass collectionClass) {
        this.collectionClass = collectionClass;
    }
}
