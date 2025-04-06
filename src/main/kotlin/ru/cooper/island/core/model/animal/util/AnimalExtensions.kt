package ru.cooper.island.core.model.animal.util

import ru.cooper.island.core.model.Animal
import ru.cooper.island.config.SimulationConfig
import ru.cooper.island.util.Randomizer

/**
 * Инициализирует параметры животного на основе настроек
 * Initializes animal parameters based on settings
 *
 * @param T тип животного (animal type)
 * @param initializer функция инициализации (initialization function)
 * @throws IllegalStateException если параметры не найдены (if parameters not found)
 */
inline fun <reified T : Animal> Animal.initAnimal(
    initializer: (DoubleArray) -> Unit = { params ->
        this.aClass = T::class.java
        this.weight = Randomizer.getRandom(params[0] / 2, params[0])
    }
) {
    val params = requireNotNull(SimulationConfig.ANIMAL_PARAMETERS[T::class.java]) {
        "Параметры для ${T::class.simpleName} не найдены в IslandSettings!"
        "Parameters for ${T::class.simpleName} not found in IslandSettings!"
    }
    initializer(params)
}
