package base;
/**
 * Enum с типом т.с.
 * */
public enum VehicleType implements Comparable<VehicleType>{ //отсортированы в порядке записи (самое первое - самое маленькое)
    //компаратор возвращает разницу позиций: CAR.comp(PLANE) = -1
    CAR,
    PLANE,
    SUBMARINE,
    BOAT,
    BICYCLE;
}