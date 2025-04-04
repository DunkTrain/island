package ru.cooper.island.services

import ru.cooper.island.entities.islandMap.Location
import ru.cooper.island.entities.organism.Animal
import ru.cooper.island.entities.organism.interfaces.Herbivorous
import ru.cooper.island.entities.organism.interfaces.Omnivore
import ru.cooper.island.entities.organism.interfaces.Predators

/**
 * Класс AnimalSimulationTask представляет собой задачу симуляции жизни для конкретного животного
 * на определенной локации на острове животных. Класс реализует интерфейс Runnable, позволяя
 * выполнять симуляцию в отдельном потоке.
 */
class AnimalSimulationTask(private val animal: Animal, private val location: Location) : Runnable {
    /**
     * Метод run() представляет собой точку входа для выполнения симуляции жизни животного.
     * Внутри метода происходит выполнение действий животного, таких как питание, размножение,
     * потеря веса и перемещение на другую локацию.
     */
    override fun run() {
        animal.lock.lock()
        try {
            if (animal is Predators) {
                animal.eat(location)
            } else if (animal is Herbivorous) {
                animal.eat(location)
            } else if (animal is Omnivore) {
                animal.eat(location)
            } else {
                throw IllegalArgumentException("Unknown animal type: " + animal.javaClass.name)
            }
        } catch (ex: Exception) {
            System.err.println("Error during animal simulation task: " + ex.message)
            ex.printStackTrace()
        } finally {
            animal.lock.unlock()
        }
        animal.reproduction(location)
        animal.weightLoss(location)
        animal.timeToDie(location)
        try {
            animal.move(location)
        } catch (ex: Exception) {
            System.err.println("Error during animal movement: " + ex.message)
            ex.printStackTrace()
        } finally {
            animal.lock.unlock()
        }
    }
}
