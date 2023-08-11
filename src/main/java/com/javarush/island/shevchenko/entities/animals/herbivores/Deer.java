package com.javarush.island.shevchenko.entities.animals.herbivores;

import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.entities.organism.interfaces.*;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;

/**
 * Класс Deer представляет существо лось на острове животных.
 * Лоси являются травоядными и используют поведение из интерфейса Herbivorous для питания.
 */
public class Deer extends Animal implements Herbivorous {

    public Deer() {
        this.aClass = Deer.class;
        this.weight = Randomizer.getRandom(IslandSettings.ANIMAL_PARAMETERS.get(Deer.class)[0] / 2, IslandSettings.ANIMAL_PARAMETERS.get(Deer.class)[0]);
    }
}
