package ru.cooper.island.entities.animals.herbivores

import ru.cooper.island.entities.animals.herbivores.function.initAnimal
import ru.cooper.island.entities.organism.Animal
import ru.cooper.island.entities.organism.interfaces.Herbivorous

class Buffalo : Animal(), Herbivorous {
    init { initAnimal<Buffalo>() }
}
