package ru.cooper.island.core.model.animal.herbivores

import ru.cooper.island.core.model.animal.util.initAnimal
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.traits.Herbivorous

/**
 * Коза (травоядное животное)
 * Goat (herbivorous animal)
 */
class Goat : Animal(), Herbivorous {
    init { initAnimal<Goat>() }
}
