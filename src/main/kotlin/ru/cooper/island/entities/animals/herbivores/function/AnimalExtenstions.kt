package ru.cooper.island.entities.animals.herbivores.function

import ru.cooper.island.entities.organism.Animal
import ru.cooper.island.settings.IslandSettings
import ru.cooper.island.util.Randomizer

inline fun <reified T : Animal> Animal.initAnimal() {
    this.aClass = T::class.java
    val params = requireNotNull(IslandSettings.ANIMAL_PARAMETERS[T::class.java]) {
        "Параметры для ${T::class.simpleName} не найдены в IslandSettings!"
    }
    this.weight = Randomizer.getRandom(params[0] / 2, params[0])
}
