package com.javarush.island.shevchenko.entities.animals.herbivores;

import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.entities.organism.interfaces.*;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;

/**
 * Класс Horse представляет существо единорог на острове животных.
 * Лошади (единороги) являются травоядными и используют поведение из интерфейса Herbivorous для питания.
 */
public class Horse extends Animal implements Herbivorous {

    public Horse() {
        this.aClass = Horse.class;
        this.weight = Randomizer.getRandom(IslandSettings.ANIMAL_PARAMETERS.get(Horse.class)[0] / 2, IslandSettings.ANIMAL_PARAMETERS.get(Horse.class)[0]);
    }
}
