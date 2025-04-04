package ru.cooper.island.entities.animals.predators

import ru.cooper.island.entities.organism.Animal
import ru.cooper.island.entities.organism.interfaces.Omnivore
import ru.cooper.island.settings.IslandSettings
import ru.cooper.island.util.Randomizer

/**
 * Класс Bear представляет существо медведь на острове животных.
 * Медведи являются всеядными и используют поведение из интерфейса Omnivore для питания и охоты.
 */
class Bear : Animal(), Omnivore {
    init {
        this.aClass = Bear::class.java
        this.weight = Randomizer.getRandom(
            IslandSettings.ANIMAL_PARAMETERS.get(Bear::class.java).get(0) / 2, IslandSettings.ANIMAL_PARAMETERS.get(
                Bear::class.java
            ).get(0)
        )
    }
}
