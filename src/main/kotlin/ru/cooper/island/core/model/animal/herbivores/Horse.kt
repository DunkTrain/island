package ru.cooper.island.core.model.animal.herbivores

import ru.cooper.island.core.model.animal.util.initAnimal
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.traits.Herbivorous

/**
 * Лошадь (травоядное животное)
 * Horse (herbivorous animal)
 */
class Horse : Animal(), Herbivorous {
    init { initAnimal<Horse>() }
}
