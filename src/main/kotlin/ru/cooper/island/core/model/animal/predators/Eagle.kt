package ru.cooper.island.core.model.animal.predators

import ru.cooper.island.core.model.animal.util.initAnimal
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.traits.Omnivore

class Eagle : Animal(), Omnivore {
    init { initAnimal<Eagle>()}
}
