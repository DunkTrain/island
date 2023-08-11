package com.javarush.island.shevchenko.entities.animals.predators;

import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.entities.organism.interfaces.*;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;

/**
 * Класс Wolf представляет существо волка на острове животных.
 * Волки являются хищниками и используют поведение из интерфейса Predators для питания и охоты.
 */
public class Wolf extends Animal implements Predators {
    public Wolf() {
        this.aClass = Wolf.class;
        this.weight = Randomizer.getRandom(IslandSettings.ANIMAL_PARAMETERS.get(Wolf.class)[0] / 2, IslandSettings.ANIMAL_PARAMETERS.get(Wolf.class)[0]);
    }
}
