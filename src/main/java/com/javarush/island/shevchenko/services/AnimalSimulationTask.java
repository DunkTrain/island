package com.javarush.island.shevchenko.services;


import com.javarush.island.shevchenko.entities.islandMap.Location;
import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.entities.organism.interfaces.*;


/**
 * Класс AnimalSimulationTask представляет собой задачу симуляции жизни для конкретного животного
 * на определенной локации на острове животных. Класс реализует интерфейс Runnable, позволяя
 * выполнять симуляцию в отдельном потоке.
 */



public class AnimalSimulationTask implements Runnable{
    private final Animal animal;
    private final Location location;

    public AnimalSimulationTask(Animal animal, Location location) {
        this.animal = animal;
        this.location = location;
    }

    /**
     * Метод run() представляет собой точку входа для выполнения симуляции жизни животного.
     * Внутри метода происходит выполнение действий животного, таких как питание, размножение,
     * потеря веса и перемещение на другую локацию.
     */

    @Override
    public void run() {
        animal.getLock().lock();
        try {
            if (animal instanceof Predators predator) {
                predator.eat(location);
            } else if (animal instanceof Herbivorous herbivore) {
                herbivore.eat(location);
            } else if (animal instanceof Omnivore poisonous) {
                poisonous.eat(location);
            } else {
                throw new IllegalArgumentException("Unknown animal type: " + animal.getClass().getName());
            }
        } catch (Exception ex) {
            System.err.println("Error during animal simulation task: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            animal.getLock().unlock();
        }
        animal.reproduction(location);
        animal.weightLoss(location);
        animal.timeToDie(location);
        try {
            animal.move(location);
        } catch (Exception ex) {
            System.err.println("Error during animal movement: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            animal.getLock().unlock();
        }
    }

}
