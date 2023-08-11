package com.javarush.island.shevchenko.services;

import com.javarush.island.shevchenko.entities.islandMap.Location;


/**
 * Класс PlantGrowthTask представляет собой задачу роста растений на определенной локации
 * на острове животных. Класс реализует интерфейс Runnable, позволяя выполнять рост растений
 * в отдельном потоке.
 */



public class PlantGrowthTask implements Runnable {
    private final Location location;
    public PlantGrowthTask(Location location) {
        this.location = location;
    }

    public void growUp() {
        location.plantGrowth();
    }

    @Override
    public void run() {
        location.getLock().lock();
        try {
            growUp();
        } finally {
            location.getLock().unlock();
        }
    }
}
