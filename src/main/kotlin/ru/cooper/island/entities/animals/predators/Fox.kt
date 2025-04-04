package ru.cooper.island.entities.animals.predators

import ru.cooper.island.entities.organism.Animal
import ru.cooper.island.entities.organism.interfaces.Predators
import ru.cooper.island.settings.IslandSettings
import ru.cooper.island.util.Randomizer

/**
 * Класс Fox представляет существо лиса на острове животных.
 * Лисы являются хищниками и используют поведение из интерфейса Predators для питания и охоты.
 */
class Fox : Animal(), Predators {
    init {
        this.aClass = Fox::class.java
        this.weight = Randomizer.getRandom(
            IslandSettings.ANIMAL_PARAMETERS[Fox::class.java]!![0] / 2,
            IslandSettings.ANIMAL_PARAMETERS[Fox::class.java]!![0]
        )
    }
}