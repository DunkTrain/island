package com.javarush.island.shevchenko.entities.islandMap;



import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.settings.IslandSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс IslandField представляет собой поле - остров с клетками (локациями),
 * где живут различные виды животных и растения.
 */
public class IslandField {

    private final Location[][] locations;
    private int iterationCount = 0; // Счетчик прошедших итераций (дней)

    public IslandField(int y, int x) {
        this.locations = new Location[x][y];
    }

    public Location[][] getLocations() {
        return this.locations;
    }

    public Location getLocation(int yPosition, int xPosition) {
        return locations[xPosition][yPosition];
    }

    public void output() {


        for (int y = 0; y < locations[y].length; y++) {
            for (Location[] location : locations) {
                System.out.print(location[y]);
            }
            System.out.println();
        }


        System.out.println("\n*******************************************************************************************\n");
        // Вывод информации о жизни на острове
        System.out.println("Статистика острова:");

        // Подсчет количества животных каждого вида
        Map<Class<? extends Animal>, Integer> animalCountMap = new HashMap<>();
        for (int y = 0; y < locations[y].length; y++) {
            for (Location[] value : locations) {
                Location location = value[y];
                for (Class<? extends Animal> animalClass : IslandSettings.ANIMAL_CLASSES) {
                    int animalCount = location.getAnimals().get(animalClass).size();
                    animalCountMap.put(animalClass, animalCountMap.getOrDefault(animalClass, 0) + animalCount);
                }
            }
        }

        // Вывод количества животных каждого вида
        for (Map.Entry<Class<? extends Animal>, Integer> entry : animalCountMap.entrySet()) {
            Class<? extends Animal> animalClass = entry.getKey();
            int count = entry.getValue();
            System.out.println(animalClass.getSimpleName() + ": " + count);
        }

        // Вывод общего количества растений на острове
        int totalPlantCount = 0;
        for (int y = 0; y < locations[y].length; y++) {
            for (Location[] value : locations) {
                Location location = value[y];
                totalPlantCount += location.getPlant();
            }
        }
        System.out.println("Общее количество растений: " + totalPlantCount);


        // Вывод информации о прошедших итерациях (днях)
        // Output information about the passed iterations (days)
        System.out.println("Прошедшие дни: " + iterationCount + " из 30");
        increaseIterationCount();
        System.out.println("\n*******************************************************************************************\n");


    }

    // Метод для увеличения счетчика прошедших итераций (дней)
    // Method to increase the iteration count (number of days)
    public void increaseIterationCount() {
        iterationCount++;
    }



}
