package ru.cooper.island.core.model.animal.predators

import ru.cooper.island.core.model.animal.util.initAnimal
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.traits.Omnivore

/**
 * Медведь (всеядное животное)
 * Bear (omnivorous animal)
 */
class Bear : Animal(), Omnivore {
    init { initAnimal<Bear>()}
}
