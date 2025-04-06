package ru.cooper.island.core.model.animal.herbivores

import ru.cooper.island.core.model.animal.util.initAnimal
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.traits.Omnivore

/**
 * Мышь (травоядное/всеядное животное)
 * Mouse (herbivorous/omnivorous animal)
 */
class Mouse : Animal(), Omnivore {
    init { initAnimal<Mouse>() }
}
