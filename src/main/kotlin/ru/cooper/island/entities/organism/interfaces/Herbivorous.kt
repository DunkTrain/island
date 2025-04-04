package ru.cooper.island.entities.organism.interfaces

import ru.cooper.island.entities.islandMap.Location
import ru.cooper.island.entities.organism.Animal
import ru.cooper.island.settings.IslandSettings
import kotlin.math.max
import kotlin.math.min

/**
 * Интерфейс Herbivorous предоставляет функциональность для травоядных существ на острове животных.
 * Он содержит метод eat, который определяет, как травоядное существо будет питаться на определенной локации.
 */
interface Herbivorous {

    /**
     * Метод eat определяет поведение травоядных существ при питании.
     * Травоядное существо питается растениями на локации.
     * Если на локации достаточно растений, травоядное существо увеличивает свой вес на вес съеденного растения.
     * Если растений недостаточно, травоядное существо увеличивает свой вес только на доступное количество растений.
     *
     * @param location Локация, на которой находится травоядное существо.
     */
    fun eat(location: Location) {
        location.lock.lock()
        val herbivorous = this as Animal
        val satiation = IslandSettings.ANIMAL_PARAMETERS[herbivorous.aClass]!![3]
        try {
            if (location.plant > satiation) {
                location.plant = max(location.plant - satiation, 0.0)
                herbivorous.weight = min(
                    herbivorous.currentWeight + satiation,
                    IslandSettings.ANIMAL_PARAMETERS[herbivorous.aClass]!![0]
                )
            } else {
                herbivorous.weight = herbivorous.weight + location.plant
                location.plant = 0.0
            }
        } finally {
            location.lock.unlock()
        }
    }
}
