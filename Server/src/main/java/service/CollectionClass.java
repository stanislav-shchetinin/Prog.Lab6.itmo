package service;

import base.Vehicle;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Менеджер по работе с коллекцией<p>
 * Аннотация @Log создает поле логгера
 * */
@Log
public class CollectionClass{
    private Date date;
    private PriorityQueue<Vehicle> collection = new PriorityQueue();
    /**
     * Поле хранит id всех объектов, чтобы отслеживать не уникальные id
     * */
    private HashSet<UUID> uuidHashSet = new HashSet<>();
    public CollectionClass (PriorityQueue collection){
        this.collection = collection;
        date = new Date(); //при создании устанавливает текущую дату
    }
    public CollectionClass(){
        date = new Date();
    }
    public CollectionClass (CollectionClass collectionClass){
        this.collection = new PriorityQueue<>(collectionClass.getCollection());
        this.date = collectionClass.getTime();
        this.uuidHashSet = new HashSet<>(collectionClass.getUuidHashSet());
    }

    public Date getTime() {
        return date;
    }

    public PriorityQueue getCollection() {
        return collection;
    }

    public HashSet<UUID> getUuidHashSet() {
        return uuidHashSet;
    }

    @Override
    public String toString() {
        String cs = collection.toString();
        return cs.substring(1, cs.length() - 1);
    }

    public void clear(){
        collection.clear();
        uuidHashSet.clear();
    }
    public void removeFirst(){
        if (collection.size() == 0){
            log.warning("Коллекция пустая");
            return;
        }
        UUID id = collection.poll().getId();
        uuidHashSet.remove(id);
    }
    public void printAscending(ObjectOutputStream out){
        try {
            String answer = collection
                    .stream()
                    .toList()
                    .toString();
            out.writeObject(answer.substring(1, answer.length() - 1));
            out.flush();
        } catch (IOException e){
            log.warning(e.getMessage());
        }


    }
    public void printUniqueEnginePower(ObjectOutputStream out){
        try {
            out.writeObject(collection
                    .stream()
                    .map(Vehicle::getEnginePower)
                    .distinct().toList()
                    .toString());
            out.flush();
        } catch (IOException e){
            log.warning(e.getMessage());
        }
    }
    public void removeById (UUID id){
        uuidHashSet.remove(id);
        collection = collection.stream().filter(x -> !x.getId().equals(id))
                .collect(Collectors.toCollection(PriorityQueue::new));
    }

    public void countByCapacity(Long capacity, ObjectOutputStream out){
        /*try {
            Integer count = 0;
            PriorityQueue<Vehicle> collectionCopy = new PriorityQueue<>(collection);
            while (!collectionCopy.isEmpty()){
                if (collectionCopy.poll().getCapacity().equals(capacity)){
                    count += 1;
                }
            }
            out.writeObject(count.toString());
            out.flush();
        } catch (IOException e) {
            log.warning(e.getMessage());
        }*/
        try {
            Long dist = collection
                    .stream()
                    .map(Vehicle::getCapacity)
                    .distinct()
                    .count();

            out.writeObject(
                    dist.toString()
            );
            out.flush();
        } catch (IOException e){
            log.warning(e.getMessage());
        }


    }

    public void add(Vehicle vehicle){
        collection.add(vehicle);
        uuidHashSet.add(vehicle.getId());
    }

    public void updateById(Pair<Vehicle, UUID> pair){
        Vehicle vehicleNew = pair.getL();
        UUID id = pair.getR();
        vehicleNew.setId(id);

        PriorityQueue<Vehicle> collectionNew = new PriorityQueue<>();
        while (!collection.isEmpty()){
            Vehicle vehicleOld = collection.poll();
            if (vehicleOld.getId().equals(id)){
                uuidHashSet.remove(vehicleOld.getId());
                uuidHashSet.add(vehicleNew.getId());
                collectionNew.add(vehicleNew);
            } else {
                collectionNew.add(vehicleOld);
            }
        }

        collection = collectionNew;

    }

    public void addIfMax(Vehicle vehicle){
        if (collection.size() == 0 || vehicle.compareTo(collection.peek()) > 0){
            collection.add(vehicle);
            uuidHashSet.add(vehicle.getId());
        }
    }

    public void addIfMin(Vehicle vehicle){
        if (collection.size() == 0 || vehicle.compareTo(collection.peek()) < 0){
            collection.add(vehicle);
            uuidHashSet.add(vehicle.getId());
        }
    }

}
