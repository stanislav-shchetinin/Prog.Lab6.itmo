package service.command;

import base.Vehicle;
import service.CollectionClass;

import java.io.OutputStream;

/**
 * Интерфейс Command для паттерна Command
 * */
public interface Command {
    void execute();
    void setCollection(CollectionClass collectionClass);
    default void setElement(Vehicle vehicle){}
    default void setParametr(String parametr){}
    default void clearFields(){}
    String description();

    String name();
}
