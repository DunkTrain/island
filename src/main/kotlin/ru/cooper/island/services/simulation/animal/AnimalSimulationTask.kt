package ru.cooper.island.services.simulation.animal

import ru.cooper.island.core.model.map.IslandCell
import ru.cooper.island.core.model.Animal
import ru.cooper.island.core.model.traits.Herbivorous
import ru.cooper.island.core.model.traits.Omnivore
import ru.cooper.island.core.model.traits.Predators

/**
 * Класс AnimalSimulationTask представляет собой задачу симуляции жизни для конкретного животного
 * на определенной локации на острове животных. Класс реализует интерфейс Runnable, позволяя
 * выполнять симуляцию в отдельном потоке.
 */
class AnimalSimulationTask(private val animal: Animal, private val islandCell: IslandCell) : Runnable {
    override fun run() {
        animal.lock.lock()
        try {
            if (animal is Predators) {
                animal.eat(islandCell)
            } else if (animal is Herbivorous) {
                animal.eat(islandCell)
            } else if (animal is Omnivore) {
                animal.eat(islandCell)
            } else {
                throw IllegalArgumentException("Unknown animal type: " + animal.javaClass.name)
            }
        } catch (ex: Exception) {
            System.err.println("Error during animal simulation task: " + ex.message)
            ex.printStackTrace()
        } finally {
            animal.lock.unlock()
        }
        animal.reproduction(islandCell)
        animal.weightLoss(islandCell)
        animal.timeToDie(islandCell)
        try {
            animal.move(islandCell)
        } catch (ex: Exception) {
            System.err.println("Error during animal movement: " + ex.message)
            ex.printStackTrace()
        } finally {
            animal.lock.unlock()
        }
    }
}
