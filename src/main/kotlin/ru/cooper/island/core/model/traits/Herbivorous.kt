package ru.cooper.island.core.model.traits

import ru.cooper.island.core.model.map.IslandCell
import ru.cooper.island.core.model.Animal
import ru.cooper.island.config.SimulationConfig

interface Herbivorous {

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
