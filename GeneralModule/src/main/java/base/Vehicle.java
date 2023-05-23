package base;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Класс объекты которого хранятся в коллекции<p>
 * Реализует Comparable, чтобы была возможность сравнить пару объктов класса
 * */
public class  Vehicle implements Comparable<Vehicle>, Serializable {
    private UUID id = UUID.randomUUID(); //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private double enginePower; //Значение поля должно быть больше 0
    private Long capacity; //Поле может быть null, Значение поля должно быть больше 0
    private Double distanceTravelled; //Поле может быть null, Значение поля должно быть больше 0
    private VehicleType type; //Поле не может быть null

    public Vehicle(){
        this.creationDate = ZonedDateTime.now();
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public double getEnginePower() {
        return enginePower;
    }

    public UUID getId() {
        return id;
    }

    public Long getCapacity() {
        return capacity;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    private int compDouble (double l, double r){
        return Double.valueOf(l).compareTo(Double.valueOf(r));
    }

    /**
     * Сравнение по (от более важного к менее): типу т.с., мощности, вместимости, пробегу, координатам, имени, дате создания, id
     * */
    @Override
    public int compareTo(Vehicle o) {

        int arrRes[] = new int[8];
        arrRes[0] = this.type.compareTo(o.type);
        arrRes[1] = compDouble(this.enginePower, o.enginePower);
        arrRes[2] = this.capacity.compareTo(o.capacity);
        arrRes[3] = compDouble(this.distanceTravelled, o.distanceTravelled);
        arrRes[4] = this.coordinates.compareTo(o.coordinates);
        arrRes[5] = this.name.compareTo(o.name);
        arrRes[6] = this.creationDate.compareTo(o.creationDate);
        arrRes[7] = this.id.compareTo(o.id);

        for (int x : arrRes){
            if (x != 0){
                return x;
            }
        }
        return  0;

    }

    @Override
    public String toString() {
        return String.format(
                "\nId: %s\nName: %s\nCoordinates: %s\nCreation Date: %s" +
                        "\nEngine Power: %f\nCapacity: %d\nDistance Travelled: %f\nType: %s\n******************\n",
                id.toString(), name, coordinates.toString(), creationDate.toString(), enginePower, capacity,
                distanceTravelled, type.toString()
        );
    }
}