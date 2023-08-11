package com.javarush.island.shevchenko.entities.animals.herbivores;

import com.javarush.island.shevchenko.entities.islandMap.Location;
import com.javarush.island.shevchenko.entities.organism.Animal;
import com.javarush.island.shevchenko.entities.organism.interfaces.*;
import com.javarush.island.shevchenko.settings.IslandSettings;
import com.javarush.island.shevchenko.util.Randomizer;

/**
 * Класс Caterpillar представляет существо гусеницы на острове животных.
 * Гусеницы являются травоядными и используют поведение из интерфейса Herbivorous для питания.
 */
public class Caterpillar extends Animal implements Herbivorous {
    public Caterpillar() {
        this.aClass = Caterpillar.class;
        this.weight = Randomizer.getRandom(IslandSettings.ANIMAL_PARAMETERS.get(Caterpillar.class)[0] / 2, IslandSettings.ANIMAL_PARAMETERS.get(Caterpillar.class)[0]);
    }

    @Override
    public void move(Location location) {

    }
}
