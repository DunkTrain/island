package ru.cooper.island.core.model.animal.herbivores

import ru.cooper.island.core.model.animal.util.initAnimal
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.traits.Omnivore

/**
 * Утка (травоядное/всеядное животное)
 * Duck (herbivorous/omnivorous animal)
 */
class Duck : Animal(), Omnivore {
    init { initAnimal<Duck>() }
}
