package com.javarush.island.shevchenko.entities.organism;



import com.javarush.island.shevchenko.entities.islandMap.Location;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  Animal - абстрактный класс, представляющий общие характеристики и поведение для всех животных на острове в симуляции.
 *  Он расширяет класс Nature и реализует интерфейс Cloneable, что позволяет использовать клонирование объектов для животных.
 *  Каждый экземпляр класса Animal представляет одного животного на острове.
 */
public abstract class Animal extends Nature implements Cloneable {

    public Class<? extends Animal> aClass;
    private final Lock lock = new ReentrantLock(true);

    public Animal() {
        super();
        this.aClass = getClass();
    }

    public void weightLoss(Location location) {
        location.getLock().lock();
        try {
            weight = weight - weight / 10;
        } finally {
            location.getLock().unlock();
        }
    }


    public void timeToDie(Location location) {
        location.getLock().lock();
        try {
            if (weight < IslandSettings.ANIMAL_PARAMETERS.get(aClass)[0] / 3) {
                location.removeAnimal(this);
            }
        } finally {
            location.getLock().unlock();
        }
    }


    public void reproduction(Location location) {
        location.getLock().lock();
        try {
            Set<Animal> animals = location.getAnimals().get(aClass);
            if (weight == IslandSettings.ANIMAL_PARAMETERS.get(aClass)[0] && animals.size() > 1) {
                Animal clone = this.clone();
                location.addAnimalToLocation(clone);
                weightLoss(location);
            }
        } finally {
            location.getLock().unlock();
        }
    }

    public void move(Location location) {
        location.getLock().lock();
        Location newLocation = choiceOfAvailableLocation(location);
        try {
            if (newLocation.isThereEnoughSpace(this.aClass)) {
                newLocation.addAnimalToLocation(this);
                location.removeAnimal(this);
            }
        } finally {
            location.getLock().unlock();
        }
    }



    private Location choiceOfAvailableLocation(Location location) {
        int steps = getMaxNumberOfStepsAnimal();
        for (int i = steps; i >= 0; i--) {
            location = location.getNeighboringLocations().get(ThreadLocalRandom.current().nextInt(0, location.getNeighboringLocations().size()));
        }
        return location;
    }


    @Override
    public Animal clone() {
        try {
            Animal clone = (Animal) super.clone();
            clone.weight = Randomizer.getRandom(IslandSettings.ANIMAL_PARAMETERS.get(aClass)[0] / 1.5, IslandSettings.ANIMAL_PARAMETERS.get(this.aClass)[0]);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }


    public double getCurrentWeight() {
        return weight;
    }


    private int getMaxNumberOfStepsAnimal() {
        return (int) IslandSettings.ANIMAL_PARAMETERS.get(this.aClass)[2];
    }


    public Lock getLock() {
        return lock;
    }
}