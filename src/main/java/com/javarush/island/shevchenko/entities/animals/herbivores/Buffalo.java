package com.javarush.island.shevchenko.entities.animals.herbivores;

import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.entities.organism.interfaces.*;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;


/**
 * Класс Buffalo представляет существо буйвола на острове животных.
 * Буйволы являются травоядными и используют поведение из интерфейса Herbivorous для питания.
 */
public class Buffalo extends Animal implements Herbivorous {

    public Buffalo() {
        this.aClass = Buffalo.class;
        this.weight = Randomizer.getRandom(IslandSettings.ANIMAL_PARAMETERS.get(Buffalo.class)[0] / 2, IslandSettings.ANIMAL_PARAMETERS.get(Buffalo.class)[0]);
    }
}
