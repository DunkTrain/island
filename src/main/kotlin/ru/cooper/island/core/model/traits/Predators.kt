package ru.cooper.island.core.model.traits

import ru.cooper.island.core.model.map.IslandCell
import ru.cooper.island.core.model.Animal
import ru.cooper.island.config.SimulationConfig
import ru.cooper.island.core.model.traits.util.HuntingUtils

/**
 * Определяет поведение хищных животных при охоте.
 * Defines predatory animals' hunting behavior.
 */
interface Predators {

    /**
     * Реализует стратегию охоты хищника.
     * Implements predator hunting strategy.
     *
     * @param islandCell Текущая локация хищника / Predator's current location
     *
     * Алгоритм:
     * 1. Проверяет доступных жертв
     * 2. Атакует с учетом вероятности успеха
     * 3. Прекращает при насыщении
     *
     * Algorithm:
     * 1. Checks available prey
     * 2. Attacks with success probability
     * 3. Stops when satiated
     */
    fun eat(islandCell: IslandCell) {
        islandCell.lock.lock()
        try {
            val predator = this as Animal
            val maxWeight = SimulationConfig.ANIMAL_PARAMETERS[predator.aClass]!![0]
            val remainingHunger = maxWeight - predator.weight

            if (remainingHunger > 0) {
                HuntingUtils.hunt(islandCell, predator)
            }
        } finally {
            islandCell.lock.unlock()
        }
    }
}
