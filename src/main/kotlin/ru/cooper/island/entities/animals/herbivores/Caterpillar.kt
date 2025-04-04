package ru.cooper.island.entities.animals.herbivores

import ru.cooper.island.entities.animals.herbivores.function.initAnimal
import ru.cooper.island.entities.islandMap.Location
import ru.cooper.island.entities.organism.Animal
import ru.cooper.island.entities.organism.interfaces.Herbivorous

class Caterpillar : Animal(), Herbivorous {
    init { initAnimal<Caterpillar>() }

    override fun move(location: Location) {
        // Реализация движения
    }
}
