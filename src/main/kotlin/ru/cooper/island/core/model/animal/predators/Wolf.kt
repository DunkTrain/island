package ru.cooper.island.core.model.animal.predators

import ru.cooper.island.core.model.animal.util.initAnimal
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.traits.Predators

class Wolf : Animal(), Predators {
    init { initAnimal<Wolf>()}
}
