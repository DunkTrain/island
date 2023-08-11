package com.javarush.island.shevchenko.entities.animals.herbivores;

import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.entities.organism.interfaces.*;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;

/**
 * Класс Sheep представляет существо овца на острове животных.
 * Овцы являются травоядными и используют поведение из интерфейса Herbivorous для питания.
 */
public class Sheep extends Animal implements Herbivorous {
    public Sheep() {
        super();
        this.aClass = Sheep.class;
        this.weight = Randomizer.getRandom(IslandSettings.ANIMAL_PARAMETERS.get(Sheep.class)[0] / 2, IslandSettings.ANIMAL_PARAMETERS.get(Sheep.class)[0]);
    }
}
