package com.javarush.island.shevchenko.entities.animals.predators;

import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.entities.organism.interfaces.*;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;

/**
 * Класс Eagle представляет существо орла на острове животных.
 * Орлы являются всеядными и используют поведение из интерфейса Omnivore для питания и охоты.
 */
public class Eagle extends Animal implements Omnivore {

    public Eagle() {
        this.aClass = Eagle.class;
        this.weight = Randomizer.getRandom(IslandSettings.ANIMAL_PARAMETERS.get(Eagle.class)[0] / 2, IslandSettings.ANIMAL_PARAMETERS.get(Eagle.class)[0]);
    }
}
