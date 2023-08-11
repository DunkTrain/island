package com.javarush.island.shevchenko.entities.animals.herbivores;

import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.entities.organism.interfaces.*;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;

/**
 * Класс Mouse представляет существо мышь на острове животных.
 * Мыши являются всеядными и используют поведение из интерфейса Omnivore для питания и охоты.
 */
public class Mouse extends Animal implements Omnivore {

    public Mouse() {
        this.aClass = Mouse.class;
        this.weight = Randomizer.getRandom(IslandSettings.ANIMAL_PARAMETERS.get(Mouse.class)[0] / 2, IslandSettings.ANIMAL_PARAMETERS.get(Mouse.class)[0]);
    }
}
