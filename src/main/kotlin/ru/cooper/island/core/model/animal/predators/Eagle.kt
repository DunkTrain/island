package ru.cooper.island.core.model.animal.predators

import ru.cooper.island.core.model.animal.util.initAnimal
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.traits.Omnivore

/**
 * Орёл (всеядное животное)
 * Eagle (omnivorous animal)
 */
class Eagle : Animal(), Omnivore {
    init { initAnimal<Eagle>()}
}
