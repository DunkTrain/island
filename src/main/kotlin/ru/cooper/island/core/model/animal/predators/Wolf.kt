package ru.cooper.island.core.model.animal.predators

import ru.cooper.island.core.model.animal.util.initAnimal
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.traits.Predators

/**
 * Волк (хищное животное)
 * Wolf (predatory animal)
 */
class Wolf : Animal(), Predators {
    init { initAnimal<Wolf>()}
}
