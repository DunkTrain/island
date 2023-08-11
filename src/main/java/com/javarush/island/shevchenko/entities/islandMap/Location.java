package com.javarush.island.shevchenko.entities.islandMap;

import com.javarush.island.shevchenko.entities.animals.herbivores.*;
import com.javarush.island.shevchenko.entities.animals.predators.*;
import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.services.AnimalSimulationTask;
import com.javarush.island.shevchenko.services.PlantGrowthTask;
import com.javarush.island.shevchenko.settings.IslandSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



/**
 * Location - Класс, представляющий отдельную локацию на игровом поле "Остров животных".
 * Каждая локация содержит информацию о своих координатах (позиции), количестве растений, соседних локациях и животных.
 * Локация управляет выполнением симуляции для всех животных и ростом растений, а также предоставляет методы для доступа к своим данным.
 */




public class Location {

    private final ExecutorService threadPool = Executors.newFixedThreadPool(4);
    private final int coordinate_X;
    private final int coordinate_Y;
    private volatile double plant;
    private final List<Location> neighboringLocations = new ArrayList<>();
    private final Map<Class<? extends Animal>, Set<Animal>> animals = new ConcurrentHashMap<>();
    private final Lock lock = new ReentrantLock(true);

    public Location(int y, int x) {
        this.coordinate_Y = y;
        this.coordinate_X = x;
    }

    public void start() {
        lock.lock();
        try {
            for (Class<? extends Animal> animalClass : animals.keySet()) {
                for (Animal animal : animals.get(animalClass)) {
                    threadPool.submit(new AnimalSimulationTask(animal, this));
                }
            }
            threadPool.submit(new PlantGrowthTask(this));
        } finally {
            lock.unlock();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void await(int milliseconds) throws InterruptedException {
        threadPool.awaitTermination(milliseconds, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        threadPool.shutdown();
    }

    public void removeAnimal(Animal animal) {
        animals.get(animal.getClass()).remove(animal);
    }

    public void addAnimalToLocation(Animal animal) {
        if (isThereEnoughSpace(animal.getClass())) {
            animals.get(animal.getClass()).add(animal);
        }
    }

    public boolean isThereEnoughSpace(Class<? extends Animal> animalClass) {
        return animals.get(animalClass).size() < IslandSettings.ANIMAL_PARAMETERS.get(animalClass)[1];
    }

    public void plantGrowth() {
        getLock().lock();
        try {
            double plantGrowth = IslandSettings.GROWTH_OF_PLANT;
            double newPlantValue = this.plant + plantGrowth;
            this.plant = Math.min(newPlantValue, IslandSettings.MAX_AMOUNT_OF_PLANT_ON_ONE_CELL);
        } finally {
            getLock().unlock();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Координаты локации: [").append(coordinate_X).append(", ").append(coordinate_Y).append("]\n");

        // Животные на локации
        sb.append("Животные на локации:\n");
        for (Class<? extends Animal> animalClass : animals.keySet()) {
            int animalCount = animals.get(animalClass).size();
            if (animalCount > 0) {
                String animalName = animalClass.getSimpleName();
                sb.append("\t").append(animalName).append(": ").append(animalCount).append(" ").append(getAnimalEmoji(animalName)).append("\n");
            }
        }

        // Растения на локации
        sb.append("Растения на локации: ").append(String.format("%.2f", plant)).append(" ").append("\uD83C\uDF31").append("\n\n");

        return "[" + "\uD83D\uDC3B" + animals.get(Bear.class).size()
                + "\uD83D\uDC32" + animals.get(Snake.class).size()
                + "\uD83D\uDC17" + animals.get(Boar.class).size()
                + "\uD83D\uDC03" + animals.get(Buffalo.class).size()
                + "\uD83E\uDD8C" + animals.get(Deer.class).size()
                + "\uD83E\uDD8A" + animals.get(Fox.class).size()
                + "\uD83D\uDC10" + animals.get(Goat.class).size()
                + "\uD83E\uDD84" + animals.get(Horse.class).size()
                + "\uD83D\uDC01" + animals.get(Mouse.class).size()
                + "\uD83D\uDC07" + animals.get(Rabbit.class).size()
                + "\uD83D\uDC11" + animals.get(Sheep.class).size()
                + "\uD83D\uDC3A" + animals.get(Wolf.class).size()
                + "\uD83E\uDD86" + animals.get(Duck.class).size()
                + "\uD83E\uDD85" + animals.get(Eagle.class).size()
                + "\uD83D\uDC1B" + animals.get(Caterpillar.class).size()
                + "\uD83C\uDF31" + String.format("%.2f", plant) + "]\n"
                + sb;
    }

    // Вспомогательный метод для получения смайлика по имени животного
    private String getAnimalEmoji(String animalName) {
        return switch (animalName) {
            case "Bear" -> "\uD83D\uDC3B";
            case "Snake" -> "\uD83D\uDC32";
            case "Boar" -> "\uD83D\uDC17";
            case "Buffalo" -> "\uD83D\uDC03";
            case "Deer" -> "\uD83E\uDD8C";
            case "Fox" -> "\uD83E\uDD8A";
            case "Goat" -> "\uD83D\uDC10";
            case "Horse" -> "\uD83E\uDD84";
            case "Mouse" -> "\uD83D\uDC01";
            case "Rabbit" -> "\uD83D\uDC07";
            case "Sheep" -> "\uD83D\uDC11";
            case "Wolf" -> "\uD83D\uDC3A";
            case "Duck" -> "\uD83E\uDD86";
            case "Eagle" -> "\uD83E\uDD85";
            case "Caterpillar" -> "\uD83D\uDC1B";
            default -> "";
        };
    }


    public Lock getLock() {
        return lock;
    }

    public Map<Class<? extends Animal>, Set<Animal>> getAnimals() {
        return animals;
    }

    public List<Location> getNeighboringLocations() {
        return neighboringLocations;
    }

    public double getPlant() {
        return plant;
    }

    public void setPlant(double plant) {
        this.plant = plant;
    }

    public int getCoordinate_X() {
        return coordinate_X;
    }

    public int getCoordinate_Y() {
        return coordinate_Y;
    }

}