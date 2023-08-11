package com.javarush.island.shevchenko.entities.animals.herbivores;

import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.entities.organism.interfaces.*;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;

/**
 * Класс Duck представляет существо утка на острове животных.
 * Утки являются всеядными и используют поведение из интерфейса Omnivore для питания и охоты.
 */
public class Duck extends Animal implements Omnivore {

    public Duck() {
        this.aClass = Duck.class;
        this.weight = Randomizer.getRandom(IslandSettings.ANIMAL_PARAMETERS.get(Duck.class)[0] / 2, IslandSettings.ANIMAL_PARAMETERS.get(Duck.class)[0]);
    }
}
