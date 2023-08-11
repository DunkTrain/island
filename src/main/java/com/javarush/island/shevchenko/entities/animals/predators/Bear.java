package com.javarush.island.shevchenko.entities.animals.predators;

import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.entities.organism.interfaces.*;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;

/**
 * Класс Bear представляет существо медведь на острове животных.
 * Медведи являются всеядными и используют поведение из интерфейса Omnivore для питания и охоты.
 */
public class Bear extends Animal implements Omnivore {

    public Bear() {
        this.aClass = Bear.class;
        this.weight = Randomizer.getRandom(IslandSettings.ANIMAL_PARAMETERS.get(Bear.class)[0] / 2, IslandSettings.ANIMAL_PARAMETERS.get(Bear.class)[0]);
    }
}
