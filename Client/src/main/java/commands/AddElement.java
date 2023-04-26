package commands;

import base.Vehicle;
import service.CollectionClass;
import service.command.Command;
import service.command.ElementArgument;

/**
 * Класс команды добавления элемента: add {element}<p>
 * Реализует класс Command, чтобы можно было вызывать выполнение команды<p>
 * Реализует маркировочный интерфейс ElementArgument, чтобы можно было проверить какие аргументы принимает команда
 * */
public class AddElement implements Command, ElementArgument {

    private Vehicle vehicle;
    private CollectionClass collectionClass;

    public AddElement(CollectionClass collectionClass){
        this.collectionClass = collectionClass;
    }

    /**
     * Пустой конструктор нужен для создания пустых объектов в списках команд
     * */
    public AddElement(){
    }

    @Override
    public void setElement(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public String description() {
        return "add {element} : добавить новый элемент в коллекцию";
    }


    @Override
    public String name() {
        return "add";
    }

    @Override
    public void execute() {
        collectionClass.add(vehicle);
    }
}
