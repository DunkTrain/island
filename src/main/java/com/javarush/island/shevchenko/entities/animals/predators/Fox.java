package com.javarush.island.shevchenko.entities.animals.predators;

import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.entities.organism.interfaces.*;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;

/**
 * Класс Fox представляет существо лиса на острове животных.
 * Лисы являются хищниками и используют поведение из интерфейса Predators для питания и охоты.
 */
public class Fox extends Animal implements Predators {

    public Fox() {
        this.aClass = Fox.class;
        this.weight = Randomizer.getRandom(IslandSettings.ANIMAL_PARAMETERS.get(Fox.class)[0] / 2, IslandSettings.ANIMAL_PARAMETERS.get(Fox.class)[0]);
    }
}