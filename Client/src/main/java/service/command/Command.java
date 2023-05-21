package service.command;

import base.Vehicle;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Интерфейс Command для паттерна Command
 * */
public interface Command extends Serializable {

    default void execute() throws IOException {}
    default void execute(ObjectOutputStream out) throws IOException{
        execute();
        out.writeObject(null);
        out.flush();
    }
    default void setElement(Vehicle vehicle){}
    default void setParametr(String parametr){}
    default void clearFields(){}
    String description();
    String name();
}
