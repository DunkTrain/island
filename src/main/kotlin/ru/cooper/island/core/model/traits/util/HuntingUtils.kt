package ru.cooper.island.core.model.traits.util

import ru.cooper.island.core.model.map.IslandCell
import ru.cooper.island.core.model.Animal
import ru.cooper.island.config.SimulationConfig
import ru.cooper.island.util.Randomizer

object HuntingUtils {
    fun hunt(islandCell: IslandCell, hunter: Animal) {
        SimulationConfig.FEEDING_CHANCES[hunter.aClass]?.forEach { (preyClass, probability) ->
            if (Randomizer.getRandom(probability)) {
                islandCell.animals[preyClass]?.firstOrNull()?.let { prey ->
                    hunter.weight = (hunter.weight + prey.currentWeight)
                        .coerceAtMost(SimulationConfig.ANIMAL_PARAMETERS[hunter.aClass]!![0])
                    islandCell.removeAnimal(prey)
                }
            }
        }
    }
}
