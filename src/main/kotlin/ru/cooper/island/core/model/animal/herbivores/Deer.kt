package ru.cooper.island.core.model.animal.herbivores

import ru.cooper.island.core.model.animal.util.initAnimal
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.traits.Herbivorous

/**
 * Олень (травоядное животное)
 * Deer (herbivorous animal)
 */
class Deer : Animal(), Herbivorous {
    init { initAnimal<Deer>() }
}
