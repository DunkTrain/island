package com.javarush.island.shevchenko.entities.animals.herbivores;


import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.entities.organism.interfaces.*;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;

/**
 * Класс Boar представляет существо кабана на острове животных.
 * Кабаны являются всеядными и используют поведение из интерфейса Omnivore для питания и охоты.
 */
public class Boar extends Animal implements Omnivore {
    public Boar() {
        this.aClass = Boar.class;
        this.weight = Randomizer.getRandom(IslandSettings.ANIMAL_PARAMETERS.get(Boar.class)[0] / 2, IslandSettings.ANIMAL_PARAMETERS.get(Boar.class)[0]);
    }
}
