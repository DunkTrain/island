package ru.cooper.island.core.model.animal.herbivores

import ru.cooper.island.core.model.animal.util.initAnimal
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.traits.Herbivorous

/**
 * Буйвол (травоядное животное)
 * Buffalo (herbivorous animal)
 */
class Buffalo : Animal(), Herbivorous {
    init { initAnimal<Buffalo>() }
}
