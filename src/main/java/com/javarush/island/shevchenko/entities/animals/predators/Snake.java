package com.javarush.island.shevchenko.entities.animals.predators;

import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.entities.organism.interfaces.*;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;
/**
 * Класс Snake представляет существо змеи на острове животных.
 * Змеи являются хищниками и используют поведение из интерфейса Predators для питания и охоты.
 */
public class Snake extends Animal implements Predators {

    public Snake() {
        this.aClass = Snake.class;
        this.weight = Randomizer.getRandom(IslandSettings.ANIMAL_PARAMETERS.get(Snake.class)[0] / 2, IslandSettings.ANIMAL_PARAMETERS.get(Snake.class)[0]);
    }
}
