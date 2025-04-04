package ru.cooper.island.entities.animals.predators

import ru.cooper.island.entities.organism.Animal
import ru.cooper.island.entities.organism.interfaces.Predators
import ru.cooper.island.settings.IslandSettings
import ru.cooper.island.util.Randomizer

/**
 * Класс Snake представляет существо змеи на острове животных.
 * Змеи являются хищниками и используют поведение из интерфейса Predators для питания и охоты.
 */
class Snake : Animal(), Predators {
    init {
        this.aClass = Snake::class.java
        this.weight = Randomizer.getRandom(
            IslandSettings.ANIMAL_PARAMETERS[Snake::class.java]!![0] / 2,
            IslandSettings.ANIMAL_PARAMETERS[Snake::class.java]!![0]
        )
    }
}
