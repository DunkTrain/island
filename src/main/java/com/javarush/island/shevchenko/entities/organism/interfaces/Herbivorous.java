package com.javarush.island.shevchenko.entities.organism.interfaces;

import com.javarush.island.shevchenko.entities.islandMap.Location;
import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.settings.IslandSettings;


/**
 * Интерфейс Herbivorous предоставляет функциональность для травоядных существ на острове животных.
 * Он содержит метод eat, который определяет, как травоядное существо будет питаться на определенной локации.
 */



public interface Herbivorous {

    /**
     * Метод eat определяет поведение травоядных существ при питании.
     * Травоядное существо питается растениями на локации.
     * Если на локации достаточно растений, травоядное существо увеличивает свой вес на вес съеденного растения.
     * Если растений недостаточно, травоядное существо увеличивает свой вес только на доступное количество растений.
     *
     * @param location Локация, на которой находится травоядное существо.
     */

    default void eat(Location location) {
        location.getLock().lock();
        Animal herbivorous = (Animal) this;
        double satiation = IslandSettings.ANIMAL_PARAMETERS.get(herbivorous.aClass)[3];
        try {
            if (location.getPlant() > satiation) {
                location.setPlant(Math.max(location.getPlant() - satiation, 0));
                herbivorous.weight = Math.min(herbivorous.getCurrentWeight() + satiation, IslandSettings.ANIMAL_PARAMETERS.get(herbivorous.aClass)[0]);
            } else {
                herbivorous.weight = herbivorous.weight + location.getPlant();
                location.setPlant(0);
            }
        } finally {
            location.getLock().unlock();
        }
    }
}
