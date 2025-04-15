package ru.cooper.island.core.model.traits

import ru.cooper.island.core.model.map.IslandCell
import ru.cooper.island.core.model.Animal
import ru.cooper.island.config.SimulationConfig
import ru.cooper.island.core.model.traits.util.HuntingUtils

interface Predators {

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
