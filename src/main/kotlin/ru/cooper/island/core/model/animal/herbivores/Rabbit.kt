package ru.cooper.island.core.model.animal.herbivores

import ru.cooper.island.core.model.animal.util.initAnimal
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.traits.Herbivorous

class Rabbit : Animal(), Herbivorous {
    init { initAnimal<Rabbit>() }
}
