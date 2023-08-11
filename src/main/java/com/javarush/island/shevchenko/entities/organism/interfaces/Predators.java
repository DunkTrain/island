package com.javarush.island.shevchenko.entities.organism.interfaces;

import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;
import com.javarush.island.shevchenko.entities.islandMap.Location;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Интерфейс Predators предоставляет функциональность для хищников на острове животных.
 * Он содержит метод eat, который определяет, как хищник будет питаться и охотиться на других животных на определенной локации.
 */


public interface Predators {


    /**
     * Метод eat определяет поведение хищников при питании.
     * Хищник охотится на других животных на локации и пытается их съесть.
     * При успешной охоте, хищник увеличивает свой вес на вес съеденного животного до определенного максимального значения.
     * Если хищник становится насыщенным (его вес достигает определенного предела) или все животные на локации были съедены, процесс питания прекращается.
     *
     * @param location Локация, на которой находится хищник.
     */


    default void eat(Location location) {
        location.getLock().lock();
        Animal carnivorous = (Animal) this;
        boolean isAte = false;
        double startingWeightCarnivorous = carnivorous.weight;
        double maxWeightCarnivorous = IslandSettings.ANIMAL_PARAMETERS.get(carnivorous.aClass)[0];
        double satiation = IslandSettings.ANIMAL_PARAMETERS.get(carnivorous.aClass)[3];
        double differentWeight = maxWeightCarnivorous - startingWeightCarnivorous;
        try {
            Map<Class<?extends Animal>, Integer> victimsMap = IslandSettings.FEEDING_CHANCES.get(carnivorous.aClass);
            if (differentWeight > 0) {
                Iterator<Map.Entry<Class<?extends Animal>, Integer>> victimsMapIterator = victimsMap.entrySet().iterator();
                while (!isAte && victimsMapIterator.hasNext()) {
                    Map.Entry<Class<?extends Animal>, Integer> probabilityPair = victimsMapIterator.next();
                    Class<?> classVictim = probabilityPair.getKey();
                    Integer probability = probabilityPair.getValue();
                    Set<Animal> victims = location.getAnimals().get(classVictim);
                    Iterator<Animal> victimsIterator = victims.iterator();
                    if (Randomizer.getRandom(probability) && !victims.isEmpty() && victimsIterator.hasNext()) {
                        Animal victim = victimsIterator.next();
                        carnivorous.weight = Math.min(carnivorous.weight + victim.getCurrentWeight(), maxWeightCarnivorous);
                        if (carnivorous.weight >= startingWeightCarnivorous + satiation || carnivorous.weight == maxWeightCarnivorous) {
                            isAte = true;
                        }
                        victimsIterator.remove();
                    }
                }
            }
        } finally {
            location.getLock().unlock();
        }
    }
}
