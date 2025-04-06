package ru.cooper.island.core.model.animal.herbivores

import ru.cooper.island.core.model.animal.util.initAnimal
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.traits.Omnivore

/**
 * Кабан (травоядное/всеядное животное)
 * Boar (herbivorous/omnivorous animal)
 */
class Boar : Animal(), Omnivore {
    init { initAnimal<Boar>() }
}
