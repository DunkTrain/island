package ru.cooper.island.core.model.traits

import ru.cooper.island.core.model.map.IslandCell
import ru.cooper.island.core.model.Animal
import ru.cooper.island.config.SimulationConfig
import ru.cooper.island.core.model.traits.util.HuntingUtils
import kotlin.math.min

/**
 * Определяет поведение всеядных животных при питании.
 * Defines omnivorous animal feeding behavior.
 *
 * Всеядные могут:
 * 1. Охотиться на других животных
 * 2. Питаться растениями
 *
 * Omnivores can:
 * 1. Hunt other animals
 * 2. Eat plants
 */
interface Omnivore {

    /**
     * Реализует стратегию питания всеядного животного.
     * Implements omnivore feeding strategy.
     *
     * @param islandCell Текущая локация животного / Animal's current location
     *
     * Алгоритм:
     * 1. Сначала пытается охотиться на других животных
     * 2. Затем питается растениями (если остался голодным)
     *
     * Algorithm:
     * 1. First tries to hunt other animals
     * 2. Then eats plants (if still hungry)
     */
    fun eat(islandCell: IslandCell) {
        islandCell.lock.lock()
        try {
            val omnivore = this as Animal
            val maxWeight = SimulationConfig.ANIMAL_PARAMETERS[omnivore.aClass]!![0]
            val remainingHunger = maxWeight - omnivore.weight

            // Фаза охоты / Hunting phase
            if (remainingHunger > 0) {
                HuntingUtils.hunt(islandCell, omnivore)
            }

            // Фаза поедания растений / Plant eating phase
            if (islandCell.plant > 0) {
                val currentHunger = maxWeight - omnivore.weight
                if (currentHunger > 0) {
                    val plantsToEat = min(islandCell.plant, currentHunger)
                    omnivore.weight += plantsToEat
                    islandCell.plant -= plantsToEat
                }
            }
        } finally {
            islandCell.lock.unlock()
        }
    }
}
