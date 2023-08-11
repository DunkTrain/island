package com.javarush.island.shevchenko.services;

import com.javarush.island.shevchenko.entities.islandMap.IslandField;
import com.javarush.island.shevchenko.entities.islandMap.Location;
import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Класс `WorldInitializer` отвечает за инициализацию и создание начального состояния игрового мира.
 * Этот класс создает поле (остров) из локаций, заполняет его животными и растениями,
 * а также определяет соседние локации для каждой локации.
 */


public class WorldInitializer {
    private final IslandField islandField; // остров

    /**
     * Конструктор класса `WorldInitializer` создает объект класса, инициализируя игровое поле заданными размерами.
     * @param y Количество строк (вертикальных координат)
     * @param x Количество столбцов (горизонтальных координат)
     */
    public WorldInitializer(int y, int x) {
        this.islandField = new IslandField(y, x);
        initializeLocation();
        generateLocations();
    }


    /**
     * Метод `initializeLocation` заполняет поле (остров) объектами класса Location
     * в каждой ячейке, инициализируя их координатами.
     */
    private void initializeLocation() {
        Location[][] locations = this.islandField.getLocations();
        for (int y = 0; y < locations[y].length; y++) {
            for (int x = 0; x < locations.length; x++) {
                locations[x][y] = new Location(y, x);
            }
        }
    }

    private void setNeighboringLocations(Location location) {
        int yMin = Math.max(location.getCoordinate_Y() - 1, 0);
        int yMax = Math.min(location.getCoordinate_Y() + 1, IslandSettings.FIELD_TO_SIZE_Y - 1);
        int xMin = Math.max(location.getCoordinate_X() - 1, 0);
        int xMax = Math.min(location.getCoordinate_X() + 1, IslandSettings.FIELD_TO_SIZE_X - 1);

        for (int y = yMin; y <= yMax; y++) {
            for (int x = xMin; x <= xMax; x++) {
                if (location.getCoordinate_Y() != y || location.getCoordinate_X() != x) {
                    location.getNeighboringLocations().add(islandField.getLocation(y, x));
                }
            }
        }
    }


    /**
     * Метод `generateLocations` заполняет каждую локацию на острове соседними локациями,
     * создает наборы животных и растений, а также размещает их на каждой локации.
     */
    private void generateLocations() {
        Location[][] locations = this.islandField.getLocations();
        for (int y = 0; y < locations[y].length; y++) {
            for (Location[] location : locations) {
                setNeighboringLocations(location[y]);
                generationAnimals(location[y]);
                generationPlants(location[y]);
            }
        }
    }

    private void generationAnimals(Location location) {
        for (Class<? extends Animal> classAnimal : IslandSettings.ANIMAL_CLASSES) {
            Set<Animal> set = ConcurrentHashMap.newKeySet();
            location.getAnimals().put(classAnimal, set);

            if (isCreateEntityType()) {
                int numberOfAnimalType = Randomizer.getRandom(0, (int) IslandSettings.ANIMAL_PARAMETERS.get(classAnimal)[1]);
                for (int i = 0; i < numberOfAnimalType; i++) {
                    Animal animal = tryCreateAnimal(classAnimal);
                    set.add(animal);
                }
            }
        }
    }

    private void generationPlants(Location location) {
        if (isCreateEntityType()) {
            location.setPlant(Randomizer.getRandom(0, (int) IslandSettings.MAX_AMOUNT_OF_PLANT_ON_ONE_CELL));
        }
    }

    private <T> T tryCreateAnimal(Class<T> tClass) {
        try {
            return tClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Error creating animal: " + e.getMessage(), e);
        }
    }


    private boolean isCreateEntityType() {
        return Randomizer.getRandom();
    }

    /**
     * Метод `getIslandField` возвращает игровое поле (остров), созданное классом.
     * @return Объект класса IslandField
     */
    public IslandField getIslandField() {
        return islandField;
    }
}
