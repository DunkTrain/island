package ru.cooper.island.core.model.animal.predators

import ru.cooper.island.core.model.animal.util.initAnimal
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.traits.Predators

/**
 * Змея (хищное животное)
 * Snake (predatory animal)
 */
class Snake : Animal(), Predators {
    init { initAnimal<Snake>()}
}
