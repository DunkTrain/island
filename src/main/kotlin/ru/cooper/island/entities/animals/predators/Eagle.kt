package ru.cooper.island.entities.animals.predators

import ru.cooper.island.entities.organism.Animal
import ru.cooper.island.entities.organism.interfaces.Omnivore
import ru.cooper.island.settings.IslandSettings
import ru.cooper.island.util.Randomizer

/**
 * Класс Eagle представляет существо орла на острове животных.
 * Орлы являются всеядными и используют поведение из интерфейса Omnivore для питания и охоты.
 */
class Eagle : Animal(), Omnivore {
    init {
        this.aClass = Eagle::class.java
        this.weight = Randomizer.getRandom(
            IslandSettings.ANIMAL_PARAMETERS[Eagle::class.java]!![0] / 2,
            IslandSettings.ANIMAL_PARAMETERS[Eagle::class.java]!![0]
        )
    }
}
