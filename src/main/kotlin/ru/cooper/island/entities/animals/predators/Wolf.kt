package ru.cooper.island.entities.animals.predators

import ru.cooper.island.entities.organism.Animal
import ru.cooper.island.entities.organism.interfaces.Predators
import ru.cooper.island.settings.IslandSettings
import ru.cooper.island.util.Randomizer

/**
 * Класс Wolf представляет существо волка на острове животных.
 * Волки являются хищниками и используют поведение из интерфейса Predators для питания и охоты.
 */
class Wolf : Animal(), Predators {
    init {
        this.aClass = Wolf::class.java
        this.weight = Randomizer.getRandom(
            IslandSettings.ANIMAL_PARAMETERS[Wolf::class.java]!![0] / 2,
            IslandSettings.ANIMAL_PARAMETERS[Wolf::class.java]!![0]
        )
    }
}
