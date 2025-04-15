package ru.cooper.island.core.model

import ru.cooper.island.core.model.map.IslandCell
import ru.cooper.island.config.SimulationConfig
import ru.cooper.island.util.Randomizer
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * Абстрактный базовый класс для всех животных
 * Abstract base class for all animals
 *
 * @property aClass конкретный класс животного
 *                 (specific animal class)
 * @property lock блокировка для потокобезопасных операций
 *                (lock for thread-safe operations)
 * @property currentWeight текущий вес животного
 *                         (current weight of the animal)
 */
abstract class Animal : Nature(), Cloneable {
    var aClass: Class<out Animal> = javaClass

    @JvmField
    val lock: Lock = ReentrantLock(true)

    /**
     * Уменьшает вес животного на 10%
     * Reduces animal weight by 10%
     *
     * @param islandCell текущая локация животного
     *                 (current location of the animal)
     */
    fun weightLoss(islandCell: IslandCell) {
        islandCell.lock.lock()
        try {
            weight -= weight / 10
        } finally {
            islandCell.lock.unlock()
        }
    }

    /**
     * Проверяет, должно ли животное умереть от истощения
     * Checks if animal should die from exhaustion
     *
     * @param islandCell текущая локация животного
     *                 (current location of the animal)
     */
    fun timeToDie(islandCell: IslandCell) {
        islandCell.lock.lock()
        try {
            if (weight < SimulationConfig.ANIMAL_PARAMETERS[aClass]!![0] / 3) {
                islandCell.removeAnimal(this)
            }
        } finally {
            islandCell.lock.unlock()
        }
    }

    /**
     * Размножение животного при выполнении условий
     * Animal reproduction when conditions are met
     *
     * @param islandCell текущая локация животного
     *                 (current location of the animal)
     */
    fun reproduction(islandCell: IslandCell) {
        islandCell.lock.lock()
        try {
            val animals: Set<Animal>? = islandCell.animals[aClass]
            if (weight == SimulationConfig.ANIMAL_PARAMETERS[aClass]!![0] && animals!!.size > 1) {
                val clone = this.clone()
                islandCell.addAnimal(clone)
                weightLoss(islandCell)
            }
        } finally {
            islandCell.lock.unlock()
        }
    }

    /**
     * Перемещение животного на соседнюю локацию
     * Moving animal to neighboring location
     *
     * @param islandCell текущая локация животного
     *                 (current location of the animal)
     */
    open fun move(islandCell: IslandCell) {
        islandCell.lock.lock()
        try {
            val newLocation = choiceOfAvailableLocation(islandCell)
            if (newLocation.hasSpaceFor(this.aClass)) {
                newLocation.addAnimal(this)
                islandCell.removeAnimal(this)
            }
        } finally {
            islandCell.lock.unlock()
        }
    }

    /**
     * Выбор доступной соседней локации для перемещения
     * Choosing an available neighboring location for movement
     *
     * @param islandCell текущая локация животного
     *                 (current location of the animal)
     * @return новая локация для перемещения
     *         (new location for movement)
     */
    private fun choiceOfAvailableLocation(islandCell: IslandCell): IslandCell {
        var location = islandCell
        val steps = maxNumberOfStepsAnimal
        for (i in steps downTo 0) {
            location = location.neighboringLocations[ThreadLocalRandom.current()
                .nextInt(0, location.neighboringLocations.size)]
        }
        return location
    }

    /**
     * Создание клона животного
     * Creating a clone of the animal
     *
     * @return клон животного с случайно заданным весом в пределах допустимых значений
     *         (clone of the animal with randomly assigned weight within allowed limits)
     */
    public override fun clone(): Animal {
        try {
            val clone = super.clone() as Animal
            clone.weight = Randomizer.getRandom(
                SimulationConfig.ANIMAL_PARAMETERS[aClass]!![0] / 1.5, SimulationConfig.ANIMAL_PARAMETERS[aClass]!![0]
            )
            return clone
        } catch (e: CloneNotSupportedException) {
            throw AssertionError()
        }
    }

    /**
     * Возвращает текущий вес животного
     * Returns the current weight of the animal
     *
     * @return текущий вес животного
     *         (current weight of the animal)
     */
    val currentWeight: Double
        get() = weight

    /**
     * Возвращает максимальное количество шагов, которое может сделать животное
     * Returns the maximum number of steps the animal can take
     *
     * @return максимальное количество шагов
     *         (maximum number of steps)
     */
    private val maxNumberOfStepsAnimal: Int
        get() = SimulationConfig.ANIMAL_PARAMETERS[aClass]!![2].toInt()
}
