package ru.cooper.island.services.simulation.plant

import ru.cooper.island.core.model.map.IslandCell

/**
 * Класс PlantGrowthTask представляет собой задачу роста растений на определенной локации
 * на острове животных. Класс реализует интерфейс Runnable, позволяя выполнять рост растений
 * в отдельном потоке.
 */
class PlantGrowthTask(private val islandCell: IslandCell) : Runnable {
    fun growUp() {
        islandCell.growPlants()
    }

    override fun run() {
        islandCell.lock.lock()
        try {
            growUp()
        } finally {
            islandCell.lock.unlock()
        }
    }
}
