package com.javarush.island.shevchenko.entities.animals.herbivores;

import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.entities.organism.interfaces.*;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;

/**
 * Класс Rabbit представляет существо кролик на острове животных.
 * Кролики являются травоядными и используют поведение из интерфейса Herbivorous для питания.
 */
public class Rabbit extends Animal implements Herbivorous {

    public Rabbit() {
        this.aClass = Rabbit.class;
        this.weight = Randomizer.getRandom(IslandSettings.ANIMAL_PARAMETERS.get(Rabbit.class)[0] / 2, IslandSettings.ANIMAL_PARAMETERS.get(Rabbit.class)[0]);
    }
}
