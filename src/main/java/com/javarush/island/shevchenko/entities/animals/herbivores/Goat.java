package com.javarush.island.shevchenko.entities.animals.herbivores;

import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.entities.organism.interfaces.*;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;

/**
 * Класс Goat представляет существо козел на острове животных.
 * Козлы являются травоядными и используют поведение из интерфейса Herbivorous для питания.
 */
public class Goat extends Animal implements Herbivorous {

    public Goat() {
        this.aClass = Goat.class;
        this.weight = Randomizer.getRandom(IslandSettings.ANIMAL_PARAMETERS.get(Goat.class)[0] / 2, IslandSettings.ANIMAL_PARAMETERS.get(Goat.class)[0]);
    }
}
