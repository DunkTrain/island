package ru.cooper.island.entities.organism

import ru.cooper.island.entities.islandMap.Location
import ru.cooper.island.settings.IslandSettings
import ru.cooper.island.util.Randomizer
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * Animal - абстрактный класс, представляющий общие характеристики и поведение для всех животных на острове в симуляции.
 * Он расширяет класс Nature и реализует интерфейс Cloneable, что позволяет использовать клонирование объектов для животных.
 * Каждый экземпляр класса Animal представляет одного животного на острове.
 */
abstract class Animal : Nature(), Cloneable {
    var aClass: Class<out Animal> = javaClass

    @JvmField
    val lock: Lock = ReentrantLock(true)

    fun weightLoss(location: Location) {
        location.lock.lock()
        try {
            weight = weight - weight / 10
        } finally {
            location.lock.unlock()
        }
    }

    fun timeToDie(location: Location) {
        location.lock.lock()
        try {
            if (weight < IslandSettings.ANIMAL_PARAMETERS[aClass]!![0] / 3) {
                location.removeAnimal(this)
            }
        } finally {
            location.lock.unlock()
        }
    }

    fun reproduction(location: Location) {
        location.lock.lock()
        try {
            val animals: Set<Animal>? = location.animals[aClass]
            if (weight == IslandSettings.ANIMAL_PARAMETERS[aClass]!![0] && animals!!.size > 1) {
                val clone = this.clone()
                location.addAnimalToLocation(clone)
                weightLoss(location)
            }
        } finally {
            location.lock.unlock()
        }
    }

    open fun move(location: Location) {
        location.lock.lock()
        val newLocation = choiceOfAvailableLocation(location)
        try {
            if (newLocation.isThereEnoughSpace(this.aClass)) {
                newLocation.addAnimalToLocation(this)
                location.removeAnimal(this)
            }
        } finally {
            location.lock.unlock()
        }
    }

    private fun choiceOfAvailableLocation(location: Location): Location {
        var location = location
        val steps = maxNumberOfStepsAnimal
        for (i in steps downTo 0) {
            location = location.neighboringLocations[ThreadLocalRandom.current()
                .nextInt(0, location.neighboringLocations.size)]
        }
        return location
    }

    public override fun clone(): Animal {
        try {
            val clone = super.clone() as Animal
            clone.weight = Randomizer.getRandom(
                IslandSettings.ANIMAL_PARAMETERS[aClass]!![0] / 1.5, IslandSettings.ANIMAL_PARAMETERS[aClass]!![0]
            )
            return clone
        } catch (e: CloneNotSupportedException) {
            throw AssertionError()
        }
    }

    val currentWeight: Double
        get() = weight

    private val maxNumberOfStepsAnimal: Int
        get() = IslandSettings.ANIMAL_PARAMETERS[aClass]!![2].toInt()
}
