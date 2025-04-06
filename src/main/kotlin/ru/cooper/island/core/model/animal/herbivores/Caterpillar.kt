package ru.cooper.island.core.model.animal.herbivores

import ru.cooper.island.core.model.animal.util.initAnimal
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.traits.Herbivorous

/**
 * Гусеница (травоядное животное)
 * Caterpillar (herbivorous animal)
 */
class Caterpillar : Animal(), Herbivorous {
    init { initAnimal<Caterpillar>() }
}
