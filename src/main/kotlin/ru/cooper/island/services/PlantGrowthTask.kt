package ru.cooper.island.services

import ru.cooper.island.entities.islandMap.Location

/**
 * Класс PlantGrowthTask представляет собой задачу роста растений на определенной локации
 * на острове животных. Класс реализует интерфейс Runnable, позволяя выполнять рост растений
 * в отдельном потоке.
 */
class PlantGrowthTask(private val location: Location) : Runnable {
    fun growUp() {
        location.plantGrowth()
    }

    override fun run() {
        location.lock.lock()
        try {
            growUp()
        } finally {
            location.lock.unlock()
        }
    }
}
