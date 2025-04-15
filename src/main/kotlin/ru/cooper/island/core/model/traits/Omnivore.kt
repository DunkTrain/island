package ru.cooper.island.core.model.traits

import ru.cooper.island.core.model.map.IslandCell
import ru.cooper.island.core.model.Animal
import ru.cooper.island.config.SimulationConfig
import ru.cooper.island.core.model.traits.util.HuntingUtils
import kotlin.math.min

interface Omnivore {

    fun eat(islandCell: IslandCell) {
        islandCell.lock.lock()
        try {
            val omnivore = this as Animal
            val maxWeight = SimulationConfig.ANIMAL_PARAMETERS[omnivore.aClass]!![0]
            val remainingHunger = maxWeight - omnivore.weight

            if (remainingHunger > 0) {
                HuntingUtils.hunt(islandCell, omnivore)
            }

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
