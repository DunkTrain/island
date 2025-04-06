package ru.cooper.island.core.model.traits

import ru.cooper.island.core.model.map.IslandCell
import ru.cooper.island.core.model.Animal
import ru.cooper.island.config.SimulationConfig

/**
 * Определяет поведение травоядных животных при питании растениями.
 * Defines herbivorous animal behavior when eating plants.
 */
interface Herbivorous {

    /**
     * Позволяет животному питаться растениями в локации.
     * Allows animal to eat plants at the location.
     *
     * @param islandCell Локация, где находится животное / Location where animal is present
     *
     * Логика питания:
     * 1. Если растений больше, чем нужно для насыщения - съедает часть
     * 2. Если растений меньше - съедает все доступные
     *
     * Feeding logic:
     * 1. If plants > needed satiation - consumes part
     * 2. If plants < needed - consumes all available
     */
    fun eat(islandCell: IslandCell) {
        islandCell.lock.lock()
        try {
            val herbivore = this as Animal
            val maxSatiation = SimulationConfig.ANIMAL_PARAMETERS[herbivore.aClass]!![3]
            val maxWeight = SimulationConfig.ANIMAL_PARAMETERS[herbivore.aClass]!![0]

            when {
                islandCell.plant > maxSatiation -> {
                    islandCell.plant -= maxSatiation
                    herbivore.weight = (herbivore.weight + maxSatiation).coerceAtMost(maxWeight)
                }
                else -> {
                    herbivore.weight += islandCell.plant
                    islandCell.plant = 0.0
                }
            }
        } finally {
            islandCell.lock.unlock()
        }
    }
}
