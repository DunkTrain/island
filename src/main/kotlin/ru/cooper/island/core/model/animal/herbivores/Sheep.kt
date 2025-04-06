package ru.cooper.island.core.model.animal.herbivores

import ru.cooper.island.core.model.animal.util.initAnimal
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.traits.Herbivorous

/**
 * Овца (травоядное животное)
 * Sheep (herbivorous animal)
 */
class Sheep : Animal(), Herbivorous {
    init { initAnimal<Sheep>() }
}