package com.javarush.island.shevchenko.entities.organism.interfaces;

import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;
import com.javarush.island.shevchenko.entities.islandMap.Location;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * Интерфейс Omnivore предоставляет функциональность для всеядных существ на острове животных.
 * Он содержит метод eat, который определяет, как всеядное существо будет питаться на определенной локации.
 */


public interface Omnivore {

    /**
     * Метод eat определяет поведение всеядных существ при питании.
     * Всеядное существо охотится на других животных на локации и пытается их съесть.
     * При успешной охоте, всеядное существо увеличивает свой вес на вес съеденного животного до определенного максимального значения.
     * Если всеядное существо становится насыщенным (его вес достигает определенного предела) или все животные на локации были съедены, процесс питания прекращается.
     * После этого всеядное существо может попытаться съесть растения на локации.
     * Если на локации достаточно растений, всеядное существо увеличивает свой вес на съеденное растение.
     * Если растений недостаточно, всеядное существо увеличивает свой вес только на доступное количество растений.
     *
     * @param location Локация, на которой находится всеядное существо.
     */


    default void eat(Location location) {
        location.getLock().lock();
        Animal omnivores = (Animal) this;
        boolean isAte = false;
        double startingWeightOmnivores = omnivores.weight;
        double maxWeightOmnivores = IslandSettings.ANIMAL_PARAMETERS.get(omnivores.aClass)[0];
        double satiation = IslandSettings.ANIMAL_PARAMETERS.get(omnivores.aClass)[3];
        double differentWeight = maxWeightOmnivores - startingWeightOmnivores;
        try {
            Map<Class<? extends Animal>, Integer> victimsMap = IslandSettings.FEEDING_CHANCES.get(omnivores.aClass);
            if (differentWeight > 0) {
                Iterator<Map.Entry<Class<? extends Animal>, Integer>> victimsMapIterator = victimsMap.entrySet().iterator();
                while (!isAte && victimsMapIterator.hasNext()) {
                    Map.Entry<Class<? extends Animal>, Integer> probabilityPair = victimsMapIterator.next();
                    Class<?> classVictim = probabilityPair.getKey();
                    Integer probability = probabilityPair.getValue();
                    Set<Animal> victims = location.getAnimals().get(classVictim);
                    Iterator<Animal> victimsIterator = victims.iterator();
                    if (Randomizer.getRandom(probability) && !victims.isEmpty() && victimsIterator.hasNext()) {
                        Animal victim = victimsIterator.next();
                        omnivores.weight = Math.min(omnivores.weight + victim.getCurrentWeight(), maxWeightOmnivores);
                        if (omnivores.weight >= startingWeightOmnivores + satiation || omnivores.weight == maxWeightOmnivores) {
                            isAte = true;
                        }
                        victimsIterator.remove();
                    }
                }
            }
            satiation = omnivores.weight - startingWeightOmnivores;
            if (location.getPlant() > satiation) {
                location.setPlant(Math.max(location.getPlant() - satiation, 0));
                omnivores.weight = Math.min(omnivores.getCurrentWeight() + satiation, IslandSettings.ANIMAL_PARAMETERS.get(omnivores.aClass)[0]);
            } else {
                omnivores.weight = omnivores.weight + location.getPlant();
                location.setPlant(0);
            }
        } finally {
            location.getLock().unlock();
        }
    }
}
